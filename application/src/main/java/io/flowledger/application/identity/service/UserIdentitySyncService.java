package io.flowledger.application.identity.service;

import io.flowledger.application.identity.model.UserIdentityPayload;
import io.flowledger.domain.identity.aggregate.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Synchronizes user identity information from JWT payloads into the database.
 */
@Service
@RequiredArgsConstructor
public class UserIdentitySyncService {

  private static final String STATUS_ACTIVE = "ACTIVE";
  private static final String FIND_BY_EMAIL_QUERY = "select u from User u where u.email = :email";
  private static final String EMAIL_PARAMETER = "email";

  private final EntityManager entityManager;

  /**
   * Synchronizes user identity data using the supplied payload.
   *
   * @param payload the identity payload extracted from the JWT
   */
  @Transactional
  public void syncUser(UserIdentityPayload payload) {
    Instant now = Instant.now();
    Optional<User> existingUser = findUserByEmail(payload.getEmail());
    if (existingUser.isPresent()) {
      updateExistingUser(existingUser.get(), now);
      return;
    }

    User newUser = createNewUser(payload.getEmail(), now);
    entityManager.persist(newUser);
  }

  /**
   * Looks up a user by email address.
   *
   * @param email the user email
   * @return the user if found
   */
  private Optional<User> findUserByEmail(String email) {
    TypedQuery<User> query = entityManager.createQuery(FIND_BY_EMAIL_QUERY, User.class);
    query.setParameter(EMAIL_PARAMETER, email);
    query.setMaxResults(1);
    List<User> results = query.getResultList();
    return results.stream().findFirst();
  }

  /**
   * Updates timestamps and status for an existing user.
   *
   * @param user the existing user entity
   * @param now the timestamp to apply
   */
  private void updateExistingUser(User user, Instant now) {
    user.setStatus(STATUS_ACTIVE);
    user.setUpdatedAt(now);
    entityManager.merge(user);
  }

  /**
   * Creates a new user entity for the given email.
   *
   * @param email the user email
   * @param now the timestamp to apply
   * @return the newly built user entity
   */
  private User createNewUser(String email, Instant now) {
    return User.builder()
        .email(email)
        .status(STATUS_ACTIVE)
        .createdAt(now)
        .updatedAt(now)
        .build();
  }
}
