# Development Rules

---

## Part I — Java

---

### 1. General Principles

- Follow **Clean Code** and **SOLID** design principles.
- Code must be readable, maintainable, and predictable.
- Avoid unnecessary complexity.
- Prefer **composition over inheritance**.
- Prefer **immutability** where possible — use `record` for value objects.
- Avoid reflection unless absolutely required.
- Avoid magic numbers and magic strings — use named constants.
- Use meaningful names for variables, classes, and methods.
- Write code that is easy to test.
- All new code must compile.
- Must use Lombok annotations where applicable.
- When saving entities, do not set IDs — let the database generate them.
- When changing code, always update the corresponding Javadoc.
- All migration files must be safe (mark as ran on failure).

---

### 2. Javadoc

Every `public`, `protected`, and `private` method **must** have Javadoc covering:

- Purpose of the method
- `@param` for each parameter
- `@return` for non-void methods
- `@throws` for any declared exceptions
- Any important behavior or side effects

All classes must also have class-level Javadoc.

```java
/**
 * Service responsible for managing user accounts.
 *
 * <p>Handles user creation, updates, and retrieval operations.
 */
public class UserService {

  /**
   * Calculates the total price including tax.
   *
   * @param price the base price
   * @param taxRate the tax rate as a decimal (e.g. 0.1 for 10%)
   * @return the final price including tax
   */
  public BigDecimal calculateTotal(BigDecimal price, BigDecimal taxRate) {
    return price.multiply(BigDecimal.ONE.add(taxRate));
  }
}
```

---

### 3. Naming Conventions

| Kind      | Style              | Example                                |
|-----------|--------------------|----------------------------------------|
| Classes   | `PascalCase`       | `UserService`, `QueryMapper`           |
| Methods   | `camelCase`        | `calculateBalance()`, `findUserById()` |
| Variables | `camelCase`        | `userId`, `totalAmount`                |
| Constants | `UPPER_SNAKE_CASE` | `DEFAULT_PAGE_SIZE`, `MAX_RETRY_COUNT` |

---

### 4. Method & Parameter Design

- Each method must do **one thing**.
- Recommended size: **≤ 20 lines**, hard limit: **≤ 50 lines**.
- Prefer **0–3 parameters** — extract a parameter object if more are needed.

```java
// ❌ Bad — does too many things, too many params
processUserDataAndSaveAndSendNotification(name, email, role, org, ...);

// ✅ Good — decomposed and uses a parameter object
record UserCreateRequest(String name, String email) {}

validateUser(request);
saveUser(request);
sendNotification(request);
```

---

### 5. Null Safety & Collections

- Prefer `Optional<T>` over returning `null`.
- Never return `null` for collections — always return an empty collection.

```java
// ✅ Correct
Optional<User> findUserById(UUID id);
List<User> findAll(); // returns List.of() when empty, never null
```

---

### 6. Immutability

Prefer immutable objects using Java `record`:

```java
public record Money(BigDecimal amount) {}
```

---

### 7. Logging

- Use **SLF4J** exclusively — never `System.out.println`.
- Prefer Lombok's `@Slf4j` annotation.

```java
@Slf4j
@Service
public class UserService {
  // use log.info(...), log.error(...) etc.
}
```

---

### 8. Exception Handling

- Never swallow exceptions.
- Wrap low-level exceptions into domain exceptions where appropriate.
- Never catch generic `Exception` — be specific.

```java
throw new UserNotFoundException(userId);
```

---

### 9. Dependency Injection

- Always use **constructor injection** — never field injection.
- Use Lombok's `@RequiredArgsConstructor` to avoid boilerplate.

```java
@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository repository;
}
```

---

### 10. Collections & Streams

- Always declare collections using **interfaces**, not concrete types.
- Use streams when they improve readability — avoid overly complex pipelines.

```java
// ❌ Bad
ArrayList<User> users;

// ✅ Good
List<User> users;
```

---

### 11. Package Structure

