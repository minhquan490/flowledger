package io.flowledger.domain.category.aggregate;

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
 * Aggregate root representing a transaction category.
 */
@Entity
@Table(name = Category.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
  public static final String TABLE_NAME = "categories";

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "parent_id")
  private UUID parentId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "icon")
  private String icon;

  @Column(name = "color")
  private String color;

  @Column(name = "rules")
  private String rules;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
