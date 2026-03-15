package io.flowledger.domain.account.view;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.domain.account.aggregate.Account;
import io.flowledger.domain.identity.view.UserView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * GraphQL view for accounts.
 */
@EntityView(Account.class)
@GraphQlModel(value = "account", accessPolicy = RbacGraphQlAccessPolicy.class)
public interface AccountView {

  /**
   * Returns the account identifier.
   *
   * @return the account id
   */
  UUID getId();

  /**
   * Returns the account name.
   *
   * @return the account name
   */
  String getName();

  /**
   * Returns the owning user identifier.
   *
   * @return the user id
   */
  UUID getUserId();

  /**
   * Returns the owning user details.
   *
   * @return the user view
   */
  UserView getUser();

  /**
   * Returns the account type.
   *
   * @return the account type
   */
  String getAccountType();

  /**
   * Returns the currency code.
   *
   * @return the currency code
   */
  String getCurrencyCode();

  /**
   * Returns the opening balance.
   *
   * @return the opening balance
   */
  BigDecimal getOpeningBalance();

  /**
   * Returns the account status.
   *
   * @return the account status
   */
  String getStatus();

  /**
   * Returns the account icon.
   *
   * @return the icon
   */
  String getIcon();

  /**
   * Returns the account color.
   *
   * @return the color
   */
  String getColor();

  /**
   * Returns the creation timestamp.
   *
   * @return the creation timestamp
   */
  Instant getCreatedAt();

  /**
   * Returns the update timestamp.
   *
   * @return the update timestamp
   */
  Instant getUpdatedAt();
}
