# Platform Query Module

## Purpose
The `platform:query` module provides query infrastructure, including Blaze-Persistence integration and JPA compatibility for advanced querying.

## How To Use
1. Add a Gradle dependency on the module:
   ```
   implementation project(':platform:query')
   ```
2. Use it as the query foundation for data access layers that need advanced filtering, pagination, and sorting.
3. Register entity view models to enable reusable projections.

## Notes
- This module is packaged as a library (jar) and is not executable.

## High-Level Design
- Wraps Blaze-Persistence to provide consistent query construction across modules.
- Supplies entity view registration and query utilities used by GraphQL and RBAC layers.
- Focuses on advanced filtering, pagination, and sorting for domain views.

## Why This Architecture
- Provides a single query abstraction across the platform.
- Avoids duplication of query logic in application modules.
- Enables GraphQL and RBAC to share the same view definitions.

## Pros
- Centralized query behavior and tuning.
- Reusable view-based projections.
- Consistent filtering and pagination semantics.

## Cons
- Adds a dependency on Blaze-Persistence.
- Requires understanding of entity views for advanced use cases.

## Project Structure
- `platform/query/src/main/java/io/flowledger/platform/query` query utilities and builders.
- `platform/query/src/main/java/io/flowledger/platform/query/blaze` Blaze integration.

## Example Code
```java
CriteriaBuilder<?> builder = criteriaBuilderFactory.create(entityManager, Object.class);
```
