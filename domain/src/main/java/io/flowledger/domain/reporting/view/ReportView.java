package io.flowledger.domain.reporting.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.reporting.aggregate.Report;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for reports.
 */
@EntityView(Report.class)
@GraphQlModel(value = "report", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface ReportView {

  /**
   * Returns the report identifier.
   *
   * @return the report id
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
