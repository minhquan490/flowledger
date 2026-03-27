package io.flowledger.application.identity.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

/**
 * Payload describing identity attributes extracted from the JWT.
 */
@Getter
@ToString
@Builder
public class UserIdentityPayload {

  private final String email;
  private final Optional<String> username;
  private final Optional<String> fullName;
  private final Optional<String> firstName;
  private final Optional<String> lastName;
}
