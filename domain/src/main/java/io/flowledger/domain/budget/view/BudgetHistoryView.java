package io.flowledger.domain.budget.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.budget.entity.BudgetHistory;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * GraphQL view for budget history.
 */
@EntityView(BudgetHistory.class)
@GraphQlModel("budgetHistory")
public interface BudgetHistoryView {

  /**
   * Returns the budget history identifier.
   *
   * @return the budget history id
   */
  UUID getId();

  /**
   * Returns the snapshot date.
   *
   * @return the snapshot date
   */
  LocalDate getSnapshotDate();

  /**
   * Returns the budget identifier.
   *
   * @return the budget id
   */
  UUID getBudgetId();

  /**
   * Returns the spent amount.
   *
   * @return the spent amount
   */
  BigDecimal getSpentAmount();

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
