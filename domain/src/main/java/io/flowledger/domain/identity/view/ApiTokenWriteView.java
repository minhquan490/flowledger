package io.flowledger.domain.identity.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.identity.entity.ApiToken;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for API tokens.
 */
@EntityView(ApiToken.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("apiTokenWrite")
public interface ApiTokenWriteView extends ApiTokenView {

  /**
   * Sets the token identifier.
   *
   * @param id the token id
   */
  void setId(UUID id);

  /**
   * Sets the user identifier.
   *
   * @param userId the user id
   */
  void setUserId(UUID userId);

  /**
   * Sets the token hash.
   *
   * @param tokenHash the token hash
   */
  void setTokenHash(String tokenHash);

  /**
   * Sets the expiration timestamp.
   *
   * @param expiresAt the expiration timestamp
   */
  void setExpiresAt(Instant expiresAt);

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
