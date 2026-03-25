package io.flowledger.platform.rbac.domain.permission.entity;

import io.flowledger.platform.rbac.domain.role.valueobject.RbacAction;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents resource-level permissions assigned to a role.
 */
@Entity
@Table(name = "rbac_role_resource_permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RbacRoleResourcePermission {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, length = Integer.MAX_VALUE)
  private UUID id;

  @Column(name = "role_id", nullable = false, length = Integer.MAX_VALUE)
  private UUID roleId;

  @Column(name = "resource_id", nullable = false, length = Integer.MAX_VALUE)
  private UUID resourceId;

  @Enumerated(EnumType.STRING)
  @Column(name = "action", nullable = false, length = 20)
  private RbacAction action;

  @Column(name = "allowed", nullable = false, length = Integer.MAX_VALUE)
  private boolean allowed;

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
