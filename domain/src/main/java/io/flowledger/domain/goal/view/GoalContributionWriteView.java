package io.flowledger.domain.goal.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.goal.entity.GoalContribution;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for goal contributions.
 */
@EntityView(GoalContribution.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "goalContributionWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = GoalContributionWriteViewPayloadValidator.class
)
public interface GoalContributionWriteView extends GoalContributionView {
  /**
   * Sets the saving goal identifier.
   *
   * @param savingGoalId the saving goal id
   */
  void setSavingGoalId(UUID savingGoalId);

  /**
   * Sets the amount.
   *
   * @param amount the amount
   */
  void setAmount(BigDecimal amount);

  /**
   * Sets the contribution timestamp.
   *
   * @param contributedAt the contribution timestamp
   */
  void setContributedAt(Instant contributedAt);

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
