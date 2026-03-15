# DB Migration Module

## Purpose
The `db-migration` module is a command line application that runs Liquibase migrations to create or update database tables. It can also load optional seed data for testing.

## How To Use
1. Configure database connection settings using environment variables:
   - `DB_URL` (default: `jdbc:postgresql://localhost:5432/flowledger`)
   - `DB_USERNAME` (default: `flowledger`)
   - `DB_PASSWORD` (default: `flowledger`)
2. Run migrations:
   ```
   ./gradlew :db-migration:bootRun
   ```
3. Run migrations with seed data:
   ```
   ./gradlew :db-migration:bootRun --args="--seed"
   ```

## Notes
- Liquibase changelogs are located under `db-migration/src/main/resources/db/changelog`.
- Seed data changesets are applied only when the `--seed` argument is provided.
- All migration changesets must be safe to re-run (use on-failure mark-ran semantics where needed).

## High-Level Design
- Provides a standalone Spring Boot entry point dedicated to running Liquibase migrations.
- Loads changelogs and optional seed data based on command-line flags.
- Runs against the target database configured via environment variables to keep deployments consistent.

## Why This Architecture
- Isolates schema changes from the runtime application to reduce operational risk.
- Enables CI/CD to run migrations independently from app deployments.
- Keeps migration tooling minimal and consistent across environments.

## Pros
- Clear separation of schema lifecycle from application runtime.
- Safe to run in automated pipelines and local development.
- Supports optional seed data without polluting production runs.

## Cons
- Requires an extra deployment step for migrations.
- Needs careful coordination with application releases.

## Project Structure
- `db-migration/src/main/java` entry point and boot wiring.
- `db-migration/src/main/resources/db/changelog` Liquibase changelogs and seed data.

## Example Code
```bash
./gradlew :db-migration:bootRun
```
