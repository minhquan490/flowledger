package io.flowledger.domain.identity.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.identity.entity.UserPreference;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for user preferences.
 */
@EntityView(UserPreference.class)
@GraphQlModel(value = "userPreference", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface UserPreferenceView {

  /**
   * Returns the preference identifier.
   *
   * @return the preference id
   */
  UUID getId();

  /**
   * Returns the linked user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the timezone preference.
   *
   * @return the timezone
   */
  String getTimezone();

  /**
   * Returns the language preference.
   *
   * @return the language
   */
  String getLanguage();

  /**
   * Returns the theme preference.
   *
   * @return the theme
   */
  String getTheme();

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
