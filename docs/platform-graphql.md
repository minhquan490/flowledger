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
