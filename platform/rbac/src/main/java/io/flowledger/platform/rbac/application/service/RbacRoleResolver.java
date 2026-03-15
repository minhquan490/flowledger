package io.flowledger.platform.rbac.application.service;

import io.flowledger.platform.rbac.application.RbacSubjectProvider;
import io.flowledger.platform.rbac.domain.entity.RbacRoleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
  private static final String DEFAULT_ROLE_QUERY =
      "select r from RbacRoleEntity r where r.defaultRole = true";

  private static final String USER_ROLE_QUERY =
      "select r from RbacRoleEntity r "
          + "join RbacUserRoleEntity ur on ur.roleId = r.id "
          + "where ur.userId = :userId";

  private final RbacSubjectProvider subjectProvider;

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Resolves roles for the current subject.
   *
   * @return the resolved roles
   */
  public List<RbacRoleEntity> resolveRoles() {
    Optional<UUID> subjectId = subjectProvider.currentSubjectId();
    if (subjectId.isPresent()) {
      List<RbacRoleEntity> roles = findRolesByUser(subjectId.get());
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
  private List<RbacRoleEntity> findRolesByUser(UUID userId) {
    TypedQuery<RbacRoleEntity> query = entityManager.createQuery(USER_ROLE_QUERY, RbacRoleEntity.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }

  /**
   * Finds default roles.
   *
   * @return the default roles
   */
  private List<RbacRoleEntity> findDefaultRoles() {
    return entityManager.createQuery(DEFAULT_ROLE_QUERY, RbacRoleEntity.class).getResultList();
  }
}
