# Platform RBAC Module

## Purpose
The `platform:rbac` module provides role-based access control services, GraphQL access policies, and resource
synchronization utilities.

## How To Use
1. Add a Gradle dependency on the module:
   ```
   implementation project(':platform:rbac')
   ```
2. Provide a `RbacSubjectProvider` bean to supply the current subject identifier.
3. Ensure the query and GraphQL modules are configured so RBAC policies can be applied.

## Notes
- The module auto-configures a no-op subject provider if none is supplied.
- RBAC resource synchronization requires an `EntityManager` and the GraphQL model registry.

## High-Level Design
- Provides RBAC services for field, row, and permission evaluation.
- Integrates with GraphQL access policies to enforce authorization rules.
- Synchronizes RBAC resources from the GraphQL model registry using JPA.

## Why This Architecture
- Centralizes authorization logic to avoid duplication in each module.
- Aligns permissions with GraphQL models for consistent enforcement.
- Supports automated RBAC resource synchronization.

## Pros
- Consistent authorization across queries and mutations.
- Clear separation of auth concerns from business logic.
- Works with generic GraphQL handlers.

## Cons
- Requires a reliable subject provider configuration.
- Adds extra setup for RBAC resource syncing.

## Project Structure
- `platform/rbac/src/main/java/io/flowledger/platform/rbac/application` RBAC service interfaces.
- `platform/rbac/src/main/java/io/flowledger/platform/rbac/infrastructure` access policies and sync utilities.

## Example Code
```java
@Component
public class JwtRbacSubjectProvider implements RbacSubjectProvider {
}
```
