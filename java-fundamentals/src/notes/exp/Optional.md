# Should Optional type be avoid to be used as a method parameter?
Yes, using `Optional` as a method parameter is **generally considered an anti-pattern** in Java, and the official Java documentation recommends against it. Here's why, and what to do instead.

## Why `Optional` Was Not Designed for Parameters

`Optional` was introduced in Java 8 specifically as a **return type** to communicate that a method may or may not produce a value — not to wrap inputs. The Java language authors have been explicit about this intent.

## Key Arguments Against It

- **Doesn't actually eliminate null checks** — the method still needs to verify the `Optional` itself isn't `null`, meaning you check twice instead of once 
- **Burdens callers with boilerplate** — every call site must wrap values in `Optional.of(...)` or `Optional.empty()`, even when values are clearly present 
- **Invariance makes it inflexible** — `Optional<String>` is strictly invariant; a plain `String` can never be passed where `Optional<String>` is expected, making the API rigid and unintuitive
- **Static analysis tools flag it** — SonarLint, FindBugs, and similar tools actively warn against this pattern

## Better Alternatives

| Scenario | Recommended Approach |
|---|---|
| One optional parameter | Method overloading (with and without the param) |
| Multiple optional parameters | Builder pattern with a configuration object |
| Simple nullable input | Accept `T` directly and use `Optional.ofNullable(t)` **inside** the method |
| Documenting nullability | Use `@Nullable` / `@NonNull` annotations |

---

Here is a concrete Java example for each of the four alternatives, applied consistently to a `Person` search scenario from your file. [linkedin](https://www.linkedin.com/pulse/java-optional-method-parameter-best-practice-pedro-warick-eivuf)

## 1. Method Overloading

Create separate method signatures — one without the optional parameter, one with it.

```java
// Without age filter
public static List<Person> search(List<Person> people, String name) {
    return search(people, name, 0);
}

// With age filter
public static List<Person> search(List<Person> people, String name, int minAge) {
    return people.stream()
        .filter(p -> name.equals(p.getName().orElse(null)))
        .filter(p -> p.getAge().orElse(0) >= minAge)
        .collect(Collectors.toList());
}

// Usage
search(people, "Peter");          // no age filter
search(people, "Peter", 25);      // with age filter
```

---

## 2. Builder Pattern

Best when you have **multiple** optional parameters. A fluent builder constructs the query cleanly.

```java
public class PersonSearch {
    private final List<Person> people;
    private String name;
    private int minAge = 0;

    public PersonSearch(List<Person> people) {
        this.people = people;
    }

    public PersonSearch name(String name) {
        this.name = name;
        return this;
    }

    public PersonSearch minAge(int minAge) {
        this.minAge = minAge;
        return this;
    }

    public List<Person> execute() {
        return people.stream()
            .filter(p -> name.equals(p.getName().orElse(null)))
            .filter(p -> p.getAge().orElse(0) >= minAge)
            .collect(Collectors.toList());
    }
}

// Usage
List<Person> result = new PersonSearch(people)
    .name("Peter")
    .minAge(25)       // optional — omit if not needed
    .execute();
```

***

## 3. Accept `T` Directly, Wrap `Optional` Inside the Method

The method takes a plain nullable `Integer`, and handles the `Optional` logic internally — keeping the caller's interface clean.

```java
public static List<Person> search(List<Person> people, String name, Integer minAge) {
    int ageThreshold = Optional.ofNullable(minAge).orElse(0);
    return people.stream()
        .filter(p -> name.equals(p.getName().orElse(null)))
        .filter(p -> p.getAge().orElse(0) >= ageThreshold)
        .collect(Collectors.toList());
}

// Usage
search(people, "Peter", null);   // no age filter
search(people, "Peter", 25);     // with age filter
```

---

## 4. `@Nullable` / `@NonNull` Annotations

When you want to keep a single method signature with a nullable parameter but communicate intent explicitly, annotations document the contract without runtime overhead. 

```java
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public static List<Person> search(
        @NotNull List<Person> people,
        @NotNull String name,
        @Nullable Integer minAge) {

    int ageThreshold = (minAge != null) ? minAge : 0;
    return people.stream()
        .filter(p -> name.equals(p.getName().orElse(null)))
        .filter(p -> p.getAge().orElse(0) >= ageThreshold)
        .collect(Collectors.toList());
}

// Usage — IDE/static analysis tools will warn if you pass null to @NotNull params
search(people, "Peter", null);
search(people, "Peter", 25);
```

IDEs like IntelliJ IDEA use `@Nullable`/`@NotNull` to flag potential null misuse at compile time, rather than at runtime.

