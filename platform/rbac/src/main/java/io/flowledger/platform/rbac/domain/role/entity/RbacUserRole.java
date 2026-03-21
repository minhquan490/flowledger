package io.flowledger.platform.rbac.domain.role.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
 * Represents a user-to-role assignment in RBAC.
 */
@Entity
@Table(name = "rbac_user_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RbacUserRole {

  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "role_id", nullable = false)
  private UUID roleId;

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
