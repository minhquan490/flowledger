package io.flowledger.platform.graphql.infrastructure.blaze;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.flowledger.platform.graphql.application.GraphQlMutationService;
import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.application.GraphQlQueryHandlerRegistry;
import io.flowledger.platform.graphql.domain.GraphQlMutationHandler;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import io.flowledger.platform.graphql.domain.GraphQlMutationResult;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

// TODO add way to validate input
/**
 * Generic GraphQL mutation handler backed by Blaze entity views.
 */
@RequiredArgsConstructor
public class BlazeGraphQlMutationHandler implements GraphQlMutationHandler {
  public static final String MODEL_WILDCARD = "*";
  public static final int UNSUPPORTED = 422;
  private static final String ID_PROPERTY = "id";

  private final BlazeGraphQlModelRegistry modelRegistry;
  private final BlazeQueryBuilder queryBuilder;
  private final EntityViewManager entityViewManager;

  /**
   * Returns the wildcard model name used by this generic handler.
   *
   * @return the wildcard model name
   */
  @Override
  public String model() {
    return MODEL_WILDCARD;
  }

  /**
   * Executes a mutation request using Blaze entity views.
   *
   * @param request the mutation request
   * @return the mutation result
   */
  @Override
  public GraphQlMutationResult mutate(GraphQlMutationRequest request) {
    BlazeGraphQlViewDefinition definition = modelRegistry.viewFor(request.model());
    return switch (request.action()) {
      case GraphQlMutationService.ACTION_CREATE -> create(definition, request);
      case GraphQlMutationService.ACTION_UPDATE -> update(definition, request);
      case GraphQlMutationService.ACTION_DELETE -> delete(definition, request);
      default -> throw new GraphQlApiException("Unsupported mutation action: " + request.action(), UNSUPPORTED);
    };
  }

  /**
   * Creates a new entity via a creatable Blaze view.
   *
   * @param definition the view definition
   * @param request the mutation request
   * @return the mutation result
   */
  private GraphQlMutationResult create(BlazeGraphQlViewDefinition definition, GraphQlMutationRequest request) {
    Class<?> viewClass = definition.viewClass();
    if (!viewClass.isAnnotationPresent(CreatableEntityView.class)) {
      throw new GraphQlApiException("View " + viewClass.getName() + " is not creatable.", UNSUPPORTED);
    }
    Object view = entityViewManager.create(viewClass);
    Map<String, Object> payload = mergedPayload(request);
    applyPayload(view, payload);
    entityViewManager.save(getEntityManager(), view);
    return new GraphQlMutationResult(true, view);
  }

  /**
   * Updates an existing entity via an updatable Blaze view.
   *
   * @param definition the view definition
   * @param request the mutation request
   * @return the mutation result
   */
  private GraphQlMutationResult update(BlazeGraphQlViewDefinition definition, GraphQlMutationRequest request) {
    Class<?> viewClass = definition.viewClass();
    if (!viewClass.isAnnotationPresent(UpdatableEntityView.class)) {
      throw new GraphQlApiException("View " + viewClass.getName() + " is not updatable.", UNSUPPORTED);
    }
    Object id = resolveId(request.key());
    Object view = entityViewManager.find(getEntityManager(), viewClass, id);
    if (view == null) {
      throw new GraphQlApiException("No entity found for model " + request.model() + " with id " + id, GraphQlQueryHandlerRegistry.NOT_FOUND);
    }
    Map<String, Object> payload = mergedPayload(request);
    applyPayload(view, payload);
    entityViewManager.save(getEntityManager(), view);
    return new GraphQlMutationResult(true, view);
  }

  /**
   * Deletes an entity by identifier.
   *
   * @param definition the view definition
   * @param request the mutation request
   * @return the mutation result
   */
  private GraphQlMutationResult delete(BlazeGraphQlViewDefinition definition, GraphQlMutationRequest request) {
    Object id = resolveId(request.key());
    Object entity = getEntityManager().find(definition.entityClass(), id);
    if (entity == null) {
      throw new GraphQlApiException("No entity found for model " + request.model() + " with id " + id, GraphQlQueryHandlerRegistry.NOT_FOUND);
    }
    getEntityManager().remove(entity);
    return new GraphQlMutationResult(true, null);
  }

  /**
   * Merges key and data payloads.
   *
   * @param request the mutation request
   * @return merged payload
   */
  private Map<String, Object> mergedPayload(GraphQlMutationRequest request) {
    Map<String, Object> payload = new HashMap<>();
    if (request.key() != null) {
      payload.putAll(request.key());
    }
    if (request.data() != null) {
      payload.putAll(request.data());
    }
    return payload;
  }

  /**
   * Applies payload values to the view instance.
   *
   * @param view the target view
   * @param payload the payload data
   */
  private void applyPayload(Object view, Map<String, Object> payload) {
    BeanWrapper wrapper = new BeanWrapperImpl(view);
    wrapper.setAutoGrowNestedPaths(true);
    for (Map.Entry<String, Object> entry : payload.entrySet()) {
      String key = entry.getKey();
      if (key != null && !key.isBlank() && wrapper.isWritableProperty(key)) {
        wrapper.setPropertyValue(key, entry.getValue());
      }
    }
  }

  /**
   * Resolves an identifier from the key map.
   *
   * @param key the key map
   * @return the identifier value
   */
  private Object resolveId(Map<String, Object> key) {
    if (key == null || key.isEmpty()) {
      throw new GraphQlApiException("Mutation request must include a key.", 400);
    }
    if (key.containsKey(ID_PROPERTY)) {
      return key.get(ID_PROPERTY);
    }
    if (key.size() == 1) {
      return key.values().iterator().next();
    }
    throw new GraphQlApiException("Mutation key must include an id.", 400);
  }

  /**
   * Returns the entity manager.
   *
   * @return the entity manager
   */
  private EntityManager getEntityManager() {
    return queryBuilder.getEntityManager();
  }
}
