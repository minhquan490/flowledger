package io.flowledger.domain.category.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.category.aggregate.Tag;
import io.flowledger.domain.category.validator.TagWriteViewPayloadValidator;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.time.Instant;

/**
 * Write-capable view for tags.
 */
@EntityView(Tag.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "tagWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = TagWriteViewPayloadValidator.class
)
public interface TagWriteView extends TagView {
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
