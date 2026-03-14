package io.flowledger.domain.reporting.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.reporting.aggregate.Report;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for reports.
 */
@EntityView(Report.class)
@GraphQlModel("report")
public interface ReportView {

  /**
   * Returns the report identifier.
   *
   * @return the report id
   */
  UUID getId();

  /**
   * Returns the user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the report type.
   *
   * @return the report type
   */
  String getReportType();

  /**
   * Returns the report parameters.
   *
   * @return the parameters
   */
  String getParameters();

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
