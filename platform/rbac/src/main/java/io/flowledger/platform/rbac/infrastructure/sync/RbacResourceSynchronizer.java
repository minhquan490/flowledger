package io.flowledger.platform.rbac.infrastructure.sync;

import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlModelRegistry;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlViewDefinition;
import io.flowledger.platform.rbac.domain.resource.entity.RbacResourceField;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacAction;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacFieldAction;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import io.flowledger.platform.rbac.domain.role.entity.RbacRoleFieldActionPermission;
import io.flowledger.platform.rbac.domain.role.entity.RbacRoleResourcePermission;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
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
    Map<String, Class<?>> modelViews = collectModelViews();
    syncResources(modelViews.keySet().stream().toList());
    syncResourceFields(modelViews);
    ensureDefaultRole();
    RbacRole adminRole = ensureAdminRole();
    ensureAdminRolePermissions(adminRole);
    ensureAdminFieldPermissions(adminRole);
  }

  /**
   * Synchronizes model resources from GraphQL definitions in batches.
   *
   * <p>Collects all model names first, then loads existing resources in
   * batches of {@value BATCH_SIZE} to avoid long {@code IN} clauses,
   * and finally persists inserts and updates in bulk before flushing
   * to obtain generated identifiers.
   */
  private void syncResources(List<String> modelNames) {
    if (modelNames.isEmpty()) {
      return;
    }

    Map<String, RbacResource> existingByName = loadExistingResources(modelNames);
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
   * Collects model names and associated view classes from the GraphQL model registry.
   *
   * @return a non-null map of model name to view class
   */
  private Map<String, Class<?>> collectModelViews() {
    return modelRegistry.getViewsByModel().values().stream()
        .collect(Collectors.toMap(
            BlazeGraphQlViewDefinition::model,
            BlazeGraphQlViewDefinition::viewClass,
            (first, _) -> first,
            LinkedHashMap::new
        ));
  }

  /**
   * Loads existing {@link RbacResource} records for the given names
   * using batched {@code IN} queries to avoid database clause size limits.
   *
   * @param modelNames the full list of model names to look up
   * @return a map of resource name to entity for all found records
   */
  private Map<String, RbacResource> loadExistingResources(List<String> modelNames) {
    List<RbacResource> results = new ArrayList<>();
    List<List<String>> batches = partition(modelNames);

    for (List<String> batch : batches) {
      TypedQuery<RbacResource> query = entityManager.createQuery(
          "select r from RbacResource r where r.name in :names",
          RbacResource.class
      );
      query.setParameter("names", batch);
      results.addAll(query.getResultList());
    }

    return results.stream()
        .collect(Collectors.toMap(RbacResource::getName, Function.identity()));
  }

  /**
   * Updates the {@code updatedAt} timestamp on a system-managed resource.
   *
   * @param existing the existing resource entity
   * @param now the current timestamp
   */
  private void updateIfSystemManaged(RbacResource existing, Instant now) {
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
    RbacResource resource = new RbacResource();
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
    TypedQuery<RbacRole> query = entityManager.createQuery(
        "select r from RbacRole r where r.defaultRole = true",
        RbacRole.class
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
    RbacRole role = new RbacRole();
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
  private RbacRole ensureAdminRole() {
    TypedQuery<RbacRole> query = entityManager.createQuery(
        "select r from RbacRole r where r.code = :code",
        RbacRole.class
    );
    query.setParameter("code", DEFAULT_ADMIN_ROLE_CODE);
    List<RbacRole> results = query.getResultList();
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
  private RbacRole persistAdminRole() {
    Instant now = Instant.now();
    RbacRole role = new RbacRole();
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
  private void ensureAdminRolePermissions(RbacRole adminRole) {
    if (adminRole == null || adminRole.getId() == null) {
      return;
    }
    List<RbacResource> resources = entityManager.createQuery(
        "select r from RbacResource r",
        RbacResource.class
    ).getResultList();
    if (resources.isEmpty()) {
      return;
    }
    List<RbacRoleResourcePermission> existing = loadRoleResourcePermissions(adminRole.getId(), resources);
    Map<String, RbacRoleResourcePermission> existingByKey = existing.stream()
        .collect(Collectors.toMap(this::permissionKey, Function.identity()));
    Instant now = Instant.now();
    for (RbacResource resource : resources) {
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
   * Ensures the administrator role has full action permissions for every resource field.
   *
   * @param adminRole the administrator role
   */
  private void ensureAdminFieldPermissions(RbacRole adminRole) {
    if (adminRole == null || adminRole.getId() == null) {
      return;
    }
    List<RbacResourceField> resourceFields = entityManager.createQuery(
        "select rf from RbacResourceField rf",
        RbacResourceField.class
    ).getResultList();
    if (resourceFields.isEmpty()) {
      return;
    }
    List<RbacRoleFieldActionPermission> existing = loadRoleFieldActionPermissions(adminRole.getId(), resourceFields);
    Map<String, RbacRoleFieldActionPermission> existingByKey = existing.stream()
        .collect(Collectors.toMap(this::fieldPermissionKey, Function.identity()));
    Instant now = Instant.now();
    for (RbacResourceField resourceField : resourceFields) {
      for (RbacFieldAction action : RbacFieldAction.values()) {
        String key = fieldPermissionKey(adminRole.getId(), resourceField.getId(), action);
        if (!existingByKey.containsKey(key)) {
          persistFieldActionPermission(adminRole.getId(), resourceField.getId(), action, now);
        } else {
          ensureFieldPermissionAllowed(existingByKey.get(key), now);
        }
      }
    }
  }

  /**
   * Synchronizes field catalogs for all model views.
   *
   * @param modelViews the map of model names to view classes
   */
  private void syncResourceFields(Map<String, Class<?>> modelViews) {
    if (modelViews.isEmpty()) {
      return;
    }
    List<RbacResource> resources = loadExistingResources(modelViews.keySet().stream().toList()).values().stream().toList();
    Map<String, RbacResource> resourcesByName = resources.stream()
        .collect(Collectors.toMap(RbacResource::getName, Function.identity()));
    List<RbacResourceField> existingFields = entityManager.createQuery(
        "select rf from RbacResourceField rf",
        RbacResourceField.class
    ).getResultList();
    Map<String, RbacResourceField> existingByKey = existingFields.stream()
        .collect(Collectors.toMap(this::resourceFieldKey, Function.identity(), (first, _) -> first));
    Instant now = Instant.now();
    for (Map.Entry<String, Class<?>> modelView : modelViews.entrySet()) {
      String model = modelView.getKey();
      RbacResource resource = resourcesByName.get(model);
      if (resource == null || resource.getId() == null) {
        continue;
      }
      for (FieldMethod fieldMethod : extractFieldMethods(modelView.getValue())) {
        String key = resourceFieldKey(resource.getId(), fieldMethod.fieldName());
        if (!existingByKey.containsKey(key)) {
          persistResourceField(resource.getId(), fieldMethod.fieldName(), fieldMethod.sourceMethodName(), now);
          existingByKey.put(key, placeholderResourceField(resource.getId(), fieldMethod.fieldName()));
        } else {
          updateResourceField(existingByKey.get(key), fieldMethod.sourceMethodName(), now);
        }
      }
    }
    entityManager.flush();
  }

  /**
   * Loads existing role-resource permissions for the given role and resources.
   *
   * @param roleId the role identifier
   * @param resources the resources to check
   * @return the existing permissions for the role
   */
  private List<RbacRoleResourcePermission> loadRoleResourcePermissions(
      UUID roleId,
      List<RbacResource> resources
  ) {
    List<UUID> resourceIds = resources.stream()
        .map(RbacResource::getId)
        .filter(Objects::nonNull)
        .toList();
    if (resourceIds.isEmpty()) {
      return List.of();
    }
    TypedQuery<RbacRoleResourcePermission> query = entityManager.createQuery(
        "select p from RbacRoleResourcePermission p "
            + "where p.roleId = :roleId and p.resourceId in :resourceIds",
        RbacRoleResourcePermission.class
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
  private String permissionKey(RbacRoleResourcePermission permission) {
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
   * Loads existing role-field-action permissions for the given role and resource fields.
   *
   * @param roleId the role identifier
   * @param resourceFields the resource fields to check
   * @return the existing role-field-action permissions
   */
  private List<RbacRoleFieldActionPermission> loadRoleFieldActionPermissions(
      UUID roleId,
      List<RbacResourceField> resourceFields
  ) {
    List<UUID> resourceFieldIds = resourceFields.stream()
        .map(RbacResourceField::getId)
        .filter(Objects::nonNull)
        .toList();
    if (resourceFieldIds.isEmpty()) {
      return List.of();
    }
    TypedQuery<RbacRoleFieldActionPermission> query = entityManager.createQuery(
        "select p from RbacRoleFieldActionPermission p "
            + "where p.roleId = :roleId and p.resourceFieldId in :resourceFieldIds",
        RbacRoleFieldActionPermission.class
    );
    query.setParameter("roleId", roleId);
    query.setParameter("resourceFieldIds", resourceFieldIds);
    return query.getResultList();
  }

  /**
   * Creates a unique key for a field permission entry.
   *
   * @param permission the field permission entity
   * @return the permission key
   */
  private String fieldPermissionKey(RbacRoleFieldActionPermission permission) {
    return fieldPermissionKey(permission.getRoleId(), permission.getResourceFieldId(), permission.getAction());
  }

  /**
   * Creates a unique key for role-field-action permission lookup.
   *
   * @param roleId the role id
   * @param resourceFieldId the resource field id
   * @param action the field action
   * @return the permission key
   */
  private String fieldPermissionKey(UUID roleId, UUID resourceFieldId, RbacFieldAction action) {
    return roleId + ":" + resourceFieldId + ":" + action.name();
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
    RbacRoleResourcePermission permission = new RbacRoleResourcePermission();
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
  private void ensurePermissionAllowed(RbacRoleResourcePermission permission, Instant now) {
    if (permission == null || permission.isAllowed()) {
      return;
    }
    permission.setAllowed(true);
    permission.setUpdatedAt(now);
    entityManager.merge(permission);
  }

  /**
   * Persists a new role-field-action permission for the administrator role.
   *
   * @param roleId the role id
   * @param resourceFieldId the resource field id
   * @param action the field action
   * @param now the current timestamp
   */
  private void persistFieldActionPermission(UUID roleId, UUID resourceFieldId, RbacFieldAction action, Instant now) {
    if (resourceFieldId == null) {
      return;
    }
    RbacRoleFieldActionPermission permission = new RbacRoleFieldActionPermission();
    permission.setRoleId(roleId);
    permission.setResourceFieldId(resourceFieldId);
    permission.setAction(action);
    permission.setAllowed(true);
    permission.setSystemManaged(true);
    permission.setCreatedAt(now);
    permission.setUpdatedAt(now);
    entityManager.persist(permission);
  }

  /**
   * Ensures an existing field permission is marked as allowed.
   *
   * @param permission the existing field permission
   * @param now the current timestamp
   */
  private void ensureFieldPermissionAllowed(RbacRoleFieldActionPermission permission, Instant now) {
    if (permission == null || permission.isAllowed()) {
      return;
    }
    permission.setAllowed(true);
    permission.setUpdatedAt(now);
    entityManager.merge(permission);
  }

  /**
   * Persists a new resource field for a resource.
   *
   * @param resourceId the resource id
   * @param fieldName the normalized field name
   * @param sourceMethodName the source method name
   * @param now the current timestamp
   */
  private void persistResourceField(UUID resourceId, String fieldName, String sourceMethodName, Instant now) {
    RbacResourceField resourceField = new RbacResourceField();
    resourceField.setResourceId(resourceId);
    resourceField.setFieldName(fieldName);
    resourceField.setSourceMethodName(sourceMethodName);
    resourceField.setSystemManaged(true);
    resourceField.setCreatedAt(now);
    resourceField.setUpdatedAt(now);
    entityManager.persist(resourceField);
  }

  /**
   * Updates an existing synchronized resource field when it is system-managed.
   *
   * @param resourceField the existing resource field
   * @param sourceMethodName the current source method name
   * @param now the current timestamp
   */
  private void updateResourceField(RbacResourceField resourceField, String sourceMethodName, Instant now) {
    if (resourceField == null || !resourceField.isSystemManaged()) {
      return;
    }
    resourceField.setSourceMethodName(sourceMethodName);
    resourceField.setUpdatedAt(now);
    entityManager.merge(resourceField);
  }

  /**
   * Creates a unique key for resource field lookup.
   *
   * @param resourceField the resource field
   * @return the resource-field key
   */
  private String resourceFieldKey(RbacResourceField resourceField) {
    return resourceFieldKey(resourceField.getResourceId(), resourceField.getFieldName());
  }

  /**
   * Creates a unique key for resource field lookup.
   *
   * @param resourceId the resource id
   * @param fieldName the normalized field name
   * @return the resource-field key
   */
  private String resourceFieldKey(UUID resourceId, String fieldName) {
    return resourceId + ":" + fieldName;
  }

  /**
   * Extracts normalized field definitions from a GraphQL view type.
   *
   * @param viewClass the view class
   * @return a non-null list of normalized field methods
   */
  private List<FieldMethod> extractFieldMethods(Class<?> viewClass) {
    if (viewClass == null) {
      return List.of();
    }
    Map<String, FieldMethod> fieldsByName = new LinkedHashMap<>();
    List<Method> methods = List.of(viewClass.getMethods());
    methods.stream()
        .sorted(Comparator.comparingInt(method -> methodPriority(method.getName())))
        .forEach(method -> {
          if (method.getDeclaringClass() == Object.class) {
            return;
          }
          String normalized = normalizeFieldName(method.getName());
          if (normalized == null) {
            return;
          }
          fieldsByName.putIfAbsent(normalized, new FieldMethod(normalized, method.getName()));
        });
    return new ArrayList<>(fieldsByName.values());
  }

  /**
   * Returns method priority for field normalization selection.
   *
   * @param methodName the method name
   * @return the sort priority
   */
  private int methodPriority(String methodName) {
    if (methodName == null) {
      return Integer.MAX_VALUE;
    }
    if (methodName.startsWith("get")) {
      return 0;
    }
    if (methodName.startsWith("is")) {
      return 1;
    }
    if (methodName.startsWith("set")) {
      return 2;
    }
    return 3;
  }

  /**
   * Creates an in-memory placeholder resource field used to avoid duplicate inserts in one sync run.
   *
   * @param resourceId the resource identifier
   * @param fieldName the field name
   * @return the placeholder resource field
   */
  private RbacResourceField placeholderResourceField(UUID resourceId, String fieldName) {
    RbacResourceField field = new RbacResourceField();
    field.setResourceId(resourceId);
    field.setFieldName(fieldName);
    field.setSystemManaged(true);
    return field;
  }

  /**
   * Normalizes accessor method names to RBAC field names.
   *
   * @param methodName the raw method name
   * @return the normalized field name or null when method is not a field accessor
   */
  private String normalizeFieldName(String methodName) {
    if (methodName == null || methodName.isBlank()) {
      return null;
    }
    if (methodName.startsWith("get") || methodName.startsWith("set")) {
      return decapitalize(methodName.substring(3));
    }
    if (methodName.startsWith("is")) {
      return decapitalize(methodName.substring(2));
    }
    return null;
  }

  /**
   * Converts the first character of a string to lowercase.
   *
   * @param value the input string
   * @return a decapitalized value
   */
  private String decapitalize(String value) {
    if (value == null || value.isBlank()) {
      return value;
    }
    if (value.length() == 1) {
      return value.toLowerCase();
    }
    return Character.toLowerCase(value.charAt(0)) + value.substring(1);
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

  /**
   * Captures a normalized field name and source method.
   *
   * @param fieldName the normalized field name
   * @param sourceMethodName the source method name
   */
  private record FieldMethod(String fieldName, String sourceMethodName) {
  }
}
