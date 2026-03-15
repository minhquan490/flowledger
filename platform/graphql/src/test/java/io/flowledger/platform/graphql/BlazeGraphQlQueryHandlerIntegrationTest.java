package io.flowledger.platform.graphql;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.FullQueryBuilder;
import com.blazebit.persistence.KeysetPage;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.RestrictionBuilder;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import java.util.Collections;
import io.flowledger.platform.query.blaze.BlazeViewLoader;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Integration test for Blaze-backed generic GraphQL handler wiring.
 */
@SpringBootTest(
    classes = BlazeGraphQlQueryHandlerIntegrationTest.TestApplication.class,
    properties = "spring.autoconfigure.exclude=io.flowledger.platform.query.autoconfigure.CoreQueryAutoConfiguration"
)
class BlazeGraphQlQueryHandlerIntegrationTest {

  @Autowired
  private ExecutionGraphQlService graphQlService;

  @Autowired
  private CriteriaBuilderFactory criteriaBuilderFactory;
  @Autowired
  private EntityViewManager entityViewManager;
  @Autowired
  private EntityManager entityManager;
  private CriteriaBuilder<AccountEntity> criteriaBuilder;

  /**
   * Configures mock beans, captures the criteria builder, and sets up Blaze view discovery.
   */
  @BeforeEach
  @SuppressWarnings({"rawtypes", "unchecked"})
  void configureMocks() {
    criteriaBuilder = org.mockito.Mockito.mock(CriteriaBuilder.class);
    FullQueryBuilder viewQuery = org.mockito.Mockito.mock(FullQueryBuilder.class);
    PaginatedCriteriaBuilder pagedQuery = org.mockito.Mockito.mock(PaginatedCriteriaBuilder.class);
    RestrictionBuilder restrictionBuilder = org.mockito.Mockito.mock(RestrictionBuilder.class);

    when(criteriaBuilderFactory.create(entityManager, AccountEntity.class)).thenReturn(criteriaBuilder);
    when(entityViewManager.applySetting(any(EntityViewSetting.class), any(CriteriaBuilder.class))).thenReturn(viewQuery);
    when(viewQuery.page(0, 1)).thenReturn(pagedQuery);
    doReturn(restrictionBuilder).when(criteriaBuilder).where(anyString());
    doReturn(null).when(restrictionBuilder).eq(any());
    SimplePagedList<Object> results =
        new SimplePagedList<>(List.of(Map.of("id", "acc_100", "name", "Primary")), 1L, 0, 0, 1);
    when(pagedQuery.getResultList()).thenReturn(results);
  }

  /**
   * Verifies the generic Blaze handler is invoked through the GraphQL stack.
   */
  @Test
  void readUsesBlazeHandler() {
    String document = """
        query {
          read(
            request: {
              model: "account"
              key: { id: "acc_100" }
              fields: ["id", "name"]
            }
          ) {
            found
            item
          }
        }
        """;

    ExecutionGraphQlServiceTester tester = ExecutionGraphQlServiceTester.create(graphQlService);

    var item = tester.document(document)
        .execute()
        .path("read.item")
        .entity(Map.class)
        .get();

    assertNotNull(item);
    assertEquals("acc_100", item.get("id"));
    assertEquals("Primary", item.get("name"));
  }

  /**
   * Verifies the generic Blaze handler is invoked for search queries with filters.
   */
  @Test
  void searchUsesBlazeHandler() {
    String document = """
        query {
          search(
            request: {
              model: "account"
              filter: { name: "Primary" }
              fields: ["id", "name"]
              page: {
                offset: 0
                limit: 1
              }
            }
          ) {
            total
            items
          }
        }
        """;

    ExecutionGraphQlServiceTester tester = ExecutionGraphQlServiceTester.create(graphQlService);

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> items = (List<Map<String, Object>>) (List<?>) tester.document(document)
        .execute()
        .path("search.items")
        .entityList(Map.class)
        .get();

    assertNotNull(items);
    assertEquals(1, items.size());
    assertEquals("acc_100", items.getFirst().get("id"));
    assertEquals("Primary", items.getFirst().get("name"));
    org.mockito.Mockito.verify(criteriaBuilder).where("name");
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
          return List.of(AccountView.class);
        }
      };
    }

    /**
     * Exposes the CriteriaBuilderFactory mock.
     *
     * @return criteria builder factory
     */
    @Bean
    CriteriaBuilderFactory criteriaBuilderFactory() {
      return org.mockito.Mockito.mock(CriteriaBuilderFactory.class);
    }

    /**
     * Exposes the EntityViewManager mock.
     *
     * @return entity view manager
     */
    @Bean
    EntityViewManager entityViewManager() {
      return org.mockito.Mockito.mock(EntityViewManager.class);
    }

    /**
     * Exposes the EntityManager mock.
     *
     * @return entity manager
     */
    @Bean
    EntityManager entityManager() {
      return org.mockito.Mockito.mock(EntityManager.class);
    }

    /**
     * Exposes the Blaze query builder.
     *
     * @param criteriaBuilderFactory the criteria builder factory
     * @param entityManager the entity manager
     * @return the Blaze query builder
     */
    @Bean
    BlazeQueryBuilder blazeQueryBuilder(
        CriteriaBuilderFactory criteriaBuilderFactory,
        EntityManager entityManager
    ) {
      return new BlazeQueryBuilder(criteriaBuilderFactory, entityManager, Collections.emptyList());
    }
  }

  @Entity
  @Data
  static class AccountEntity {
    @Id
    private String id;
    private String name;
  }

  @EntityView(AccountEntity.class)
  @GraphQlModel("account")
  interface AccountView {
    String getId();
    String getName();
  }

  /**
   * Simple paged list implementation for tests.
   *
   * @param <T> item type
   */
  @RequiredArgsConstructor
  private static class SimplePagedList<T> extends ArrayList<T> implements PagedList<T> {
    private final long totalSize;
    private final int page;
    private final int firstResult;
    private final int maxResults;

    SimplePagedList(List<T> items, long totalSize, int page, int firstResult, int maxResults) {
      super(items);
      this.totalSize = totalSize;
      this.page = page;
      this.firstResult = firstResult;
      this.maxResults = maxResults;
    }

    @Override
    public int getSize() {
      return size();
    }

    @Override
    public long getTotalSize() {
      return totalSize;
    }

    @Override
    public int getPage() {
      return page;
    }

    @Override
    public int getTotalPages() {
      if (maxResults == 0) {
        return 0;
      }
      return (int) Math.ceil((double) totalSize / maxResults);
    }

    @Override
    public int getFirstResult() {
      return firstResult;
    }

    @Override
    public int getMaxResults() {
      return maxResults;
    }

    @Override
    public KeysetPage getKeysetPage() {
      return null;
    }
  }
}
