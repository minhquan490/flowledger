package io.flowledger.domain.importexport.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.importexport.aggregate.ExportJob;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for export jobs.
 */
@EntityView(ExportJob.class)
@GraphQlModel(value = "exportJob", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface ExportJobView {

  /**
   * Returns the export job identifier.
   *
   * @return the export job id
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
   * Returns the export type.
   *
   * @return the export type
   */
  String getExportType();

  /**
   * Returns the export job status.
   *
   * @return the status
   */
  String getStatus();

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
