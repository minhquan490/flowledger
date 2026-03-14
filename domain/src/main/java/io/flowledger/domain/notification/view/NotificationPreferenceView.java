package io.flowledger.domain.notification.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.notification.entity.NotificationPreference;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for notification preferences.
 */
@EntityView(NotificationPreference.class)
@GraphQlModel("notificationPreference")
public interface NotificationPreferenceView {

  /**
   * Returns the preference identifier.
   *
   * @return the preference id
   */
  UUID getId();

  /**
   * Returns the user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the channel.
   *
   * @return the channel
   */
  String getChannel();

  /**
   * Returns whether the channel is enabled.
   *
   * @return true when enabled
   */
  boolean isEnabled();

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
