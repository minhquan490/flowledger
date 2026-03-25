package io.flowledger.platform.rbac.infrastructure.blaze;

import io.flowledger.platform.query.blaze.BlazeQueryBuilderExtension;
import io.flowledger.platform.rbac.application.context.RbacRequestContextHolder;
import io.flowledger.platform.rbac.application.service.RbacRowFilterService;
import io.flowledger.platform.rbac.infrastructure.autoconfigure.RbacProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Blaze query builder extension that applies RBAC-aware filter and field customization.
 */
@RequiredArgsConstructor
@Component
public class BlazeRbacQueryBuilderExtension implements BlazeQueryBuilderExtension {
  private final RbacRowFilterService rowFilterService;
  private final RbacProperties properties;

  /**
   * Customizes filters by applying RBAC constraints.
   *
   * @param filters incoming filters
   * @return RBAC-adjusted filters
   */
  @Override
  public Map<String, Object> customizeFilters(@NonNull Map<String, Object> filters) {
    if (!properties.isEnabled()) {
      return filters;
    }
    return RbacRequestContextHolder.get()
        .map(context -> applyRowFilters(context.resource(), filters))
        .orElse(filters);
  }

  /**
   * Customizes projected fields by applying RBAC constraints.
   *
   * @param fields incoming field selections
   * @return RBAC-adjusted fields
   */
  @Override
  public List<String> customizeFields(@NonNull List<String> fields) {
    return fields;
  }

  /**
   * Applies row filters for the resource and clears the request context.
   *
   * @param resource the resource name
   * @param filters incoming filters
   * @return merged filters
   */
  private Map<String, Object> applyRowFilters(String resource, Map<String, Object> filters) {
    Map<String, Object> merged = new HashMap<>(filters);
    Map<String, Object> rowFilters = rowFilterService.resolveRowFilters(resource);
    if (rowFilters != null && !rowFilters.isEmpty()) {
      merged.putAll(rowFilters);
    }
    RbacRequestContextHolder.clear();
    return merged;
  }
}
