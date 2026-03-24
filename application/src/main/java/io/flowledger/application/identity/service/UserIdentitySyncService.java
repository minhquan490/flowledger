package io.flowledger.application.identity.service;

import com.blazebit.persistence.view.EntityViewManager;
import io.flowledger.application.identity.model.UserIdentityPayload;
import io.flowledger.domain.identity.aggregate.UserStatus;
import io.flowledger.domain.identity.view.mutation.UserMutationView;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Synchronizes user identity information from JWT payloads into the database.
 */
@Service
@RequiredArgsConstructor
public class UserIdentitySyncService {

  private static final UserStatus STATUS_ACTIVE = UserStatus.ACTIVE;

  private final EntityManager entityManager;
  private final EntityViewManager entityViewManager;
  private final UserIdentityLookupService userIdentityLookupService;

  /**
   * Synchronizes user identity data using the supplied payload.
   *
   * @param payload the identity payload extracted from the JWT
   */
  @Transactional
  public void syncUser(UserIdentityPayload payload) {
    Instant now = Instant.now();
    Optional<UUID> existingUserId = userIdentityLookupService.findUserIdByEmail(payload.getEmail());
    if (existingUserId.isPresent()) {
      updateExistingUser(existingUserId.get(), now);
      return;
    }

    createNewUser(payload.getEmail(), now);
  }

  /**
   * Updates timestamps and status for an existing user.
   *
   * @param userId the user identifier to update
   * @param now the timestamp to apply
   */
  private void updateExistingUser(UUID userId, Instant now) {
    UserMutationView user = entityViewManager.find(entityManager, UserMutationView.class, userId);
    if (user == null) {
      throw new IllegalStateException("User not found for id " + userId);
    }
    user.setStatus(STATUS_ACTIVE);
    user.setUpdatedAt(now);
    entityViewManager.save(entityManager, user);
  }

  /**
   * Creates a new user mutation view for the given email.
   *
   * @param email the user email
   * @param now the timestamp to apply
   */
  private void createNewUser(String email, Instant now) {
    UserMutationView user = entityViewManager.create(UserMutationView.class);
    user.setEmail(email);
    user.setStatus(STATUS_ACTIVE);
    user.setCreatedAt(now);
    user.setUpdatedAt(now);
    entityViewManager.save(entityManager, user);
  }
}
