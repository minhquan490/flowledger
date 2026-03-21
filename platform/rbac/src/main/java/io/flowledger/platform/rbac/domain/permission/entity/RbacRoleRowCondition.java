package io.flowledger.platform.rbac.domain.permission.entity;

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
 * Represents a row-level condition assigned to a role and resource.
 */
@Entity
@Table(name = "rbac_role_row_conditions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RbacRoleRowCondition {

  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "role_id", nullable = false)
  private UUID roleId;

  @Column(name = "resource_id", nullable = false)
  private UUID resourceId;

  @Column(name = "condition_json", nullable = false, columnDefinition = "text")
  private String conditionJson;

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
