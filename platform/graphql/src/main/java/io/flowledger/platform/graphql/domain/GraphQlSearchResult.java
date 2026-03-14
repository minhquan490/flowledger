package io.flowledger.platform.graphql.domain;

import java.util.List;

/**
 * Result payload for search operations.
 *
 * @param items the resulting items represented as JSON
 * @param total the total number of matches
 */
public record GraphQlSearchResult(
    List<Object> items,
    Long total
) {
}
