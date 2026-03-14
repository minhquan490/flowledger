package io.flowledger.domain.category.view;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.category.aggregate.Tag;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import java.time.Instant;
import java.util.UUID;

/**
 * Write-capable view for tags.
 */
@EntityView(Tag.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel("tagWrite")
public interface TagWriteView extends TagView {

  /**
   * Sets the tag identifier.
   *
   * @param id the tag id
   */
  void setId(UUID id);

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
