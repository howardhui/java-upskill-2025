# The Evolution of Unmodifiable Collections 不可修改集合的演進

| Version 版本 | Feature 功能 |
|---|---|
| Java 2+ | `Collections.unmodifiableXXX()` — unmodifiable **view** wrappers 不可修改的**視圖**包裝器 |
| Java 9 | `List.of()`, `Set.of()`, `Map.of()` — truly immutable factory methods 真正不可變的工廠方法 |
| Java 10 | `List.copyOf()`, `Set.copyOf()`, `Map.copyOf()` + `Collectors.toUnmodifiableXXX()` |
| Java 16 | `Stream.toList()` — convenient unmodifiable list from stream 從 stream 直接產生不可修改的 List |

***

## Important Distinction: Unmodifiable vs Immutable 重要區別：不可修改（Unmodifiable）vs 不可變（Immutable）

**English:** These two terms are **not the same** in Java. An **unmodifiable** collection is just a read-only **wrapper** around a mutable collection — if the original backing collection changes, those changes are reflected through the wrapper. An **immutable** collection has no backing mutable reference at all — it can never change by any means. [baeldung](https://www.baeldung.com/java-collection-immutable-unmodifiable-differences)

**中文（繁體）：** 這兩個術語在 Java 中**不相同**。**不可修改（unmodifiable）**集合只是可變集合的唯讀**包裝器** — 若原始的底層集合改變，那些改變會透過包裝器反映出來。**不可變（immutable）**集合完全沒有底層可變參照 — 它絕對無法被任何方式改變 。 [baeldung](https://www.baeldung.com/java-collection-immutable-unmodifiable-differences)

```java
// Collections.unmodifiableList — NOT truly immutable! 並非真正不可變！
List<String> mutable = new ArrayList<>(Arrays.asList("A", "B", "C"));
List<String> unmodifiable = Collections.unmodifiableList(mutable);

System.out.println(unmodifiable); // [A, B, C]

mutable.add("D");  // modify the original  修改原始集合
System.out.println(unmodifiable); // [A, B, C, D] ← changed! 已改變！

unmodifiable.add("E"); // ❌ UnsupportedOperationException — can't modify via wrapper
                       // 無法透過包裝器修改
```

***

## Java 9: Truly Immutable Factory Methods 真正不可變的工廠方法

**English:** Java 9 introduced `List.of()`, `Set.of()`, and `Map.of()` — static factory methods that create **truly immutable** collections with no backing mutable reference. These collections also do **not allow `null` elements**. [stackoverflow](https://stackoverflow.com/questions/7713274/java-immutable-collections)

**中文（繁體）：** Java 9 引入了 `List.of()`、`Set.of()` 和 `Map.of()` — 建立**真正不可變**集合的靜態工廠方法，沒有底層可變參照。這些集合也**不允許 `null` 元素** 。 [stackoverflow](https://stackoverflow.com/questions/7713274/java-immutable-collections)

```java
// Java 9 — truly immutable  真正不可變
List<String> immutableList = List.of("A", "B", "C");
Set<Integer>  immutableSet  = Set.of(1, 2, 3);
Map<String, Integer> immutableMap = Map.of("one", 1, "two", 2);

immutableList.add("D");    // ❌ UnsupportedOperationException
immutableList.set(0, "X"); // ❌ UnsupportedOperationException
immutableList = null;      // ✅ reference can change, but the list itself cannot
                           // 參照可以改變，但集合本身不能

// ❌ Null NOT allowed  不允許 null
List<String> withNull = List.of("A", null, "C"); // NullPointerException
```

***

## Java 10 Feature 1: `copyOf()` Methods

**English:** Java 9's `List.of()` works great for inline creation, but what if you already have a mutable collection and want an immutable copy of it? Java 10 introduced `List.copyOf()`, `Set.copyOf()`, and `Map.copyOf()` to create a **truly immutable snapshot** of an existing collection. [javagoal](https://javagoal.com/immutable-collection-in-java/)

**中文（繁體）：** Java 9 的 `List.of()` 非常適合內嵌建立，但如果你已有一個可變集合並想要其不可變副本呢？Java 10 引入了 `List.copyOf()`、`Set.copyOf()` 和 `Map.copyOf()`，用於建立現有集合的**真正不可變快照** 。 [javagoal](https://javagoal.com/immutable-collection-in-java/)

```java
// Java 10 — copyOf() creates an immutable snapshot
// copyOf() 建立不可變快照
List<String> mutable = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie"));
List<String> immutableCopy = List.copyOf(mutable);

System.out.println(immutableCopy); // [Alice, Bob, Charlie]

mutable.add("Dave");               // modify original  修改原始集合
System.out.println(mutable);       // [Alice, Bob, Charlie, Dave]
System.out.println(immutableCopy); // [Alice, Bob, Charlie] ← NOT affected! 不受影響！

immutableCopy.add("Eve");          // ❌ UnsupportedOperationException

// Same for Set and Map  Set 和 Map 同樣適用
Set<Integer> mutableSet = new HashSet<>(Arrays.asList(1, 2, 3));
Set<Integer> immutableSet = Set.copyOf(mutableSet);

Map<String, Integer> mutableMap = new HashMap<>();
mutableMap.put("one", 1);
Map<String, Integer> immutableMap = Map.copyOf(mutableMap);
```

**English:** An important optimisation: if you call `copyOf()` on a collection that is **already immutable** (created by `List.of()` etc.), Java returns the **same instance** without creating a new copy. [cscode](https://cscode.io/java/collections/immutable-collections/)

**中文（繁體）：** 一個重要的最佳化：如果你對一個**已經是不可變**的集合（由 `List.of()` 等建立）呼叫 `copyOf()`，Java 會回傳**相同實例**而不建立新副本 。 [cscode](https://cscode.io/java/collections/immutable-collections/)

```java
List<String> original = List.of("A", "B", "C");
List<String> copy = List.copyOf(original);

System.out.println(original == copy); // true ← same instance! 相同實例！
```

***

## Java 10 Feature 2: `Collectors.toUnmodifiableXXX()`

**English:** Java 10 also added three new `Collectors` methods to **collect a Stream directly into an unmodifiable collection**, eliminating the need to wrap the result afterwards. [4comprehension](https://4comprehension.com/java-immutable-unmodifiable-stream-api-collectors/)

**中文（繁體）：** Java 10 還新增了三個 `Collectors` 方法，可以**直接將 Stream 收集成不可修改的集合**，省去事後包裝的步驟 。 [4comprehension](https://4comprehension.com/java-immutable-unmodifiable-stream-api-collectors/)

- `Collectors.toUnmodifiableList()`
- `Collectors.toUnmodifiableSet()`
- `Collectors.toUnmodifiableMap()`

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// ❌ Old way — verbose  舊方式，冗長
List<Integer> oldWay = Collections.unmodifiableList(
    numbers.stream()
           .filter(n -> n % 2 == 0)
           .collect(Collectors.toList())
);

// ✅ Java 10 — direct  Java 10，直接
List<Integer> evenNumbers = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toUnmodifiableList());

System.out.println(evenNumbers); // [2, 4, 6, 8, 10]
evenNumbers.add(12);             // ❌ UnsupportedOperationException

// toUnmodifiableMap example  toUnmodifiableMap 範例
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
Map<String, Integer> nameToLength = names.stream()
    .collect(Collectors.toUnmodifiableMap(
        name -> name,
        String::length
    ));
System.out.println(nameToLength); // {Alice=5, Bob=3, Charlie=7}
nameToLength.put("Dave", 4);      // ❌ UnsupportedOperationException
```

***

## Java 16: `Stream.toList()`

**English:** Java 16 added `Stream.toList()` as a shorthand that replaces `.collect(Collectors.toUnmodifiableList())`. It is more concise and returns an **unmodifiable list**. [cscode](https://cscode.io/java/collections/immutable-collections/)

**中文（繁體）：** Java 16 新增了 `Stream.toList()` 作為 `.collect(Collectors.toUnmodifiableList())` 的簡寫，更加簡潔，且回傳**不可修改的清單** 。 [cscode](https://cscode.io/java/collections/immutable-collections/)

```java
// Java 16 — most concise way  最簡潔的方式
List<Integer> evenNumbers = numbers.stream()
    .filter(n -> n % 2 == 0)
    .toList(); // ✅ unmodifiable  不可修改

evenNumbers.add(12); // ❌ UnsupportedOperationException
```

***

## Full Comparison Table 完整比較表

| Method 方法 | Java Version 版本 | Truly Immutable? 真正不可變？ | Allows `null`? 允許 null？ | Source 來源 |
|---|---|---|---|---|
| `Collections.unmodifiableList()` | Java 2+ | ❌ View only 僅視圖 | ✅ | Wraps existing 包裝現有 |
| `List.of()` / `Set.of()` / `Map.of()` | Java 9 | ✅ | ❌ | Inline values 內嵌值 |
| `List.copyOf()` / `Set.copyOf()` / `Map.copyOf()` | Java 10 | ✅ | ❌ | Copies existing 複製現有 |
| `Collectors.toUnmodifiableList()` etc. | Java 10 | ✅ | ❌ | From Stream |
| `Stream.toList()` | Java 16 | ✅ | ✅ | From Stream |

The general recommendation is to **prefer immutable collections by default** — they are thread-safe, prevent unintended mutation, and make code easier to reason about. Use `List.of()` for fixed known values, `List.copyOf()` when converting an existing mutable collection, and `Stream.toList()` or `Collectors.toUnmodifiableList()` when collecting from a stream. [alexn](https://alexn.org/blog/2022/10/27/immutable-collections-your-default/)

一般建議是**預設使用不可變集合** — 它們是執行緒安全的，防止意外修改，讓程式碼更容易推理 。對已知的固定值使用 `List.of()`，轉換現有可變集合時使用 `List.copyOf()`，從 stream 收集時使用 `Stream.toList()` 或 `Collectors.toUnmodifiableList()`。 [alexn](https://alexn.org/blog/2022/10/27/immutable-collections-your-default/)
