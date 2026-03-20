package io.flowledger.platform.query.blaze;

import com.blazebit.persistence.BaseWhereBuilder;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.FullQueryBuilder;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.WhereAndBuilder;
import com.blazebit.persistence.WhereOrBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import io.flowledger.platform.query.QuerySystemException;
import io.flowledger.platform.query.blaze.filter.BlazeFilterOperators;
import io.flowledger.platform.query.blaze.filter.BlazeFilterOperatorRegistry;
import io.flowledger.platform.query.blaze.filter.BlazeFilterSyntax;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * Builder for Blaze-Persistence criteria queries with filter application support.
 *
 * <p>This builder helps construct a {@link CriteriaBuilder} and apply simple equality filters,
 * sorting, field selection, and pagination that translate into query clauses.
 */
@Getter
@Component
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@ConditionalOnMissingBean(BlazeQueryBuilder.class)
public class BlazeQueryBuilder {
  private final CriteriaBuilderFactory criteriaBuilderFactory;
  private final List<BlazeQueryBuilderExtension> extensions;
  private final BlazeFilterOperatorRegistry filterOperatorRegistry = BlazeFilterOperatorRegistry.defaultRegistry();

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Creates a {@link CriteriaBuilder} for the given entity type.
   *
   * @param entityClass the entity class
   * @param <T> entity type for the query
   * @return the criteria builder
   */
  public <T> CriteriaBuilder<T> forEntity(Class<T> entityClass) {
    return criteriaBuilderFactory.create(entityManager, entityClass);
  }

  /**
   * Applies key filters to the criteria builder.
   *
   * @param criteriaBuilder the criteria builder
   * @param keyFilters      key filters keyed by field name
   * @param <T>             entity type for the query
   * @throws QuerySystemException when no key filters are provided
   */
  public <T> void applyKeyFilters(
      CriteriaBuilder<T> criteriaBuilder,
      Map<String, Object> keyFilters
  ) {
    Map<String, Object> resolvedFilters = applyKeyExtensions(keyFilters);
    if (resolvedFilters == null || resolvedFilters.isEmpty()) {
      throw new QuerySystemException("Read request must include a key.");
    }
    applyFilter(criteriaBuilder, resolvedFilters);
  }

  /**
   * Applies structured filters to the criteria builder.
   *
   * <p>Example JSON:
   * <pre>{@code
   * {
   *   "status": { "op": "eq", "value": "ACTIVE" },
   *   "amount": { "op": "between", "value": { "from": 100, "to": 500 } },
   *   "orgId": { "op": "in", "value": ["org-1", "org-2"] },
   *   "name": { "op": "ilike", "value": "%ledger%" },
   *   "deletedAt": { "op": "isNull" },
   *   "or": [
   *     { "priority": { "op": "gt", "value": 3 } },
   *     { "tier": { "op": "eq", "value": "GOLD" } }
   *   ]
   * }
   * }</pre>
   *
   * @param criteriaBuilder the criteria builder
   * @param filters         filter map keyed by field name
   * @param <T>             entity type for the query
   */
  public <T> void applyFilter(
      CriteriaBuilder<T> criteriaBuilder,
      Map<String, Object> filters
  ) {
    Map<String, Object> resolvedFilters = applyFilterExtensions(filters);
    if (resolvedFilters == null || resolvedFilters.isEmpty()) {
      return;
    }
    for (Map.Entry<String, Object> entry : resolvedFilters.entrySet()) {
      applyFilterEntry(criteriaBuilder, entry);
    }
  }

  /**
   * Applies a single filter entry to the criteria builder.
   *
   * @param criteriaBuilder the criteria builder
   * @param entry the filter entry
   * @param <T> entity type for the query
   */
  private <T> void applyFilterEntry(CriteriaBuilder<T> criteriaBuilder, Map.Entry<String, Object> entry) {
    String field = entry.getKey();
    if (field == null || field.isBlank()) {
      return;
    }
    if (isLogicalOrKey(field)) {
      applyLogicalOr(criteriaBuilder, entry.getValue());
      return;
    }
    applyFieldPredicate(criteriaBuilder, field, entry.getValue());
  }

  /**
   * Applies a single field predicate to the criteria builder.
   *
   * @param criteriaBuilder the criteria builder
   * @param field the field name
   * @param rawValue the raw predicate value
   * @param <T> entity type for the query
   */
  private <T> void applyFieldPredicate(CriteriaBuilder<T> criteriaBuilder, String field, Object rawValue) {
    applyFieldPredicate((BaseWhereBuilder<?>) criteriaBuilder, field, rawValue);
  }

