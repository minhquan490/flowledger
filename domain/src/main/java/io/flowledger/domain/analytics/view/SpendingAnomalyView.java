package io.flowledger.domain.analytics.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.analytics.aggregate.SpendingAnomaly;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for spending anomalies.
 */
@EntityView(SpendingAnomaly.class)
@GraphQlModel(value = "spendingAnomaly", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface SpendingAnomalyView {

  /**
   * Returns the anomaly identifier.
   *
   * @return the anomaly id
   */
  UUID getId();

  /**
   * Returns the user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the detection timestamp.
   *
   * @return the detection timestamp
   */
  Instant getDetectedAt();

  /**
   * Returns the anomaly description.
   *
   * @return the description
   */
  String getDescription();

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
