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
