package io.flowledger.platform.query.blaze;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.core.boot.ClasspathScanner;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

/**
 * Loads Blaze entity view classes from configured base packages.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(BlazeViewLoader.class)
public class BlazeViewLoader {
  private final ClasspathScanner classpathScanner;

  /**
   * Scans base packages and returns classes annotated with {@link EntityView}.
   *
   * @param basePackages package names used for scanning
   * @return discovered Blaze entity view classes
   */
  public Collection<Class<?>> loadViews(String[] basePackages) {
    Set<Class<?>> classes = classpathScanner.scan(basePackages);
    return classes.stream()
        .filter(clazz -> clazz.isAnnotationPresent(EntityView.class))
        .toList();
  }
}
