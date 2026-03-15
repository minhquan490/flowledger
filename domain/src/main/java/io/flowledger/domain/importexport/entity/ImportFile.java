package io.flowledger.domain.importexport.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a file attached to an import job.
 */
@Entity
@Table(name = ImportFile.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportFile {
  public static final String TABLE_NAME = "import_files";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "import_job_id", nullable = false)
  private UUID importJobId;

  @Column(name = "file_name", nullable = true, length = 500)
  private String fileName;

  @Column(name = "file_type", nullable = true, length = 50)
  private String fileType;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
