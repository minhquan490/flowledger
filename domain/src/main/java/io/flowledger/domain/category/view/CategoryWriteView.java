package io.flowledger.domain.category.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.category.aggregate.Category;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for categories.
 */
@EntityView(Category.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("categoryWrite")
public interface CategoryWriteView extends CategoryView {

  /**
   * Sets the category identifier.
   *
   * @param id the category id
   */
  void setId(UUID id);

  /**
   * Sets the category name.
   *
   * @param name the category name
   */
  void setName(String name);

  /**
   * Sets the parent category identifier.
   *
   * @param parentId the parent category id
   */
  void setParentId(UUID parentId);

  /**
   * Sets the icon.
   *
   * @param icon the icon
   */
  void setIcon(String icon);

  /**
   * Sets the color.
   *
   * @param color the color
   */
  void setColor(String color);

  /**
   * Sets the rules.
   *
   * @param rules the rules
   */
  void setRules(String rules);

  /**
   * Sets the creation timestamp.
   *
   * @param createdAt the creation timestamp
   */
  void setCreatedAt(Instant createdAt);

  /**
   * Sets the update timestamp.
   *
   * @param updatedAt the update timestamp
   */
  void setUpdatedAt(Instant updatedAt);
}
