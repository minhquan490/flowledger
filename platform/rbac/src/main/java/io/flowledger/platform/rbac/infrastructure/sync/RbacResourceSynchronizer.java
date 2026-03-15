package io.flowledger.platform.rbac.infrastructure.sync;

import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlModelRegistry;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlViewDefinition;
import io.flowledger.platform.rbac.domain.RbacAction;
import io.flowledger.platform.rbac.domain.entity.RbacResourceEntity;
import io.flowledger.platform.rbac.domain.entity.RbacRoleEntity;
import io.flowledger.platform.rbac.domain.entity.RbacRoleResourcePermissionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/**
 * Synchronizes GraphQL models into RBAC resources and seeds defaults.
 *
 * <p>All resource lookups are batched to avoid N+1 queries and to prevent
 * database errors from overly long {@code IN} clauses.
 */
@RequiredArgsConstructor
public class RbacResourceSynchronizer {

  private static final String DEFAULT_ROLE_CODE = "default";
  private static final String DEFAULT_ROLE_NAME = "Default Role";
  private static final String DEFAULT_ADMIN_ROLE_CODE = "super-admin";
  private static final String DEFAULT_ADMIN_ROLE_NAME = "Super Admin";
  private static final int BATCH_SIZE = 500;

  private final BlazeGraphQlModelRegistry modelRegistry;
  private final EntityManager entityManager;

  /**
   * Synchronizes resources, default roles, and administrator permissions.
   */
  @Transactional
  public void synchronize() {
    syncResources();
    ensureDefaultRole();
    RbacRoleEntity adminRole = ensureAdminRole();
    ensureAdminRolePermissions(adminRole);
  }

  /**
   * Synchronizes model resources from GraphQL definitions in batches.
   *
   * <p>Collects all model names first, then loads existing resources in
   * batches of {@value BATCH_SIZE} to avoid long {@code IN} clauses,
   * and finally persists inserts and updates in bulk before flushing
   * to obtain generated identifiers.
   */
  private void syncResources() {
    List<String> modelNames = collectModelNames();
    if (modelNames.isEmpty()) {
      return;
    }

    Map<String, RbacResourceEntity> existingByName = loadExistingResources(modelNames);
    Instant now = Instant.now();

    for (String model : modelNames) {
      if (existingByName.containsKey(model)) {
        updateIfSystemManaged(existingByName.get(model), now);
      } else {
        persistNewResource(model, now);
      }
    }
    entityManager.flush();
  }

  /**
   * Collects all model names from the GraphQL model registry.
   *
   * @return a non-null list of model names
   */
  private List<String> collectModelNames() {
    return modelRegistry.getViewsByModel().values().stream()
        .map(BlazeGraphQlViewDefinition::model)
        .toList();
  }

  /**
   * Loads existing {@link RbacResourceEntity} records for the given names
   * using batched {@code IN} queries to avoid database clause size limits.
   *
   * @param modelNames the full list of model names to look up
   * @return a map of resource name to entity for all found records
   */
  private Map<String, RbacResourceEntity> loadExistingResources(List<String> modelNames) {
    List<RbacResourceEntity> results = new ArrayList<>();
    List<List<String>> batches = partition(modelNames);

    for (List<String> batch : batches) {
      TypedQuery<RbacResourceEntity> query = entityManager.createQuery(
          "select r from RbacResourceEntity r where r.name in :names",
          RbacResourceEntity.class
      );
      query.setParameter("names", batch);
      results.addAll(query.getResultList());
    }

    return results.stream()
        .collect(Collectors.toMap(RbacResourceEntity::getName, Function.identity()));
  }

  /**
   * Updates the {@code updatedAt} timestamp on a system-managed resource.
   *
   * @param existing the existing resource entity
   * @param now the current timestamp
   */
  private void updateIfSystemManaged(RbacResourceEntity existing, Instant now) {
    if (!existing.isSystemManaged()) {
      return;
    }
    existing.setUpdatedAt(now);
    entityManager.merge(existing);
  }

  /**
   * Persists a new system-managed resource entity.
   *
   * @param model the resource name
   * @param now the current timestamp
   */
  private void persistNewResource(String model, Instant now) {
    RbacResourceEntity resource = new RbacResourceEntity();
    resource.setName(model);
    resource.setSystemManaged(true);
    resource.setCreatedAt(now);
    resource.setUpdatedAt(now);
    entityManager.persist(resource);
  }

  /**
   * Ensures a default role exists for users without explicit roles.
   */
  private void ensureDefaultRole() {
    TypedQuery<RbacRoleEntity> query = entityManager.createQuery(
        "select r from RbacRoleEntity r where r.defaultRole = true",
        RbacRoleEntity.class
    );
    if (!query.getResultList().isEmpty()) {
      return;
    }
    persistDefaultRole();
  }

  /**
   * Persists the default system-managed role.
   */
  private void persistDefaultRole() {
    Instant now = Instant.now();
    RbacRoleEntity role = new RbacRoleEntity();
    role.setCode(DEFAULT_ROLE_CODE);
    role.setName(DEFAULT_ROLE_NAME);
    role.setDefaultRole(true);
    role.setSystemManaged(true);
    role.setCreatedAt(now);
    role.setUpdatedAt(now);
    entityManager.persist(role);
  }

