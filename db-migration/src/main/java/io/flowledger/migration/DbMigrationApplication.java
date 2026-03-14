package io.flowledger.migration;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Command line application for running database migrations and optional seed data.
 */
@SpringBootApplication
@Slf4j
public class DbMigrationApplication {

  /**
   * Runs the migration application with optional seed data context.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(DbMigrationApplication.class);
    Map<String, Object> defaults = new HashMap<>();
    defaults.put("spring.liquibase.change-log", "classpath:db/changelog/db.changelog-master.yaml");

    if (isSeedEnabled(args)) {
      defaults.put("spring.liquibase.contexts", "seed");
      log.info("Seed data context enabled.");
    }

    application.setDefaultProperties(defaults);
    application.run(args);
  }

  /**
   * Determines whether the seed data context should be enabled.
   *
   * @param args command line arguments
   * @return true when seed data should be applied
   */
  private static boolean isSeedEnabled(String[] args) {
    if (args == null) {
      return false;
    }
    for (String arg : args) {
      if ("--seed".equalsIgnoreCase(arg) || "--seed=true".equalsIgnoreCase(arg)) {
        return true;
      }
    }
    return false;
  }
}
