package io.flowledger.platform.query.blaze;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.spi.CriteriaBuilderConfigurationProvider;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.FullQueryBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.persistence.view.EntityViews;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import io.flowledger.platform.query.mapping.MappingExpressionResolver;
import io.flowledger.platform.query.test.IntegrationEntity;
import io.flowledger.platform.query.test.IntegrationView;
import io.flowledger.platform.query.test.IntegrationViewMixed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test for runtime mapping injection with Blaze.
 */
class BlazeRuntimeInjectionIntegrationTest {

  /**
   * Verifies that Blaze can build an {@link EntityViewManager} after mapping injection.
   */
  @Test
  @SuppressWarnings({"unchecked", "rawtypes"})
  void buildsEntityViewManagerWithInjectedMapping() throws Exception {
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
        .applySetting("hibernate.connection.driver_class", "org.h2.Driver")
        .applySetting("hibernate.connection.url", "jdbc:h2:mem:flowledger;DB_CLOSE_DELAY=-1")
        .applySetting("hibernate.connection.username", "sa")
        .applySetting("hibernate.connection.password", "")
        .applySetting("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
        .applySetting("hibernate.hbm2ddl.auto", "create-drop")
        .build();

    MetadataSources sources = new MetadataSources(registry)
        .addAnnotatedClass(IntegrationEntity.class);
    Metadata metadata = sources.getMetadataBuilder().build();
    SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

    CriteriaBuilderConfiguration cbConfig = loadCriteriaBuilderConfiguration();
    CriteriaBuilderFactory cbf = cbConfig.createCriteriaBuilderFactory(sessionFactory);

    BlazeMappingResolver resolver = new BlazeMappingResolver(new MappingExpressionResolver());
    BlazeViewMappingApplier applier = new BlazeViewMappingApplier(resolver);
    EntityViewConfiguration evConfig = EntityViews.createDefaultConfiguration();

    Class<?> registeredView = applier.apply(IntegrationView.class, evConfig);
    EntityViewManager evm = evConfig.createEntityViewManager(cbf);

    EntityManager em = sessionFactory.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    IntegrationEntity first = new IntegrationEntity();
    first.setId(1L);
    first.setAmount(new BigDecimal("10.00"));
    IntegrationEntity second = new IntegrationEntity();
    second.setId(2L);
    second.setAmount(new BigDecimal("20.00"));
    em.persist(first);
    em.persist(second);
    tx.commit();

    CriteriaBuilder<IntegrationEntity> cb = cbf.create(em, IntegrationEntity.class);
    EntityViewSetting setting = EntityViewSetting.create(registeredView);
    FullQueryBuilder viewCb = evm.applySetting(setting, cb);
    List<Object> results = (List<Object>) viewCb.getResultList();

    assertNotNull(evm);
    assertEquals(1, results.size());
    Object result = results.getFirst();
    Method getter = registeredView.getMethod("getTotalAmount");
    BigDecimal total = (BigDecimal) getter.invoke(result);
    assertEquals(new BigDecimal("30.00"), total);

    em.close();
    sessionFactory.close();
    StandardServiceRegistryBuilder.destroy(registry);
  }

  /**
   * Verifies that custom and Blaze @Mapping can coexist in the same view.
   */
  @Test
  @SuppressWarnings({"unchecked", "rawtypes"})
  void supportsCustomAndBlazeMappingTogether() throws Exception {
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
        .applySetting("hibernate.connection.driver_class", "org.h2.Driver")
        .applySetting("hibernate.connection.url", "jdbc:h2:mem:flowledger2;DB_CLOSE_DELAY=-1")
        .applySetting("hibernate.connection.username", "sa")
        .applySetting("hibernate.connection.password", "")
        .applySetting("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
        .applySetting("hibernate.hbm2ddl.auto", "create-drop")
        .build();

    MetadataSources sources = new MetadataSources(registry)
        .addAnnotatedClass(IntegrationEntity.class);
    Metadata metadata = sources.getMetadataBuilder().build();
    SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

    CriteriaBuilderConfiguration cbConfig = loadCriteriaBuilderConfiguration();
    CriteriaBuilderFactory cbf = cbConfig.createCriteriaBuilderFactory(sessionFactory);

    BlazeMappingResolver resolver = new BlazeMappingResolver(new MappingExpressionResolver());
    BlazeViewMappingApplier applier = new BlazeViewMappingApplier(resolver);
    EntityViewConfiguration evConfig = EntityViews.createDefaultConfiguration();

    Class<?> registeredView = applier.apply(IntegrationViewMixed.class, evConfig);
    EntityViewManager evm = evConfig.createEntityViewManager(cbf);

    EntityManager em = sessionFactory.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    IntegrationEntity entity = new IntegrationEntity();
    entity.setId(1L);
    entity.setAmount(new BigDecimal("7.50"));
    em.persist(entity);
    tx.commit();

    CriteriaBuilder<IntegrationEntity> cb = cbf.create(em, IntegrationEntity.class);
    EntityViewSetting setting = EntityViewSetting.create(registeredView);
    FullQueryBuilder viewCb = evm.applySetting(setting, cb);
    List<Object> results = (List<Object>) viewCb.getResultList();

    assertEquals(1, results.size());
    Object result = results.getFirst();
    BigDecimal total = (BigDecimal) registeredView.getMethod("getTotalAmount").invoke(result);
    BigDecimal raw = (BigDecimal) registeredView.getMethod("getRawAmount").invoke(result);
    assertEquals(new BigDecimal("7.50"), raw);
    assertEquals(new BigDecimal("7.50"), total);

    em.close();
    sessionFactory.close();
    StandardServiceRegistryBuilder.destroy(registry);
  }

  /**
   * Loads the Blaze {@link CriteriaBuilderConfiguration} via service provider.
   *
   * @return configuration instance
   */
  private CriteriaBuilderConfiguration loadCriteriaBuilderConfiguration() {
    return java.util.ServiceLoader.load(CriteriaBuilderConfigurationProvider.class)
        .findFirst()
        .orElseThrow()
        .createConfiguration();
  }

  // IntegrationEntity and IntegrationView are defined as top-level types
  // to avoid classloader access errors during bytecode injection.
}
