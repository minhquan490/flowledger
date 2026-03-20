package io.flowledger.platform.query.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.RestrictionBuilder;
import com.blazebit.persistence.WhereAndBuilder;
import com.blazebit.persistence.WhereOrBuilder;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for filter application logic in {@link BlazeQueryBuilder}.
 */
class BlazeQueryBuilderFilterTest {

  /**
   * Verifies an AND with grouped OR filter is applied through the expected builder flow.
   */
  @Test
  void applyFilterSupportsAndWithGroupedOr() {
    CriteriaBuilderFactory criteriaBuilderFactory = Mockito.mock(CriteriaBuilderFactory.class);
    BlazeQueryBuilder builder = new BlazeQueryBuilder(criteriaBuilderFactory, List.of());
    CriteriaBuilder<Object> criteriaBuilder = mockCriteriaBuilderFlow();

    builder.applyFilter(
        criteriaBuilder,
        Map.of(
            "tenantId", Map.of("op", "eq", "value", "t1"),
            "or", List.of(
                Map.of("status", Map.of("op", "eq", "value", "ACTIVE")),
                Map.of("status", Map.of("op", "eq", "value", "PENDING"))
            )
        )
    );

    verify(criteriaBuilder, atLeastOnce()).where("tenantId");
    verify(criteriaBuilder, times(1)).whereOr();
  }

  /**
   * Verifies an OR with an AND clause in one branch is applied correctly.
   */
  @Test
  void applyFilterSupportsOrWithAndClause() {
    CriteriaBuilderFactory criteriaBuilderFactory = Mockito.mock(CriteriaBuilderFactory.class);
    BlazeQueryBuilder builder = new BlazeQueryBuilder(criteriaBuilderFactory, List.of());
    CriteriaBuilder<Object> criteriaBuilder = mockCriteriaBuilderFlow();

    builder.applyFilter(
        criteriaBuilder,
        Map.of(
            "or", List.of(
                Map.of("status", Map.of("op", "eq", "value", "ACTIVE")),
                Map.of(
                    "type", Map.of("op", "eq", "value", "BANK"),
                    "country", Map.of("op", "eq", "value", "US")
                )
            )
        )
    );

    verify(criteriaBuilder, times(1)).whereOr();
  }

  /**
   * Creates and wires mocks for Blaze where-builder flow used by filter tests.
   *
   * @return mocked criteria builder
   */
  @SuppressWarnings({"unchecked"})
  private CriteriaBuilder<Object> mockCriteriaBuilderFlow() {
    CriteriaBuilder<Object> criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
    RestrictionBuilder<CriteriaBuilder<Object>> rootRestriction = Mockito.mock(RestrictionBuilder.class);
    when(criteriaBuilder.where(anyString())).thenReturn(rootRestriction);
    when(rootRestriction.eq(Mockito.any(Object.class))).thenReturn(criteriaBuilder);

    WhereOrBuilder<CriteriaBuilder<Object>> whereOrBuilder = Mockito.mock(WhereOrBuilder.class);
    when(criteriaBuilder.whereOr()).thenReturn(whereOrBuilder);

    WhereAndBuilder<WhereOrBuilder<CriteriaBuilder<Object>>> whereAndBuilder = Mockito.mock(WhereAndBuilder.class);
    when(whereOrBuilder.whereAnd()).thenReturn(whereAndBuilder);
    when(whereAndBuilder.endAnd()).thenReturn(whereOrBuilder);
    when(whereOrBuilder.endOr()).thenReturn(criteriaBuilder);

    RestrictionBuilder<WhereAndBuilder<WhereOrBuilder<CriteriaBuilder<Object>>>> andRestriction = Mockito.mock(
        RestrictionBuilder.class
    );
    when(whereAndBuilder.where(anyString())).thenReturn(andRestriction);
    when(andRestriction.eq(Mockito.any(Object.class))).thenReturn(whereAndBuilder);
    return criteriaBuilder;
  }
}
