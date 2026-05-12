# Explain Record Patterns and the changes in all Java versions with examples

Here is a full explanation of Record Patterns across all Java versions.

***

## Version History 版本演進

| Version 版本 | Status 狀態 | JEP |
|---|---|---|
| Java 16 | Pattern Matching for `instanceof` (foundation) 模式比對基礎 | JEP 394 |
| Java 19 | Record Patterns — **Preview 1** 預覽版一 | JEP 405 |
| Java 20 | Record Patterns — **Preview 2** 預覽版二 | JEP 432 |
| Java 21 | Record Patterns — **Stable / Official 正式版** | JEP 440 |
| Java 23+ | Primitive type support in record patterns 原始型別支援 | JEP 455 |

***

## What is a Record Pattern? 什麼是 Record Pattern？

**English:** A **record pattern** extends pattern matching to allow you to **deconstruct a record's components directly** inside an `instanceof` or `switch` expression — eliminating the need to call accessor methods manually after the type check. Think of it as the inverse of a constructor: a constructor **puts data into** an object, while a record pattern **takes data out of** a record in one step. [inside](https://inside.java/2023/11/06/sip087/)

**中文（繁體）：** **Record 模式**擴展了模式比對，讓你可以在 `instanceof` 或 `switch` 表達式中**直接解構 record 的元件** — 消除了型別檢查後手動呼叫存取器方法的需要。可以把它想成建構子的反向操作：建構子將資料**放入**物件，而 record 模式一步將資料從 record **取出** 。 [inside](https://inside.java/2023/11/06/sip087/)

***

## The Foundation: Pattern Matching for `instanceof` (Java 16)
## 基礎：`instanceof` 的模式比對（Java 16）

**English:** Before record patterns could exist, Java 16 introduced pattern matching for `instanceof` — binding the checked object to a variable directly in the condition. [baeldung](https://www.baeldung.com/java-19-record-patterns)

**中文（繁體）：** 在 record 模式存在之前，Java 16 引入了 `instanceof` 的模式比對 — 直接在條件中將被檢查的物件綁定到一個變數 。 [baeldung](https://www.baeldung.com/java-19-record-patterns)

```java
record Point(double x, double y) { }

Object obj = new Point(3.0, 4.0);

// ❌ Before Java 16 — verbose  Java 16 之前：冗長
if (obj instanceof Point) {
    Point p = (Point) obj;           // manual cast 手動轉型
    System.out.println(p.x());
}

// ✅ Java 16 — type pattern binding  型別模式綁定
if (obj instanceof Point p) {        // bind to p directly 直接綁定到 p
    System.out.println(p.x());       // still need to call accessor 仍需呼叫存取器
    System.out.println(p.y());
}
```

***

## Java 19: Record Patterns — Preview 1
## Java 19：Record 模式 — 第一次預覽

**English:** Java 19 introduced record patterns, which go one step further — you can **deconstruct the record's components directly** into named variables inside the pattern, without needing to call accessor methods at all. [baeldung](https://www.baeldung.com/java-19-record-patterns)

**中文（繁體）：** Java 19 引入了 record 模式，更進一步 — 你可以在模式中**直接將 record 的元件解構**成具名變數，完全不需要呼叫存取器方法 。 [baeldung](https://www.baeldung.com/java-19-record-patterns)

```java
record Point(double x, double y) { }

Object obj = new Point(3.0, 4.0);

// ✅ Java 19 — record pattern: deconstruct directly!
// 直接解構！
if (obj instanceof Point(double x, double y)) {
    System.out.println("x = " + x); // x = 3.0
    System.out.println("y = " + y); // y = 4.0
    // No need for p.x() or p.y() — components extracted directly
    // 不需要 p.x() 或 p.y() — 元件直接提取
}
```

### Using `var` for Conciseness `var` 簡化寫法

**English:** You can replace the explicit types with `var` and let the compiler infer them: [baeldung](https://www.baeldung.com/java-19-record-patterns)

**中文（繁體）：** 可以用 `var` 取代明確型別，讓編譯器推斷 ： [baeldung](https://www.baeldung.com/java-19-record-patterns)

```java
if (obj instanceof Point(var x, var y)) {
    System.out.println("x = " + x + ", y = " + y);
}
```

### Named Record Pattern 具名 Record 模式

**English:** You can also give the outer record pattern an identifier, giving access to both the whole record and its components: [baeldung](https://www.baeldung.com/java-19-record-patterns)

**中文（繁體）：** 也可以給外部 record 模式一個識別符，同時存取整個 record 和其元件 ： [baeldung](https://www.baeldung.com/java-19-record-patterns)

```java
if (obj instanceof Point(var x, var y) p) {
    System.out.println(p);      // Point[x=3.0, y=4.0]  — whole record
    System.out.println(x + y);  // 7.0  — deconstructed components
}
```

***

## Nested Record Patterns 巢狀 Record 模式

**English:** One of the most powerful features is **nested record patterns** — deconstructing a record that contains another record, all in one expression. [docs.oracle](https://docs.oracle.com/en/java/javase/22/language/record-patterns.html)

**中文（繁體）：** 最強大的功能之一是**巢狀 record 模式** — 在一個表達式中解構包含另一個 record 的 record 。 [docs.oracle](https://docs.oracle.com/en/java/javase/22/language/record-patterns.html)

```java
record Point(double x, double y) { }
record Circle(Point centre, double radius) { }

Object shape = new Circle(new Point(1.0, 2.0), 5.0);

// ❌ Old way — multiple accessor calls  舊方式：多次存取器呼叫
if (shape instanceof Circle c) {
    double x = c.centre().x();
    double y = c.centre().y();
    double r = c.radius();
    System.out.println("Centre: (" + x + "," + y + "), Radius: " + r);
}

// ✅ Nested record pattern — extract everything at once
// 巢狀 record 模式：一次提取所有內容
if (shape instanceof Circle(Point(var x, var y), var radius)) {
    System.out.println("Centre: (" + x + "," + y + "), Radius: " + radius);
    // Centre: (1.0,2.0), Radius: 5.0
}
```

***

## Record Patterns in `switch` (Java 21)
## `switch` 中的 Record 模式（Java 21）

**English:** Java 21 fully stabilised record patterns and made them work seamlessly with **pattern matching for `switch`** — the combination is especially powerful with `sealed` hierarchies. [payara](https://payara.fish/blog/a-leap-towards-expressive-coding-with-record-patterns-in-java-21/)

**中文（繁體）：** Java 21 完全穩定了 record 模式，並使其與 **`switch` 的模式比對**無縫整合 — 與 `sealed` 繼承架構的組合特別強大 。 [payara](https://payara.fish/blog/a-leap-towards-expressive-coding-with-record-patterns-in-java-21/)

```java
sealed interface Shape permits Circle, Rectangle, Triangle { }
record Circle(double radius)            implements Shape { }
record Rectangle(double width, double height) implements Shape { }
record Triangle(double base, double height)   implements Shape { }

// ✅ Java 21 — switch + record patterns = exhaustive & clean
// switch + record 模式 = 完整且整潔
double area(Shape shape) {
    return switch (shape) {
        case Circle(var r)         -> Math.PI * r * r;
        case Rectangle(var w, var h) -> w * h;
        case Triangle(var b, var h)  -> 0.5 * b * h;
        // No default needed — sealed + record patterns = exhaustive
        // 不需要 default — sealed + record 模式 = 完整涵蓋
    };
}

System.out.println(area(new Circle(5.0)));          // 78.53...
System.out.println(area(new Rectangle(4.0, 6.0)));  // 24.0
System.out.println(area(new Triangle(3.0, 8.0)));   // 12.0
```

### Guards (`when`) with Record Patterns Record 模式搭配守衛（`when`）

**English:** You can add a `when` guard clause to add extra conditions beyond the pattern match: [payara](https://payara.fish/blog/a-leap-towards-expressive-coding-with-record-patterns-in-java-21/)

**中文（繁體）：** 可以新增 `when` 守衛子句來增加模式比對之外的額外條件 ： [payara](https://payara.fish/blog/a-leap-towards-expressive-coding-with-record-patterns-in-java-21/)

```java
String classify(Shape shape) {
    return switch (shape) {
        case Circle(var r) when r > 10  -> "Large circle 大圓";
        case Circle(var r)              -> "Small circle 小圓";
        case Rectangle(var w, var h) when w == h -> "Square 正方形";
        case Rectangle(var w, var h)    -> "Rectangle 長方形";
        case Triangle(var b, var h)     -> "Triangle 三角形";
    };
}

System.out.println(classify(new Circle(15.0)));       // Large circle
System.out.println(classify(new Circle(3.0)));        // Small circle
System.out.println(classify(new Rectangle(5.0, 5.0)));// Square
```

***

## Java 20: What Changed from Preview 1? Java 20：與預覽版一有何不同？

**English:** Java 20's second preview (JEP 432) made one notable change: it **removed support for record patterns in the header of enhanced `for` loops**, which had been in the Java 19 preview. The feature was deemed premature and was removed to focus on `instanceof` and `switch` usage. [infoq](https://www.infoq.com/news/2023/05/java-gets-boost-with-record/)

**中文（繁體）：** Java 20 的第二次預覽（JEP 432）做了一個重要變更：**移除了在增強型 `for` 迴圈標頭中使用 record 模式的支援**，這在 Java 19 預覽版中曾存在。此功能被認為尚未成熟，因此移除，以便專注於 `instanceof` 和 `switch` 的使用 。 [infoq](https://www.infoq.com/news/2023/05/java-gets-boost-with-record/)

```java
// ❌ Was in Java 19 preview, removed in Java 20+
// 曾在 Java 19 預覽版中存在，Java 20+ 已移除
for (Point(var x, var y) : listOfPoints) {  // removed 已移除
    System.out.println(x + ", " + y);
}

// ✅ Still valid — use explicit for-each instead
// 仍有效 — 改用明確的 for-each
for (var point : listOfPoints) {
    if (point instanceof Point(var x, var y)) {
        System.out.println(x + ", " + y);
    }
}
```

***

## Java 23+: Primitive Types in Record Patterns
## Java 23+：Record 模式中的原始型別

**English:** Java 23 (JEP 455, preview) extended record patterns to support **primitive types** as pattern components. Previously, if a record had a primitive field like `int`, the pattern variable had to match the exact type. Now, safe widening/narrowing conversions are also permitted. [docs.oracle](https://docs.oracle.com/en/java/javase/23/language/record-patterns.html)

**中文（繁體）：** Java 23（JEP 455，預覽版）擴展了 record 模式，支援**原始型別**作為模式元件。之前，如果 record 有 `int` 等原始欄位，模式變數必須完全匹配型別。現在也允許安全的擴展/縮窄轉換 。 [docs.oracle](https://docs.oracle.com/en/java/javase/23/language/record-patterns.html)

```java
record Sensor(int value) { }

Object s = new Sensor(42);

// Java 23+ — primitive type widening in record pattern
// 原始型別擴展
if (s instanceof Sensor(long v)) {   // int component matched as long
    System.out.println(v);            // 42
}
```

***

## Full Evolution Comparison 完整演進比較

```java
record Name(String first, String last, int age) { }

Name host = new Name("Alice", "Smith", 30);

// ❌ Java 8–15 — cast + manual accessor calls  手動轉型和存取器呼叫
if (host instanceof Object) {
    Name n = (Name) host;
    String first = n.first();
    String last  = n.last();
    int age = n.age();
}

// ✅ Java 16 — type pattern (bind, then call accessors)
// 型別模式：綁定後呼叫存取器
if (host instanceof Name n) {
    System.out.println(n.first() + " " + n.last()); // still needs accessors
}

// ✅ Java 19+ — record pattern (deconstruct directly)
// Record 模式：直接解構
if (host instanceof Name(var first, var last, var age)) {
    System.out.println(first + " " + last + ", age " + age); // Alice Smith, age 30
}

// ✅ Java 21 — in switch with guards  在 switch 中搭配守衛
String category = switch (host) {
    case Name(var f, var l, var a) when a < 18 -> f + " is a minor 未成年";
    case Name(var f, var l, var a)              -> f + " is an adult 成年人";
};
System.out.println(category); // Alice is an adult
```

***

## Summary Table 總結表

| Version 版本 | Feature 功能 |
|---|---|
| Java 16 | `instanceof` type pattern — bind typed variable 型別模式綁定 |
| Java 19 | Record patterns in `instanceof` (Preview 1) — deconstruct components 解構元件 |
| Java 20 | Record patterns (Preview 2) — removed `for` loop support 移除 for 迴圈支援 |
| Java 21 | **Stable** — record patterns in `instanceof` + `switch` + guards 完整穩定版 |
| Java 23+ | Primitive type support in record patterns 原始型別支援 |

Record patterns, sealed classes, and `switch` pattern matching together form the core of Java's shift towards a more **functional, data-oriented** programming style — allowing you to safely and exhaustively navigate complex data structures with minimal boilerplate. [infoq](https://www.infoq.com/news/2023/05/java-gets-boost-with-record/)

Record 模式、密封類別和 `switch` 模式比對共同構成了 Java 朝向更**函數式、以資料為導向**的程式設計風格轉變的核心 — 讓你能以最少的樣板程式碼安全且完整地導航複雜的資料結構 。 [infoq](https://www.infoq.com/news/2023/05/java-gets-boost-with-record/)