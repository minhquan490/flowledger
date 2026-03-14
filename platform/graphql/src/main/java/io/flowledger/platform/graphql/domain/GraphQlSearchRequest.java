package io.flowledger.platform.graphql.domain;

import java.util.List;
import java.util.Map;

/**
 * Request payload for a search operation.
 *
 * @param model the logical model name handled by a read/search handler
 * @param filter the filter expression represented as JSON
 * @param page pagination input for offset/limit
 * @param sort sorting input for the query
 * @param fields optional field projections requested by the client
 */
public record GraphQlSearchRequest(
    String model,
    Map<String, Object> filter,
    GraphQlPageInput page,
    List<GraphQlSortInput> sort,
    List<String> fields
) {
}
