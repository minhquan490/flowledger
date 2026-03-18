package io.flowledger.platform.graphql;

import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.query.blaze.BlazeViewLoader;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.test.tester.ExecutionGraphQlServiceTester;
import org.springframework.test.util.ReflectionTestUtils;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Integration test to verify the GraphQL mutation pipeline.
 */
@SpringBootTest(classes = GraphQlMutationIntegrationTest.TestApplication.class, properties = "spring.autoconfigure.exclude=io.flowledger.platform.query.autoconfigure.CoreQueryAutoConfiguration")
class GraphQlMutationIntegrationTest {

  @Autowired
  private ExecutionGraphQlService graphQlService;

  /**
   * Verifies update mutations return the expected item.
   */
  @Test
  void mutateUpdateReturnsItem() {
    String document = """
        mutation {
          mutate(request: {
            model: "account",
            action: "update",
            key: { id: "acc_42" },
            data: { name: "Updated" }
          }) {
            success
            item
          }
        }
        """;

    ExecutionGraphQlServiceTester tester = ExecutionGraphQlServiceTester.create(graphQlService);
    @SuppressWarnings("unchecked")
    Map<String, Object> item = tester.document(document)
        .execute()
        .path("mutate.item")
        .entity(Map.class)
        .get();

    assertNotNull(item);
    assertEquals("acc_42", item.get("id"));
    assertEquals("Updated", item.get("name"));
  }

  /**
   * Verifies create mutations return the expected item.
   */
  @Test
  void mutateCreateReturnsItem() {
    String document = """
        mutation {
          mutate(request: {
            model: "account",
            action: "create",
            data: { name: "New Account" }
          }) {
            success
            item
          }
        }
        """;

    ExecutionGraphQlServiceTester tester = ExecutionGraphQlServiceTester.create(graphQlService);
    @SuppressWarnings("unchecked")
    Map<String, Object> item = tester.document(document)
        .execute()
        .path("mutate.item")
        .entity(Map.class)
        .get();

    assertNotNull(item);
    assertNotNull(item.get("id"));
    assertEquals("New Account", item.get("name"));
  }

  /**
   * Verifies delete mutations return success without an item.
   */
  @Test
  void mutateDeleteReturnsSuccessWithoutItem() {
    String document = """
        mutation {
          mutate(request: {
            model: "account",
            action: "delete",
            key: { id: "acc_delete" }
          }) {
            success
            item
          }
        }
        """;

    ExecutionGraphQlServiceTester tester = ExecutionGraphQlServiceTester.create(graphQlService);
    @SuppressWarnings("unchecked")
    Map<String, Object> response = tester.document(document)
        .execute()
        .path("mutate")
        .entity(Map.class)
        .get();

    assertNotNull(response);
    assertEquals(Boolean.TRUE, response.get("success"));
    assertNull(response.get("item"));
  }

  @SpringBootConfiguration
  @EnableAutoConfiguration
  @AutoConfigurationPackage
  @Configuration
  static class TestApplication {

    /**
     * Provides a stub Blaze view loader for integration testing.
     *
     * @return the Blaze view loader
     */
    @Bean
    BlazeViewLoader blazeViewLoader() {
      return new BlazeViewLoader(new io.flowledger.core.boot.ClasspathScanner()) {
        @Override
        public java.util.Collection<Class<?>> loadViews(String[] basePackages) {
          return java.util.List.of(AccountMutationView.class);
        }
      };
    }

    /**
     * Exposes the EntityManager mock.
     *
     * @return the entity manager
     */
    @Bean
    EntityManager entityManager() {
      EntityManager entityManager = org.mockito.Mockito.mock(EntityManager.class);
      when(entityManager.find(AccountEntity.class, "acc_42")).thenAnswer(_ -> {
        AccountEntity entity = new AccountEntity();
        entity.setId("acc_42");
        return entity;
      });
      when(entityManager.find(AccountEntity.class, "acc_delete")).thenAnswer(_ -> {
        AccountEntity entity = new AccountEntity();
        entity.setId("acc_delete");
        return entity;
      });
      return entityManager;
    }

    /**
     * Exposes the EntityViewManager mock.
     *
     * @return the entity view manager
     */
    @Bean
    com.blazebit.persistence.view.EntityViewManager entityViewManager() {
      var manager = org.mockito.Mockito.mock(com.blazebit.persistence.view.EntityViewManager.class);
      when(manager.create(AccountMutationView.class)).thenAnswer(_ -> {
        AccountMutationViewImpl view = new AccountMutationViewImpl();
        view.setId("acc_new");
        return view;
      });
      when(manager.find(any(EntityManager.class), eq(AccountMutationView.class), any()))
          .thenAnswer(invocation -> {
            AccountMutationViewImpl view = new AccountMutationViewImpl();
            Object idValue = invocation.getArgument(2);
            view.setId(idValue == null ? null : idValue.toString());
            return view;
          });
      return manager;
    }

    /**
     * Exposes the CriteriaBuilderFactory mock.
     *
     * @return criteria builder factory
     */
    @Bean
    CriteriaBuilderFactory criteriaBuilderFactory() {
      return mock(CriteriaBuilderFactory.class);
    }

    /**
     * Exposes the Blaze query builder.
     *
     * @param criteriaBuilderFactory the criteria builder factory
     * @param entityManager          the entity manager
     * @return the Blaze query builder
     */
    @Bean
    BlazeQueryBuilder blazeQueryBuilder(
        CriteriaBuilderFactory criteriaBuilderFactory,
        EntityManager entityManager) {
      BlazeQueryBuilder queryBuilder = new BlazeQueryBuilder(criteriaBuilderFactory, java.util.List.of());
      ReflectionTestUtils.setField(queryBuilder, "entityManager", entityManager);
      return queryBuilder;
    }

  }

  /**
   * Dummy entity for mutation view mapping.
   */
  @Entity
  @Getter
  @Setter
  private static class AccountEntity {
    @Id
    private String id;
    private String name;
  }

  /**
   * Mutation view for account data.
   */
  @EntityView(AccountEntity.class)
  @CreatableEntityView
  @UpdatableEntityView
  @GraphQlModel("account")
  private interface AccountMutationView {
    /**
     * Returns the account id.
     *
     * @return the account id
     */
    String getId();

    /**
     * Sets the account id.
     *
     * @param id the account id
     */
    void setId(String id);

    /**
     * Returns the account name.
     *
     * @return the account name
     */
    String getName();

    /**
     * Sets the account name.
     *
     * @param name the account name
     */
    void setName(String name);
  }

  /**
   * Simple mutable implementation of the account mutation view.
   */
  @Getter
  @Setter
  private static class AccountMutationViewImpl implements AccountMutationView {
    private String id;
    private String name;
  }
}
