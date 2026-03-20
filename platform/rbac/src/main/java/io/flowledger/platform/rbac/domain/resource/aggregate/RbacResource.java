package io.flowledger.platform.rbac.domain.resource.aggregate;

import io.flowledger.platform.rbac.domain.resource.entity.RbacResourceField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.GenerationType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a resource registered for RBAC evaluation.
 */
@Entity
@Table(name = "rbac_resources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RbacResource {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "name", nullable = false, unique = true, length = 200)
  private String name;

  @Column(name = "description", length = 500, nullable = true)
  private String description;

  @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY)
  private List<RbacResourceField> fields = new ArrayList<>();

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  /**
   * Initializes timestamps before persisting.
   */
  @PrePersist
  public void prePersist() {
    Instant now = Instant.now();
    if (createdAt == null) {
      createdAt = now;
    }
    if (updatedAt == null) {
      updatedAt = now;
    }
  }

  /**
   * Updates the timestamp before updating.
   */
  @PreUpdate
  public void preUpdate() {
    updatedAt = Instant.now();
  }
}
