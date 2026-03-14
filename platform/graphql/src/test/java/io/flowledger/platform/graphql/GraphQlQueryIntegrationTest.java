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

@SpringBootTest(
    classes = GraphQlQueryIntegrationTest.TestApplication.class,
    properties = "spring.autoconfigure.exclude=io.flowledger.platform.query.autoconfigure.CoreQueryAutoConfiguration"
)
class GraphQlQueryIntegrationTest {

  @Autowired
  private ExecutionGraphQlService graphQlService;

  @Test
  void readQueryReturnsJsonPayload() {
    String document = """
        query($id: String!) {
          read(request: { model: "account", key: { id: $id }, fields: ["id", "name"] }) {
            found
            item
          }
        }
        """;

    ExecutionGraphQlServiceTester tester = ExecutionGraphQlServiceTester.create(graphQlService);

    tester.document(document)
        .variable("id", "acc_123")
        .execute()
        .path("read.found")
        .entity(Boolean.class)
        .isEqualTo(true);

    var item = tester.document(document)
        .variable("id", "acc_123")
        .execute()
        .path("read.item")
        .entity(Map.class)
        .get();

    assertEquals("acc_123", item.get("id"));
    assertEquals("Primary", item.get("name"));
  }

  @Test
  @SuppressWarnings("rawtypes")
  void searchQueryReturnsItemsAndTotal() {
    String document = """
        query {
          search(request: {
            model: "transaction",
            filter: { status: "POSTED" },
            page: { offset: 0, limit: 10 },
            sort: [{ field: "postedAt", direction: DESC }],
            fields: ["id", "amount"]
          }) {
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
    assertEquals("txn_1", items.getFirst().get("id"));
    assertEquals(450, items.get(1).get("amount"));
    assertTrue(items.stream().allMatch(it -> it.containsKey("id")));
  }

  @SpringBootConfiguration
  @EnableAutoConfiguration
  @Configuration
  static class TestApplication {

    @Bean
    GraphQlQueryHandler accountHandler() {
      return new GraphQlQueryHandler() {
        @Override
        public String model() {
          return "account";
        }

        @Override
        public GraphQlReadResult read(GraphQlReadRequest request) {
          return new GraphQlReadResult(true, Map.of("id", "acc_123", "name", "Primary"));
        }

        @Override
        public GraphQlSearchResult search(GraphQlSearchRequest request) {
          return new GraphQlSearchResult(List.of(), 0L);
        }
      };
    }

    @Bean
    GraphQlQueryHandler transactionHandler() {
      return new GraphQlQueryHandler() {
        @Override
        public String model() {
          return "transaction";
        }

        @Override
        public GraphQlReadResult read(GraphQlReadRequest request) {
          return new GraphQlReadResult(false, null);
        }

        @Override
        public GraphQlSearchResult search(GraphQlSearchRequest request) {
          List<Object> items = List.of(
              Map.of("id", "txn_1", "amount", 1200),
              Map.of("id", "txn_2", "amount", 450)
          );
          return new GraphQlSearchResult(items, 2L);
        }
      };
    }
  }
}
