package io.flowledger.platform.graphql.domain;

/**
 * Handles read and search requests for a specific domain model.
 *
 * <p>Example handler implementation:
 *
 * <pre>{@code
 * public final class AccountGraphQlHandler implements GraphQlQueryHandler {
 *   @Override
 *   public String model() {
 *     return "account";
 *   }
 *
 *   @Override
 *   public GraphQlReadResult read(GraphQlReadRequest request) {
 *     // Resolve account by key and map to JSON-friendly structure.
 *     return new GraphQlReadResult(true, Map.of("id", "acc_123"));
 *   }
 *
 *   @Override
 *   public GraphQlSearchResult search(GraphQlSearchRequest request) {
 *     return new GraphQlSearchResult(List.of(), 0L);
 *   }
 * }
 * }</pre>
 */
public interface GraphQlQueryHandler {

  /**
   * Returns the model name handled by this handler.
   *
   * @return the model name
   */
  String model();

  /**
   * Executes a read request.
   *
   * @param request the read request
   * @return the read result
   */
  GraphQlReadResult read(GraphQlReadRequest request);

  /**
   * Executes a search request.
   *
   * @param request the search request
   * @return the search result
   */
  GraphQlSearchResult search(GraphQlSearchRequest request);
}