Organize by **feature**, not by layer:

```
user/
├── controller/
├── service/
├── repository/
├── domain/
└── dto/
```

Avoid large shared `util` packages.

---

### 12. Testing

- All business logic must be testable — avoid static dependencies.
- Preferred stack: **JUnit 5**, **AssertJ**, **Testcontainers** (for integration tests).

---

### 13. Formatting

| Rule             | Value                   |
|------------------|-------------------------|
| Indentation      | 2 spaces                |
| Max line length  | 120 characters          |
| Braces style     | Same-line opening brace |

---

### 14. Forbidden Practices

| Practice                       | Status |
|--------------------------------|--------|
| Field injection                | ❌      |
| Returning `null` collections   | ❌      |
| Methods without Javadoc        | ❌      |
| Magic numbers or strings       | ❌      |
| Deep nested logic (> 3 levels) | ❌      |
| Reflection for normal logic    | ❌      |
| Methods > 50 lines             | ❌      |
| `System.out.println`           | ❌      |
| Setting entity IDs manually    | ❌      |

---

### 15. Valid Code Example

```java
/**
 * Service responsible for calculating invoice totals.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceCalculator {

  /**
   * Calculates the final invoice total including tax.
   *
   * @param subtotal the subtotal amount
   * @param taxRate the tax rate as a decimal
   * @return the final invoice total
   */
  public BigDecimal calculateTotal(BigDecimal subtotal, BigDecimal taxRate) {
    return subtotal.multiply(BigDecimal.ONE.add(taxRate));
  }
}
```

---

## Part II — Frontend (SvelteKit + shadcn)

---

### 16. Component Architecture ⚠️ Critical

- Reuse **shadcn-svelte primitives** for all UI — source path: `src/lib/components/ui/*`.
- Never re-implement primitives (`Button`, `Input`, `Dialog`, `Card`, etc.) if a shadcn version exists.
- Must reuse existing `common-ui` wrappers (`buttons/*`, `data-display/*`, `forms/*`) before composing from primitives again.
- Build domain/app wrappers in `src/lib/components/*` by composing `ui/*` primitives.
- If a required primitive is missing → generate it via the **shadcn CLI**, then compose from it.
- Keep components **single-responsibility**.
- Co-locate `Component.svelte` and `Component.test.ts`.
- All story files live in `src/stories/` — do not create stories for `ui/` components.
- Prefer **slots** over prop-drilling.
- Use `setContext` / `getContext` only for low-frequency global patterns (theme, form, layout).

```ts
// ❌ Invalid — raw HTML when a primitive exists
<button class="...">Click me</button>

// ✅ Correct — always use the shadcn primitive
import { Button } from "$lib/components/ui/button";
<Button>Click me</Button>
```

> **Violation = invalid output.**

---

### 17. File & Folder Structure

```
src/
├── routes/                # Pages (SvelteKit routing)
│   ├── +layout.svelte
│   ├── +page.svelte
│   └── (feature)/
│       ├── +page.svelte
│       ├── +page.ts
│       └── +layout.svelte
│
├── lib/
│   ├── components/
│   │   ├── ui/            # shadcn primitives — do not modify manually
│   │   └── */             # domain/app wrappers
│   ├── stores/            # Svelte stores
│   ├── api/               # Fetch layer
│   ├── utils/             # Pure functions
│   ├── types/             # Shared TS types
│   ├── constants/         # App-wide constants
│   └── schemas/           # Zod schemas
│
├── stories/               # Storybook stories
├── styles/                # Global styles & CSS tokens
└── app.html
```

- Use barrel files (`index.ts`) for clean exports.
- Never import from another feature's internal files.
- UI primitives live **only** inside `ui/`.

---

### 18. TypeScript

- All components must use `lang="ts"` — no `any`.
- Define props with `interface Props { ... }`.
- Use `satisfies` for config objects.
- Prefer `unknown` over `any` for truly unknown types.
- Shared types live in `src/lib/types`.

