package io.flowledger.domain.importexport.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.importexport.aggregate.ImportJob;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for import jobs.
 */
@EntityView(ImportJob.class)
@GraphQlModel(value = "importJob", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface ImportJobView {

  /**
   * Returns the import job identifier.
   *
   * @return the import job id
   */
  UUID getId();

  /**
   * Returns the user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the source type.
   *
   * @return the source type
   */
  String getSourceType();

  /**
   * Returns the job status.
   *
   * @return the status
   */
  String getStatus();

  /**
   * Returns the start timestamp.
   *
   * @return the start timestamp
   */
  Instant getStartedAt();

  /**
   * Returns the completion timestamp.
   *
   * @return the completion timestamp
   */
  Instant getFinishedAt();

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
