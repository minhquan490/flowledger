package io.flowledger.domain.goal.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.goal.aggregate.SavingGoal;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * GraphQL view for saving goals.
 */
@EntityView(SavingGoal.class)
@GraphQlModel(value = "savingGoal", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface SavingGoalView {

  /**
   * Returns the saving goal identifier.
   *
   * @return the saving goal id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the saving goal name.
   *
   * @return the saving goal name
   */
  String getName();

  /**
   * Returns the owning user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the linked account identifier.
   *
   * @return the account id
   */
  UUID getAccountId();

  /**
   * Returns the target amount.
   *
   * @return the target amount
   */
  BigDecimal getTargetAmount();

  /**
   * Returns the target date.
   *
   * @return the target date
   */
  LocalDate getTargetDate();

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
