package io.flowledger.platform.graphql.domain;

/**
 * Pagination input for search requests.
 *
 * @param offset zero-based offset into the result set
 * @param limit  maximum number of items to return
 */
public record GraphQlPageInput(
    Integer offset,
    Integer limit
) {
}
