package io.flowledger.domain.budget.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.budget.entity.BudgetAlert;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for budget alerts.
 */
@EntityView(BudgetAlert.class)
@GraphQlModel(value = "budgetAlert", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface BudgetAlertView {

  /**
   * Returns the budget alert identifier.
   *
   * @return the budget alert id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the alert type.
   *
   * @return the alert type
   */
  String getAlertType();

  /**
   * Returns the budget identifier.
   *
   * @return the budget id
   */
  UUID getBudgetId();

  /**
   * Returns the alert threshold.
   *
   * @return the threshold
   */
  BigDecimal getThreshold();

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
