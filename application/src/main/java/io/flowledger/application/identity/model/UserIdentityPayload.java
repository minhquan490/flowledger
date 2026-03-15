package io.flowledger.application.identity.model;

import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Payload describing identity attributes extracted from the JWT.
 */
@Getter
@ToString
@Builder
public class UserIdentityPayload {

  private final Optional<String> subject;
  private final String email;
  private final Optional<String> username;
  private final Optional<String> fullName;
}
