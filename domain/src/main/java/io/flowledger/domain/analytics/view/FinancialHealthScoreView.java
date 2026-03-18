package io.flowledger.domain.analytics.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.analytics.aggregate.FinancialHealthScore;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * GraphQL view for financial health scores.
 */
@EntityView(FinancialHealthScore.class)
@GraphQlModel(value = "financialHealthScore", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface FinancialHealthScoreView {

  /**
   * Returns the score identifier.
   *
   * @return the score id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the score date.
   *
   * @return the score date
   */
  LocalDate getScoreDate();

  /**
   * Returns the score value.
   *
   * @return the score value
   */
  BigDecimal getScore();

  /**
   * Returns the creation timestamp.
   *
   * @return the creation timestamp
   */
  Instant getCreatedAt();

  /**
   * Returns the update timestamp.
   *
   * @return the update timestamp
   */
  Instant getUpdatedAt();
}
