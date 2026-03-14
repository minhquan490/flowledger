package io.flowledger.platform.graphql;

import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.application.GraphQlQueryHandlerRegistry;
import io.flowledger.platform.graphql.domain.GraphQlQueryHandler;
import io.flowledger.platform.graphql.domain.GraphQlReadRequest;
import io.flowledger.platform.graphql.domain.GraphQlReadResult;
import io.flowledger.platform.graphql.domain.GraphQlSearchRequest;
import io.flowledger.platform.graphql.domain.GraphQlSearchResult;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link GraphQlQueryHandlerRegistry}.
 */
class GraphQlQueryHandlerRegistryTest {

  /**
   * Verifies model-specific handlers win over the generic handler.
   */
  @Test
  void handlerForPrefersSpecificHandler() {
    GraphQlQueryHandler generic = new TestHandler(GraphQlQueryHandlerRegistry.GENERIC_MODEL);
    GraphQlQueryHandler specific = new TestHandler("account");
    GraphQlQueryHandlerRegistry registry = new GraphQlQueryHandlerRegistry(List.of(generic, specific));

    GraphQlQueryHandler resolved = registry.handlerFor("account");

    assertSame(specific, resolved);
  }

  /**
   * Verifies the generic handler is used when no model-specific handler exists.
   */
  @Test
  void handlerForFallsBackToGenericHandler() {
    GraphQlQueryHandler generic = new TestHandler(GraphQlQueryHandlerRegistry.GENERIC_MODEL);
    GraphQlQueryHandlerRegistry registry = new GraphQlQueryHandlerRegistry(List.of(generic));

    GraphQlQueryHandler resolved = registry.handlerFor("invoice");

    assertSame(generic, resolved);
  }

  /**
   * Verifies missing handlers raise a query exception.
   */
  @Test
  void handlerForThrowsWhenMissingHandlers() {
    GraphQlQueryHandlerRegistry registry = new GraphQlQueryHandlerRegistry(List.of());

    GraphQlApiException exception = assertThrows(
        GraphQlApiException.class,
        () -> registry.handlerFor("unknown")
    );

    assertEquals("No handler registered for model: unknown", exception.getMessage());
  }

  /**
   * Simple handler stub for registry tests.
   */
  private record TestHandler(String model) implements GraphQlQueryHandler {
    /**
     * Returns the handler model.
     *
     * @return the handler model
     */
    @Override
    public String model() {
      return model;
    }

    /**
     * Returns a dummy read result.
     *
     * @param request the read request
     * @return the read result
     */
    @Override
    public GraphQlReadResult read(GraphQlReadRequest request) {
      return new GraphQlReadResult(false, null);
    }

    /**
     * Returns a dummy search result.
     *
     * @param request the search request
     * @return the search result
     */
    @Override
    public GraphQlSearchResult search(GraphQlSearchRequest request) {
      return new GraphQlSearchResult(List.of(), 0L);
    }
  }
}
