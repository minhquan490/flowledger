package io.flowledger.domain.budget.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.budget.entity.BudgetPeriod;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Write-capable view for budget periods.
 */
@EntityView(BudgetPeriod.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "budgetPeriodWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = BudgetPeriodWriteViewPayloadValidator.class
)
public interface BudgetPeriodWriteView extends BudgetPeriodView {
  /**
   * Sets the budget identifier.
   *
   * @param budgetId the budget id
   */
  void setBudgetId(UUID budgetId);

  /**
   * Sets the period start date.
   *
   * @param periodStart the period start date
   */
  void setPeriodStart(LocalDate periodStart);

  /**
   * Sets the period end date.
   *
   * @param periodEnd the period end date
   */
  void setPeriodEnd(LocalDate periodEnd);

  /**
   * Sets the amount.
   *
   * @param amount the amount
   */
  void setAmount(BigDecimal amount);

  /**
   * Sets the rollover amount.
   *
   * @param rolloverAmount the rollover amount
   */
  void setRolloverAmount(BigDecimal rolloverAmount);

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
