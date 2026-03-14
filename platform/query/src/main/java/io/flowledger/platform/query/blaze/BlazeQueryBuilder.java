package io.flowledger.platform.query.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.FullQueryBuilder;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import io.flowledger.platform.query.QuerySystemException;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
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
  private final EntityManager entityManager;
  private final List<BlazeQueryBuilderExtension> extensions;

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
   * Applies equality filters to the criteria builder.
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
      String field = entry.getKey();
      if (field == null || field.isBlank()) {
        continue;
      }
      criteriaBuilder.where(field).eq(entry.getValue());
    }
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
