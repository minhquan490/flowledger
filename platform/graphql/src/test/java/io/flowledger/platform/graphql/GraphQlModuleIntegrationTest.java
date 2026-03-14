package io.flowledger.platform.graphql;

import io.flowledger.platform.graphql.domain.GraphQlQueryHandler;
import io.flowledger.platform.graphql.domain.GraphQlReadRequest;
import io.flowledger.platform.graphql.domain.GraphQlReadResult;
import io.flowledger.platform.graphql.domain.GraphQlSearchRequest;
import io.flowledger.platform.graphql.domain.GraphQlSearchResult;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.test.tester.ExecutionGraphQlServiceTester;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test to verify the GraphQL module autoconfiguration works end-to-end.
 */
@SpringBootTest(
    classes = GraphQlModuleIntegrationTest.TestApplication.class,
    properties = "spring.autoconfigure.exclude=io.flowledger.platform.query.autoconfigure.CoreQueryAutoConfiguration"
)
class GraphQlModuleIntegrationTest {

  @Autowired
  private ExecutionGraphQlService graphQlService;

  /**
   * Verifies read queries work with the module autoconfiguration.
   */
  @Test
  void readQueryWorks() {
    String document = """
        query {
          read(request: { model: "account", key: { id: "acc_42" }, fields: ["id", "name"] }) {
            found
            item
          }
        }
        """;

    ExecutionGraphQlServiceTester tester = ExecutionGraphQlServiceTester.create(graphQlService);

    tester.document(document)
        .execute()
        .path("read.found")
        .entity(Boolean.class)
        .isEqualTo(true);

    var item = tester.document(document)
        .execute()
        .path("read.item")
        .entity(Map.class)
        .get();

    assertEquals("acc_42", item.get("id"));
    assertEquals("Primary", item.get("name"));
  }

  /**
   * Verifies search queries work with the module autoconfiguration.
   */
  @Test
  @SuppressWarnings("rawtypes")
  void searchQueryWorks() {
    String document = """
        query {
          search(request: { model: "transaction", fields: ["id", "amount"] }) {
            items
            total
          }
        }
        """;

    ExecutionGraphQlServiceTester tester = ExecutionGraphQlServiceTester.create(graphQlService);

    tester.document(document)
        .execute()
        .path("search.total")
        .entity(Long.class)
        .isEqualTo(2L);

    List<Map> items = tester.document(document)
        .execute()
        .path("search.items")
        .entityList(Map.class)
        .get();

    assertEquals(2, items.size());
    assertTrue(items.stream().allMatch(it -> it.containsKey("id")));
  }

  @SpringBootConfiguration
  @EnableAutoConfiguration
  @Configuration
  static class TestApplication {

    @Bean
    GraphQlQueryHandler accountHandler() {
      return new StaticHandler("account", Map.of("id", "acc_42", "name", "Primary"), List.of(), 0L);
    }

    @Bean
    GraphQlQueryHandler transactionHandler() {
      List<Object> items = List.of(
          Map.of("id", "txn_1", "amount", 1200),
          Map.of("id", "txn_2", "amount", 450)
      );
      return new StaticHandler("transaction", null, items, 2L);
    }
  }

  /**
   * Simple static handler for integration tests.
   */
  private record StaticHandler(String model, Object item, List<Object> items,
                               long total) implements GraphQlQueryHandler {
    /**
     * Returns the handler model.
     *
     * @return the model name
     */
    @Override
    public String model() {
      return model;
    }

    /**
     * Returns a fixed read result.
     *
     * @param request the read request
     * @return the read result
     */
    @Override
    public GraphQlReadResult read(GraphQlReadRequest request) {
      return new GraphQlReadResult(item != null, item);
    }

    /**
     * Returns a fixed search result.
     *
     * @param request the search request
     * @return the search result
     */
    @Override
    public GraphQlSearchResult search(GraphQlSearchRequest request) {
      return new GraphQlSearchResult(items, total);
    }
  }
}
