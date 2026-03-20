package io.flowledger.domain.category.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.category.aggregate.Category;
import io.flowledger.domain.category.validator.CategoryMutationViewPayloadValidator;
import io.flowledger.domain.category.view.CategoryView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for categories.
 */
@EntityView(Category.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "categoryMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = CategoryMutationViewPayloadValidator.class
)
public interface CategoryMutationView extends CategoryView {
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
