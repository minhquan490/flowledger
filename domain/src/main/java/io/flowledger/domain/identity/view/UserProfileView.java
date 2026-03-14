package io.flowledger.domain.identity.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.identity.entity.UserProfile;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for user profiles.
 */
@EntityView(UserProfile.class)
@GraphQlModel("userProfile")
public interface UserProfileView {

  /**
   * Returns the profile identifier.
   *
   * @return the profile id
   */
  UUID getId();

  /**
   * Returns the linked user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the display name.
   *
   * @return the display name
   */
  String getDisplayName();

  /**
   * Returns the avatar URL.
   *
   * @return the avatar URL
   */
  String getAvatarUrl();

  /**
   * Returns the locale.
   *
   * @return the locale
   */
  String getLocale();

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
