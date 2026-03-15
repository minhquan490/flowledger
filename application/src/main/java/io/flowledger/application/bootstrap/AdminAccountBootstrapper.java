package io.flowledger.application.bootstrap;

import io.flowledger.domain.identity.aggregate.User;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import io.flowledger.platform.rbac.domain.role.entity.RbacUserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
  private static final String DEFAULT_ADMIN_STATUS = "ACTIVE";

  private final EntityManager entityManager;

  /**
   * Creates the default administrator user and assigns the admin role.
   *
   * @param args application arguments
   */
  @Override
  public void run(ApplicationArguments args) {
    RbacRole adminRole = findAdminRole();
    if (adminRole == null) {
      log.warn("Admin role {} not found. Skipping admin account bootstrap.", DEFAULT_ADMIN_ROLE_CODE);
      return;
    }
    User adminUser = ensureAdminUser();
    ensureAdminUserRole(adminUser, adminRole);
  }

  /**
   * Finds the administrator role by code.
   *
   * @return the administrator role, or null when missing
   */
  private RbacRole findAdminRole() {
    TypedQuery<RbacRole> query = entityManager.createQuery(
        "select r from RbacRole r where r.code = :code",
        RbacRole.class
    );
    query.setParameter("code", DEFAULT_ADMIN_ROLE_CODE);
    List<RbacRole> results = query.getResultList();
    if (results.isEmpty()) {
      return null;
    }
    return results.getFirst();
  }

  /**
   * Ensures the administrator user account exists.
   *
   * @return the administrator user
   */
  private User ensureAdminUser() {
    TypedQuery<User> query = entityManager.createQuery(
        "select u from User u where u.email = :email",
        User.class
    );
    query.setParameter("email", DEFAULT_ADMIN_EMAIL);
    List<User> results = query.getResultList();
    if (!results.isEmpty()) {
      return results.getFirst();
    }
    return persistAdminUser();
  }

  /**
   * Persists the administrator user account.
   *
   * @return the newly persisted administrator user
   */
  private User persistAdminUser() {
    Instant now = Instant.now();
    User user = new User();
    user.setEmail(DEFAULT_ADMIN_EMAIL);
    user.setStatus(DEFAULT_ADMIN_STATUS);
    user.setCreatedAt(now);
    user.setUpdatedAt(now);
    entityManager.persist(user);
    flushAndRefresh(user);
    return user;
  }

  /**
   * Ensures the administrator user is assigned to the administrator role.
   *
   * @param adminUser the administrator user
   * @param adminRole the administrator role
   */
  private void ensureAdminUserRole(User adminUser, RbacRole adminRole) {
    if (adminUser == null || adminUser.getId() == null) {
      return;
    }
    if (adminRole == null || adminRole.getId() == null) {
      return;
    }
    TypedQuery<RbacUserRole> query = entityManager.createQuery(
        "select ur from RbacUserRole ur where ur.userId = :userId and ur.roleId = :roleId",
        RbacUserRole.class
    );
    query.setParameter("userId", adminUser.getId());
    query.setParameter("roleId", adminRole.getId());
    if (!query.getResultList().isEmpty()) {
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
  private void persistAdminUserRole(java.util.UUID userId, java.util.UUID roleId) {
    Instant now = Instant.now();
    RbacUserRole assignment = new RbacUserRole();
    assignment.setUserId(userId);
    assignment.setRoleId(roleId);
    assignment.setCreatedAt(now);
    assignment.setUpdatedAt(now);
    entityManager.persist(assignment);
  }

  /**
   * Flushes and refreshes the given entity to load generated identifiers.
   *
   * @param entity the managed entity to refresh
   */
  private void flushAndRefresh(Object entity) {
    entityManager.flush();
    entityManager.refresh(entity);
  }
}
