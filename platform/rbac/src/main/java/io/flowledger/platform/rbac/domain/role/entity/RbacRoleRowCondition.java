package io.flowledger.platform.rbac.domain.role.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @Column(name = "system_managed", nullable = false)
  private boolean systemManaged;

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
