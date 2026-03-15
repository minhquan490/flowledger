# Platform Core Module

## Purpose
The `platform:core` module provides foundational Spring Boot configuration and shared runtime utilities for platform modules.

## How To Use
1. Add a Gradle dependency on the module:
   ```
   implementation project(':platform:core')
   ```
2. Build additional platform modules on top of core to inherit shared defaults.

## Notes
- This module is packaged as a library (jar) and is not executable.
- Authentication configuration properties are defined in `io.flowledger.core.auth.AppAuthProperties`.

## High-Level Design
- Provides shared configuration primitives and utilities used by other platform modules.
- Hosts configuration properties that standardize authentication setup.
- Avoids module-specific logic to keep the core module lightweight and reusable.

## Why This Architecture
- Centralizes shared configuration to avoid duplication across platform modules.
- Keeps cross-cutting concerns isolated from business modules.
- Simplifies maintenance by enforcing consistent configuration patterns.

## Pros
- Reduced duplication in platform modules.
- Consistent configuration semantics across the platform.
- Clear separation of core utilities from feature logic.

## Cons
- Core changes can ripple into multiple modules.
- Requires careful versioning across modules.

## Project Structure
- `platform/core/src/main/java/io/flowledger/core` shared utilities and configuration.
- `platform/core/src/main/java/io/flowledger/core/auth` authentication properties.

## Example Code
```java
@Component
@ConfigurationProperties(prefix = "app.auth")
public class AppAuthProperties {
}
```
