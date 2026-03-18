package io.flowledger.platform.rbac.infrastructure.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.blazebit.persistence.view.EntityView;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlModelRegistry;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlViewDefinition;
import io.flowledger.platform.rbac.domain.resource.aggregate.RbacResource;
import io.flowledger.platform.rbac.infrastructure.graphql.RbacGraphQlAccessPolicy;
import jakarta.persistence.EntityManager;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link RbacResourceSynchronizer}.
 */
@SpringBootTest(classes = RbacResourceSynchronizerIntegrationTest.TestAppConfiguration.class)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:rbac_sync_it;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.packages-to-scan=io.flowledger.platform.rbac",
    "spring.jpa.properties.hibernate.show_sql=false",
    "spring.autoconfigure.exclude="
        + "io.flowledger.platform.query.autoconfigure.CoreQueryAutoConfiguration,"
        + "io.flowledger.platform.rbac.infrastructure.autoconfigure.RbacAutoConfiguration,"
        + "org.springframework.boot.graphql.autoconfigure.GraphQlAutoConfiguration"
})
@DirtiesContext
@Transactional
class RbacResourceSynchronizerIntegrationTest {
  private static final String MODEL_ACCOUNT = "account";
  private static final String MODEL_TRANSACTION = "transaction";
  private static final String MODEL_NORMALIZED = "normalized";

  private static final Map<String, Class<?>> MODEL_VIEWS = new LinkedHashMap<>();

  @Autowired
  private RbacResourceSynchronizer synchronizer;

  @Autowired
  private EntityManager entityManager;

  /**
   * Resets model registry fixtures before each test.
   */
  @BeforeEach
  void setup() {
    MODEL_VIEWS.clear();
    MODEL_VIEWS.put(MODEL_ACCOUNT, AccountSyncView.class);
    MODEL_VIEWS.put(MODEL_TRANSACTION, TransactionSyncView.class);
  }

  /**
   * Verifies initial synchronization persists resources, fields, and admin field permissions.
   */
  @Test
  void synchronizePersistsResourcesFieldsAndAdminFieldPermissions() {
    synchronizer.synchronize();
    assertThat(count("select count(*) from rbac_resources")).isEqualTo(2L);
    assertThat(count("select count(*) from rbac_resource_fields")).isEqualTo(5L);
    assertThat(count(
        "select count(*) from rbac_role_field_action_permissions p "
            + "join rbac_roles r on p.role_id = r.id "
            + "where r.code = 'super-admin'"
    )).isEqualTo(15L);
  }

  /**
   * Verifies synchronization is idempotent across repeated runs.
   */
  @Test
  void synchronizeIsIdempotent() {
    synchronizer.synchronize();
    long resources = count("select count(*) from rbac_resources");
    long fields = count("select count(*) from rbac_resource_fields");
    long permissions = count("select count(*) from rbac_role_field_action_permissions");

    synchronizer.synchronize();

    assertThat(count("select count(*) from rbac_resources")).isEqualTo(resources);
    assertThat(count("select count(*) from rbac_resource_fields")).isEqualTo(fields);
    assertThat(count("select count(*) from rbac_role_field_action_permissions")).isEqualTo(permissions);
  }

  /**
   * Verifies getter/setter/is methods normalize to one field name.
   */
  @Test
  void synchronizeNormalizesGetterSetterAndIsMethods() {
    MODEL_VIEWS.clear();
    MODEL_VIEWS.put(MODEL_NORMALIZED, NormalizedSyncView.class);

    synchronizer.synchronize();

    assertThat(count(
        "select count(*) from rbac_resource_fields rf "
            + "join rbac_resources r on r.id = rf.resource_id "
            + "where r.name = 'normalized' and rf.field_name = 'name'"
    )).isEqualTo(1L);
  }

  /**
   * Verifies non-accessor methods are ignored during field synchronization.
   */
  @Test
  void synchronizeIgnoresNonAccessorMethods() {
    MODEL_VIEWS.clear();
    MODEL_VIEWS.put(MODEL_NORMALIZED, NormalizedSyncView.class);

    synchronizer.synchronize();

    assertThat(count(
        "select count(*) from rbac_resource_fields rf "
            + "join rbac_resources r on r.id = rf.resource_id "
            + "where r.name = 'normalized' and rf.field_name in ('helper', 'helperMethod')"
    )).isZero();
  }


  /**
   * Verifies synchronization handles empty model registry without creating resources.
   */
  @Test
  void synchronizeHandlesEmptyRegistry() {
    MODEL_VIEWS.clear();
    synchronizer.synchronize();

    assertThat(count("select count(*) from rbac_resources")).isZero();
    assertThat(count("select count(*) from rbac_resource_fields")).isZero();
    assertThat(count("select count(*) from rbac_roles where code in ('default', 'super-admin')")).isEqualTo(2L);
  }

