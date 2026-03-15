package io.flowledger.domain.notification.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.notification.aggregate.Notification;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for notifications.
 */
@EntityView(Notification.class)
@GraphQlModel(value = "notification", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface NotificationView {

  /**
   * Returns the notification identifier.
   *
   * @return the notification id
   */
  UUID getId();

  /**
   * Returns the user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the notification type.
   *
   * @return the notification type
   */
  String getNotificationType();

  /**
   * Returns the message.
   *
   * @return the message
   */
  String getMessage();

  /**
   * Returns the status.
   *
   * @return the status
   */
  String getStatus();

  /**
   * Returns the sent timestamp.
   *
   * @return the sent timestamp
   */
  Instant getSentAt();

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
