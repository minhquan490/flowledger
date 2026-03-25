package io.flowledger.platform.rbac.domain.resource.entity;

import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a synchronized RBAC field belonging to a resource.
 */
@Entity
@Table(name = "rbac_resource_fields")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RbacResourceField {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, length = Integer.MAX_VALUE)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "resource_id", nullable = false)
  private RbacResource resource;

  @Column(name = "field_name", nullable = false, length = 200)
  private String fieldName;

  @Column(name = "source_method_name", nullable = false, length = 200)
  private String sourceMethodName;

  @Column(name = "created_at", nullable = false, length = Integer.MAX_VALUE)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false, length = Integer.MAX_VALUE)
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
    updatedAt = now;
  }

  /**
   * Updates the timestamp before updating.
   */
  @PreUpdate
  public void preUpdate() {
    updatedAt = Instant.now();
  }
}
