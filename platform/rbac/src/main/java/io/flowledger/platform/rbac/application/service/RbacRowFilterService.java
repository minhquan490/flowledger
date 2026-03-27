package io.flowledger.platform.rbac.application.service;

import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.query.blaze.filter.BlazeFilterSyntax;
import io.flowledger.platform.query.blaze.filter.LogicalOperator;
import io.flowledger.platform.rbac.domain.permission.entity.RbacRoleRowCondition;
import io.flowledger.platform.rbac.domain.permission.valueobject.RbacRowConditionJson;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.rbac.domain.role.aggregate.RbacRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Resolves row-level filter conditions for RBAC.
 */
@Component
public class RbacRowFilterService {
  private final RbacRoleResolver roleResolver;
  private final ObjectMapper objectMapper;
  private final BlazeQueryBuilder blazeQueryBuilder;

  @PersistenceContext
  private EntityManager entityManager;

  public RbacRowFilterService(
      RbacRoleResolver roleResolver,
      ObjectMapper objectMapper,
      @Lazy BlazeQueryBuilder blazeQueryBuilder
  ) {
    this.roleResolver = roleResolver;
    this.objectMapper = objectMapper;
    this.blazeQueryBuilder = blazeQueryBuilder;
  }

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
    RbacRowConditionJson rowCondition = objectMapper.readValue(condition, RbacRowConditionJson.class);
    return toBlazeFilters(rowCondition);
  }

  /**
   * Converts a row condition tree into Blaze filter syntax.
   *
   * @param conditionJson row condition root
   * @return blaze-compatible filter map
   */
  Map<String, Object> toBlazeFilters(RbacRowConditionJson conditionJson) {
    if (conditionJson == null || conditionJson.kind() == null) {
      return Map.of();
    }
    if (conditionJson.kind() == RbacRowConditionJson.NodeKind.RULE) {
      return toRuleFilter(conditionJson);
    }
    return toGroupFilter(conditionJson);
  }

  /**
   * Converts a single rule node into Blaze filter syntax.
   *
   * @param ruleNode rule node
   * @return blaze-compatible rule filter
   */
  private Map<String, Object> toRuleFilter(RbacRowConditionJson ruleNode) {
    if (ruleNode.field() == null || ruleNode.field().isBlank() || ruleNode.op() == null) {
      return Map.of();
    }
    Map<String, Object> operatorPayload = new LinkedHashMap<>();
    operatorPayload.put(BlazeFilterSyntax.OPERATOR_KEY, ruleNode.op().keyword());
    operatorPayload.put(BlazeFilterSyntax.OPERATOR_VALUE_KEY, ruleNode.value());
    return Map.of(ruleNode.field(), operatorPayload);
  }

  /**
   * Converts a group node into Blaze filter syntax.
   *
   * @param groupNode group node
   * @return blaze-compatible group filter
   */
  private Map<String, Object> toGroupFilter(RbacRowConditionJson groupNode) {
    List<RbacRowConditionJson> children = groupNode.children();
    if (children == null || children.isEmpty()) {
      return Map.of();
    }
    if (groupNode.logicalOp() == LogicalOperator.OR) {
      return toOrGroupFilter(children);
    }
    return toAndGroupFilter(children);
  }

  /**
   * Converts OR children into Blaze OR-clause syntax.
   *
   * @param children group children
   * @return blaze-compatible OR filter
   */
  private Map<String, Object> toOrGroupFilter(List<RbacRowConditionJson> children) {
    List<Map<String, Object>> clauses = new ArrayList<>();
    for (RbacRowConditionJson child : children) {
      Map<String, Object> childFilter = toBlazeFilters(child);
      if (!childFilter.isEmpty()) {
        clauses.add(childFilter);
      }
    }
    if (clauses.isEmpty()) {
      return Map.of();
    }
    return Map.of(BlazeFilterSyntax.LOGICAL_OR, clauses);
  }

  /**
   * Converts AND children into Blaze merged-clause syntax.
   *
   * @param children group children
   * @return blaze-compatible AND filter
   */
  private Map<String, Object> toAndGroupFilter(List<RbacRowConditionJson> children) {
    Map<String, Object> merged = new LinkedHashMap<>();
    for (RbacRowConditionJson child : children) {
      Map<String, Object> childFilter = toBlazeFilters(child);
      if (!childFilter.isEmpty()) {
        merged.putAll(childFilter);
      }
    }
    return merged;
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
