package io.flowledger.platform.graphql.domain;

/**
 * Result payload for generic GraphQL mutations.
 *
 * @param success indicates whether the mutation succeeded
 * @param item the returned item payload
 */
public record GraphQlMutationResult(
    boolean success,
    Object item
) {
}
