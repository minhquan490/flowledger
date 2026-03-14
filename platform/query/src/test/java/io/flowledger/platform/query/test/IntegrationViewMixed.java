package io.flowledger.platform.query.test;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import io.flowledger.platform.query.annotation.Sum;
import java.math.BigDecimal;

/**
 * Blaze view with both custom and native Blaze mapping annotations.
 */
@EntityView(IntegrationEntity.class)
public interface IntegrationViewMixed {
  @Sum("amount")
  BigDecimal getTotalAmount();

  @Mapping("amount")
  BigDecimal getRawAmount();
}
