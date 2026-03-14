package io.flowledger.platform.query.test;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.platform.query.annotation.Sum;

/**
 * Dummy Blaze view used for mapping tests.
 */
@EntityView(DummyEntity.class)
public interface DummyView {
  /**
   * Returns total amount mapping.
   *
   * @return total amount
   */
  @Sum("amount")
  String totalAmount();
}
