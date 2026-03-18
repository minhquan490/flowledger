package io.flowledger.domain.goal.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.goal.entity.GoalContribution;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for goal contributions.
 */
@EntityView(GoalContribution.class)
@GraphQlModel(value = "goalContribution", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface GoalContributionView {

  /**
   * Returns the contribution identifier.
   *
   * @return the contribution id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the saving goal identifier.
   *
   * @return the saving goal id
   */
  UUID getSavingGoalId();

  /**
   * Returns the contribution amount.
   *
   * @return the amount
   */
  BigDecimal getAmount();

  /**
   * Returns the contribution timestamp.
   *
   * @return the contribution timestamp
   */
  Instant getContributedAt();

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
