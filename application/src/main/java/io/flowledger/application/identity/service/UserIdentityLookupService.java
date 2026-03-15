package io.flowledger.application.identity.service;

import io.flowledger.application.identity.config.IdentityCacheNames;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Resolves user identifiers from identity attributes.
 */
@Service
@RequiredArgsConstructor
public class UserIdentityLookupService {

  private static final String FIND_ID_BY_EMAIL_QUERY = "select u.id from User u where u.email = :email";
  private static final String EMAIL_PARAMETER = "email";

  private final EntityManager entityManager;

  /**
   * Finds a user identifier by email.
   *
   * @param email the user email
   * @return the user identifier if found
   */
  @Cacheable(cacheNames = IdentityCacheNames.USER_ID_BY_EMAIL, key = "#email")
  public Optional<UUID> findUserIdByEmail(String email) {
    TypedQuery<UUID> query = entityManager.createQuery(FIND_ID_BY_EMAIL_QUERY, UUID.class);
    query.setParameter(EMAIL_PARAMETER, email);
    query.setMaxResults(1);
    List<UUID> results = query.getResultList();
    return results.stream().findFirst();
  }

}
