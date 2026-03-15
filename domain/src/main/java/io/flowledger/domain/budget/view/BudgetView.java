package io.flowledger.domain.budget.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.budget.aggregate.Budget;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for budgets.
 */
@EntityView(Budget.class)
@GraphQlModel(value = "budget", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface BudgetView {

  /**
   * Returns the budget identifier.
   *
   * @return the budget id
   */
  UUID getId();

  /**
   * Returns the budget name.
   *
   * @return the budget name
   */
  String getName();

  /**
   * Returns the owning user identifier.
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
   * Returns the account identifier.
   *
   * @return the account id
   */
  UUID getAccountId();

  /**
   * Returns the budget amount.
   *
   * @return the budget amount
   */
  BigDecimal getAmount();

  /**
   * Returns the budget period type.
   *
   * @return the period type
   */
  String getPeriodType();

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
