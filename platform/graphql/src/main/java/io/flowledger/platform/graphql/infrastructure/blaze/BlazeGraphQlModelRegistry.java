package io.flowledger.platform.graphql.infrastructure.blaze;

import com.blazebit.persistence.view.EntityView;
import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.application.GraphQlInternalException;
import io.flowledger.platform.graphql.application.GraphQlQueryHandlerRegistry;
import io.flowledger.platform.graphql.domain.GraphQlModel;
import io.flowledger.platform.query.blaze.BlazeViewLoader;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;

/**
 * Resolves GraphQL model names to Blaze entity view definitions.
 */
public class BlazeGraphQlModelRegistry {
  @Getter
  private final Map<String, BlazeGraphQlViewDefinition> viewsByModel;
  private final ApplicationContext applicationContext;

  /**
   * Creates a registry by scanning for Blaze entity views.
   *
   * @param blazeViewLoader    loader for Blaze views
   * @param applicationContext Spring application context
   */
  public BlazeGraphQlModelRegistry(
      BlazeViewLoader blazeViewLoader,
      ApplicationContext applicationContext
  ) {
    this.applicationContext = applicationContext;
    this.viewsByModel = loadViews(blazeViewLoader, applicationContext);
  }

  /**
   * Returns the view definition for the given model name.
   *
   * @param model the GraphQL model name
   * @return the resolved view definition
   */
  public BlazeGraphQlViewDefinition viewFor(String model) {
    String key = normalizeModel(model);
    BlazeGraphQlViewDefinition definition = viewsByModel.get(key);
    if (definition == null) {
      throw new GraphQlApiException("No Blaze view registered for model: " + model, GraphQlQueryHandlerRegistry.NOT_FOUND);
    }
    return definition;
  }

  /**
   * Loads and indexes Blaze entity views by model name.
   *
   * @param blazeViewLoader    loader for Blaze views
   * @param applicationContext Spring application context
   * @return the indexed view map
   */
  private Map<String, BlazeGraphQlViewDefinition> loadViews(
      BlazeViewLoader blazeViewLoader,
      ApplicationContext applicationContext
  ) {
    if (!AutoConfigurationPackages.has(applicationContext)) {
      throw new GraphQlInternalException("No auto-configuration base packages found for Blaze view scanning");
    }
    String[] basePackages = AutoConfigurationPackages.get(applicationContext).toArray(new String[0]);
    Collection<Class<?>> views = blazeViewLoader.loadViews(basePackages);
    return views.stream()
        .map(this::toDefinition)
        .collect(Collectors.toUnmodifiableMap(
            definition -> normalizeModel(definition.model()),
            Function.identity(),
            (first, _) -> {
              throw new GraphQlInternalException(
                  "Multiple Blaze views mapped to model: " + first.model());
            }
        ));
  }

  /**
   * Builds a view definition from a Blaze view class.
   *
   * @param viewClass the Blaze entity view type
   * @return the resolved view definition
   */
  private BlazeGraphQlViewDefinition toDefinition(Class<?> viewClass) {
    EntityView entityView = viewClass.getAnnotation(EntityView.class);
    if (entityView == null) {
      throw new GraphQlInternalException(
          "Blaze view " + viewClass.getName() + " is missing @EntityView");
    }
    BlazeGraphQlViewDefinition.ModelInformation modelInformation = resolveModelInformation(viewClass);
    return new BlazeGraphQlViewDefinition(modelInformation, viewClass, entityView.value(), applicationContext);
  }

  /**
   * Resolves model metadata from a Blaze view class.
   *
   * @param viewClass the Blaze view class
   * @return the resolved model information
   */
  private BlazeGraphQlViewDefinition.ModelInformation resolveModelInformation(Class<?> viewClass) {
    GraphQlModel model = viewClass.getAnnotation(GraphQlModel.class);

    if (model == null) {
      throw new GraphQlInternalException("GraphQl model " + viewClass.getName() + " is missing GraphQlModel");
    }

    String modelName = resolveModelName(viewClass, model);
    return new BlazeGraphQlViewDefinition.ModelInformation(modelName, model.mutationPolicy(), model.accessPolicy(), model.mutationPayloadValidator());
  }

  private String resolveModelName(Class<?> viewClass, GraphQlModel model) {
    if (!model.value().isBlank()) {
      return model.value();
    }
    String simpleName = viewClass.getSimpleName();
    if (simpleName.endsWith("View") && simpleName.length() > 4) {
      return decapitalize(simpleName.substring(0, simpleName.length() - 4));
    }
    return decapitalize(simpleName);
  }

  /**
   * Normalizes model names for lookups.
   *
   * @param model the model name
   * @return the normalized model name
   */
  private String normalizeModel(String model) {
    return model == null ? "" : model.toLowerCase(Locale.ROOT).trim();
  }

  /**
   * Lowercases the first character of the provided name.
   *
   * @param value the input value
   * @return decapitalized name
   */
  private String decapitalize(String value) {
    if (value == null || value.isBlank()) {
      return value;
    }
    char first = value.charAt(0);
    if (Character.isLowerCase(first)) {
      return value;
    }
    return Character.toLowerCase(first) + value.substring(1);
  }
}
