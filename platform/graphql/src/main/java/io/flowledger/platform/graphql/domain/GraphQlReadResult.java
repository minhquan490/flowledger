package io.flowledger.platform.graphql.domain;

/**
 * Result payload for read operations.
 *
 * @param found indicates whether the item exists
 * @param item the item represented as JSON when found
 */
public record GraphQlReadResult(
    boolean found,
    Object item
) {
}
