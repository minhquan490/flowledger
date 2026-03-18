package io.flowledger.migration;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configures Liquibase execution for the migration application.
 */
@Configuration
public class DbMigrationLiquibaseConfiguration {

  /**
   * Creates the Liquibase runner bean for database migrations.
   *
   * @param dataSource the JDBC data source
   * @param environment the Spring environment
   * @return the configured Liquibase runner
   */
  @Bean
  @ConditionalOnMissingBean(SpringLiquibase.class)
  public SpringLiquibase springLiquibase(DataSource dataSource, Environment environment) {
    SpringLiquibase liquibase = new SpringLiquibase();
    liquibase.setDataSource(dataSource);
    liquibase.setChangeLog(environment.getProperty(
        "spring.liquibase.change-log",
        "classpath:db/changelog/db.changelog-master.yaml"
    ));
    liquibase.setContexts(environment.getProperty("spring.liquibase.contexts"));
    liquibase.setDefaultSchema(environment.getProperty("spring.liquibase.default-schema"));
    liquibase.setShouldRun(Boolean.parseBoolean(environment.getProperty("spring.liquibase.enabled", "true")));
    return liquibase;
  }
}
