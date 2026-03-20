package io.flowledger.domain.identity.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.identity.entity.UserPreference;
import io.flowledger.domain.identity.validator.UserPreferenceMutationViewPayloadValidator;
import io.flowledger.domain.identity.view.UserPreferenceView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for user preferences.
 */
@EntityView(UserPreference.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "userPreferenceMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = UserPreferenceMutationViewPayloadValidator.class
)
public interface UserPreferenceMutationView extends UserPreferenceView {
  /**
   * Sets the user identifier.
   *
   * @param userId the user id
   */
  void setUserId(UUID userId);

  /**
   * Sets the timezone.
   *
   * @param timezone the timezone
   */
  void setTimezone(String timezone);

  /**
   * Sets the language.
   *
   * @param language the language
   */
  void setLanguage(String language);

  /**
   * Sets the theme.
   *
   * @param theme the theme
   */
  void setTheme(String theme);

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
