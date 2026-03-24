package io.flowledger.application.identity.service;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import io.flowledger.application.identity.config.IdentityCacheNames;
import io.flowledger.domain.identity.aggregate.User;
import io.flowledger.domain.identity.aggregate.UserStatus;
import io.flowledger.domain.identity.view.UserView;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
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

  private static final String USER_ID_FIELD = "id";
  private static final String USER_EMAIL_FIELD = "email";
  private static final String USER_STATUS_FIELD = "status";
  private static final UserStatus ACTIVE_STATUS = UserStatus.ACTIVE;

  private final BlazeQueryBuilder blazeQueryBuilder;
  private final EntityViewManager entityViewManager;

  /**
   * Finds a user identifier by email.
   *
   * @param email the user email
   * @return the user identifier if found
   */
  @Cacheable(cacheNames = IdentityCacheNames.USER_ID_BY_EMAIL, key = "#email")
  public Optional<UUID> findUserIdByEmail(String email) {
    CriteriaBuilder<User> criteriaBuilder = blazeQueryBuilder.forEntity(User.class);
    criteriaBuilder.where(USER_EMAIL_FIELD).eq(email);
    criteriaBuilder.where(USER_STATUS_FIELD).eq(ACTIVE_STATUS);

    EntityViewSetting<UserView, CriteriaBuilder<UserView>> setting = EntityViewSetting.create(UserView.class);
    blazeQueryBuilder.applyFields(setting, List.of(USER_ID_FIELD));

    List<UserView> results = entityViewManager
        .applySetting(setting, criteriaBuilder)
        .setMaxResults(1)
        .getResultList();
    return results.stream().map(UserView::getId).findFirst();
  }

}
