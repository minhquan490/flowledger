package io.flowledger.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;

/**
 * Main Spring Boot application entry point for FlowLedger.
 */
@SpringBootApplication(
    scanBasePackages = {
        "io.flowledger"
    }
)
@AutoConfigurationPackage(basePackages = "io.flowledger")
public class FlowLedgerApplication {

  /**
   * Starts the FlowLedger application.
   *
   * @param args the command-line arguments
   */
  static void main(String[] args) {
    SpringApplication.run(FlowLedgerApplication.class, args);
  }
}
