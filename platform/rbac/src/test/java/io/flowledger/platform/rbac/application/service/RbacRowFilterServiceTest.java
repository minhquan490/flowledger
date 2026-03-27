package io.flowledger.platform.rbac.application.service;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.RestrictionBuilder;
import com.blazebit.persistence.WhereAndBuilder;
import com.blazebit.persistence.WhereOrBuilder;
import tools.jackson.databind.ObjectMapper;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.query.blaze.filter.BlazeFilterOperators;
import io.flowledger.platform.query.blaze.filter.LogicalOperator;
import io.flowledger.platform.rbac.domain.permission.valueobject.RbacRowConditionJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link RbacRowFilterService} row-condition mapping and filter application.
 *
 * <p>Tests are grouped into nested classes by responsibility:
 * <ul>
 *   <li>{@link ToBlazeFilters} — JSON condition tree → filter map conversion</li>
 *   <li>{@link ApplyFilter} — filter map → Blaze criteria builder application</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class RbacRowFilterServiceTest {

  @Mock
  private RbacRoleResolver roleResolver;

  @Mock
  private BlazeQueryBuilder blazeQueryBuilder;

  private RbacRowFilterService service;

  @BeforeEach
  void setUp() {
    service = new RbacRowFilterService(roleResolver, new ObjectMapper(), blazeQueryBuilder);
  }

  private static RbacRowConditionJson rule(String id, String field, BlazeFilterOperators op, String value) {
    return new RbacRowConditionJson(
        RbacRowConditionJson.NodeKind.RULE,
        id,
        LogicalOperator.AND,
        List.of(),
        field,
        op,
        value
    );
  }

  private static RbacRowConditionJson group(String id, LogicalOperator operator, List<RbacRowConditionJson> children) {
    return new RbacRowConditionJson(
        RbacRowConditionJson.NodeKind.GROUP,
        id,
        operator,
        children,
        "",
        BlazeFilterOperators.EQ,
        ""
    );
  }

  private static RbacRowConditionJson andGroupWith(RbacRowConditionJson... children) {
    return group("root", LogicalOperator.AND, List.of(children));
  }

  private static RbacRowConditionJson orGroupWith(RbacRowConditionJson... children) {
    return group("g-or", LogicalOperator.OR, List.of(children));
  }

  /**
   * Tests for {@link RbacRowFilterService#toBlazeFilters(RbacRowConditionJson)}.
   */
  @Nested
  @DisplayName("toBlazeFilters")
  class ToBlazeFilters {

    @Test
    @DisplayName("single rule produces a simple field predicate map")
    void singleRuleProducesSimpleFieldPredicate() {
      RbacRowConditionJson rule = rule("r1", "email", BlazeFilterOperators.LIKE, "%@flowledger.io");

      Map<String, Object> filters = service.toBlazeFilters(rule);

      assertThat(filters)
          .containsOnlyKeys("email")
          .containsEntry("email", Map.of("op", "like", "value", "%@flowledger.io"));
    }

    @Test
    @DisplayName("AND group with multiple rules flattens all predicates into one map")
    void andGroupWithMultipleRulesFlattensPredicates() {
      RbacRowConditionJson root = andGroupWith(
          rule("r1", "status", BlazeFilterOperators.EQ, "ACTIVE"),
          rule("r2", "country", BlazeFilterOperators.EQ, "US")
      );

      Map<String, Object> filters = service.toBlazeFilters(root);

      assertThat(filters)
          .containsEntry("status", Map.of("op", "eq", "value", "ACTIVE"))
          .containsEntry("country", Map.of("op", "eq", "value", "US"));
    }

    @Test
    @DisplayName("OR subgroup is nested under 'or' key with each child as a separate entry")
    void orSubgroupIsNestedUnderOrKey() {
      RbacRowConditionJson root = andGroupWith(
          rule("r1", "status", BlazeFilterOperators.EQ, "ACTIVE"),
          orGroupWith(
              rule("r2", "type", BlazeFilterOperators.EQ, "BANK"),
              rule("r3", "country", BlazeFilterOperators.EQ, "US")
          )
      );

      Map<String, Object> filters = service.toBlazeFilters(root);

      assertThat(filters)
          .containsEntry("or", List.of(
              Map.of("type", Map.of("op", "eq", "value", "BANK")),
              Map.of("country", Map.of("op", "eq", "value", "US"))
          ))
          .containsEntry("status", Map.of("op", "eq", "value", "ACTIVE"))
          .containsKey("or");
    }

    @Test
    @DisplayName("nested AND group inside OR group is mapped correctly")
    void nestedAndGroupInsideOrGroupMappedCorrectly() {
      RbacRowConditionJson nestedAnd = group("g-and", LogicalOperator.AND, List.of(
          rule("r1", "type", BlazeFilterOperators.EQ, "BANK"),
          rule("r2", "currency", BlazeFilterOperators.EQ, "USD")
      ));
      RbacRowConditionJson root = andGroupWith(orGroupWith(nestedAnd));

      Map<String, Object> filters = service.toBlazeFilters(root);

      assertThat(filters).containsKey("or");
      // nested AND entry inside OR should preserve both fields
      @SuppressWarnings("unchecked")
      List<Map<String, Object>> orList = (List<Map<String, Object>>) filters.get("or");
      assertThat(orList).hasSize(1);
      assertThat(orList.getFirst())
          .containsEntry("type", Map.of("op", "eq", "value", "BANK"))
          .containsEntry("currency", Map.of("op", "eq", "value", "USD"));
    }

    @Test
    @DisplayName("empty group returns empty filter map")
    void emptyGroupReturnsEmptyFilterMap() {
      RbacRowConditionJson emptyGroup = group("g-empty", LogicalOperator.AND, List.of());

      Map<String, Object> filters = service.toBlazeFilters(emptyGroup);

      assertThat(filters).isEmpty();
    }

    @Test
    @DisplayName("group with only empty-child OR group returns empty filter map")
    void groupWithEmptyOrSubgroupReturnsEmptyMap() {
      RbacRowConditionJson root = andGroupWith(orGroupWith());

      Map<String, Object> filters = service.toBlazeFilters(root);

      assertThat(filters).isEmpty();
    }

  }

  /**
   * Tests for {@link RbacRowFilterService} filter application to a Blaze
   * {@link CriteriaBuilder}.
   */
  @Nested
  @DisplayName("applyFilter")
  class ApplyFilter {

    private CriteriaBuilder<Object> criteriaBuilder;
    private BlazeQueryBuilder builder;

    @BeforeEach
    void setUpCriteriaBuilder() {
      CriteriaBuilderFactory factory = Mockito.mock(CriteriaBuilderFactory.class);
      builder = new BlazeQueryBuilder(factory, List.of());
      criteriaBuilder = mockCriteriaBuilderChain();
    }

    @Test
    @DisplayName("simple field predicate calls where() exactly once with the correct field name")
    void simplePredicateCallsWhereOnce() {
      Map<String, Object> filters = Map.of(
          "status", Map.of("op", "eq", "value", "ACTIVE")
      );

      builder.applyFilter(criteriaBuilder, filters);

      verify(criteriaBuilder, times(1)).where("status");
    }

    @Test
    @DisplayName("OR group calls whereOr() exactly once")
    void orGroupCallsWhereOrOnce() {
      Map<String, Object> filters = Map.of(
          "or", List.of(
              Map.of("type", Map.of("op", "eq", "value", "BANK")),
              Map.of("country", Map.of("op", "eq", "value", "US"))
          )
      );

      builder.applyFilter(criteriaBuilder, filters);

      verify(criteriaBuilder, times(1)).whereOr();
    }

    @Test
    @DisplayName("combined AND+OR filter calls both where() and whereOr() the correct number of times")
    void combinedAndOrFilterCallsCorrectMethods() {
      Map<String, Object> filters = Map.of(
          "status", Map.of("op", "eq", "value", "ACTIVE"),
          "or", List.of(
              Map.of("type", Map.of("op", "eq", "value", "BANK")),
              Map.of("country", Map.of("op", "eq", "value", "US"))
          )
      );

      builder.applyFilter(criteriaBuilder, filters);

      verify(criteriaBuilder, times(1)).where("status");
      verify(criteriaBuilder, times(1)).whereOr();
    }

    @Test
    @DisplayName("empty filter map does not call where() or whereOr()")
    void emptyFilterMapCallsNoCriteriaMethods() {
      builder.applyFilter(criteriaBuilder, Map.of());

      verify(criteriaBuilder, times(0)).where(anyString());
      verify(criteriaBuilder, times(0)).whereOr();
    }

    /**
     * Wires a full Blaze where-builder mock chain covering AND, OR, and
     * root-level restriction paths used by {@code applyFilter}.
     */
    @SuppressWarnings("unchecked")
    private CriteriaBuilder<Object> mockCriteriaBuilderChain() {
      CriteriaBuilder<Object> cb = Mockito.mock(CriteriaBuilder.class);

      RestrictionBuilder<CriteriaBuilder<Object>> rootRestriction = Mockito.mock(RestrictionBuilder.class);
      Mockito.lenient().when(cb.where(anyString())).thenReturn(rootRestriction);

      WhereOrBuilder<CriteriaBuilder<Object>> whereOrBuilder = Mockito.mock(WhereOrBuilder.class);
      Mockito.lenient().when(cb.whereOr()).thenReturn(whereOrBuilder);
      Mockito.lenient().when(whereOrBuilder.endOr()).thenReturn(cb);

      WhereAndBuilder<WhereOrBuilder<CriteriaBuilder<Object>>> whereAndBuilder = Mockito.mock(WhereAndBuilder.class);
      Mockito.lenient().when(whereOrBuilder.whereAnd()).thenReturn(whereAndBuilder);
      Mockito.lenient().when(whereAndBuilder.endAnd()).thenReturn(whereOrBuilder);

      RestrictionBuilder<WhereAndBuilder<WhereOrBuilder<CriteriaBuilder<Object>>>> andRestriction =
          Mockito.mock(RestrictionBuilder.class);
      Mockito.lenient().when(whereAndBuilder.where(anyString())).thenReturn(andRestriction);

      return cb;
    }
  }
}
