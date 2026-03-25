package io.flowledger.platform.query.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.RestrictionBuilder;
import com.blazebit.persistence.WhereAndBuilder;
import com.blazebit.persistence.WhereOrBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
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
   * Verifies nested OR groups inside an OR branch are applied correctly.
   */
  @Test
  void applyFilterSupportsNestedOrInsideOrClause() {
    CriteriaBuilderFactory criteriaBuilderFactory = Mockito.mock(CriteriaBuilderFactory.class);
    BlazeQueryBuilder builder = new BlazeQueryBuilder(criteriaBuilderFactory, List.of());
    CriteriaBuilder<Object> criteriaBuilder = mockCriteriaBuilderFlow();

    builder.applyFilter(
        criteriaBuilder,
        Map.of(
            "tenantId", Map.of("op", "eq", "value", "t1"),
            "or", List.of(
                Map.of("status", Map.of("op", "eq", "value", "ACTIVE")),
                Map.of(
                    "type", Map.of("op", "eq", "value", "BANK"),
                    "or", List.of(
                        Map.of("country", Map.of("op", "eq", "value", "US")),
                        Map.of("country", Map.of("op", "eq", "value", "CA"))
                    )
                )
            )
        )
    );

    verify(criteriaBuilder, atLeastOnce()).where("tenantId");
    verify(criteriaBuilder, times(1)).whereOr();
  }

  /**
   * Verifies complex nested filters are processed without errors and trigger nested OR builder invocations.
   */
  @Test
  void applyFilterSupportsVeryComplexNestedStructure() {
    CriteriaBuilderFactory criteriaBuilderFactory = Mockito.mock(CriteriaBuilderFactory.class);
    BlazeQueryBuilder builder = new BlazeQueryBuilder(criteriaBuilderFactory, List.of());
    CriteriaBuilder<Object> criteriaBuilder = mockCriteriaBuilderFlow();

    builder.applyFilter(
        criteriaBuilder,
        Map.of(
            "tenantId", Map.of("op", "eq", "value", "t1"),
            "region", Map.of("op", "eq", "value", "APAC"),
            "or", List.of(
                Map.of(
                    "status", Map.of("op", "eq", "value", "ACTIVE"),
                    "or", List.of(
                        Map.of(
                            "type", Map.of("op", "eq", "value", "BANK"),
                            "or", List.of(
                                Map.of("country", Map.of("op", "eq", "value", "US")),
                                Map.of("country", Map.of("op", "eq", "value", "CA"))
                            )
                        ),
                        Map.of("priority", Map.of("op", "gt", "value", 10))
                    )
                ),
                Map.of(
                    "category", Map.of("op", "eq", "value", "CORP"),
                    "or", List.of(
                        Map.of("risk", Map.of("op", "eq", "value", "LOW")),
                        Map.of(
                            "risk", Map.of("op", "eq", "value", "HIGH"),
                            "or", List.of(
                                Map.of("score", Map.of("op", "gte", "value", 700)),
                                Map.of(
                                    "score", Map.of("op", "lt", "value", 300),
                                    "or", List.of(
                                        Map.of("flagged", Map.of("op", "eq", "value", false)),
                                        Map.of("manualReview", Map.of("op", "eq", "value", true))
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    );

    verify(criteriaBuilder, atLeastOnce()).where("tenantId");
    verify(criteriaBuilder, atLeastOnce()).where("region");
    verify(criteriaBuilder, atLeastOnce()).whereOr();
    verify(criteriaBuilder, never()).where("manualReview");
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

    WhereOrBuilder<WhereAndBuilder<WhereOrBuilder<CriteriaBuilder<Object>>>> nestedWhereOrBuilder = Mockito.mock(
        WhereOrBuilder.class
    );
    when(whereAndBuilder.whereOr()).thenReturn(nestedWhereOrBuilder);

    WhereAndBuilder<WhereOrBuilder<WhereAndBuilder<WhereOrBuilder<CriteriaBuilder<Object>>>>> nestedWhereAndBuilder = Mockito.mock(
        WhereAndBuilder.class
    );
    when(nestedWhereOrBuilder.whereAnd()).thenReturn(nestedWhereAndBuilder);
    when(nestedWhereAndBuilder.endAnd()).thenReturn(nestedWhereOrBuilder);
    when(nestedWhereOrBuilder.endOr()).thenReturn(whereAndBuilder);

    RestrictionBuilder<WhereAndBuilder<WhereOrBuilder<WhereAndBuilder<WhereOrBuilder<CriteriaBuilder<Object>>>>>> nestedAndRestriction = Mockito.mock(
        RestrictionBuilder.class
    );
    when(nestedWhereAndBuilder.where(anyString())).thenReturn(nestedAndRestriction);
    when(nestedAndRestriction.eq(Mockito.any(Object.class))).thenReturn(nestedWhereAndBuilder);

    Mockito.doReturn(nestedWhereOrBuilder).when(nestedWhereAndBuilder).whereOr();
    return criteriaBuilder;
  }
}
