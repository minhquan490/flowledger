package io.flowledger.platform.rbac.application.service;

import io.flowledger.core.web.HttpStatusCodes;
import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.rbac.domain.permission.entity.RbacRoleResourcePermission;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Evaluates RBAC permissions for resources and actions.
 */
@Component
@RequiredArgsConstructor
public class RbacPermissionService {
  private final RbacRoleResolver roleResolver;
  private final BlazeQueryBuilder blazeQueryBuilder;

  /**
   * Ensures the current subject has access to the resource and action.
   *
   * @param resource the resource name
   * @param action the action to check
   */
  public void assertHasPermission(String resource, RbacAction action) {
    if (!hasPermission(resource, action)) {
      throw new GraphQlApiException(
          "Access denied for " + action + " on resource " + resource + ".",
          HttpStatusCodes.FORBIDDEN
      );
    }
  }

  /**
   * Returns true when the current subject has access to the resource and action.
   *
   * @param resource the resource name
   * @param action the action to check
   * @return true when allowed
   */
  public boolean hasPermission(String resource, RbacAction action) {
    RbacResource resourceEntity = findResource(resource);
    List<RbacRole> roles = roleResolver.resolveRoles();
    if (roles.isEmpty()) {
      return false;
    }
    List<UUID> roleIds = roles.stream().map(RbacRole::getId).toList();
    long permissionCount = blazeQueryBuilder.forEntity(RbacRoleResourcePermission.class)
        .where("roleId").in(roleIds)
        .where("resourceId").eq(resourceEntity.getId())
        .where("action").eq(action)
        .where("allowed").eq(true)
        .getQueryRootCountQuery()
        .getSingleResult();
    return permissionCount > 0;
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

  static RbacResource getRbacResourceEntity(
      String resource,
      BlazeQueryBuilder blazeQueryBuilder
  ) {
    List<RbacResource> results = blazeQueryBuilder.forEntity(RbacResource.class)
        .where("name")
        .eq(resource)
        .setMaxResults(1)
        .getResultList();
    if (results.isEmpty()) {
      throw new GraphQlApiException(
          "No RBAC resource registered for " + resource + ".",
          HttpStatusCodes.NOT_FOUND
      );
    }
    return results.getFirst();
  }
}
