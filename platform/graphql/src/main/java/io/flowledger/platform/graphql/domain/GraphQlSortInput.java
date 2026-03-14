package io.flowledger.platform.graphql.domain;

/**
 * Sort input for a search request.
 *
 * @param field the field name to sort by
 * @param direction the sort direction
 */
public record GraphQlSortInput(
    String field,
    GraphQlSortDirection direction
) {
}
