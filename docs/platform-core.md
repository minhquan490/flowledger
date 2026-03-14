# Platform Core Module

## Purpose
The `platform:core` module provides foundational Spring Boot configuration and shared runtime dependencies for platform modules.

## How To Use
1. Add a Gradle dependency on the module:
   ```
   implementation project(':platform:core')
   ```
2. Build additional platform modules on top of core to inherit shared defaults.

## Notes
- This module is packaged as a library (jar) and is not executable.
