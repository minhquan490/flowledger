package io.flowledger.domain.budget.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.budget.aggregate.Budget;
import io.flowledger.domain.budget.validator.BudgetMutationViewPayloadValidator;
import io.flowledger.domain.budget.view.BudgetView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for budgets.
 */
@EntityView(Budget.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "budgetMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = BudgetMutationViewPayloadValidator.class
)
public interface BudgetMutationView extends BudgetView {
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
