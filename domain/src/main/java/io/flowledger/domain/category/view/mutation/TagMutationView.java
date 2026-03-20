package io.flowledger.domain.category.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.category.aggregate.Tag;
import io.flowledger.domain.category.validator.TagMutationViewPayloadValidator;
import io.flowledger.domain.category.view.TagView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;

/**
 * Mutation-capable view for tags.
 */
@EntityView(Tag.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "tagMutation",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = TagMutationViewPayloadValidator.class
)
public interface TagMutationView extends TagView {
  /**
   * Sets the tag name.
   *
   * @param name the tag name
   */
  void setName(String name);

  /**
   * Sets the tag color.
   *
   * @param color the tag color
   */
  void setColor(String color);

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
