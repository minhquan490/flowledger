# Application Module

## Purpose
The `application` module hosts the runtime application wiring for FlowLedger, including authentication integration,
identity synchronization, and module composition.

## How To Use
1. Configure authentication settings in `application/src/main/resources/application.yml`.
2. Ensure Keycloak is available and the JWT issuer URI is configured for resource server validation.
3. Run the module with Spring Boot:
   ```
   ./gradlew :application:bootRun
   ```

## Notes
- JWT-based identity synchronization is handled asynchronously via application events.
- The module enables JPA, caching, and security auto-configuration to support identity lookups and RBAC.

## High-Level Design
- Wires authentication, identity synchronization, RBAC subject resolution, and module composition.
- Publishes identity sync events on authenticated GraphQL requests to keep user data aligned with JWT claims.
- Serves as the runtime Spring Boot application layer that stitches together platform and domain modules.

## Why This Architecture
- Keeps the runtime wiring in a single module to reduce cross-module coupling.
- Uses events for identity synchronization to keep request latency low.
- Centralizes authentication and RBAC subject resolution for consistent security behavior.

## Pros
- Clear runtime entry point for the application.
- Scalable identity synchronization through async events.
- Simplifies composition of platform modules.

## Cons
- Requires careful coordination of module dependencies.
- Async processing can complicate debugging.

## Project Structure
- `application/src/main/java/io/flowledger/application` Spring Boot entry point.
- `application/src/main/java/io/flowledger/application/identity` identity sync, lookup, and RBAC subject provider.
- `application/src/main/resources` application configuration.

## Example Code
```java
@SpringBootApplication(scanBasePackages = {"io.flowledger.application", "io.flowledger.core"})
public class FlowLedgerApplication {
}
```
