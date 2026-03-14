# Domain Module

## Purpose
The `domain` module contains the core domain entities for FlowLedger. It is intentionally free of runtime infrastructure concerns so it can be reused by other modules without pulling in the rest of the stack.

## How To Use
1. Add a Gradle dependency on the module:
   ```
   implementation project(':domain')
   ```
2. Use the entity classes in your services, repositories, or mapping layers.

## Notes
- The module uses Lombok for boilerplate reduction.
- Entities use Jakarta Persistence annotations to support JPA-based persistence layers.
