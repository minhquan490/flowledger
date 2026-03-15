package io.flowledger.platform.graphql.infrastructure.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.FullQueryBuilder;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import io.flowledger.platform.graphql.application.GraphQlAccessPolicy;
import io.flowledger.platform.graphql.application.GraphQlQueryHandlerRegistry;
import io.flowledger.platform.graphql.domain.GraphQlQueryHandler;
import io.flowledger.platform.graphql.domain.GraphQlReadRequest;
import io.flowledger.platform.graphql.domain.GraphQlReadResult;
import io.flowledger.platform.graphql.domain.GraphQlSearchRequest;
import io.flowledger.platform.graphql.domain.GraphQlSearchResult;
import io.flowledger.platform.graphql.domain.GraphQlSortDirection;
import io.flowledger.platform.graphql.domain.GraphQlSortInput;
import io.flowledger.platform.query.blaze.BlazeQueryBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * Generic GraphQL handler backed by Blaze entity views.
 */
@RequiredArgsConstructor
public class BlazeGraphQlQueryHandler implements GraphQlQueryHandler {
  public static final String MODEL_WILDCARD = GraphQlQueryHandlerRegistry.GENERIC_MODEL;

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
   * Executes a read request by selecting a single Blaze view.
   *
   * @param request the read request
   * @return the read result
   */
  @Override
  public GraphQlReadResult read(GraphQlReadRequest request) {
    BlazeGraphQlViewDefinition definition = resolveDefinition(request.model());
    GraphQlAccessPolicy accessPolicy = definition.resolveAccessPolicy();
    GraphQlReadRequest authorized = accessPolicy.authorizeRead(request);
    CriteriaBuilder<?> criteriaBuilder = queryBuilder.forEntity(definition.entityClass());
    queryBuilder.applyKeyFilters(criteriaBuilder, authorized.key());

    PagedList<?> results = applyView(definition, criteriaBuilder, authorized.fields())
        .page(0, 1)
        .getResultList();

    Object item = results.isEmpty() ? null : results.getFirst();
    return new GraphQlReadResult(item != null, item);
  }

  /**
   * Executes a search request and returns matching Blaze views.
   *
   * @param request the search request
   * @return the search result
   */
  @Override
  @SuppressWarnings({"unchecked"})
  public GraphQlSearchResult search(GraphQlSearchRequest request) {
    BlazeGraphQlViewDefinition definition = resolveDefinition(request.model());
    GraphQlAccessPolicy accessPolicy = definition.resolveAccessPolicy();
    GraphQlSearchRequest authorized = accessPolicy.authorizeSearch(request);
    CriteriaBuilder<?> criteriaBuilder = queryBuilder.forEntity(definition.entityClass());
    queryBuilder.applyFilter(criteriaBuilder, authorized.filter());
    queryBuilder.applySort(
        criteriaBuilder,
        authorized.sort(),
        GraphQlSortInput::field,
        sortInput -> sortInput.direction() == GraphQlSortDirection.DESC
    );

    PagedList<?> results = queryBuilder
        .applyPagination(
            applyView(definition, criteriaBuilder, authorized.fields()),
            authorized.page() == null ? null : authorized.page().offset(),
            authorized.page() == null ? null : authorized.page().limit()
        )
        .getResultList();

    List<Object> items = (List<Object>) results;
    long total = results.getTotalSize();
    return new GraphQlSearchResult(items, total);
  }

  /**
   * Resolves the Blaze view definition for the requested model.
   *
   * @param model the model name
   * @return the view definition
   */
  private BlazeGraphQlViewDefinition resolveDefinition(String model) {
    return modelRegistry.viewFor(model);
  }

  /**
   * Applies the entity view and field selection to the criteria builder.
   *
   * @param definition the view definition
   * @param criteriaBuilder the criteria builder
   * @param fields the requested fields
   * @return the full query builder
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private FullQueryBuilder applyView(
      BlazeGraphQlViewDefinition definition,
      CriteriaBuilder<?> criteriaBuilder,
      List<String> fields
  ) {
    EntityViewSetting<?, ?> setting = EntityViewSetting.create(definition.viewClass());
    queryBuilder.applyFields(setting, fields);
    return entityViewManager.applySetting((EntityViewSetting) setting, criteriaBuilder);
  }
}