  /**
   * Applies a single field predicate to a generic where builder.
   *
   * @param whereBuilder the target where builder
   * @param field the field name
   * @param rawValue the raw predicate value
   */
  private void applyFieldPredicate(BaseWhereBuilder<?> whereBuilder, String field, Object rawValue) {
    if (!(rawValue instanceof Map<?, ?> valueMap) || !valueMap.containsKey(BlazeFilterSyntax.OPERATOR_KEY)) {
      filterOperatorRegistry.apply(BlazeFilterOperators.EQ, whereBuilder, field, rawValue);
      return;
    }
    String operator = resolveOperator(valueMap);
    Object operatorValue = valueMap.get(BlazeFilterSyntax.OPERATOR_VALUE_KEY);
    filterOperatorRegistry.apply(operator, whereBuilder, field, operatorValue);
  }

  /**
   * Applies OR predicates from a value collection.
   *
   * @param criteriaBuilder the criteria builder
   * @param logicalOrValue the OR value that contains disjunctive clauses
   * @param <T> entity type for the query
   */
  private <T> void applyLogicalOr(CriteriaBuilder<T> criteriaBuilder, Object logicalOrValue) {
    Collection<Map<String, Object>> clauses = resolveOrClauses(logicalOrValue);
    if (clauses.isEmpty()) {
      return;
    }
    WhereOrBuilder<CriteriaBuilder<T>> whereOrBuilder = criteriaBuilder.whereOr();
    for (Map<String, Object> clause : clauses) {
      WhereAndBuilder<WhereOrBuilder<CriteriaBuilder<T>>> whereAndBuilder = whereOrBuilder.whereAnd();
      applyAndClause(whereAndBuilder, clause);
      whereOrBuilder = whereAndBuilder.endAnd();
    }
    whereOrBuilder.endOr();
  }

  /**
   * Applies AND predicates inside a single OR clause.
   *
   * @param whereAndBuilder the and builder
   * @param clause the clause map
   * @param <T> parent builder type
   */
  private <T> void applyAndClause(WhereAndBuilder<T> whereAndBuilder, Map<String, Object> clause) {
    if (clause == null || clause.isEmpty()) {
      return;
    }
    for (Map.Entry<String, Object> entry : clause.entrySet()) {
      String field = entry.getKey();
      if (field == null || field.isBlank() || isLogicalOrKey(field)) {
        continue;
      }
      applyFieldPredicate(whereAndBuilder, field, entry.getValue());
    }
  }

  /**
   * Applies a single field predicate to a where-and builder.
   *
   * @param whereAndBuilder the and builder
   * @param field the field name
   * @param rawValue the raw predicate value
   * @param <T> parent builder type
   */
  private <T> void applyFieldPredicate(WhereAndBuilder<T> whereAndBuilder, String field, Object rawValue) {
    applyFieldPredicate((BaseWhereBuilder<?>) whereAndBuilder, field, rawValue);
  }

  /**
   * Resolves the operator from the filter operator map.
   *
   * @param valueMap map containing operator metadata
   * @return normalized operator string
   */
  private String resolveOperator(Map<?, ?> valueMap) {
    Object operatorValue = valueMap.get(BlazeFilterSyntax.OPERATOR_KEY);
    if (!(operatorValue instanceof String operator) || operator.isBlank()) {
      throw new QuerySystemException("Filter operator must be a non-empty string.");
    }
    return BlazeFilterOperators.normalize(operator);
  }

  /**
   * Resolves OR clauses from a filter value.
   *
   * @param logicalOrValue the OR value
   * @return disjunctive clauses
   */
  private Collection<Map<String, Object>> resolveOrClauses(Object logicalOrValue) {
    if (!(logicalOrValue instanceof Collection<?> values)) {
      throw new QuerySystemException("OR filter must be a collection of filter objects.");
    }
    List<Map<String, Object>> clauses = new ArrayList<>();
    for (Object value : values) {
      if (!(value instanceof Map<?, ?> rawClause)) {
        throw new QuerySystemException("OR filter contains a non-object clause.");
      }
      @SuppressWarnings("unchecked")
      Map<String, Object> clause = (Map<String, Object>) rawClause;
      clauses.add(clause);
    }
    return clauses;
  }

  /**
   * Indicates whether a key is used for OR logical grouping.
   *
   * @param key filter key
   * @return {@code true} when key is OR logical marker
   */
  private boolean isLogicalOrKey(String key) {
    return BlazeFilterSyntax.LOGICAL_OR.equalsIgnoreCase(key)
        || BlazeFilterSyntax.LOGICAL_OR_ALIAS.equalsIgnoreCase(key);
  }

