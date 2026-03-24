package io.flowledger.platform.rbac.application.service;

import io.flowledger.platform.rbac.application.RbacSubjectProvider;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import io.flowledger.platform.rbac.domain.role.entity.RbacUserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Resolves roles for the current subject, falling back to default roles.
 */
@Component
@RequiredArgsConstructor
public class RbacRoleResolver {
  private static final String ROLE_ALIAS = "role";
  private static final String USER_ROLE_ALIAS = "userRole";
  private static final String DEFAULT_ROLE_FIELD = "defaultRole";
  private static final String ROLE_ID_FIELD = "id";
  private static final String USER_ROLE_ID_FIELD = "roleId";
  private static final String USER_ROLE_USER_ID_FIELD = "userId";

  private final RbacSubjectProvider subjectProvider;
  private final BlazeQueryBuilder blazeQueryBuilder;

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Resolves roles for the current subject.
   *
   * @return the resolved roles
   */
  public List<RbacRole> resolveRoles() {
    Optional<UUID> subjectId = subjectProvider.currentSubjectId();
    if (subjectId.isPresent()) {
      List<RbacRole> roles = findRolesByUser(subjectId.get());
      if (!roles.isEmpty()) {
        return roles;
      }
    }
    return findDefaultRoles();
  }

  /**
   * Finds roles assigned to a user.
   *
   * @param userId the user identifier
   * @return the assigned roles
   */
  private List<RbacRole> findRolesByUser(UUID userId) {
    return blazeQueryBuilder.getCriteriaBuilderFactory()
        .create(entityManager, RbacRole.class)
        .from(RbacRole.class, ROLE_ALIAS)
        .innerJoinOn(RbacUserRole.class, USER_ROLE_ALIAS)
        .on(ROLE_ALIAS + "." + ROLE_ID_FIELD).eqExpression(USER_ROLE_ALIAS + "." + USER_ROLE_ID_FIELD)
        .end()
        .where(USER_ROLE_ALIAS + "." + USER_ROLE_USER_ID_FIELD).eq(userId)
        .getResultList();
  }

  /**
   * Finds default roles.
   *
   * @return the default roles
   */
  private List<RbacRole> findDefaultRoles() {
    return blazeQueryBuilder.forEntity(RbacRole.class)
        .where(DEFAULT_ROLE_FIELD)
        .eq(true)
        .getResultList();
  }
}
