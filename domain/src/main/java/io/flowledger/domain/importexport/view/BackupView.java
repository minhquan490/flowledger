package io.flowledger.domain.importexport.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.importexport.aggregate.Backup;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for backups.
 */
@EntityView(Backup.class)
@GraphQlModel("backup")
public interface BackupView {

  /**
   * Returns the backup identifier.
   *
   * @return the backup id
   */
  UUID getId();

  /**
   * Returns the user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the backup type.
   *
   * @return the backup type
   */
  String getBackupType();

  /**
   * Returns the storage URL.
   *
   * @return the storage URL
   */
  String getStorageUrl();

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
