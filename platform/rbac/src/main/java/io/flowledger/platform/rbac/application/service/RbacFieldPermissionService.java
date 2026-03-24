package io.flowledger.platform.rbac.application.service;

import io.flowledger.core.web.HttpStatusCodes;
import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.rbac.domain.permission.entity.RbacRoleFieldActionPermission;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.rbac.domain.resource.entity.RbacResourceField;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacFieldAction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static io.flowledger.platform.rbac.application.service.RbacPermissionService.getRbacResourceEntity;

/**
 * Evaluates field-level permissions for RBAC.
 */
@Component
@RequiredArgsConstructor
public class RbacFieldPermissionService {
  private final RbacRoleResolver roleResolver;
  private final BlazeQueryBuilder blazeQueryBuilder;

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
      throw new GraphQlApiException(
          "No field permissions available for " + action + " on " + resource + ".",
          HttpStatusCodes.FORBIDDEN
      );
    }
    List<String> denied = fieldNames.stream()
        .filter(field -> !allowed.contains(field))
        .toList();
    if (!denied.isEmpty()) {
      throw new GraphQlApiException(
          "Access denied for action " + action + " on fields " + denied + " for resource " + resource + ".",
          HttpStatusCodes.FORBIDDEN
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
    return getRbacResourceEntity(resource, blazeQueryBuilder);
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
    return blazeQueryBuilder.getCriteriaBuilderFactory()
        .create(entityManager, String.class)
        .from(RbacRoleFieldActionPermission.class, "p")
        .innerJoinOn(RbacResourceField.class, "rf")
        .on("p.resourceFieldId").eqExpression("rf.id")
        .end()
        .select("rf.fieldName")
        .distinct()
        .where("p.roleId").in(roleIds)
        .where("rf.resource.id").eq(resourceEntity.getId())
        .where("p.action").eq(action)
        .where("p.allowed").eq(true)
        .getResultList();
  }
}
