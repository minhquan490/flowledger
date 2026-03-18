package io.flowledger.platform.rbac.application.service;

import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacFieldAction;
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
    return allowedFields(resource, RbacFieldAction.VIEW);
  }

  /**
   * Validates field access for the provided action and field names.
   *
   * @param resource the resource name
   * @param fieldNames the field names to validate
   * @param action the field action to validate
   */
  public void validateFieldsForAction(String resource, List<String> fieldNames, RbacFieldAction action) {
    if (fieldNames == null || fieldNames.isEmpty()) {
      return;
    }
    List<String> allowed = allowedFields(resource, action);
    if (allowed.isEmpty()) {
      throw new GraphQlApiException("No field permissions available for " + action + " on " + resource + ".", FORBIDDEN_STATUS);
    }
    List<String> denied = fieldNames.stream()
        .filter(field -> !allowed.contains(field))
        .toList();
    if (!denied.isEmpty()) {
      throw new GraphQlApiException(
          "Access denied for action " + action + " on fields " + denied + " for resource " + resource + ".",
          FORBIDDEN_STATUS
      );
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

  /**
   * Resolves fields allowed for the subject and field action.
   *
   * @param resource the resource name
   * @param action the field action
   * @return the allowed field names
   */
  private List<String> allowedFields(String resource, RbacFieldAction action) {
    RbacResource resourceEntity = findResource(resource);
    List<RbacRole> roles = roleResolver.resolveRoles();
    if (roles.isEmpty()) {
      return List.of();
    }
    List<UUID> roleIds = roles.stream().map(RbacRole::getId).toList();
    TypedQuery<String> query = entityManager.createQuery(
        """
            select distinct rf.fieldName
            from RbacRoleFieldActionPermission p
            join RbacResourceField rf on p.resourceFieldId = rf.id
            where p.roleId in :roleIds
              and rf.resourceId = :resourceId
              and p.action = :action
              and p.allowed = true
            """,
        String.class
    );
    query.setParameter("roleIds", roleIds);
    query.setParameter("resourceId", resourceEntity.getId());
    query.setParameter("action", action);
    return query.getResultList();
  }
}
