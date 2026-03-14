package io.flowledger.domain.budget.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.budget.entity.BudgetAlert;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for budget alerts.
 */
@EntityView(BudgetAlert.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("budgetAlertWrite")
public interface BudgetAlertWriteView extends BudgetAlertView {

  /**
   * Sets the budget alert identifier.
   *
   * @param id the budget alert id
   */
  void setId(UUID id);

  /**
   * Sets the budget identifier.
   *
   * @param budgetId the budget id
   */
  void setBudgetId(UUID budgetId);

  /**
   * Sets the alert type.
   *
   * @param alertType the alert type
   */
  void setAlertType(String alertType);

  /**
   * Sets the threshold.
   *
   * @param threshold the threshold
   */
  void setThreshold(BigDecimal threshold);

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
