package io.flowledger.domain.budget.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.budget.entity.BudgetPeriod;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * GraphQL view for budget periods.
 */
@EntityView(BudgetPeriod.class)
@GraphQlModel(value = "budgetPeriod", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface BudgetPeriodView {

  /**
   * Returns the budget period identifier.
   *
   * @return the budget period id
   */
  UUID getId();

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
   * Returns the budget identifier.
   *
   * @return the budget id
   */
  UUID getBudgetId();

  /**
   * Returns the budgeted amount.
   *
   * @return the budgeted amount
   */
  BigDecimal getAmount();

  /**
   * Returns the rollover amount.
   *
   * @return the rollover amount
   */
  BigDecimal getRolloverAmount();

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
