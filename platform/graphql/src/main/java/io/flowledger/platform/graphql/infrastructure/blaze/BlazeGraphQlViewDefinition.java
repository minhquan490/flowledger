package io.flowledger.platform.graphql.infrastructure.blaze;

/**
 * Describes the Blaze view bound to a GraphQL model name.
 *
 * @param model the GraphQL model name
 * @param viewClass the Blaze entity view class
 * @param entityClass the JPA entity class
 */
public record BlazeGraphQlViewDefinition(
    String model,
    Class<?> viewClass,
    Class<?> entityClass
) {
}
