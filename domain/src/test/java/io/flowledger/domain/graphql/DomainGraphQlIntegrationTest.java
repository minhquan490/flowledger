package io.flowledger.domain.graphql;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.FullQueryBuilder;
import com.blazebit.persistence.KeysetPage;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.RestrictionBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import io.flowledger.domain.account.aggregate.Account;
import io.flowledger.domain.account.view.AccountView;
import io.flowledger.domain.identity.view.UserView;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import io.flowledger.platform.query.blaze.BlazeViewLoader;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
 * Integration test ensuring GraphQL queries can resolve domain views.
 */
@SpringBootTest(classes = DomainGraphQlIntegrationTest.TestApplication.class, properties = "spring.autoconfigure.exclude=io.flowledger.platform.query.autoconfigure.CoreQueryAutoConfiguration")
class DomainGraphQlIntegrationTest {

  @Autowired
  private ExecutionGraphQlService graphQlService;

  @Autowired
  private CriteriaBuilderFactory criteriaBuilderFactory;

  @Autowired
  private EntityViewManager entityViewManager;

  @Autowired
  private EntityManager entityManager;

  @SuppressWarnings("rawtypes")
  private PaginatedCriteriaBuilder pagedQuery;

  /**
   * Configures the Blaze query mocks for each test.
   */
  @BeforeEach
  @SuppressWarnings({ "rawtypes", "unchecked" })
  void configureMocks() {
    CriteriaBuilder criteriaBuilder = org.mockito.Mockito.mock(CriteriaBuilder.class);
    FullQueryBuilder viewQuery = org.mockito.Mockito.mock(FullQueryBuilder.class);
    pagedQuery = org.mockito.Mockito.mock(PaginatedCriteriaBuilder.class);
    RestrictionBuilder restrictionBuilder = org.mockito.Mockito.mock(RestrictionBuilder.class);

    when(criteriaBuilderFactory.create(entityManager, Account.class)).thenReturn(criteriaBuilder);
    when(entityViewManager.applySetting(any(EntityViewSetting.class), any(CriteriaBuilder.class)))
        .thenReturn(viewQuery);
    when(viewQuery.page(0, 1)).thenReturn(pagedQuery);
    doReturn(restrictionBuilder).when(criteriaBuilder).where(anyString());
    doReturn(null).when(restrictionBuilder).eq(any());
  }

  /**
   * Verifies account read returns basic account data from domain views.
   */
  @Test
  void readAccountReturnsViewData() {
    SimplePagedList<Object> results = new SimplePagedList<>(
        List.of(Map.of("id", "acc_100", "name", "Primary", "status", "ACTIVE")),
        1L,
        0,
        0,
        1);
    doReturn(results).when(pagedQuery).getResultList();

    String document = """
        query {
          read(request: { model: "account", key: { id: "acc_100" }, fields: ["id", "name", "status"] }) {
            found
            item
          }
        }
        """;

    ExecutionGraphQlServiceTester tester = ExecutionGraphQlServiceTester.create(graphQlService);
    @SuppressWarnings("unchecked")
    Map<String, Object> item = tester.document(document)
        .execute()
        .path("read.item")
        .entity(Map.class)
        .get();

    assertNotNull(item);
    assertEquals("acc_100", item.get("id"));
    assertEquals("Primary", item.get("name"));
    assertEquals("ACTIVE", item.get("status"));
  }

  /**
   * Verifies account read can return joined user data through the view.
   */
  @Test
  void readAccountReturnsUserJoin() {
    Map<String, Object> user = Map.of("id", "usr_100", "email", "user@flowledger.local");
    SimplePagedList<Object> results = new SimplePagedList<>(
        List.of(Map.of("id", "acc_200", "name", "Checking", "user", user)),
        1L,
        0,
        0,
        1);
    doReturn(results).when(pagedQuery).getResultList();

    String document = """
        query {
          read(request: {
            model: "account",
            key: { id: "acc_200" },
            fields: ["id", "name", "user.id", "user.email"]
          }) {
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
    assertEquals("acc_200", item.get("id"));
    assertEquals("Checking", item.get("name"));
    @SuppressWarnings("unchecked")
    Map<String, Object> userItem = (Map<String, Object>) item.get("user");
    assertNotNull(userItem);
    assertEquals("usr_100", userItem.get("id"));
    assertEquals("user@flowledger.local", userItem.get("email"));
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
          return List.of(AccountView.class, UserView.class);
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
     * @param entityManager          the entity manager
     * @return the Blaze query builder
     */
    @Bean
    BlazeQueryBuilder blazeQueryBuilder(
        CriteriaBuilderFactory criteriaBuilderFactory,
        EntityManager entityManager) {
      return new BlazeQueryBuilder(criteriaBuilderFactory, List.of());
    }
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

    /**
     * Creates a paged list instance.
     *
     * @param items       the items
     * @param totalSize   total item count
     * @param page        current page
     * @param firstResult first result index
     * @param maxResults  max results
     */
    SimplePagedList(List<T> items, long totalSize, int page, int firstResult, int maxResults) {
      super(items);
      this.totalSize = totalSize;
      this.page = page;
      this.firstResult = firstResult;
      this.maxResults = maxResults;
    }

    /**
     * Returns the page size.
     *
     * @return the page size
     */
    @Override
    public int getSize() {
      return size();
    }

    /**
     * Returns the total item count.
     *
     * @return the total size
     */
    @Override
    public long getTotalSize() {
      return totalSize;
    }

    /**
     * Returns the current page.
     *
     * @return the current page
     */
    @Override
    public int getPage() {
      return page;
    }

    /**
     * Returns the total page count.
     *
     * @return the total pages
     */
    @Override
    public int getTotalPages() {
      if (maxResults == 0) {
        return 0;
      }
      return (int) Math.ceil((double) totalSize / maxResults);
    }

    /**
     * Returns the first result index.
     *
     * @return the first result index
     */
    @Override
    public int getFirstResult() {
      return firstResult;
    }

    /**
     * Returns the max results count.
     *
     * @return the max results count
     */
    @Override
    public int getMaxResults() {
      return maxResults;
    }

    /**
     * Returns the keyset page.
     *
     * @return the keyset page
     */
    @Override
    public KeysetPage getKeysetPage() {
      return null;
    }
  }
}
