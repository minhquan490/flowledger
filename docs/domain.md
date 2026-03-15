# Domain Module

## Purpose
The `domain` module contains the core domain entities and entity views for FlowLedger. It is focused on the domain model and view definitions, while runtime infrastructure remains in platform modules.

## How To Use
1. Add a Gradle dependency on the module:
   ```
   implementation project(':domain')
   ```
2. Use the entity classes in your services, repositories, or mapping layers.
3. Define Blaze entity views for query and GraphQL exposure.

## Notes
- The module uses Lombok for boilerplate reduction.
- Entities use Jakarta Persistence annotations to support JPA-based persistence layers.
- The module depends on platform query/GraphQL to support entity view definitions and GraphQL model exposure.

## High-Level Design
- Defines aggregate roots, entities, value objects, and view interfaces for the core business model.
- Keeps the domain model free of runtime wiring to allow reuse across modules.
- Exposes Blaze entity view definitions that can be consumed by query and GraphQL infrastructure.

## Why This Architecture
- Keeps business rules centralized and reusable across application entry points.
- Decouples domain evolution from infrastructure changes.
- Supports multiple delivery channels without duplicating domain logic.

## Pros
- Stable core model with clear boundaries.
- Easy to test domain logic in isolation.
- Reusable across services and applications.

## Cons
- Requires discipline to keep infrastructure out of the domain.
- Can add indirection when onboarding new developers.

## Project Structure
- `domain/src/main/java/io/flowledger/domain/**/aggregate` aggregate roots.
- `domain/src/main/java/io/flowledger/domain/**/entity` entities.
- `domain/src/main/java/io/flowledger/domain/**/valueobject` value objects.
- `domain/src/main/java/io/flowledger/domain/**/view` Blaze entity views.

## Example Code
```java
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;
}
```
