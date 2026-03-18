package io.flowledger.domain.account.view.mutation;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.domain.account.aggregate.Account;
import io.flowledger.domain.account.validator.AccountMutationViewPayloadValidator;
import io.flowledger.domain.account.view.AccountView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQLMutationPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Mutation-capable view for accounts.
 */
@EntityView(Account.class)
@CreatableEntityView
@UpdatableEntityView
@GraphQlModel(
    value = "accountWrite",
    mutationPolicy = RbacGraphQLMutationPolicy.class,
    mutationPayloadValidator = AccountMutationViewPayloadValidator.class
)
public interface AccountMutationView extends AccountView {
  /**
   * Sets the user identifier.
   *
   * @param userId the user id
   */
  void setUserId(UUID userId);

  /**
   * Sets the account name.
   *
   * @param name the account name
   */
  void setName(String name);

  /**
   * Sets the account type.
   *
   * @param accountType the account type
   */
  void setAccountType(String accountType);

  /**
   * Sets the currency code.
   *
   * @param currencyCode the currency code
   */
  void setCurrencyCode(String currencyCode);

  /**
   * Sets the opening balance.
   *
   * @param openingBalance the opening balance
   */
  void setOpeningBalance(BigDecimal openingBalance);

  /**
   * Sets the status.
   *
   * @param status the status
   */
  void setStatus(String status);

  /**
   * Sets the icon.
   *
   * @param icon the icon
   */
  void setIcon(String icon);

  /**
   * Sets the color.
   *
   * @param color the color
   */
  void setColor(String color);

  /**
   * Sets the creation timestamp.
   *
   * @param createdAt the creation timestamp
   */
  void setCreatedAt(Instant createdAt);

  /**
   * Sets the update timestamp.
   *
   * @param updatedAt the update timestamp
   */
  void setUpdatedAt(Instant updatedAt);
}
