package io.flowledger.platform.graphql.application;

import io.flowledger.platform.graphql.domain.GraphQlPageInput;
import io.flowledger.platform.graphql.domain.GraphQlReadRequest;
import io.flowledger.platform.graphql.domain.GraphQlReadResult;
import io.flowledger.platform.graphql.domain.GraphQlSearchRequest;
import io.flowledger.platform.graphql.domain.GraphQlSearchResult;
import io.flowledger.platform.graphql.domain.GraphQlSortInput;
import java.util.List;
import java.util.Map;

/**
 * Dispatches GraphQL read and search requests to registered handlers.
 */
public class GraphQlQueryService {
  public static final int BAD_REQUEST_STATUS = 400;
  private static final int DEFAULT_OFFSET = 0;
  private static final int DEFAULT_LIMIT = 50;

  private final GraphQlQueryHandlerRegistry handlerRegistry;

  /**
   * Creates a query service.
   *
   * @param handlerRegistry registry of available handlers
   */
  public GraphQlQueryService(GraphQlQueryHandlerRegistry handlerRegistry) {
    this.handlerRegistry = handlerRegistry;
  }

  /**
   * Executes a read request.
   *
   * @param request the read request
   * @return the read result
   */
  public GraphQlReadResult read(GraphQlReadRequest request) {
    GraphQlReadRequest normalized = normalizeReadRequest(request);
    return handlerRegistry.handlerFor(normalized.model()).read(normalized);
  }

  /**
   * Executes a search request.
   *
   * @param request the search request
   * @return the search result
   */
  public GraphQlSearchResult search(GraphQlSearchRequest request) {
    GraphQlSearchRequest normalized = normalizeSearchRequest(request);
    GraphQlSearchResult result = handlerRegistry.handlerFor(normalized.model()).search(normalized);
    return ensureSearchResult(result);
  }

  /**
   * Ensures the read request has the required fields populated.
   *
   * @param request the incoming request
   * @return the normalized request
   */
  private GraphQlReadRequest normalizeReadRequest(GraphQlReadRequest request) {
    if (request == null) {
      throw new GraphQlApiException("Read request must be provided.", BAD_REQUEST_STATUS);
    }
    if (request.model() == null || request.model().isBlank()) {
      throw new GraphQlApiException("Read request must include a model name.", BAD_REQUEST_STATUS);
    }
    if (request.key() == null || request.key().isEmpty()) {
      throw new GraphQlApiException("Read request must include a key.", BAD_REQUEST_STATUS);
    }
    return new GraphQlReadRequest(request.model(), request.key(), safeFields(request.fields()));
  }

  /**
   * Ensures the search request has the required fields populated.
   *
   * @param request the incoming request
   * @return the normalized request
   */
  private GraphQlSearchRequest normalizeSearchRequest(GraphQlSearchRequest request) {
    if (request == null) {
      throw new GraphQlApiException("Search request must be provided.", 400);
    }
    if (request.model() == null || request.model().isBlank()) {
      throw new GraphQlApiException("Search request must include a model name.", 400);
    }
    GraphQlPageInput page = ensurePageInput(request.page());
    return new GraphQlSearchRequest(
        request.model(),
        safeFilter(request.filter()),
        page,
        safeSort(request.sort()),
        safeFields(request.fields())
    );
  }

  /**
   * Ensures the page input is populated with defaults.
   *
   * @param page the incoming page input
   * @return the normalized page input
   */
  private GraphQlPageInput ensurePageInput(GraphQlPageInput page) {
    if (page == null) {
      return new GraphQlPageInput(DEFAULT_OFFSET, DEFAULT_LIMIT);
    }
    Integer offset = page.offset() == null ? DEFAULT_OFFSET : page.offset();
    Integer limit = page.limit() == null ? DEFAULT_LIMIT : page.limit();
    return new GraphQlPageInput(offset, limit);
  }

  /**
   * Provides a safe filter map.
   *
   * @param filter the incoming filter
   * @return a non-null filter map
   */
  private Map<String, Object> safeFilter(Map<String, Object> filter) {
    return filter == null ? Map.of() : filter;
  }

  /**
   * Provides a safe sort list.
   *
   * @param sort the incoming sort list
   * @return a non-null sort list
   */
  private List<GraphQlSortInput> safeSort(List<GraphQlSortInput> sort) {
    return sort == null ? List.of() : sort;
  }

  /**
   * Provides a safe list of fields.
   *
   * @param fields the incoming field list
   * @return a non-null field list
   */
  private List<String> safeFields(List<String> fields) {
    return fields == null ? List.of() : fields;
  }

  /**
   * Ensures search results never return a null collection.
   *
   * @param result the raw result
   * @return the normalized result
   */
  private GraphQlSearchResult ensureSearchResult(GraphQlSearchResult result) {
    if (result == null) {
      return new GraphQlSearchResult(List.of(), 0L);
    }
    List<Object> items = result.items() == null ? List.of() : result.items();
    Long total = result.total() == null ? 0L : result.total();
    return new GraphQlSearchResult(items, total);
  }
}
