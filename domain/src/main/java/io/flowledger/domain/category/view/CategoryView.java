package io.flowledger.domain.category.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.category.aggregate.Category;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for categories.
 */
@EntityView(Category.class)
@GraphQlModel("category")
public interface CategoryView {

  /**
   * Returns the category identifier.
   *
   * @return the category id
   */
  UUID getId();

  /**
   * Returns the category name.
   *
   * @return the category name
   */
  String getName();

  /**
   * Returns the parent category identifier.
   *
   * @return the parent category id
   */
  UUID getParentId();

  /**
   * Returns the category icon.
   *
   * @return the category icon
   */
  String getIcon();

  /**
   * Returns the category color.
   *
   * @return the category color
   */
  String getColor();

  /**
   * Returns the category rules.
   *
   * @return the rules
   */
  String getRules();

  /**
   * Returns the creation timestamp.
   *
   * @return the creation timestamp
   */
  Instant getCreatedAt();

  /**
   * Returns the update timestamp.
   *
   * @return the update timestamp
   */
  Instant getUpdatedAt();
}
