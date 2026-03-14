package io.flowledger.core.boot;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Scans classpath packages and resolves discovered classes.
 */
@Component
public class ClasspathScanner {
  private static final String SCAN_CACHE_NAME = BootstrapCacheNames.CLASSPATH_SCANNER_SCAN;

  /**
   * Scans provided packages and returns discovered classes.
   *
   * @param packagesToScan package names to scan
   * @return discovered classes, or an empty set when input is null/empty
   */
  @Cacheable(cacheNames = SCAN_CACHE_NAME, key = "T(java.util.Arrays).hashCode(#packagesToScan)")
  public Set<Class<?>> scan(String[] packagesToScan) {
    if (packagesToScan == null || packagesToScan.length == 0) {
      return Collections.emptySet();
    }

    Set<Class<?>> result = new HashSet<>();
    for (String pkg : packagesToScan) {
      collectClassesForPackage(pkg, result);
    }
    return result;
  }

  /**
   * Scans a package and adds resolved classes into the target set.
   *
   * @param packageName package name to scan
   * @param target target set receiving resolved classes
   */
  private void collectClassesForPackage(String packageName, Set<Class<?>> target) {
    ClassPathScanningCandidateComponentProvider provider = createProvider(packageName);
    Set<BeanDefinition> beans = provider.findCandidateComponents(packageName);
    for (BeanDefinition bean : beans) {
      resolveClass(bean.getBeanClassName()).ifPresent(target::add);
    }
  }

  /**
   * Creates a scanner provider constrained to the given package prefix.
   *
   * @param packageName package prefix used by include filter
   * @return configured component provider
   */
  private ClassPathScanningCandidateComponentProvider createProvider(String packageName) {
    ClassPathScanningCandidateComponentProvider provider =
        new ClassPathScanningCandidateComponentProvider(false) {
          @Override
          protected boolean isCandidateComponent(@NonNull AnnotatedBeanDefinition beanDefinition) {
            return true;
          }
        };
    provider.addIncludeFilter((metadataReader, metadataReaderFactory) ->
        metadataReader.getClassMetadata().getClassName().startsWith(packageName));
    return provider;
  }

  /**
   * Resolves a class by name using the system class loader.
   *
   * @param className class name to resolve
   * @return resolved class or empty when class cannot be loaded
   */
  private Optional<Class<?>> resolveClass(String className) {
    if (className == null) {
      return java.util.Optional.empty();
    }
    try {
      return Optional.of(ClassUtils.forName(className, ClassLoader.getSystemClassLoader()));
    } catch (ClassNotFoundException ex) {
      return Optional.empty();
    }
  }
}
