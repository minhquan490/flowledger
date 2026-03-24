package io.flowledger.platform.query.blaze;

import com.blazebit.persistence.view.spi.TransactionSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Adapts Spring's {@link PlatformTransactionManager} to Blaze's {@link TransactionSupport} contract.
 *
 * <p>Used by entity-view configuration so Blaze update flows can execute callbacks within
 * a Spring-managed transaction boundary.
 */
public class BlazeTransactionSupportAdapter implements TransactionSupport {
  private final TransactionTemplate transactionTemplate;

  /**
   * Creates support backed by the given transaction manager.
   *
   * @param transactionManager the platform transaction manager
   */
  public BlazeTransactionSupportAdapter(PlatformTransactionManager transactionManager) {
    this.transactionTemplate = new TransactionTemplate(transactionManager);
  }

  /**
   * Executes the runnable inside a Spring-managed transaction.
   *
   * @param runnable the runnable to execute
   */
  @Override
  public void transactional(Runnable runnable) {
    transactionTemplate.executeWithoutResult(status -> runnable.run());
  }
}