  /**
   * Verifies adding a new model adds only missing resources and fields.
   */
  @Test
  void synchronizeAddsOnlyMissingRowsForIncrementalModelChanges() {
    MODEL_VIEWS.clear();
    MODEL_VIEWS.put(MODEL_ACCOUNT, AccountSyncView.class);
    synchronizer.synchronize();
    long resourcesAfterFirstSync = count("select count(*) from rbac_resources");
    long fieldsAfterFirstSync = count("select count(*) from rbac_resource_fields");

    MODEL_VIEWS.put(MODEL_TRANSACTION, TransactionSyncView.class);
    synchronizer.synchronize();

    assertThat(count("select count(*) from rbac_resources")).isEqualTo(resourcesAfterFirstSync + 1);
    assertThat(count("select count(*) from rbac_resource_fields")).isEqualTo(fieldsAfterFirstSync + 2);
  }

  /**
   * Returns the count for a native SQL count query.
   *
   * @param sql the SQL count query
   * @return the count value
   */
  private long count(String sql) {
    Number value = (Number) entityManager.createNativeQuery(sql).getSingleResult();
    return value.longValue();
  }

  /**
   * Test application bootstrap configuration.
   */
  @SpringBootConfiguration
  @AutoConfigurationPackage(basePackages = "io.flowledger.platform.rbac")
  @EnableAutoConfiguration
  @Import(TestConfig.class)
  static class TestAppConfiguration {
  }

  /**
   * Test configuration for synchronizer integration tests.
   */
  @TestConfiguration
  static class TestConfig {

    /**
     * Creates the synchronizer with a controlled model registry.
     *
     * @param modelRegistry the model registry
     * @param entityManager the entity manager
     * @return the RBAC synchronizer
     */
    @Bean
    RbacResourceSynchronizer rbacResourceSynchronizer(BlazeGraphQlModelRegistry modelRegistry, EntityManager entityManager) {
      return new RbacResourceSynchronizer(modelRegistry, entityManager);
    }

    /**
     * Creates a mocked model registry containing dynamic test models.
     *
     * @return the mocked model registry
     */
    @Bean
    BlazeGraphQlModelRegistry blazeGraphQlModelRegistry() {
      BlazeGraphQlModelRegistry modelRegistry = Mockito.mock(BlazeGraphQlModelRegistry.class);
      Mockito.when(modelRegistry.getViewsByModel()).thenAnswer(invocation -> {
        Map<String, BlazeGraphQlViewDefinition> views = new LinkedHashMap<>();
        MODEL_VIEWS.forEach((model, viewType) -> views.put(model, modelDefinition(model, viewType)));
        return views;
      });
      return modelRegistry;
    }

    /**
     * Creates an object mapper for RBAC services loaded by auto-configuration.
     *
     * @return the object mapper
     */
    @Bean
    ObjectMapper objectMapper() {
      return new ObjectMapper();
    }

    /**
     * Creates a mocked model definition for a model name and view type.
     *
     * @param model the model name
     * @param viewType the view type
     * @return the mocked view definition
     */
    private BlazeGraphQlViewDefinition modelDefinition(String model, Class<?> viewType) {
      BlazeGraphQlViewDefinition definition = Mockito.mock(BlazeGraphQlViewDefinition.class);
      Mockito.when(definition.model()).thenReturn(model);
      Mockito.doReturn(viewType).when(definition).viewClass();
      return definition;
    }
  }

  /**
   * GraphQL view contract for account model sync test.
   */
  @EntityView(RbacResource.class)
  @GraphQlModel(value = MODEL_ACCOUNT, accessPolicy = RbacGraphQlAccessPolicy.class)
  interface AccountSyncView {
    String getId();

    String getName();

    boolean isActive();

    void setName(String value);
  }

  /**
   * GraphQL view contract for transaction model sync test.
   */
  @EntityView(RbacResource.class)
  @GraphQlModel(value = MODEL_TRANSACTION, accessPolicy = RbacGraphQlAccessPolicy.class)
  interface TransactionSyncView {
    String getAmount();

    boolean isPosted();

    void setAmount(String amount);
  }

  /**
   * GraphQL view contract for field name normalization and method filtering tests.
   */
  @EntityView(RbacResource.class)
  @GraphQlModel(value = MODEL_NORMALIZED, accessPolicy = RbacGraphQlAccessPolicy.class)
  interface NormalizedSyncView {
    String getName();

    void setName(String value);

    boolean isName();

    String helperMethod();
  }

}
