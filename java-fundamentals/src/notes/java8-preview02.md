# Java Collectors

[https://www.baeldung.com/java-collectors](https://www.baeldung.com/java-collectors)

The **Java collect() method** is used to accumulate elements from a stream into a collection, such as a List, Set, or Map. For example, to collect even numbers from a list into a List, you can use: 

```java
List<Integer> evenNumbers = numbers.stream()
                                   .filter(x -> x % 2 == 0)
                                   .collect(Collectors.toList());
```

This will result in a List containing only the even numbers.

---

# Java Comparator.comparing()

[https://www.baeldung.com/java-8-comparator-comparing](https://www.baeldung.com/java-8-comparator-comparing)

## **Overview of the comparingInt() Method**

The `comparingInt()` method in Java is a part of the `Comparator` interface. It is specifically designed to create a comparator that sorts objects based on an integer key extracted from them. This method simplifies the process of sorting collections by integer fields.

## **Syntax**

The basic syntax for using `comparingInt()` is as follows:

```java
Comparator.comparingInt(Function<? super T, ? extends Integer> keyExtractor)
```

Here, `keyExtractor` is a function that extracts the integer value from the object to be compared.

## **Example of Using comparingInt() with the sort() Method**

To illustrate how to use `comparingInt()` with the `sort()` method, consider the following example where we have a list of `Product` objects that we want to sort by their price.

### **Product Class Definition**

```java
import java.util.*;
import java.time.LocalDate;

public class Product {
    private String name;
    private int price; // Price as an integer for this example
    private LocalDate manufactureDate;

    public Product(String name, int price, LocalDate manufactureDate) {
        this.name = name;
        this.price = price;
        this.manufactureDate = manufactureDate;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    @Override
    public String toString() {
        return "Product{name='" + name + "', price=" + price + ", manufactureDate=" + manufactureDate + '}';
    }
}

```

### **Sorting Example**

Here’s how to sort a list of `Product` objects by their price using `comparingInt()`:

```java
public class ProductSortingExample {
    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
            new Product("Laptop", 1200, LocalDate.of(2022, 1, 10)),
            new Product("Smartphone", 800, LocalDate.of(2021, 6, 25)),
            new Product("Tablet", 500, LocalDate.of(2020, 11, 5))
        );

        // Sort products by price in ascending order
        products.sort(Comparator.comparingInt(Product::getPrice));

        // Print sorted products
        products.forEach(System.out::println);
    }
}

```

### **Output**

When you run the above code, the output will display the products sorted by price:

```
Product{name='Tablet', price=500, manufactureDate=2020-11-05}
Product{name='Smartphone', price=800, manufactureDate=2021-06-25}
Product{name='Laptop', price=1200, manufactureDate=2022-01-10}
```

This example demonstrates how to effectively use the `comparingInt()` method to sort a list of objects based on an integer attribute.

---
---

## Functional Interface 是什麼？（What is a Functional Interface?）

- **EN**: A **functional interface** is an interface that has **exactly one abstract method** (a “single abstract method”, SAM). It may still have `default` / `static` methods.  
- **繁中**：**Functional Interface（函數式介面）** 是指**只有一個抽象方法**（Single Abstract Method, SAM）的介面；它仍然可以有 `default` / `static` 方法。

---

## 為什麼它是 Lambda 的基礎？（Why is it the basis for lambdas?）

- **EN**: A **lambda expression** is basically **an implementation of that one abstract method**. Java can compile a lambda only when it knows **which single method** the lambda is meant to implement—so it needs a **functional interface** as the target type.  
- **繁中**：**Lambda** 本質上就是「幫那個**唯一的抽象方法**提供一個實作」。Java 必須知道這個 lambda 要對應到**哪一個唯一的方法**才能編譯，所以需要有 **functional interface** 當作「目標型別（target type）」。

---

## 最簡單的關係一句話（One-line relationship）

- **EN**: **Functional interface = the “shape” (method signature); lambda = the “body” (implementation).**  
- **繁中**：**Functional interface 提供「方法長相/簽名」，lambda 提供「方法內容/實作」。**

---

- **Function (EN)**: When I need to **transform an input into an output** (T → R), I use it.  
  **Function (繁中)**：當我需要**把一個輸入轉換成一個輸出**（T → R）時使用

- **Predicate (EN)**: When I need to **test a condition / filter and get true or false** (T → boolean), I use it.  
  **Predicate (繁中)**：當我需要**做條件判斷/過濾並得到 true/false**（T → boolean）時使用

- **Supplier (EN)**: When I need to **provide/generate a value without any input** (() → T), I use it.  
  **Supplier (繁中)**：當我需要**不接受輸入、只提供/產生一個值**（() → T）時使用
  
- **Consumer (EN)**: When I need to **take an input and perform an action with no return value** (T → void), I use it.  
  **Consumer (繁中)**：當我需要**接收一個輸入並執行動作、但不回傳結果**（T → void）時使用

---

### Quick comparison table
| Interface       | Expression | Input | Output  | Description |
| --------------- | ---------- | ----- | ------- | ----------- |
| `Function<T,R>` | `(T t) -> R` | ✅   | ✅     | Transform an input into an output |
| `Predicate<T>`  | `(T t) -> boolean` | ✅   | boolean | Test condition and answer yes/no |
| `Supplier<T>`   | `() -> T` | ❌   | ✅     | Produce a value without any input |
| `Consumer<T>`   | `(T t) -> { /* no return */ }` | ✅   | ❌     | Take an input without output and cause side effects |
| `Runnable`      | `() -> { /* no input, no output */ }` | ❌   | ❌     | Just execute and cause side effects |
| `Callable<V>`   | `() -> V throws Exception` | ❌   | ✅     | Like Supplier + checked exceptions |


---
---

#Should Optional type be avoid to be used as a method parameter?
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

---
---

# The Assembly Line Factory 🏭 — Understanding Java Streams

Think of a Java Stream as a **modern automated factory**. You have raw materials arriving at the loading dock, a series of machines on the factory floor that process those materials, and a packaging station at the end that ships out the finished product.

***

## 🏗️ Raw Materials — The Stream Source
### 原材料 — Stream 的來源

**EN:** The factory begins with raw materials arriving on a **conveyor belt**. In Java, this is your data source — a `List`, `Set`, array, or even a file. Calling `.stream()` on a collection is like tipping a crate of materials onto the belt. The items start moving, but *nothing is processed yet*.

**繁中：** 工廠的起點是原材料被倒上**輸送帶**。在 Java 中，這就是你的資料來源——一個 `List`、`Set`、陣列，甚至是檔案。對集合呼叫 `.stream()` 就像把一箱原料倒上輸送帶。物品開始移動，但**此時什麼都還沒被加工**。

```java
List<String> employees = List.of("Alice", "Bob", "Charlie", "Anna");
employees.stream()  // 原料上了輸送帶 / Raw materials on the belt
```

> ⚡ **Key insight / 關鍵概念:** The original `List` is **never modified**. The stream is just a *view* of the data flowing through. 原始的 `List` **永遠不會被改動**，Stream 只是資料流動的「視角」。

***

## ⚙️ Processing Machines — Intermediate Operations
### 加工機器 — 中間操作

**EN:** Along the factory floor sit a series of **specialist machines**. Each machine takes items from the belt, does one job, and passes the result to the next machine. These are **Intermediate Operations** — `filter`, `map`, `sorted`, etc. Crucially, these machines are **lazy**: they don't spin up until the final packaging station demands output. You can chain as many machines as you like; they only activate when triggered. [openhome](https://openhome.cc/zh-tw/java/functional-api/stream/)

**繁中：** 工廠的生產線上排列著一系列**專業機器**。每台機器從輸送帶取走物品、執行一項工作，再把結果傳給下一台機器。這些就是**中間操作**——`filter`、`map`、`sorted` 等。最關鍵的是，這些機器是**懶惰的（Lazy）** ：在最終包裝站要求產出之前，它們根本不會啟動。你可以串接任意數量的機器，但只有被觸發時才會真正運作。 [openhome](https://openhome.cc/zh-tw/java/functional-api/stream/)

### `filter` — The Quality Inspector / 品管檢驗員

| | EN | 繁中 |
|---|---|---|
| **Factory role** | An inspector on the belt who lets some items pass and **discards the rest** | 輸送帶上的品管員，讓符合標準的物品通過，**丟棄不符合的** |
| **Takes** | A `Predicate<T>` (a yes/no test) | 一個 `Predicate<T>`（是/否的判斷） |
| **Returns** | A new Stream with only matching items | 只含符合條件物品的新 Stream |

```java
.filter(name -> name.startsWith("A"))
// Only "Alice" and "Anna" pass the inspector
// 只有 "Alice" 和 "Anna" 通過品管員
```

### `map` — The Transformation Machine / 加工改造機

| | EN | 繁中 |
|---|---|---|
| **Factory role** | A machine that **reshapes every item** — e.g. painting, resizing, or repackaging | 一台**改造每個物品**的機器——例如上色、改尺寸或重新包裝 |
| **Takes** | A `Function<T, R>` (an input → output conversion) | 一個 `Function<T, R>`（輸入 → 輸出的轉換） |
| **Returns** | A new Stream of transformed items (can be a **different type**) | 已轉換物品的新 Stream（可以是**不同型別**） |

```java
.map(name -> name.toUpperCase())
// "Alice" → "ALICE", "Anna" → "ANNA"
// 每個名字都被機器轉為大寫
```

***

## 📦 Packaging & Shipping — Terminal Operations
### 包裝出貨 — 終端操作

**EN:** At the end of the line sits the **packaging and shipping station**. This is the Terminal Operation — the moment it runs, it **wakes up every lazy machine upstream** and drives the whole pipeline. Unlike intermediate operations, a terminal operation produces a **real, concrete result**: a new list, a count, a boolean, or a printed output. Once triggered, the stream is **consumed and cannot be reused**. [nodejs](https://nodejs.org/api/stream.html)

**繁中：** 生產線的終點是**包裝出貨站**。這就是終端操作——一旦它運行，就會**喚醒上游所有懶惰的機器**，驅動整條生產線運作 。與中間操作不同，終端操作會產生一個**真實、具體的結果**：一個新的列表、一個數字、一個布林值，或是列印輸出。一旦觸發，該 Stream **就被消耗掉了，無法再次使用**。 [linkedin](https://www.linkedin.com/posts/aneervan-ray-7a394618b_javastreamapi-springboot-microservices-activity-7394102356889595905-LsZ3)

### `collect` — The Packaging Station / 包裝站

```java
.collect(Collectors.toList())
// 把輸送帶上所有剩餘物品裝箱，交付最終產品
// Boxes up all remaining items on the belt into a final product
```

***

## 🔗 The Full Pipeline / 完整的生產線

```java
List<String> employees = List.of("Alice", "Bob", "Charlie", "Anna");

List<String> result = employees.stream()           // 🏗️ 原料上輸送帶
    .filter(name -> name.startsWith("A"))          // ⚙️ 品管員：只留A開頭
    .map(name -> name.toUpperCase())               // ⚙️ 改造機：全部大寫
    .collect(Collectors.toList());                 // 📦 包裝出貨

// Result: ["ALICE", "ANNA"]
```

**EN:** Notice that the original `employees` list is untouched. The factory took the raw materials, ran them through two machines, and packaged a brand new product. This is the **declarative** power of Streams — you describe *what* transformations to apply, not *how* to loop through the data. [linkedin](https://www.linkedin.com/posts/aneervan-ray-7a394618b_javastreamapi-springboot-microservices-activity-7394102356889595905-LsZ3)

**繁中：** 注意原始的 `employees` 列表完全沒有被改動 。工廠取用了原材料，讓它通過兩台機器，最後包裝出一個全新的產品。這就是 Stream 的**宣告式（Declarative）**威力——你只需描述**要做什麼**轉換，而不是**如何**一個個迴圈處理資料。 [linkedin](https://www.linkedin.com/posts/aneervan-ray-7a394618b_javastreamapi-springboot-microservices-activity-7394102356889595905-LsZ3)

***

## 🗺️ The Three Roles at a Glance / 三個角色一覽

| Role / 角色 | Factory Analogy / 工廠比喻 | Java Example / Java 範例 |
|---|---|---|
| **Source / 來源** | Raw materials on the conveyor belt 原料上輸送帶 | `list.stream()` |
| **Intermediate / 中間操作** | Machines along the line (lazy!) 生產線機器（懶惰！） | `.filter()` `.map()` `.sorted()` |
| **Terminal / 終端操作** | Packaging & shipping station 包裝出貨站 | `.collect()` `.count()` `.forEach()` |

The laziness of intermediate operations is the factory's secret efficiency — machines only run **when the shipping station places an order**. This means if you `filter` down to 1 item from 1 million, `map` only ever runs **once**, not a million times. [openhome](https://openhome.cc/zh-tw/java/functional-api/stream/)

中間操作的懶惰性是工廠效率的秘密——機器只在**出貨站下訂單時**才啟動 。這意味著如果你從一百萬筆資料 `filter` 出 1 筆，`map` 就只會執行**一次**，而不是一百萬次。 [openhome](https://openhome.cc/zh-tw/java/functional-api/stream/)

