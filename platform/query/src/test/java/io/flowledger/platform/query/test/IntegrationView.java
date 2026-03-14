package io.flowledger.platform.query.test;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.platform.query.annotation.Sum;
import java.math.BigDecimal;

/**
 * Simple Blaze view with a custom mapping annotation.
 */
@EntityView(IntegrationEntity.class)
public interface IntegrationView {
  @Sum("amount")
  BigDecimal getTotalAmount();
}
