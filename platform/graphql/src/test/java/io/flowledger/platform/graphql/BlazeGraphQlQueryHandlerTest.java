package io.flowledger.platform.graphql;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.FullQueryBuilder;
import com.blazebit.persistence.KeysetPage;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.RestrictionBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import io.flowledger.platform.graphql.application.GraphQlAccessPolicy;
import io.flowledger.platform.graphql.domain.GraphQlPageInput;
import io.flowledger.platform.graphql.domain.GraphQlReadRequest;
import io.flowledger.platform.graphql.domain.GraphQlReadResult;
import io.flowledger.platform.graphql.domain.GraphQlSearchRequest;
import io.flowledger.platform.graphql.domain.GraphQlSearchResult;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlModelRegistry;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlQueryHandler;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlViewDefinition;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link BlazeGraphQlQueryHandler}.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("unchecked")
class BlazeGraphQlQueryHandlerTest {

  @Mock
  private BlazeGraphQlModelRegistry modelRegistry;
  @Mock
  private CriteriaBuilderFactory criteriaBuilderFactory;
  @Mock
  private EntityViewManager entityViewManager;
  @Mock
  private EntityManager entityManager;
  @Mock
  private GraphQlAccessPolicy accessPolicy;
  @Mock
  @SuppressWarnings("rawtypes")
  private CriteriaBuilder criteriaBuilder;
  @Mock
  @SuppressWarnings("rawtypes")
  private RestrictionBuilder restrictionBuilder;
  @Mock
  @SuppressWarnings("rawtypes")
  private FullQueryBuilder viewQuery;
  @Mock
  @SuppressWarnings("rawtypes")
  private PaginatedCriteriaBuilder pagedQuery;

  /**
   * Verifies read uses Blaze view query and returns a single result.
   */
  @Test
  void readReturnsFirstMatch() {
    BlazeGraphQlViewDefinition definition =
        new BlazeGraphQlViewDefinition("account", AccountView.class, AccountEntity.class);
    GraphQlReadRequest request = new GraphQlReadRequest(
        "account",
        Map.of("id", "acc_1"),
        List.of("id", "name")
    );

    when(accessPolicy.authorizeRead(request)).thenReturn(request);
    when(modelRegistry.viewFor("account")).thenReturn(definition);
    when(criteriaBuilderFactory.create(org.mockito.ArgumentMatchers.any(EntityManager.class), org.mockito.ArgumentMatchers.eq(AccountEntity.class)))
        .thenReturn(criteriaBuilder);
    when(entityViewManager.applySetting(org.mockito.ArgumentMatchers.any(EntityViewSetting.class), org.mockito.ArgumentMatchers.any(CriteriaBuilder.class)))
        .thenReturn(viewQuery);
    when(viewQuery.page(0, 1)).thenReturn(pagedQuery);
    SimplePagedList<Object> results = new SimplePagedList<>(List.of(Map.of("id", "acc_1")), 1L, 0, 0, 1);
    when(pagedQuery.getResultList()).thenReturn(results);

    BlazeGraphQlQueryHandler handler = new BlazeGraphQlQueryHandler(
        modelRegistry,
        new BlazeQueryBuilder(criteriaBuilderFactory, entityManager, List.of()),
        entityViewManager,
        accessPolicy
    );

    GraphQlReadResult result = handler.read(request);

    assertTrue(result.found());
    assertNotNull(result.item());
    verify(accessPolicy).authorizeRead(request);
  }

  /**
   * Verifies search returns items and total size.
   */
  @Test
  void searchReturnsItemsAndTotal() {
    BlazeGraphQlViewDefinition definition =
        new BlazeGraphQlViewDefinition("transaction", TransactionView.class, TransactionEntity.class);
    GraphQlSearchRequest request = new GraphQlSearchRequest(
        "transaction",
        Map.of("status", "POSTED"),
        new GraphQlPageInput(0, 10),
        List.of(),
        List.of("id", "amount")
    );

    when(accessPolicy.authorizeSearch(request)).thenReturn(request);
    when(modelRegistry.viewFor("transaction")).thenReturn(definition);
    when(criteriaBuilderFactory.create(org.mockito.ArgumentMatchers.any(EntityManager.class), org.mockito.ArgumentMatchers.eq(TransactionEntity.class)))
        .thenReturn(criteriaBuilder);
    when(entityViewManager.applySetting(org.mockito.ArgumentMatchers.any(EntityViewSetting.class), org.mockito.ArgumentMatchers.any(CriteriaBuilder.class)))
        .thenReturn(viewQuery);
    when(viewQuery.page(0, 10)).thenReturn(pagedQuery);
    SimplePagedList<Object> results = new SimplePagedList<>(
        List.of(Map.of("id", "txn_1"), Map.of("id", "txn_2")),
        2L,
        0,
        0,
        10
    );
    when(pagedQuery.getResultList()).thenReturn(results);

    BlazeGraphQlQueryHandler handler = new BlazeGraphQlQueryHandler(
        modelRegistry,
        new BlazeQueryBuilder(criteriaBuilderFactory, entityManager, List.of()),
        entityViewManager,
        accessPolicy
    );

    GraphQlSearchResult result = handler.search(request);

    assertEquals(2L, result.total());
    assertEquals(2, result.items().size());
    verify(accessPolicy).authorizeSearch(request);
  }

  /**
   * Stubs the criteria builder restriction chain used by filters.
   */
  @BeforeEach
  void stubRestrictionBuilder() {
    when(criteriaBuilder.where(org.mockito.ArgumentMatchers.anyString())).thenReturn(restrictionBuilder);
    org.mockito.Mockito.doReturn(null).when(restrictionBuilder).eq(org.mockito.ArgumentMatchers.any());
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

  private static final class AccountEntity {}
  private static final class TransactionEntity {}
  private static final class AccountView {}
  private static final class TransactionView {}
}
