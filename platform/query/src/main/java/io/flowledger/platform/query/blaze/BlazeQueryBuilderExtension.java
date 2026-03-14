package io.flowledger.platform.query.blaze;

import java.util.List;
import java.util.Map;
import lombok.NonNull;

/**
 * Extension hook for {@link BlazeQueryBuilder} to support concerns such as RBAC.
 *
 * <p>Implementations can filter or enrich query inputs before they are applied.
 */
public interface BlazeQueryBuilderExtension {

  /**
   * Allows customization of key filters used for read requests.
   *
   * @param keyFilters key filters
   * @return modified key filters
   */
  default Map<String, Object> customizeKeyFilters(@NonNull Map<String, Object> keyFilters) {
    return keyFilters;
  }

  /**
   * Allows customization of general filters.
   *
   * @param filters filters
   * @return modified filters
   */
  default Map<String, Object> customizeFilters(@NonNull Map<String, Object> filters) {
    return filters;
  }

  /**
   * Allows customization of projected fields.
   *
   * @param fields fields
   * @return modified fields
   */
  default List<String> customizeFields(@NonNull List<String> fields) {
    return fields;
  }

  /**
   * Allows customization of sort inputs.
   *
   * @param sortInputs sort inputs
   * @param <S> sort input type
   * @return modified sort inputs
   */
  default <S> List<S> customizeSortInputs(@NonNull List<S> sortInputs) {
    return sortInputs;
  }
}
