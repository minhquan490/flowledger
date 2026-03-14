package io.flowledger.domain.budget.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.budget.aggregate.Budget;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for budgets.
 */
@EntityView(Budget.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("budgetWrite")
public interface BudgetWriteView extends BudgetView {

  /**
   * Sets the budget identifier.
   *
   * @param id the budget id
   */
  void setId(UUID id);

  /**
   * Sets the user identifier.
   *
   * @param userId the user id
   */
  void setUserId(UUID userId);

  /**
   * Sets the name.
   *
   * @param name the name
   */
  void setName(String name);

  /**
   * Sets the category identifier.
   *
   * @param categoryId the category id
   */
  void setCategoryId(UUID categoryId);

  /**
   * Sets the account identifier.
   *
   * @param accountId the account id
   */
  void setAccountId(UUID accountId);

  /**
   * Sets the amount.
   *
   * @param amount the amount
   */
  void setAmount(BigDecimal amount);

  /**
   * Sets the period type.
   *
   * @param periodType the period type
   */
  void setPeriodType(String periodType);

  /**
   * Sets the creation timestamp.
   *
   * @param createdAt the creation timestamp
   */
  void setCreatedAt(Instant createdAt);

  /**
   * Sets the update timestamp.
   *
   * @param updatedAt the update timestamp
   */
  void setUpdatedAt(Instant updatedAt);
}