---

### 19. Styling

- Use **Tailwind** as the primary styling system.
- All design tokens use CSS variables defined in `src/styles/globals.css` — never hard-code hex values.
- Extend Tailwind config for custom tokens — do not use arbitrary values for project-wide tokens.
- Do not mix CSS Modules and Tailwind in the same component.
- Dark mode via `class` strategy: `<html class="dark">`.
- For animations: prefer CSS keyframes; use `svelte-motion` for complex sequences.
- Do not override shadcn internal styles unless absolutely required.

---

### 20. State Management

- **Local state first** — `let count = 0` before reaching for stores.
- Use `derived` stores for computed values — never store derived data in a `writable`.
- Global state lives in `src/lib/stores`, scoped by feature.
- Avoid unnecessary `writable` stores.

---

### 21. Data Fetching

- All API logic lives in `src/lib/api/*` — never call `fetch` directly inside components.
- Use `+page.ts` load functions for page-level data; `+layout.ts` for shared layout data.
- Validate all API responses with **Zod** at the network boundary.
- Always handle **loading**, **error**, and **empty** states explicitly.

---

### 22. Forms

- Use **TanStack Form** (`@tanstack/svelte-form`) for all forms.
- Validate with shared **Zod** schemas.
- No manual field-level `let value` state.
- Show inline errors after blur or failed submission.
- Disable submit button during submission; restore on error.
- Preserve form state on network error.
- Always associate `<Label>` with `<Input>`.

---

### 23. Performance

- Route-level code splitting is automatic in SvelteKit — rely on it.
- Use `onMount` only when necessary.
- Virtualize long lists (100+ items) with `@tanstack/virtual-core` or `svelte-virtual`.
- Use modern image formats (WebP / AVIF).
- Lazy-load heavy components with dynamic import:

```ts
const Dialog = await import('$lib/components/Dialog.svelte');
```

---

### 24. Accessibility (a11y)

- All interactive elements must be **keyboard accessible**.
- Use semantic HTML before reaching for ARIA.
- Images must have `alt` text.
- Never remove focus outlines.
- Ensure **WCAG AA** contrast ratios.
- Use `aria-live` for dynamic content updates.
- Validate with `@axe-core/playwright` in E2E tests.

---

### 25. Error Handling

- Each route must define a `+error.svelte`.
- Never expose raw error objects — show user-friendly messages.
- Log production errors to **Sentry**.
- Distinguish error types: `400` validation, `401` auth, `404` not found, `500` system.

---

### 26. Testing

| Layer      | Tool                        |
|------------|-----------------------------|
| Unit       | Vitest                      |
| Component  | `@testing-library/svelte`   |
| E2E        | Playwright                  |
| API mocks  | MSW                         |

- Query priority: `role` > `label` > `text` > `testid`.
- Target ≥ 80% coverage on `utils`, `stores`, `api`.

---

### 27. Security

- Never expose secrets in the frontend bundle.
- Sanitize HTML with **DOMPurify** before using `{@html}`.
- Use **CSP headers** in production.
- Keep dependencies updated.

---

### 28. Package Manager

- All frontend projects must use **Bun**.
- Use `bun install`, `bun add`, `bun remove`, `bun run <script>`.
- Never use `npm`, `pnpm`, or `yarn` for frontend workflows.

---

## Part III — Shared

---

### 29. Git & Collaboration

- Branch prefixes: `feat/`, `fix/`, `refactor/`
- Commit messages follow **Conventional Commits**: `feat(auth): add OAuth login`
- No direct push to `main`.
- Every PR must include: **what** changed, **why**, and **how to test**.
- Include screenshots or recordings for all UI changes.

---

### 30. AI Agent Compliance

Any code generated by an AI agent must:

- Follow **all rules** in this document.
- Include **Javadoc** for every Java method.
- Follow project formatting and architecture conventions.
- Prefer **clarity over cleverness**.
- If a rule cannot be satisfied, explain the reason in a comment.
