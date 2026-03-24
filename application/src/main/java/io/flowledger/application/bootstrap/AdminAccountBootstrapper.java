package io.flowledger.application.bootstrap;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import io.flowledger.domain.identity.aggregate.User;
import io.flowledger.domain.identity.aggregate.UserStatus;
import io.flowledger.domain.identity.view.UserView;
import io.flowledger.domain.identity.view.mutation.UserMutationView;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import io.flowledger.platform.rbac.domain.role.entity.RbacUserRole;
import io.flowledger.platform.rbac.domain.role.view.RbacRoleView;
import io.flowledger.platform.rbac.domain.role.view.mutation.RbacUserRoleMutationView;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Bootstraps the default administrator account for the application.
 */
@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class AdminAccountBootstrapper implements ApplicationRunner {

  private static final String DEFAULT_ADMIN_ROLE_CODE = "super-admin";
  private static final String DEFAULT_ADMIN_EMAIL = "admin@flowledger.local";
  private static final UserStatus DEFAULT_ADMIN_STATUS = UserStatus.ACTIVE;
  private static final String ROLE_CODE_FIELD = "code";
  private static final String USER_EMAIL_FIELD = "email";
  private static final String USER_ID_FIELD = "userId";
  private static final String ROLE_ID_FIELD = "roleId";

  private final EntityManager entityManager;
  private final BlazeQueryBuilder blazeQueryBuilder;
  private final EntityViewManager entityViewManager;

  /**
   * Creates the default administrator user and assigns the admin role.
   *
   * @param args application arguments
   */
  @Override
  public void run(@NonNull ApplicationArguments args) {
    RbacRoleView adminRole = findAdminRole();
    if (adminRole == null) {
      log.warn("Admin role {} not found. Skipping admin account bootstrap.", DEFAULT_ADMIN_ROLE_CODE);
      return;
    }
    UserView adminUser = ensureAdminUser();
    ensureAdminUserRole(adminUser, adminRole);
  }

  /**
   * Finds the administrator role by code using an entity view.
   *
   * @return the administrator role, or null when missing
   */
  private RbacRoleView findAdminRole() {
    CriteriaBuilder<RbacRole> criteriaBuilder = blazeQueryBuilder.forEntity(RbacRole.class);
    criteriaBuilder.where(ROLE_CODE_FIELD)
        .eq(DEFAULT_ADMIN_ROLE_CODE)
        .setMaxResults(1);
    List<RbacRoleView> results = entityViewManager
        .applySetting(EntityViewSetting.create(RbacRoleView.class), criteriaBuilder)
        .getResultList();
    if (results.isEmpty()) {
      return null;
    }
    return results.getFirst();
  }

  /**
   * Ensures the administrator user account exists using an entity view.
   *
   * @return the administrator user
   */
  private UserView ensureAdminUser() {
    CriteriaBuilder<User> criteriaBuilder = blazeQueryBuilder.forEntity(User.class);
    criteriaBuilder.where(USER_EMAIL_FIELD)
        .eq(DEFAULT_ADMIN_EMAIL)
        .setMaxResults(1);
    List<UserView> results = entityViewManager
        .applySetting(EntityViewSetting.create(UserView.class), criteriaBuilder)
        .getResultList();
    if (!results.isEmpty()) {
      return results.getFirst();
    }
    return persistAdminUser();
  }

  /**
   * Persists the administrator user account using a mutation view.
   *
   * @return the newly persisted administrator user view
   */
  private UserView persistAdminUser() {
    Instant now = Instant.now();
    UserMutationView user = entityViewManager.create(UserMutationView.class);
    user.setEmail(DEFAULT_ADMIN_EMAIL);
    user.setStatus(DEFAULT_ADMIN_STATUS);
    user.setCreatedAt(now);
    user.setUpdatedAt(now);
    entityViewManager.save(entityManager, user);
    UserView createdUser = entityViewManager.find(entityManager, UserView.class, user.getId());
    if (createdUser == null) {
      throw new IllegalStateException("Admin user creation failed.");
    }
    return createdUser;
  }

  /**
   * Ensures the administrator user is assigned to the administrator role using views.
   *
   * @param adminUser the administrator user
   * @param adminRole the administrator role
   */
  private void ensureAdminUserRole(UserView adminUser, RbacRoleView adminRole) {
    if (adminUser == null || adminUser.getId() == null) {
      return;
    }
    if (adminRole == null || adminRole.getId() == null) {
      return;
    }
    long assignmentCount = blazeQueryBuilder.forEntity(RbacUserRole.class)
        .where(USER_ID_FIELD)
        .eq(adminUser.getId())
        .where(ROLE_ID_FIELD)
        .eq(adminRole.getId())
        .getQueryRootCountQuery()
        .getSingleResult();
    boolean alreadyAssigned = assignmentCount > 0;
    if (alreadyAssigned) {
      return;
    }
    persistAdminUserRole(adminUser.getId(), adminRole.getId());
  }

  /**
   * Persists an administrator role assignment for the administrator user.
   *
   * @param userId the user id
   * @param roleId the role id
   */
  private void persistAdminUserRole(UUID userId, UUID roleId) {
    Instant now = Instant.now();
    RbacUserRoleMutationView assignment = entityViewManager.create(RbacUserRoleMutationView.class);
    assignment.setUserId(userId);
    assignment.setRoleId(roleId);
    assignment.setCreatedAt(now);
    assignment.setUpdatedAt(now);
    entityViewManager.save(entityManager, assignment);
  }
}
