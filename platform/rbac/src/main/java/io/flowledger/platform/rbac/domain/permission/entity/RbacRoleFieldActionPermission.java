package io.flowledger.platform.rbac.domain.permission.entity;

import io.flowledger.platform.rbac.domain.role.valueobject.RbacFieldAction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents action-specific field-level permissions assigned to a role.
 */
@Entity
@Table(name = "rbac_role_field_action_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RbacRoleFieldActionPermission {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "role_id", nullable = false)
  private UUID roleId;

  @Column(name = "resource_field_id", nullable = false)
  private UUID resourceFieldId;

  @Enumerated(EnumType.STRING)
  @Column(name = "action", nullable = false, length = 20)
  private RbacFieldAction action;

  @Column(name = "allowed", nullable = false)
  private boolean allowed;

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
