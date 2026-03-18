package io.flowledger.domain.identity.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.identity.entity.ApiToken;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for API tokens.
 */
@EntityView(ApiToken.class)
@GraphQlModel(value = "apiToken", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface ApiTokenView {

  /**
   * Returns the token identifier.
   *
   * @return the token id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the linked user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the token hash.
   *
   * @return the token hash
   */
  String getTokenHash();

  /**
   * Returns the expiration timestamp.
   *
   * @return the expiration timestamp
   */
  Instant getExpiresAt();

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
