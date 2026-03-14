# Java Development Rules

---

## 1. General Principles

- Follow **Clean Code** and **SOLID** design principles.
- Code must be readable, maintainable, and predictable.
- Avoid unnecessary complexity.
- Prefer **composition to inheritance**.
- Prefer **immutability** where possible.
- Avoid reflection unless absolutely required.
- Avoid magic numbers and magic strings.
- Use meaningful names for variables, classes, and methods.
- Write code that is easy to test.
- Ensure new files can be compiled
- Must use lombok annotations
- All migration files must be safe (add on failed mark ran)
- When change code, must update javadoc

---

## 2. Mandatory Javadoc Requirement

Every `public`, `protected`, and `private` method **must** have Javadoc describing:

- Purpose of the method
- Parameters (`@param`)
- Return value (`@return`)
- Exceptions (`@throws`) if any
- Important behavior

```java
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
```

---

## 3. Class Documentation

All classes must have class-level Javadoc.

```java
/**
 * Service responsible for managing user accounts.
 *
 * <p>This service handles user creation, updates, and retrieval operations.
 */
public class UserService {
}
```

---

## 4. Naming Conventions

| Kind      | Style              | Example                                |
|-----------|--------------------|----------------------------------------|
| Classes   | `PascalCase`       | `UserService`, `QueryMapper`           |
| Methods   | `camelCase`        | `calculateBalance()`, `findUserById()` |
| Variables | `camelCase`        | `userId`, `totalAmount`                |
| Constants | `UPPER_SNAKE_CASE` | `DEFAULT_PAGE_SIZE`, `MAX_RETRY_COUNT` |

---

## 5. Method Design Rules

- Do **one thing** per method.
- Keep methods **short and easy to understand**.
- Recommended size: **≤ 20 lines**.

```
// ❌ Bad
processUserDataAndSaveAndSendNotification(...)

// ✅ Good
validateUser();

mapUser();

saveUser();

sendNotification();
```

---

## 6. Parameter Rules

- Prefer **0–3 parameters** per method.
- If more are required, use a **parameter object**.

```java
class UserCreateRequest {
  String name;
  String email;
}
```

---

## 7. Null Safety

- Prefer `Optional<T>` over returning `null`.

```java
Optional<User> findUserById(UUID id);
```

- Never return `null` for collections — always return an empty collection:

```
List.of()
Map.of()
Set.of()
```

---

## 8. Immutability

Prefer immutable objects wherever possible.

```java
public record Money(BigDecimal amount) {

}
```

---

## 9. Logging Rules

- Use **SLF4J** for all logging.
- Never use `System.out.println`.

```java
private static final Logger log = LoggerFactory.getLogger(UserService.class);
```

---

## 10. Exception Handling

- Do **not** swallow exceptions.
- Wrap low-level exceptions into domain exceptions where appropriate.
- Avoid catching generic `Exception`.

```
throw new UserNotFoundException(userId);
```

---

## 11. Dependency Injection

- Always use **constructor injection**.
- Never use field injection.

```java

@Service
public class UserService {

  private final UserRepository repository;

  public UserService(UserRepository repository) {
    this.repository = repository;
  }
}
```

---

## 12. Collection Usage

- Always declare collections using **interfaces**, not concrete types.

```java
// ❌ Bad
ArrayList<User> users;

// ✅ Good
List<User> users;
```

---

## 13. Streams Usage

- Use streams when they **improve readability**.
- Avoid overly complex pipelines that become hard to read — break them up instead.

---

## 14. Package Structure

Organize by **feature**, not by layer.

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

## 15. Annotations Usage

- Prefer explicit Spring annotations: `@Service`, `@Repository`, `@Component`.
- Avoid excessive custom annotations unless necessary.

---

## 16. Testing Rules

- All business logic must be testable.
- Preferred stack: **JUnit 5**, **AssertJ**, **Testcontainers** (integration tests).
- Avoid static dependencies that break testability.

---

## 17. Formatting Rules

| Rule            | Value                   |
|-----------------|-------------------------|
| Indentation     | 2 spaces                |
| Max line length | 120 characters          |
| Braces style    | Same-line opening brace |

```
if(condition){

doSomething();
}
```

---

## 18. Forbidden Practices

| Practice                       | Status |
|--------------------------------|--------|
| Field injection                | ❌      |
| Returning `null` collections   | ❌      |
| Methods without Javadoc        | ❌      |
| Magic numbers or strings       | ❌      |
| Deep nested logic (> 3 levels) | ❌      |
| Reflection for normal logic    | ❌      |
| Huge methods (> 50 lines)      | ❌      |

---

## 19. Valid Code Example

```java
/**
 * Service responsible for calculating invoice totals.
 */
@Service
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

## 20. AI Agent Compliance Rule

Any code generated by an AI agent must:

- Follow **all rules** in this document.
- Include **Javadoc** for every method.
- Follow project **formatting and architecture** conventions.
- Prefer **clarity to cleverness**.
- If a rule cannot be satisfied, explain the reason in a comment.
