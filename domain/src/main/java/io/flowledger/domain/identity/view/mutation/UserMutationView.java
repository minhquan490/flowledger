package io.flowledger.domain.identity.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.identity.aggregate.User;
import io.flowledger.domain.identity.validator.UserMutationViewPayloadValidator;
import io.flowledger.domain.identity.view.UserView;
import io.flowledger.domain.identity.aggregate.UserStatus;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;

/**
 * Mutation-capable view for users.
 */
@EntityView(User.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "userMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = UserMutationViewPayloadValidator.class
)
public interface UserMutationView extends UserView {
  /**
   * Sets the user email address.
   *
   * @param email the email address
   */
  void setEmail(String email);

  /**
   * Sets the user's first name.
   *
   * @param firstName the first name
   */
  void setFirstName(String firstName);

  /**
   * Sets the user's last name.
   *
   * @param lastName the last name
   */
  void setLastName(String lastName);

  /**
   * Sets the user status.
   *
   * @param status the status
   */
  void setStatus(UserStatus status);

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
