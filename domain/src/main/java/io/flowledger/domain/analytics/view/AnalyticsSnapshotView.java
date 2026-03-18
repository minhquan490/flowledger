package io.flowledger.domain.analytics.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.analytics.aggregate.AnalyticsSnapshot;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * GraphQL view for analytics snapshots.
 */
@EntityView(AnalyticsSnapshot.class)
@GraphQlModel(value = "analyticsSnapshot", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface AnalyticsSnapshotView {

  /**
   * Returns the snapshot identifier.
   *
   * @return the snapshot id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the snapshot date.
   *
   * @return the snapshot date
   */
  LocalDate getSnapshotDate();

  /**
   * Returns the snapshot payload.
   *
   * @return the payload
   */
  String getPayload();

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
