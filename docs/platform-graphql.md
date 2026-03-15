# Platform GraphQL Module

## Purpose
The `platform:graphql` module provides GraphQL support on top of the query infrastructure, including extended scalar support.

## How To Use
1. Add a Gradle dependency on the module:
   ```
   implementation project(':platform:graphql')
   ```
2. Define Blaze entity views and annotate them with `@GraphQlModel` so the generic GraphQL handler can resolve them.
3. Use the generic `read` and `search` GraphQL operations to query view data.

## Notes
- This module is packaged as a library (jar) and is not executable.
- RBAC policies can be attached at the GraphQL layer via the platform RBAC module.

## High-Level Design
- Builds on the query module to expose generic GraphQL read/search operations.
- Registers view models and resolves them at runtime using the model registry.
- Integrates with RBAC policies to enforce access control in GraphQL queries.

## Why This Architecture
- Provides a single GraphQL surface over reusable query views.
- Avoids writing boilerplate CRUD controllers for each entity.
- Enables consistent RBAC enforcement for all GraphQL operations.

## Pros
- Fast to expose new views.
- Consistent query behavior across models.
- Centralized access control integration.

## Cons
- Requires careful modeling of entity views.
- Generic handlers can be less flexible for specialized workflows.

## Project Structure
- `platform/graphql/src/main/java/io/flowledger/platform/graphql` GraphQL infrastructure.
- `platform/graphql/src/main/java/io/flowledger/platform/graphql/infrastructure` handlers and registry wiring.

## Example Code
```java
@EntityView(Account.class)
@GraphQlModel("account")
public interface AccountView {
  UUID getId();
}
```

## Defining A View
Create a Blaze entity view interface and annotate it with `@GraphQlModel`. The model name is the identifier used in GraphQL read/search requests.

```java
@EntityView(Account.class)
@GraphQlModel("account")
public interface AccountView {

  /**
   * Returns the account identifier.
   *
   * @return the account id
   */
  UUID getId();

  /**
   * Returns the account name.
   *
   * @return the account name
   */
  String getName();
}
```

If `@GraphQlModel` is omitted, the registry uses the view class name (e.g. `AccountView` becomes `account`).

## How Views Are Resolved
1. `BlazeViewLoader` scans the application base packages for classes annotated with `@EntityView`.
2. `BlazeGraphQlModelRegistry` maps each view to its model name from `@GraphQlModel` (or the inferred name).
3. `BlazeGraphQlQueryHandler` uses the registry to resolve the view class, applies requested fields, and executes the Blaze query.
4. `GraphQlQueryController` exposes generic `read` and `search` GraphQL operations that return the view data to clients.
