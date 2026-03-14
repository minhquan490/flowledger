package io.flowledger.domain.identity.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.identity.entity.UserPreference;
import io.flowledger.platform.graphql.domain.GraphQlModel;

import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for user preferences.
 */
@EntityView(UserPreference.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("userPreferenceWrite")
public interface UserPreferenceWriteView extends UserPreferenceView {

  /**
   * Sets the preference identifier.
   *
   * @param id the preference id
   */
  void setId(UUID id);

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
