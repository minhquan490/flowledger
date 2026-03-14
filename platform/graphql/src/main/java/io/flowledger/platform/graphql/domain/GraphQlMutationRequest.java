package io.flowledger.platform.graphql.domain;

import java.util.Map;

/**
 * Request payload for generic GraphQL mutations.
 *
 * @param model the model name
 * @param action the mutation action (create, update, delete)
 * @param key the identifier map for the target entity
 * @param data the mutation payload
 */
public record GraphQlMutationRequest(
    String model,
    String action,
    Map<String, Object> key,
    Map<String, Object> data
) {
}
