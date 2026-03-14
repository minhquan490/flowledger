package io.flowledger.domain.identity.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.identity.aggregate.User;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for user aggregates.
 */
@EntityView(User.class)
@GraphQlModel("user")
public interface UserView {

  /**
   * Returns the user identifier.
   *
   * @return the user id
   */
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
  String getStatus();

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
