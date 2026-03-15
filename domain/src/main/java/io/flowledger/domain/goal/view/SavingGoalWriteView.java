package io.flowledger.domain.goal.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.goal.aggregate.SavingGoal;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Write-capable view for saving goals.
 */
@EntityView(SavingGoal.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "savingGoalWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = SavingGoalWriteViewPayloadValidator.class
)
public interface SavingGoalWriteView extends SavingGoalView {
  /**
   * Sets the user identifier.
   *
   * @param userId the user id
   */
  void setUserId(UUID userId);

  /**
   * Sets the account identifier.
   *
   * @param accountId the account id
   */
  void setAccountId(UUID accountId);

  /**
   * Sets the name.
   *
   * @param name the name
   */
  void setName(String name);

  /**
   * Sets the target amount.
   *
   * @param targetAmount the target amount
   */
  void setTargetAmount(BigDecimal targetAmount);

  /**
   * Sets the target date.
   *
   * @param targetDate the target date
   */
  void setTargetDate(LocalDate targetDate);

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
