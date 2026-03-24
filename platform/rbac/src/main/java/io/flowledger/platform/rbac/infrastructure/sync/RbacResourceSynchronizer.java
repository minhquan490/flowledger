package io.flowledger.platform.rbac.infrastructure.sync;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlModelRegistry;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlViewDefinition;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.rbac.domain.permission.entity.RbacRoleFieldActionPermission;
import io.flowledger.platform.rbac.domain.permission.entity.RbacRoleResourcePermission;
import io.flowledger.platform.rbac.domain.permission.view.RbacRoleFieldActionPermissionView;
import io.flowledger.platform.rbac.domain.permission.view.RbacRoleResourcePermissionView;
import io.flowledger.platform.rbac.domain.permission.view.mutation.RbacRoleFieldActionPermissionMutationView;
import io.flowledger.platform.rbac.domain.permission.view.mutation.RbacRoleResourcePermissionMutationView;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.rbac.domain.resource.entity.RbacResourceField;
import io.flowledger.platform.rbac.domain.resource.view.RbacResourceFieldView;
import io.flowledger.platform.rbac.domain.resource.view.RbacResourceReferenceView;
import io.flowledger.platform.rbac.domain.resource.view.RbacResourceView;
import io.flowledger.platform.rbac.domain.resource.view.mutation.RbacResourceFieldMutationView;
import io.flowledger.platform.rbac.domain.resource.view.mutation.RbacResourceMutationView;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacAction;
import io.flowledger.platform.rbac.domain.role.valueobject.RbacFieldAction;
import io.flowledger.platform.rbac.domain.role.view.RbacRoleView;
import io.flowledger.platform.rbac.domain.role.view.mutation.RbacRoleMutationView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
  private final BlazeQueryBuilder blazeQueryBuilder;
  private final EntityViewManager entityViewManager;
  private final EntityManagerFactory entityManagerFactory;

  /**
   * Synchronizes resources, default roles, and administrator permissions.
   *
   * <p>Runs within a Spring-managed transaction via {@link Transactional}. Mutation-view operations
   * resolve the transaction-bound entity manager from {@link EntityManagerFactoryUtils} to ensure
   * Blaze update operations execute with an active transaction.
   */
  @Transactional
  public void synchronize() {
    synchronizeWithinTransaction();
  }

  /**
   * Executes synchronization work inside the active Spring transaction.
   */
  private void synchronizeWithinTransaction() {
    Map<String, Class<?>> modelViews = collectModelViews();
    syncResources(modelViews.keySet().stream().toList());
    syncResourceFields(modelViews);
    ensureDefaultRole();
    RbacRoleView adminRole = ensureAdminRole();
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

    Map<String, RbacResourceView> existingByName = loadExistingResources(modelNames);
    Instant now = Instant.now();

    for (String model : modelNames) {
      if (existingByName.containsKey(model)) {
        updateResourceTimestamp(existingByName.get(model), now);
      } else {
        persistNewResource(model, now);
      }
    }
  }

  /**
   * Collects model names and associated view classes from the GraphQL model registry.
   *
   * @return a non-null map of model name to view class
   */
  private Map<String, Class<?>> collectModelViews() {
    return modelRegistry.getViewsByModel()
        .values()
        .stream()
        .filter(definition -> !(definition.isMutationView() && definition.isReferenceView()))
        .collect(Collectors.toMap(
            BlazeGraphQlViewDefinition::model,
            BlazeGraphQlViewDefinition::viewClass,
            (first, _) -> first,
            LinkedHashMap::new
        ));
  }

  /**
   * Loads existing resource views for the given names using batched {@code IN} queries.
   *
   * @param modelNames the full list of model names to look up
   * @return a map of resource name to view for all found records
   */
  private Map<String, RbacResourceView> loadExistingResources(List<String> modelNames) {
    List<RbacResourceView> results = new ArrayList<>();
    List<List<String>> batches = partition(modelNames);
    EntityViewSetting<RbacResourceView, CriteriaBuilder<RbacResourceView>> setting =
        EntityViewSetting.create(RbacResourceView.class);

    for (List<String> batch : batches) {
      CriteriaBuilder<RbacResource> criteriaBuilder = blazeQueryBuilder.forEntity(RbacResource.class);
      criteriaBuilder.where("name").in(batch);
      results.addAll(entityViewManager.applySetting(setting, criteriaBuilder).getResultList());
    }

    return results.stream()
        .collect(Collectors.toMap(RbacResourceView::getName, Function.identity()));
  }

  /**
   * Updates the {@code updatedAt} timestamp on an existing resource via mutation view.
   *
   * @param existing the existing resource view
   * @param now the current timestamp
   */
  private void updateResourceTimestamp(RbacResourceView existing, Instant now) {
    if (existing == null || existing.getId() == null) {
      return;
    }
    RbacResourceMutationView resource =
        entityViewManager.find(managedEntityManager(), RbacResourceMutationView.class, existing.getId());
    if (resource == null) {
      return;
    }
    resource.setUpdatedAt(now);
    entityViewManager.save(managedEntityManager(), resource);
  }

  /**
   * Persists a new resource using a mutation view.
   *
   * @param model the resource name
   * @param now the current timestamp
   */
  private void persistNewResource(String model, Instant now) {
    RbacResourceMutationView resource = entityViewManager.create(RbacResourceMutationView.class);
    resource.setName(model);
    resource.setCreatedAt(now);
    resource.setUpdatedAt(now);
    entityViewManager.save(managedEntityManager(), resource);
  }

  /**
   * Ensures a default role exists for users without explicit roles.
   */
  private void ensureDefaultRole() {
    CriteriaBuilder<RbacRole> criteriaBuilder = blazeQueryBuilder.forEntity(RbacRole.class);
    criteriaBuilder.where("defaultRole").eq(true);
    EntityViewSetting<RbacRoleView, CriteriaBuilder<RbacRoleView>> setting = EntityViewSetting.create(RbacRoleView.class);
    long defaultRoleCount = entityViewManager
        .applySetting(setting, criteriaBuilder)
        .getQueryRootCountQuery()
        .getSingleResult();
    boolean hasDefaultRole = defaultRoleCount > 0;
    if (hasDefaultRole) {
      return;
    }
    persistDefaultRole();
  }

  /**
   * Persists the default role.
   */
  private void persistDefaultRole() {
    Instant now = Instant.now();
    RbacRoleMutationView role = entityViewManager.create(RbacRoleMutationView.class);
    role.setCode(DEFAULT_ROLE_CODE);
    role.setName(DEFAULT_ROLE_NAME);
    role.setDefaultRole(true);
    role.setCreatedAt(now);
    role.setUpdatedAt(now);
    entityViewManager.save(managedEntityManager(), role);
  }

  /**
   * Ensures the administrator role exists.
   *
   * @return the administrator role view
   */
  private RbacRoleView ensureAdminRole() {
    CriteriaBuilder<RbacRole> criteriaBuilder = blazeQueryBuilder.forEntity(RbacRole.class);
    criteriaBuilder.where("code").eq(DEFAULT_ADMIN_ROLE_CODE).setMaxResults(1);
    EntityViewSetting<RbacRoleView, CriteriaBuilder<RbacRoleView>> setting = EntityViewSetting.create(RbacRoleView.class);
    List<RbacRoleView> results = entityViewManager.applySetting(setting, criteriaBuilder).getResultList();
    if (!results.isEmpty()) {
      return results.getFirst();
    }
    return persistAdminRole();
  }

  /**
   * Persists the administrator role using a mutation view.
   *
   * @return the newly persisted administrator role view
   */
  private RbacRoleView persistAdminRole() {
    Instant now = Instant.now();
    RbacRoleMutationView role = entityViewManager.create(RbacRoleMutationView.class);
    role.setCode(DEFAULT_ADMIN_ROLE_CODE);
    role.setName(DEFAULT_ADMIN_ROLE_NAME);
    role.setDefaultRole(false);
    role.setCreatedAt(now);
    role.setUpdatedAt(now);
    entityViewManager.save(managedEntityManager(), role);
    RbacRoleView createdRole = entityViewManager.find(managedEntityManager(), RbacRoleView.class, role.getId());
    if (createdRole == null) {
      throw new IllegalStateException("Admin role creation failed.");
    }
    return createdRole;
  }

  /**
   * Ensures the administrator role has full access to all registered resources.
   *
   * @param adminRole the administrator role view
   */
  private void ensureAdminRolePermissions(RbacRoleView adminRole) {
    if (adminRole == null || adminRole.getId() == null) {
      return;
    }
    CriteriaBuilder<RbacResource> criteriaBuilder = blazeQueryBuilder.forEntity(RbacResource.class);
    EntityViewSetting<RbacResourceView, CriteriaBuilder<RbacResourceView>> setting =
        EntityViewSetting.create(RbacResourceView.class);
    List<RbacResourceView> resources = entityViewManager.applySetting(setting, criteriaBuilder).getResultList();
    if (resources.isEmpty()) {
      return;
    }
    List<RbacRoleResourcePermissionView> existing = loadRoleResourcePermissions(adminRole.getId(), resources);
    Map<String, RbacRoleResourcePermissionView> existingByKey = existing.stream()
        .collect(Collectors.toMap(this::permissionKey, Function.identity()));
    Instant now = Instant.now();
    for (RbacResourceView resource : resources) {
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
   * @param adminRole the administrator role view
   */
  private void ensureAdminFieldPermissions(RbacRoleView adminRole) {
    if (adminRole == null || adminRole.getId() == null) {
      return;
    }
    CriteriaBuilder<RbacResourceField> criteriaBuilder =
        blazeQueryBuilder.forEntity(RbacResourceField.class);
    EntityViewSetting<RbacResourceFieldView, CriteriaBuilder<RbacResourceFieldView>> setting =
        EntityViewSetting.create(RbacResourceFieldView.class);
    List<RbacResourceFieldView> resourceFields = entityViewManager.applySetting(setting, criteriaBuilder).getResultList();
    if (resourceFields.isEmpty()) {
      return;
    }
    List<RbacRoleFieldActionPermissionView> existing = loadRoleFieldActionPermissions(adminRole.getId(), resourceFields);
    Map<String, RbacRoleFieldActionPermissionView> existingByKey = existing.stream()
        .collect(Collectors.toMap(this::fieldPermissionKey, Function.identity()));
    Instant now = Instant.now();
    for (RbacResourceFieldView resourceField : resourceFields) {
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
    List<RbacResourceView> resources = loadExistingResources(modelViews.keySet().stream().toList()).values().stream().toList();
    Map<String, RbacResourceView> resourcesByName = resources.stream()
        .collect(Collectors.toMap(RbacResourceView::getName, Function.identity()));
    CriteriaBuilder<RbacResourceField> criteriaBuilder = blazeQueryBuilder.forEntity(RbacResourceField.class);
    EntityViewSetting<RbacResourceFieldView, CriteriaBuilder<RbacResourceFieldView>> setting =
        EntityViewSetting.create(RbacResourceFieldView.class);
    List<RbacResourceFieldView> existingFields = entityViewManager.applySetting(setting, criteriaBuilder).getResultList();
    Map<String, RbacResourceFieldView> existingByKey = existingFields.stream()
        .collect(Collectors.toMap(this::resourceFieldKey, Function.identity(), (first, _) -> first));
    Set<String> expectedKeys = new java.util.HashSet<>();
    Instant now = Instant.now();
    for (Map.Entry<String, Class<?>> modelView : modelViews.entrySet()) {
      String model = modelView.getKey();
      RbacResourceView resource = resourcesByName.get(model);
      if (resource == null || resource.getId() == null) {
        continue;
      }
      for (FieldMethod fieldMethod : extractFieldMethods(modelView.getValue())) {
        String key = resourceFieldKey(resource.getId(), fieldMethod.fieldName());
        expectedKeys.add(key);
        if (!existingByKey.containsKey(key)) {
          persistResourceField(resource.getId(), fieldMethod.fieldName(), fieldMethod.sourceMethodName(), now);
          existingByKey.put(key, placeholderResourceField(resource.getId(), fieldMethod.fieldName()));
        } else {
          updateResourceField(existingByKey.get(key), fieldMethod.sourceMethodName(), now);
        }
      }
    }
    deleteStaleResourceFields(existingFields, expectedKeys);
  }

  /**
   * Deletes resource fields that are not present in the current model views.
   *
   * @param existingFields the current resource field views
   * @param expectedKeys the expected resource field keys
   */
  private void deleteStaleResourceFields(
      List<RbacResourceFieldView> existingFields,
      Set<String> expectedKeys
  ) {
    for (RbacResourceFieldView resourceField : existingFields) {
      if (resourceField == null) {
        continue;
      }
      String key = resourceFieldKey(resourceField);
      if (!expectedKeys.contains(key)) {
        removeResourceField(resourceField);
      }
    }
  }

  /**
   * Loads existing role-resource permissions for the given role and resources.
   *
   * @param roleId the role identifier
   * @param resources the resources to check
   * @return the existing permission views for the role
   */
  private List<RbacRoleResourcePermissionView> loadRoleResourcePermissions(
      UUID roleId,
      List<RbacResourceView> resources
  ) {
    List<UUID> resourceIds = resources.stream()
        .map(RbacResourceView::getId)
        .filter(Objects::nonNull)
        .toList();
    if (resourceIds.isEmpty()) {
      return List.of();
    }
    CriteriaBuilder<RbacRoleResourcePermission> criteriaBuilder = blazeQueryBuilder.forEntity(RbacRoleResourcePermission.class);
    criteriaBuilder.where("roleId").eq(roleId).where("resourceId").in(resourceIds);
    EntityViewSetting<RbacRoleResourcePermissionView, CriteriaBuilder<RbacRoleResourcePermissionView>> setting =
        EntityViewSetting.create(RbacRoleResourcePermissionView.class);
    return entityViewManager.applySetting(setting, criteriaBuilder).getResultList();
  }

  /**
   * Creates a unique key for a permission entry.
   *
   * @param permission the permission view
   * @return the permission key
   */
  private String permissionKey(RbacRoleResourcePermissionView permission) {
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
   * @return the existing role-field-action permission views
   */
  private List<RbacRoleFieldActionPermissionView> loadRoleFieldActionPermissions(
      UUID roleId,
      List<RbacResourceFieldView> resourceFields
  ) {
    List<UUID> resourceFieldIds = resourceFields.stream()
        .map(RbacResourceFieldView::getId)
        .filter(Objects::nonNull)
        .toList();
    if (resourceFieldIds.isEmpty()) {
      return List.of();
    }
    CriteriaBuilder<RbacRoleFieldActionPermission> criteriaBuilder = blazeQueryBuilder.forEntity(RbacRoleFieldActionPermission.class);
    criteriaBuilder.where("roleId").eq(roleId).where("resourceFieldId").in(resourceFieldIds);
    EntityViewSetting<RbacRoleFieldActionPermissionView, CriteriaBuilder<RbacRoleFieldActionPermissionView>> setting =
        EntityViewSetting.create(RbacRoleFieldActionPermissionView.class);
    return entityViewManager.applySetting(setting, criteriaBuilder).getResultList();
  }

  /**
   * Creates a unique key for a field permission entry.
   *
   * @param permission the field permission view
   * @return the permission key
   */
  private String fieldPermissionKey(RbacRoleFieldActionPermissionView permission) {
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
   * Persists a new role-resource permission for the administrator role using a mutation view.
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
    RbacRoleResourcePermissionMutationView permission = entityViewManager.create(RbacRoleResourcePermissionMutationView.class);
    permission.setRoleId(roleId);
    permission.setResourceId(resourceId);
    permission.setAction(action);
    permission.setAllowed(true);
    permission.setCreatedAt(now);
    permission.setUpdatedAt(now);
    entityViewManager.save(managedEntityManager(), permission);
  }

  /**
   * Ensures an existing permission is marked as allowed.
   *
   * @param permission the existing permission view
   * @param now the current timestamp
   */
  private void ensurePermissionAllowed(RbacRoleResourcePermissionView permission, Instant now) {
    if (permission == null || permission.isAllowed()) {
      return;
    }
    RbacRoleResourcePermissionMutationView updated =
        entityViewManager.find(managedEntityManager(), RbacRoleResourcePermissionMutationView.class, permission.getId());
    if (updated == null) {
      return;
    }
    updated.setAllowed(true);
    updated.setUpdatedAt(now);
    entityViewManager.save(managedEntityManager(), updated);
  }

  /**
   * Persists a new role-field-action permission for the administrator role using a mutation view.
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
    RbacRoleFieldActionPermissionMutationView permission =
        entityViewManager.create(RbacRoleFieldActionPermissionMutationView.class);
    permission.setRoleId(roleId);
    permission.setResourceFieldId(resourceFieldId);
    permission.setAction(action);
    permission.setAllowed(true);
    permission.setCreatedAt(now);
    permission.setUpdatedAt(now);
    entityViewManager.save(managedEntityManager(), permission);
  }

  /**
   * Ensures an existing field permission is marked as allowed.
   *
   * @param permission the existing field permission view
   * @param now the current timestamp
   */
  private void ensureFieldPermissionAllowed(RbacRoleFieldActionPermissionView permission, Instant now) {
    if (permission == null || permission.isAllowed()) {
      return;
    }
    RbacRoleFieldActionPermissionMutationView updated =
        entityViewManager.find(managedEntityManager(), RbacRoleFieldActionPermissionMutationView.class, permission.getId());
    if (updated == null) {
      return;
    }
    updated.setAllowed(true);
    updated.setUpdatedAt(now);
    entityViewManager.save(managedEntityManager(), updated);
  }

  /**
   * Persists a new resource field for a resource using a mutation view.
   *
   * @param resourceId the resource id
   * @param fieldName the normalized field name
   * @param sourceMethodName the source method name
   * @param now the current timestamp
   */
  private void persistResourceField(UUID resourceId, String fieldName, String sourceMethodName, Instant now) {
    RbacResourceFieldMutationView resourceField = entityViewManager.create(RbacResourceFieldMutationView.class);
    resourceField.setResource(entityViewManager.getReference(RbacResourceReferenceView.class, resourceId));
    resourceField.setFieldName(fieldName);
    resourceField.setSourceMethodName(sourceMethodName);
    resourceField.setCreatedAt(now);
    resourceField.setUpdatedAt(now);
    entityViewManager.save(managedEntityManager(), resourceField);
  }

  /**
   * Updates an existing synchronized resource field using a mutation view.
   *
   * @param resourceField the existing resource field view
   * @param sourceMethodName the current source method name
   * @param now the current timestamp
   */
  private void updateResourceField(RbacResourceFieldView resourceField, String sourceMethodName, Instant now) {
    if (resourceField == null || resourceField.getId() == null) {
      return;
    }
    RbacResourceFieldMutationView updated =
        entityViewManager.find(managedEntityManager(), RbacResourceFieldMutationView.class, resourceField.getId());
    if (updated == null) {
      return;
    }
    updated.setSourceMethodName(sourceMethodName);
    updated.setUpdatedAt(now);
    entityViewManager.save(managedEntityManager(), updated);
  }

  /**
   * Creates a unique key for resource field lookup.
   *
   * @param resourceField the resource field view
   * @return the resource-field key
   */
  private String resourceFieldKey(RbacResourceFieldView resourceField) {
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
  private RbacResourceFieldView placeholderResourceField(UUID resourceId, String fieldName) {
    return new RbacResourceFieldView() {
      @Override
      public UUID getId() {
        return null;
      }

      @Override
      public UUID getResourceId() {
        return resourceId;
      }

      @Override
      public String getFieldName() {
        return fieldName;
      }

      @Override
      public String getSourceMethodName() {
        return null;
      }
    };
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
   * Removes a resource field using a mutation view.
   *
   * @param resourceField the resource field view
   */
  private void removeResourceField(RbacResourceFieldView resourceField) {
    if (resourceField == null || resourceField.getId() == null) {
      return;
    }
    entityViewManager.remove(managedEntityManager(), RbacResourceFieldMutationView.class, resourceField.getId());
  }

  /**
   * Returns the transaction-bound entity manager used by the query builder.
   *
   * @return the transaction-bound entity manager
   */
  private EntityManager managedEntityManager() {
    EntityManager transactionalEntityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
    if (transactionalEntityManager == null) {
      throw new IllegalStateException("No transaction-bound EntityManager available for mutation view operation.");
    }
    return transactionalEntityManager;
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
