package io.flowledger.domain.analytics.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.analytics.aggregate.TopExpense;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * GraphQL view for top expense records.
 */
@EntityView(TopExpense.class)
@GraphQlModel(value = "topExpense", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface TopExpenseView {

  /**
   * Returns the top expense identifier.
   *
   * @return the top expense id
   */
  UUID getId();

  /**
   * Returns the user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the category identifier.
   *
   * @return the category id
   */
  UUID getCategoryId();

  /**
   * Returns the amount.
   *
   * @return the amount
   */
  BigDecimal getAmount();

  /**
   * Returns the period start date.
   *
   * @return the period start date
   */
  LocalDate getPeriodStart();

  /**
   * Returns the period end date.
   *
   * @return the period end date
   */
  LocalDate getPeriodEnd();

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
