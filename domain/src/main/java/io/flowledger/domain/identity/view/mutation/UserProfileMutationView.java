package io.flowledger.domain.identity.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.identity.entity.UserProfile;
import io.flowledger.domain.identity.validator.UserProfileMutationViewPayloadValidator;
import io.flowledger.domain.identity.view.UserProfileView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for user profiles.
 */
@EntityView(UserProfile.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "userProfileMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = UserProfileMutationViewPayloadValidator.class
)
public interface UserProfileMutationView extends UserProfileView {
  /**
   * Sets the user identifier.
   *
   * @param userId the user id
   */
  void setUserId(UUID userId);

  /**
   * Sets the display name.
   *
   * @param displayName the display name
   */
  void setDisplayName(String displayName);

  /**
   * Sets the avatar URL.
   *
   * @param avatarUrl the avatar URL
   */
  void setAvatarUrl(String avatarUrl);

  /**
   * Sets the locale.
   *
   * @param locale the locale
   */
  void setLocale(String locale);

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