  /**
   * Applies sorting directives to the criteria builder using the provided accessors.
   *
   * @param criteriaBuilder the criteria builder
   * @param sortInputs the sort inputs
   * @param fieldExtractor extractor for the sort field
   * @param descendingExtractor extractor for descending indicator
   * @param <T> entity type for the query
   * @param <S> sort input type
   */
  public <T, S> void applySort(
      CriteriaBuilder<T> criteriaBuilder,
      List<S> sortInputs,
      Function<S, String> fieldExtractor,
      Predicate<S> descendingExtractor
  ) {
    List<S> resolvedSortInputs = applySortExtensions(sortInputs);
    if (resolvedSortInputs == null || resolvedSortInputs.isEmpty()) {
      return;
    }
    for (S sortInput : resolvedSortInputs) {
      if (sortInput != null) {
        String field = fieldExtractor.apply(sortInput);
        if (field != null && !field.isBlank()) {
          boolean descending = descendingExtractor.test(sortInput);
          if (descending) {
            criteriaBuilder.orderByDesc(field);
          } else {
            criteriaBuilder.orderByAsc(field);
          }
        }
      }
    }
  }

  /**
   * Applies field projections to the entity view setting.
   *
   * @param setting the entity view setting
   * @param fields requested fields
   */
  public void applyFields(EntityViewSetting<?, ?> setting, List<String> fields) {
    List<String> resolvedFields = applyFieldExtensions(fields);
    if (resolvedFields == null || resolvedFields.isEmpty()) {
      return;
    }
    for (String field : resolvedFields) {
      if (field == null || field.isBlank()) {
        continue;
      }
      setting.fetch(field);
    }
  }

  /**
   * Applies pagination to the view query using offset/limit values.
   *
   * @param viewQuery the view query
   * @param offset the offset or null for default
   * @param limit the limit or null for default
   * @return paginated criteria builder
   */
  public PaginatedCriteriaBuilder<?> applyPagination(
      FullQueryBuilder<?, ?> viewQuery,
      Integer offset,
      Integer limit
  ) {
    int resolvedOffset = offset == null ? 0 : offset;
    int resolvedLimit = limit == null ? 50 : limit;
    return viewQuery.page(resolvedOffset, resolvedLimit);
  }

  /**
   * Applies extension hooks to key filters for read requests.
   *
   * @param keyFilters key filters
   * @return possibly modified key filters
   */
  private Map<String, Object> applyKeyExtensions(Map<String, Object> keyFilters) {
    Map<String, Object> current = keyFilters == null ? Map.of() : keyFilters;
    if (extensions == null || extensions.isEmpty()) {
      return current;
    }
    for (BlazeQueryBuilderExtension extension : extensions) {
      if (extension == null) {
        continue;
      }
      current = extension.customizeKeyFilters(current);
    }
    return current;
  }

  /**
   * Applies extension hooks to filters.
   *
   * @param filters filters
   * @return possibly modified filters
   */
  private Map<String, Object> applyFilterExtensions(Map<String, Object> filters) {
    Map<String, Object> current = filters == null ? Map.of() : filters;
    if (extensions == null || extensions.isEmpty()) {
      return current;
    }
    for (BlazeQueryBuilderExtension extension : extensions) {
      if (extension == null) {
        continue;
      }
      current = extension.customizeFilters(current);
    }
    return current;
  }

  /**
   * Applies extension hooks to selected fields.
   *
   * @param fields selected fields
   * @return possibly modified fields
   */
  private List<String> applyFieldExtensions(List<String> fields) {
    List<String> current = fields == null ? List.of() : fields;
    if (extensions == null || extensions.isEmpty()) {
      return current;
    }
    for (BlazeQueryBuilderExtension extension : extensions) {
      if (extension == null) {
        continue;
      }
      current = extension.customizeFields(current);
    }
    return current;
  }

  /**
   * Applies extension hooks to sort inputs.
   *
   * @param sortInputs sort inputs
   * @param <S> sort input type
   * @return possibly modified sort inputs
   */
  private <S> List<S> applySortExtensions(List<S> sortInputs) {
    List<S> current = sortInputs == null ? List.of() : sortInputs;
    if (extensions == null || extensions.isEmpty()) {
      return current;
    }
    for (BlazeQueryBuilderExtension extension : extensions) {
      if (extension == null) {
        continue;
      }
      current = extension.customizeSortInputs(current);
    }
    return current;
  }
}
