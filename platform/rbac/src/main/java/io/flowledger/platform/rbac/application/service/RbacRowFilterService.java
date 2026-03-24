package io.flowledger.platform.rbac.application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowledger.platform.graphql.application.GraphQlInternalException;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.rbac.domain.permission.entity.RbacRoleRowCondition;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Resolves row-level filter conditions for RBAC.
 */
@Component
@RequiredArgsConstructor
public class RbacRowFilterService {
  private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
  };

  private final RbacRoleResolver roleResolver;
  private final ObjectMapper objectMapper;
  private final BlazeQueryBuilder blazeQueryBuilder;

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Resolves row filter conditions for the current subject.
   *
   * @param resource the resource name
   * @return merged row filters
   */
  public Map<String, Object> resolveRowFilters(String resource) {
    RbacResource resourceEntity = findResource(resource);
    if (resourceEntity == null) {
      return Map.of();
    }
    List<RbacRole> roles = roleResolver.resolveRoles();
    if (roles.isEmpty()) {
      return Map.of();
    }
    List<UUID> roleIds = roles.stream().map(RbacRole::getId).toList();
    List<String> conditions = blazeQueryBuilder.getCriteriaBuilderFactory()
        .create(entityManager, String.class)
        .from(RbacRoleRowCondition.class, "r")
        .select("r.conditionJson")
        .where("r.roleId").in(roleIds)
        .where("r.resourceId").eq(resourceEntity.getId())
        .getResultList();
    Map<String, Object> merged = new HashMap<>();
    for (String condition : conditions) {
      Map<String, Object> parsed = parseCondition(condition);
      if (parsed == null || parsed.isEmpty()) {
        continue;
      }
      merged.putAll(parsed);
    }
    return merged;
  }

  /**
   * Parses a JSON condition into a filter map.
   *
   * @param condition the JSON condition
   * @return the parsed filter map
   */
  private Map<String, Object> parseCondition(String condition) {
    if (condition == null || condition.isBlank()) {
      return Map.of();
    }
    try {
      return objectMapper.readValue(condition, MAP_TYPE);
    } catch (IOException ex) {
      throw new GraphQlInternalException("Failed to parse RBAC row filter condition.", ex);
    }
  }

  /**
   * Finds the resource entity for a given name.
   *
   * @param resource the resource name
   * @return the resource entity or null when not found
   */
  private RbacResource findResource(String resource) {
    List<RbacResource> results = blazeQueryBuilder.forEntity(RbacResource.class)
        .where("name")
        .eq(resource)
        .setMaxResults(1)
        .getResultList();
    if (results.isEmpty()) {
      return null;
    }
    return results.getFirst();
  }
}
