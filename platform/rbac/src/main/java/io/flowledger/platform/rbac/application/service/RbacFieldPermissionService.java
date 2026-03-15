package io.flowledger.platform.rbac.application.service;

import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static io.flowledger.platform.rbac.application.service.RbacPermissionService.getRbacResourceEntity;

/**
 * Evaluates field-level permissions for RBAC.
 */
@Component
@RequiredArgsConstructor
public class RbacFieldPermissionService {
  private static final int FORBIDDEN_STATUS = 403;

  private final RbacRoleResolver roleResolver;

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Returns the list of readable fields for the current subject.
   *
   * @param resource the resource name
   * @return the readable fields
   */
  public List<String> readableFields(String resource) {
    RbacResource resourceEntity = findResource(resource);
    List<RbacRole> roles = roleResolver.resolveRoles();
    if (roles.isEmpty()) {
      return List.of();
    }
    List<UUID> roleIds = roles.stream().map(RbacRole::getId).toList();
    TypedQuery<String> query = entityManager.createQuery(
        "select distinct f.fieldName from RbacRoleFieldPermission f "
            + "where f.roleId in :roleIds and f.resourceId = :resourceId and f.canRead = true",
        String.class
    );
    query.setParameter("roleIds", roleIds);
    query.setParameter("resourceId", resourceEntity.getId());
    return query.getResultList();
  }

  /**
   * Validates write access for the provided field names.
   *
   * @param resource the resource name
   * @param fieldNames the field names to validate
   */
  public void validateWritableFields(String resource, List<String> fieldNames) {
    if (fieldNames == null || fieldNames.isEmpty()) {
      return;
    }
    RbacResource resourceEntity = findResource(resource);
    List<RbacRole> roles = roleResolver.resolveRoles();
    if (roles.isEmpty()) {
      throw new GraphQlApiException("No roles available for write validation on " + resource + ".", FORBIDDEN_STATUS);
    }
    List<UUID> roleIds = roles.stream().map(RbacRole::getId).toList();
    TypedQuery<String> query = entityManager.createQuery(
        "select distinct f.fieldName from RbacRoleFieldPermission f "
            + "where f.roleId in :roleIds and f.resourceId = :resourceId and f.canWrite = true",
        String.class
    );
    query.setParameter("roleIds", roleIds);
    query.setParameter("resourceId", resourceEntity.getId());
    List<String> allowed = query.getResultList();
    List<String> denied = fieldNames.stream()
        .filter(field -> !allowed.contains(field))
        .toList();
    if (!denied.isEmpty()) {
      throw new GraphQlApiException("Write access denied for fields " + denied + " on " + resource + ".", FORBIDDEN_STATUS);
    }
  }

  /**
   * Finds the resource entity for a given name.
   *
   * @param resource the resource name
   * @return the resource entity
   */
  private RbacResource findResource(String resource) {
    return getRbacResourceEntity(resource, entityManager);
  }
}
