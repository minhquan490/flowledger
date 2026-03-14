package io.flowledger.domain.importexport.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.importexport.aggregate.ExportJob;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for export jobs.
 */
@EntityView(ExportJob.class)
@GraphQlModel("exportJob")
public interface ExportJobView {

  /**
   * Returns the export job identifier.
   *
   * @return the export job id
   */
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