  /**
   * Ensures the system-managed administrator role exists.
   *
   * @return the administrator role entity
   */
  private RbacRoleEntity ensureAdminRole() {
    TypedQuery<RbacRoleEntity> query = entityManager.createQuery(
        "select r from RbacRoleEntity r where r.code = :code",
        RbacRoleEntity.class
    );
    query.setParameter("code", DEFAULT_ADMIN_ROLE_CODE);
    List<RbacRoleEntity> results = query.getResultList();
    if (!results.isEmpty()) {
      return results.getFirst();
    }
    return persistAdminRole();
  }

  /**
   * Persists the system-managed administrator role.
   *
   * @return the newly persisted administrator role
   */
  private RbacRoleEntity persistAdminRole() {
    Instant now = Instant.now();
    RbacRoleEntity role = new RbacRoleEntity();
    role.setCode(DEFAULT_ADMIN_ROLE_CODE);
    role.setName(DEFAULT_ADMIN_ROLE_NAME);
    role.setDefaultRole(false);
    role.setSystemManaged(true);
    role.setCreatedAt(now);
    role.setUpdatedAt(now);
    entityManager.persist(role);
    flushAndRefresh(role);
    return role;
  }

  /**
   * Ensures the administrator role has full access to all registered resources.
   *
   * @param adminRole the administrator role
   */
  private void ensureAdminRolePermissions(RbacRoleEntity adminRole) {
    if (adminRole == null || adminRole.getId() == null) {
      return;
    }
    List<RbacResourceEntity> resources = entityManager.createQuery(
        "select r from RbacResourceEntity r",
        RbacResourceEntity.class
    ).getResultList();
    if (resources.isEmpty()) {
      return;
    }
    List<RbacRoleResourcePermissionEntity> existing = loadRoleResourcePermissions(adminRole.getId(), resources);
    Map<String, RbacRoleResourcePermissionEntity> existingByKey = existing.stream()
        .collect(Collectors.toMap(this::permissionKey, Function.identity()));
    Instant now = Instant.now();
    for (RbacResourceEntity resource : resources) {
      for (RbacAction action : RbacAction.values()) {
        String key = permissionKey(adminRole.getId(), resource.getId(), action);
        if (!existingByKey.containsKey(key)) {
          persistRolePermission(adminRole.getId(), resource.getId(), action, now);
        } else {
          ensurePermissionAllowed(existingByKey.get(key), now);
        }
      }
    }
  }

  /**
   * Loads existing role-resource permissions for the given role and resources.
   *
   * @param roleId the role identifier
   * @param resources the resources to check
   * @return the existing permissions for the role
   */
  private List<RbacRoleResourcePermissionEntity> loadRoleResourcePermissions(
      UUID roleId,
      List<RbacResourceEntity> resources
  ) {
    List<UUID> resourceIds = resources.stream()
        .map(RbacResourceEntity::getId)
        .filter(Objects::nonNull)
        .toList();
    if (resourceIds.isEmpty()) {
      return List.of();
    }
    TypedQuery<RbacRoleResourcePermissionEntity> query = entityManager.createQuery(
        "select p from RbacRoleResourcePermissionEntity p "
            + "where p.roleId = :roleId and p.resourceId in :resourceIds",
        RbacRoleResourcePermissionEntity.class
    );
    query.setParameter("roleId", roleId);
    query.setParameter("resourceIds", resourceIds);
    return query.getResultList();
  }

  /**
   * Creates a unique key for a permission entry.
   *
   * @param permission the permission entity
   * @return the permission key
   */
  private String permissionKey(RbacRoleResourcePermissionEntity permission) {
    return permissionKey(permission.getRoleId(), permission.getResourceId(), permission.getAction());
  }

  /**
   * Creates a unique key for the permission lookup.
   *
   * @param roleId the role id
   * @param resourceId the resource id
   * @param action the action
   * @return the permission key
   */
  private String permissionKey(UUID roleId, UUID resourceId, RbacAction action) {
    return roleId + ":" + resourceId + ":" + action.name();
  }

  /**
   * Persists a new role-resource permission for the administrator role.
   *
   * @param roleId the role id
   * @param resourceId the resource id
   * @param action the action
   * @param now the current timestamp
   */
  private void persistRolePermission(UUID roleId, UUID resourceId, RbacAction action, Instant now) {
    if (resourceId == null) {
      return;
    }
    RbacRoleResourcePermissionEntity permission = new RbacRoleResourcePermissionEntity();
    permission.setRoleId(roleId);
    permission.setResourceId(resourceId);
    permission.setAction(action);
    permission.setAllowed(true);
    permission.setSystemManaged(true);
    permission.setCreatedAt(now);
    permission.setUpdatedAt(now);
    entityManager.persist(permission);
  }

  /**
   * Ensures an existing permission is marked as allowed.
   *
   * @param permission the existing permission
   * @param now the current timestamp
   */
  private void ensurePermissionAllowed(RbacRoleResourcePermissionEntity permission, Instant now) {
    if (permission == null || permission.isAllowed()) {
      return;
    }
    permission.setAllowed(true);
    permission.setUpdatedAt(now);
    entityManager.merge(permission);
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

  /**
   * Partitions a list into sublists of the given size.
   *
   * @param <T>  the element type
   * @param list the list to partition
   * @return a non-null list of sublists
   */
  private <T> List<List<T>> partition(List<T> list) {
    List<List<T>> partitions = new ArrayList<>();
    for (int i = 0; i < list.size(); i += RbacResourceSynchronizer.BATCH_SIZE) {
      int endIndex = Math.min(i + RbacResourceSynchronizer.BATCH_SIZE, list.size());
      partitions.add(list.subList(i, endIndex));
    }
    return partitions;
  }
}
