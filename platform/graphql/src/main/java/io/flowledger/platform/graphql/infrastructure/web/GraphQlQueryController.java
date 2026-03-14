package io.flowledger.platform.graphql.infrastructure.web;

import io.flowledger.platform.graphql.application.GraphQlMutationService;
import io.flowledger.platform.graphql.application.GraphQlQueryService;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import io.flowledger.platform.graphql.domain.GraphQlMutationResult;
import io.flowledger.platform.graphql.domain.GraphQlReadRequest;
import io.flowledger.platform.graphql.domain.GraphQlReadResult;
import io.flowledger.platform.graphql.domain.GraphQlSearchRequest;
import io.flowledger.platform.graphql.domain.GraphQlSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * GraphQL controller exposing generic read and search operations.
 *
 * <p>Example read query:
 *
 * <pre>{@code
 * query {
 *   read(request: {
 *     model: "account",
 *     key: { id: "acc_123" },
 *     fields: ["id", "name", "status"]
 *   }) {
 *     found
 *     item
 *   }
 * }
 * }</pre>
 *
 * <p>Example search query:
 *
 * <pre>{@code
 * query {
 *   search(request: {
 *     model: "transaction",
 *     filter: { status: "POSTED" },
 *     page: { offset: 0, limit: 25 },
 *     sort: [{ field: "postedAt", direction: DESC }],
 *     fields: ["id", "amount", "postedAt"]
 *   }) {
 *     items
 *     total
 *   }
 * }
 * }</pre>
 */
@Controller
@RequiredArgsConstructor
public class GraphQlQueryController {
  private final GraphQlQueryService queryService;
  private final GraphQlMutationService mutationService;

  /**
   * Handles read queries.
   *
   * @param request the read request
   * @return the read result
   */
  @QueryMapping
  public GraphQlReadResult read(@Argument GraphQlReadRequest request) {
    return queryService.read(request);
  }

  /**
   * Handles search queries.
   *
   * @param request the search request
   * @return the search result
   */
  @QueryMapping
  public GraphQlSearchResult search(@Argument GraphQlSearchRequest request) {
    return queryService.search(request);
  }

  /**
   * Handles mutation operations.
   *
   * @param request the mutation request
   * @return the mutation result
   */
  @MutationMapping
  public GraphQlMutationResult mutate(@Argument GraphQlMutationRequest request) {
    return mutationService.mutate(request);
  }
}
