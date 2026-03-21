package io.flowledger.platform.rbac.application.service;

import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.core.web.HttpStatusCodes;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacAction;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Evaluates RBAC permissions for resources and actions.
 */
@Component
@RequiredArgsConstructor
public class RbacPermissionService {
  private final RbacRoleResolver roleResolver;

  @PersistenceContext
  private EntityManager entityManager;

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
    TypedQuery<Long> query = entityManager.createQuery(
        "select count(p) from RbacRoleResourcePermission p "
            + "where p.roleId in :roleIds and p.resourceId = :resourceId "
            + "and p.action = :action and p.allowed = true",
        Long.class
    );
    query.setParameter("roleIds", roleIds);
    query.setParameter("resourceId", resourceEntity.getId());
    query.setParameter("action", action);
    return query.getSingleResult() > 0;
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

  static RbacResource getRbacResourceEntity(String resource, EntityManager entityManager) {
    TypedQuery<RbacResource> query = entityManager.createQuery(
        "select r from RbacResource r where r.name = :name",
        RbacResource.class
    );
    query.setParameter("name", resource);
    List<RbacResource> results = query.getResultList();
    if (results.isEmpty()) {
      throw new GraphQlApiException(
          "No RBAC resource registered for " + resource + ".",
          HttpStatusCodes.NOT_FOUND
      );
    }
    return results.getFirst();
  }
}
