package io.flowledger.domain.identity.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.identity.aggregate.User;
import io.flowledger.domain.identity.aggregate.UserStatus;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for user aggregates.
 */
@EntityView(User.class)
@GraphQlModel(value = "user", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface UserView {

  /**
   * Returns the user identifier.
   *
   * @return the user id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the user email address.
   *
   * @return the user email
   */
  String getEmail();

  /**
   * Returns the user status.
   *
   * @return the user status
   */
  UserStatus getStatus();

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
