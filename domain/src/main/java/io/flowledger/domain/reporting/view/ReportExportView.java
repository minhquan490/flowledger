package io.flowledger.domain.reporting.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.reporting.entity.ReportExport;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for report exports.
 */
@EntityView(ReportExport.class)
@GraphQlModel(value = "reportExport", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface ReportExportView {

  /**
   * Returns the report export identifier.
   *
   * @return the report export id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the report identifier.
   *
   * @return the report id
   */
  UUID getReportId();

  /**
   * Returns the export type.
   *
   * @return the export type
   */
  String getExportType();

  /**
   * Returns the file URL.
   *
   * @return the file URL
   */
  String getFileUrl();

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
