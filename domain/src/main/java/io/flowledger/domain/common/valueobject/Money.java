package io.flowledger.domain.common.valueobject;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

/**
 * Value object representing a monetary amount and currency code.
 */
@Value
@Builder
public class Money {
  BigDecimal amount;
  String currencyCode;
}
