package io.flowledger.domain.identity.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.identity.aggregate.User;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;

import java.time.Instant;

/**
 * Write-capable view for users.
 */
@EntityView(User.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "userWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = UserWriteViewPayloadValidator.class
)
public interface UserWriteView extends UserView {
  /**
   * Sets the user email address.
   *
   * @param email the email address
   */
  void setEmail(String email);

  /**
   * Sets the user status.
   *
   * @param status the status
   */
  void setStatus(String status);

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
