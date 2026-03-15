package io.flowledger.platform.graphql.infrastructure.blaze.mutation;

import io.flowledger.platform.graphql.application.GraphQlApiException;
import io.flowledger.platform.graphql.domain.GraphQlMutationHandler;
import io.flowledger.platform.graphql.domain.GraphQlMutationRequest;
import io.flowledger.platform.graphql.infrastructure.blaze.BlazeGraphQlMutationHandler;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashMap;
import java.util.Map;

import static io.flowledger.platform.graphql.application.GraphQlQueryService.BAD_REQUEST_STATUS;

/**
 * Base implementation for Blaze-backed GraphQL mutation handlers.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseGraphQLMutationHandler implements GraphQlMutationHandler {
  private static final String ID_PROPERTY = "id";

  private final BlazeQueryBuilder queryBuilder;

  /**
   * Returns the wildcard model name handled by Blaze mutation handlers.
   *
   * @return the wildcard model name
   */
  @Override
  public String model() {
    return BlazeGraphQlMutationHandler.MODEL_WILDCARD;
  }

  /**
   * Merges key and data payloads.
   *
   * @param request the mutation request
   * @return merged payload
   */
  protected Map<String, Object> mergedPayload(GraphQlMutationRequest request) {
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
  protected void applyPayload(Object view, Map<String, Object> payload) {
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
  protected Object resolveId(Map<String, Object> key) {
    if (key == null || key.isEmpty()) {
      throw new GraphQlApiException("Mutation request must include a key.", BAD_REQUEST_STATUS);
    }
    if (key.containsKey(ID_PROPERTY)) {
      return key.get(ID_PROPERTY);
    }
    if (key.size() == 1) {
      return key.values().iterator().next();
    }
    throw new GraphQlApiException("Mutation key must include an id.", BAD_REQUEST_STATUS);
  }

  /**
   * Returns the entity manager.
   *
   * @return the entity manager
   */
  protected EntityManager getEntityManager() {
    return queryBuilder.getEntityManager();
  }
}
