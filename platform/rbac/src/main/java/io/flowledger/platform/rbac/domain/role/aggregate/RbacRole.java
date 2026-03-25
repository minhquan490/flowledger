package io.flowledger.platform.rbac.domain.role.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Represents a role used for RBAC authorization.
 */
@Entity
@Table(name = "rbac_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RbacRole {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, length = Integer.MAX_VALUE)
  private UUID id;

  @Column(name = "code", nullable = false, unique = true, length = 100)
  private String code;

  @Column(name = "name", nullable = false, length = 200)
  private String name;

  @Column(name = "is_default", nullable = false, length = Integer.MAX_VALUE)
  private boolean defaultRole;

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
