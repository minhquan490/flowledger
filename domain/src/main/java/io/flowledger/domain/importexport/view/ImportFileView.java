package io.flowledger.domain.importexport.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.importexport.entity.ImportFile;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for import files.
 */
@EntityView(ImportFile.class)
@GraphQlModel("importFile")
public interface ImportFileView {

  /**
   * Returns the import file identifier.
   *
   * @return the import file id
   */
  UUID getId();

  /**
   * Returns the import job identifier.
   *
   * @return the import job id
   */
  UUID getImportJobId();

  /**
   * Returns the file name.
   *
   * @return the file name
   */
  String getFileName();

  /**
   * Returns the file type.
   *
   * @return the file type
   */
  String getFileType();

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
