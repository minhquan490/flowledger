package io.flowledger.domain.category.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import io.flowledger.domain.category.aggregate.Tag;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for tags.
 */
@EntityView(Tag.class)
@GraphQlModel(value = "tag", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface TagView {

  /**
   * Returns the tag identifier.
   *
   * @return the tag id
   */
  @IdMapping
  UUID getId();

  /**
   * Returns the tag name.
   *
   * @return the tag name
   */
  String getName();

  /**
   * Returns the tag color.
   *
   * @return the tag color
   */
  String getColor();

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
