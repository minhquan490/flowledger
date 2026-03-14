package io.flowledger.domain.identity.valueobject;

import lombok.Builder;
import lombok.Value;

/**
 * Value object representing a normalized email address.
 */
@Value
@Builder
public class EmailAddress {
  String value;
}
