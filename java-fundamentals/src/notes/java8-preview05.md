#Explain the use of "record" in Java 15 (and later version if any)with examples in both English and Traditional Chinese together

Here is a full explanation of `record` in Java, covering its evolution from preview to stable release.

***

## Version History 版本演進

| Version 版本 | Status 狀態 |
|---|---|
| Java 14 | Preview (JEP 359) 預覽版 |
| Java 15 | Second Preview (JEP 384) 第二次預覽版 |
| Java 16 | **Stable / Official (JEP 395) 正式版** |

***

## What is a Record? 什麼是 Record？

**English:** A `record` is a special kind of class in Java designed purely as a **transparent, immutable data carrier**. Before records, creating a simple data class required writing enormous amounts of boilerplate: a constructor, private final fields, getters, `equals()`, `hashCode()`, and `toString()`. A `record` generates all of this automatically from a single line declaration. [github](https://github.com/Snailclimb/JavaGuide/blob/main/docs/java/new-features/java14-15.md)

**中文（繁體）：** `record` 是 Java 中一種特殊的類別，專門設計作為**透明、不可變的資料承載器**。在 record 出現之前，建立一個簡單的資料類別需要撰寫大量樣板程式碼：建構子、`private final` 欄位、getter、`equals()`、`hashCode()` 和 `toString()`。`record` 可以從單行宣告自動生成所有這些內容 。 [github](https://github.com/Snailclimb/JavaGuide/blob/main/docs/java/new-features/java14-15.md)

***

## Basic Syntax 基本語法

**English:** You replace `class` with `record` and declare the **record components** (fields) directly in the header parentheses. The compiler automatically generates everything else. [it-biz](https://it-biz.online/java/java-record/)

**中文（繁體）：** 你將 `class` 替換為 `record`，並直接在標頭括號中宣告**record 元件**（欄位）。編譯器自動生成其餘所有內容 。 [it-biz](https://it-biz.online/java/java-record/)

```java
// ❌ Before record — massive boilerplate  record 之前：大量樣板程式碼
public final class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() { return name; }
    public int getAge()     { return age; }

    @Override public boolean equals(Object o) { /* ... */ }
    @Override public int hashCode()           { /* ... */ }
    @Override public String toString()        { /* ... */ }
}

// ✅ With record — one line!  使用 record：一行搞定！
record Person(String name, int age) { }
```

***

## What Gets Auto-Generated? 自動生成了什麼？

**English:** From the declaration `record Person(String name, int age)`, the compiler automatically generates: [it-biz](https://it-biz.online/java/java-record/)

**中文（繁體）：** 從 `record Person(String name, int age)` 的宣告，編譯器自動生成 ： [it-biz](https://it-biz.online/java/java-record/)

```java
// The compiler secretly creates this 編譯器偷偷建立以下內容:
public final class Person extends java.lang.Record {
    private final String name;  // ← implicitly final  隱含 final
    private final int age;      // ← implicitly final  隱含 final

    // Canonical constructor 標準建構子
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Accessors (NOT getName(), but name()) 存取器（不是 getName()，而是 name()）
    public String name() { return name; }
    public int age()     { return age; }

    // equals(), hashCode(), toString() all auto-generated
    // equals(), hashCode(), toString() 全部自動生成
    @Override public boolean equals(Object o)  { /* compares all fields */ }
    @Override public int hashCode()            { /* based on all fields  */ }
    @Override public String toString()         { /* Person[name=Alice, age=30] */ }
}
```

***

## Basic Usage 基本用法

```java
record Point(int x, int y) { }
record Person(String name, int age) { }

public class Main {
    public static void main(String[] args) {
        Person p = new Person("Alice", 30);

        // Accessor methods (name(), not getName())
        // 存取器方法（是 name()，不是 getName()）
        System.out.println(p.name());     // Alice
        System.out.println(p.age());      // 30

        // Auto-generated toString()  自動生成的 toString()
        System.out.println(p);            // Person[name=Alice, age=30]

        // Auto-generated equals()  自動生成的 equals()
        Person p2 = new Person("Alice", 30);
        System.out.println(p.equals(p2)); // true

        // Auto-generated hashCode()  自動生成的 hashCode()
        System.out.println(p.hashCode() == p2.hashCode()); // true
    }
}
```

***

## Customising a Record 自訂 Record

### Custom Compact Constructor 緊湊建構子（用於驗證）

**English:** You can add validation logic in the **compact constructor** (no parameter list needed — the compiler fills in the assignment automatically). [blog.csdn](https://blog.csdn.net/wwzskyrain/article/details/120319655)

**中文（繁體）：** 你可以在**緊湊建構子**（不需要參數列表 — 編譯器自動填入指派）中加入驗證邏輯 。 [blog.csdn](https://blog.csdn.net/wwzskyrain/article/details/120319655)

```java
record Person(String name, int age) {
    // Compact constructor — for validation  緊湊建構子，用於驗證
    public Person {
        Objects.requireNonNull(name, "name cannot be null");
        if (age < 0) throw new IllegalArgumentException("Age must be non-negative");
        // this.name = name and this.age = age happen automatically
        // this.name = name 和 this.age = age 自動執行
    }
}

Person valid   = new Person("Alice", 30);   // ✅ OK
Person invalid = new Person("Bob", -1);     // ❌ IllegalArgumentException
```

### Adding Custom Methods 新增自訂方法

```java
record Rectangle(double width, double height) {
    // Custom instance method  自訂實例方法
    public double area() {
        return width * height;
    }

    // Custom static method  自訂靜態方法
    public static Rectangle square(double side) {
        return new Rectangle(side, side);
    }
}

Rectangle r = new Rectangle(4.0, 5.0);
System.out.println(r.area());                      // 20.0

Rectangle sq = Rectangle.square(3.0);
System.out.println(sq);                            // Rectangle[width=3.0, height=3.0]
```

### Implementing Interfaces 實作介面

```java
interface Describable {
    String describe();
}

record Product(String name, double price) implements Describable {
    @Override
    public String describe() {
        return name + " costs £" + price;
    }
}

Product p = new Product("Coffee", 3.50);
System.out.println(p.describe()); // Coffee costs £3.50
```

***

## Records and Pattern Matching (Java 16+) Record 與模式比對（Java 16+）

**English:** From **Java 21**, records work seamlessly with **pattern matching for `switch`** and **record patterns**, enabling powerful and concise data deconstruction. [blog.csdn](https://blog.csdn.net/sayWhat_sayHello/article/details/121902216)

**中文（繁體）：** 從 **Java 21** 開始，records 與 **`switch` 的模式比對**和 **record 模式**無縫整合，實現強大且簡潔的資料解構 。 [blog.csdn](https://blog.csdn.net/sayWhat_sayHello/article/details/121902216)

```java
record Point(int x, int y) { }
record Circle(Point centre, double radius) { }

// Java 21 — record pattern matching  record 模式比對
Object shape = new Circle(new Point(0, 0), 5.0);

if (shape instanceof Circle(Point(var x, var y), var r)) {
    System.out.println("Centre: (" + x + "," + y + "), Radius: " + r);
    // Output: Centre: (0,0), Radius: 5.0
}
```

***

## Key Restrictions 關鍵限制



```java
// ❌ Records cannot extend other classes (already extends Record)
// Record 不能繼承其他類別（已繼承 Record）
record Foo(int x) extends SomeClass { } // COMPILE ERROR

// ❌ Record fields cannot be non-final  Record 欄位不能是非 final
// (all components are implicitly private final)
// 所有元件都是隱含的 private final

// ✅ Records CAN implement interfaces  Record 可以實作介面
record Bar(int x) implements Comparable<Bar> { ... }

// ✅ Records CAN have static fields  Record 可以有靜態欄位
record Baz(String name) {
    static int count = 0; // ✅
}
```

***

## Summary Table 總結表

| Feature 特點 | Detail 說明 |
|---|---|
| Keyword 關鍵字 | `record` replaces `class` 取代 `class` |
| Stable since 正式版本 | Java 16 |
| Fields 欄位 | Implicitly `private final` 隱含 `private final` |
| Auto-generated 自動生成 | Constructor, accessors, `equals()`, `hashCode()`, `toString()` |
| Accessor style 存取器風格 | `name()` not `getName()` 是 `name()` 不是 `getName()` |
| Can extend? 可繼承類別？ | ❌ Already extends `java.lang.Record` |
| Can implement? 可實作介面？ | ✅ Yes |
| Can add methods? 可新增方法？ | ✅ Yes |
| Pattern matching 模式比對 | ✅ Java 21+ |
| Best use case 最佳使用情境 | DTOs, value objects, data carriers DTO、值物件、資料承載器 |

The record is Java's answer to the data class pattern seen in Kotlin (`data class`) and Scala (`case class`) — it removes the pain of writing repetitive boilerplate for simple immutable data objects. [xie.infoq](https://xie.infoq.cn/article/5c718825f60f1165cdf158069)

`record` 是 Java 對 Kotlin（`data class`）和 Scala（`case class`）中資料類別模式的回應 — 它消除了為簡單不可變資料物件撰寫重複樣板程式碼的痛苦 。 [xie.infoq](https://xie.infoq.cn/article/5c718825f60f1165cdf158069)

---
---

# Explain "Sealed Classes" in Java 15 (and later version if any) with examples in both English and Traditional Chinese together

Here is a full explanation of Sealed Classes in Java.

***

## Version History 版本演進

| Version 版本 | Status 狀態 |
|---|---|
| Java 15 | Preview (JEP 360) 預覽版 |
| Java 16 | Second Preview (JEP 397) 第二次預覽版 |
| Java 17 | **Stable / Official (JEP 409) 正式版** |

***

## What is a Sealed Class? 什麼是密封類別？

**English:** A **sealed class** (or interface) restricts which other classes or interfaces may **extend or implement** it. Before sealed classes, Java had two extremes: a class was either `final` (no subclasses at all) or fully open (any class could extend it). Sealed classes introduce a middle ground — you explicitly **name a fixed set of permitted subclasses**, giving you full control over your type hierarchy. [baeldung](https://www.baeldung.com/java-sealed-classes-interfaces)

**中文（繁體）：** **密封類別**（或介面）限制了哪些其他類別或介面可以**繼承或實作**它。在密封類別出現之前，Java 有兩個極端：一個類別要麼是 `final`（完全不能有子類別），要麼是完全開放的（任何類別都可以繼承）。密封類別引入了中間地帶 — 你明確地**指定一組固定的允許子類別**，讓你對型別繼承架構擁有完全的掌控 。 [baeldung](https://www.baeldung.com/java-sealed-classes-interfaces)

***

## Basic Syntax 基本語法

**English:** Use the `sealed` keyword on the parent class/interface, and the `permits` clause to list allowed subclasses. Each permitted subclass **must** then declare exactly one of: `final`, `sealed`, or `non-sealed`. [docs.oracle](https://docs.oracle.com/en/java/javase/17/language/sealed-classes-and-interfaces.html)

**中文（繁體）：** 在父類別/介面上使用 `sealed` 關鍵字，並用 `permits` 子句列出允許的子類別。每個被允許的子類別**必須**聲明以下三者之一：`final`、`sealed` 或 `non-sealed` 。 [docs.oracle](https://docs.oracle.com/en/java/javase/17/language/sealed-classes-and-interfaces.html)

```java
// Basic sealed class  基本密封類別
public sealed class Shape permits Circle, Rectangle, Square { }
```

***

## The Three Permitted Subclass Modifiers
## 三種允許子類別的修飾符

**English:** Every direct subclass of a sealed class must declare one of these three modifiers: [docs.oracle](https://docs.oracle.com/en/java/javase/25/language/sealed-classes-and-interfaces.html)

**中文（繁體）：** 密封類別的每個直接子類別都必須聲明以下三種修飾符之一 ： [docs.oracle](https://docs.oracle.com/en/java/javase/25/language/sealed-classes-and-interfaces.html)

```java
sealed class Shape permits Circle, Rectangle, Square { }

// 1. final — cannot be extended further  不能被進一步繼承
final class Circle extends Shape {
    private final double radius;
    Circle(double radius) { this.radius = radius; }
}

// 2. sealed — can only be extended by ITS own permitted subclasses
// 只能被它自己指定的子類別繼承
sealed class Rectangle extends Shape permits FilledRectangle {
    double width, height;
    Rectangle(double w, double h) { this.width = w; this.height = h; }
}

final class FilledRectangle extends Rectangle {
    String colour;
    FilledRectangle(double w, double h, String c) {
        super(w, h); this.colour = c;
    }
}

// 3. non-sealed — opens the hierarchy back up to any class
// 重新對任何類別開放繼承
non-sealed class Square extends Shape {
    double side;
    Square(double side) { this.side = side; }
}

// Because Square is non-sealed, ANY class can now extend it
// 因為 Square 是 non-sealed，任何類別現在都可以繼承它
class SpecialSquare extends Square {
    SpecialSquare(double side) { super(side); }
}
```

***

## Sealed Interfaces 密封介面

**English:** `sealed` works equally well on **interfaces**. This is particularly powerful when combined with `record`, since records are implicitly `final`. [baeldung](https://www.baeldung.com/java-sealed-classes-interfaces)

**中文（繁體）：** `sealed` 在**介面**上同樣有效。與 `record` 結合使用時特別強大，因為 record 是隱含 `final` 的 。 [baeldung](https://www.baeldung.com/java-sealed-classes-interfaces)

```java
// Sealed interface with records — very common pattern
// 密封介面搭配 record — 非常常見的模式
sealed interface Shape permits Circle, Rectangle, Triangle { }

record Circle(double radius)           implements Shape { }
record Rectangle(double w, double h)   implements Shape { }
record Triangle(double base, double h) implements Shape { }

// Usage  用法
Shape shape = new Circle(5.0);
System.out.println(shape); // Circle[radius=5.0]
```

***

## The Real Power: Exhaustive Pattern Matching (Java 17+)
## 真正的威力：完整的模式比對（Java 17+）

**English:** Sealed classes truly shine when combined with **pattern matching for `switch`** (stable in Java 21). Because the compiler knows **all possible subtypes** at compile time, it can verify that your `switch` is **exhaustive** — no `default` case needed. [infoq](https://www.infoq.com/articles/java-sealed-classes/)

**中文（繁體）：** 密封類別與 **`switch` 模式比對**（Java 21 正式版）結合使用時真正展現威力。因為編譯器在編譯時就知道**所有可能的子型別**，它可以驗證你的 `switch` 是否**完整涵蓋** — 不需要 `default` 分支 。 [infoq](https://www.infoq.com/articles/java-sealed-classes/)

```java
sealed interface Shape permits Circle, Rectangle, Triangle { }
record Circle(double radius)           implements Shape { }
record Rectangle(double w, double h)   implements Shape { }
record Triangle(double base, double h) implements Shape { }

// Java 21 — exhaustive switch, no default needed!
// Java 21 — 完整的 switch，不需要 default！
double area(Shape shape) {
    return switch (shape) {
        case Circle c        -> Math.PI * c.radius() * c.radius();
        case Rectangle r     -> r.w() * r.h();
        case Triangle t      -> 0.5 * t.base() * t.h();
        // No default needed — compiler knows all cases are covered
        // 不需要 default — 編譯器知道所有情況都已涵蓋
    };
}

System.out.println(area(new Circle(5.0)));       // 78.53...
System.out.println(area(new Rectangle(4.0, 6.0))); // 24.0
System.out.println(area(new Triangle(3.0, 4.0)));  // 6.0
```

**English:** If you add a new permitted subclass (e.g. `Pentagon`) but forget to handle it in the `switch`, the **compiler gives you an error** immediately — a huge safety improvement over traditional polymorphism. [algomaster](https://algomaster.io/learn/java/sealed-classes)

**中文（繁體）：** 如果你新增一個允許的子類別（例如 `Pentagon`）但忘記在 `switch` 中處理它，**編譯器會立即報錯** — 比傳統多型有巨大的安全性提升 。 [algomaster](https://algomaster.io/learn/java/sealed-classes)

***

## Domain Modelling Example 領域建模範例

**English:** Sealed classes are ideal for modelling **closed domain concepts** like payment types, HTTP responses, or AST nodes — situations where you know all possible variants upfront. [infoq](https://www.infoq.com/articles/java-sealed-classes/)

**中文（繁體）：** 密封類別非常適合建模**封閉的領域概念**，例如付款類型、HTTP 回應或 AST 節點 — 你預先知道所有可能變體的情況 。 [infoq](https://www.infoq.com/articles/java-sealed-classes/)

```java
// Payment type hierarchy  付款類型繼承架構
sealed interface Payment permits CreditCard, BankTransfer, Crypto { }

record CreditCard(String cardNumber, double amount) implements Payment { }
record BankTransfer(String iban, double amount)     implements Payment { }
record Crypto(String walletId, double amount)       implements Payment { }

// Process payment exhaustively  完整處理付款
String process(Payment payment) {
    return switch (payment) {
        case CreditCard cc  -> "Charging card: " + cc.cardNumber();
        case BankTransfer bt -> "Transfer to: " + bt.iban();
        case Crypto c        -> "Sending crypto to: " + c.walletId();
    };
}

System.out.println(process(new CreditCard("1234-5678", 99.99)));
// Charging card: 1234-5678
```

***

## `permits` Clause Rules `permits` 子句規則

: [geeksforgeeks](https://www.geeksforgeeks.org/java/sealed-class-in-java/)

```java
// ✅ Permitted subclasses must be in same package or same module
// 允許的子類別必須在相同套件或相同模組中

// ✅ Permitted subclasses must directly extend/implement the sealed type
// 允許的子類別必須直接繼承/實作密封型別

// ❌ Cannot be a remote class in a different unnamed module
// 不能是不同未命名模組中的遠端類別

// ✅ If all permitted subclasses are in same file, permits clause is OPTIONAL
// 若所有允許子類別在同一個檔案中，permits 子句可以省略
sealed class Shape { }          // permits inferred from same-file subclasses
final class Circle  extends Shape { }
final class Square  extends Shape { }
```

***

## Comparison: `final` vs `sealed` vs Open Class
## 比較：`final` vs `sealed` vs 開放類別

| Feature 特點 | `final` class | `sealed` class | Open class 開放類別 |
|---|---|---|---|
| Can be extended? 可繼承？ | ❌ None 不可 | ✅ Only listed subclasses 只有列出的子類別 | ✅ Any class 任何類別 |
| Subclass control 子類別控制 | Total 完全 | Fine-grained 細粒度 | None 無 |
| Exhaustive switch 完整 switch | ✅ (only self) | ✅ All subtypes known 所有子型別已知 | ❌ Unknown subclasses 子類別未知 |
| Use case 使用情境 | Utility / value classes | Domain models, ADTs | General-purpose APIs |

Sealed classes, records, and pattern matching together form Java's answer to **Algebraic Data Types (ADTs)** — a powerful pattern from functional programming that allows safe, exhaustive modelling of a fixed set of variants. They are particularly powerful together as a trio. [infoq](https://www.infoq.com/articles/java-sealed-classes/)

密封類別、record 和模式比對共同構成了 Java 對**代數資料型別（ADT）**的回應 — 這是函數式程式設計中的一個強大模式，允許對固定的變體集合進行安全、完整的建模。三者結合使用特別強大 。 [infoq](https://www.infoq.com/articles/java-sealed-classes/)

---
---

# Record vs regular Class

## Major Differences: Record vs Regular Class
## 主要差異：Record 與一般類別

**English:** A `record` is fundamentally a **restricted, specialised form of a class** — it is always implicitly `final`, all its fields are implicitly `private final`, and it automatically generates a canonical constructor, accessors, `equals()`, `hashCode()`, and `toString()`. A regular class gives you complete freedom to design any kind of behaviour. [stackoverflow](https://stackoverflow.com/questions/71473485/what-is-the-difference-between-a-final-class-and-a-record)

**中文（繁體）：** `record` 本質上是一種**受限的特殊類別** — 它始終是隱含的 `final`，所有欄位都是隱含的 `private final`，並自動生成標準建構子、存取器、`equals()`、`hashCode()` 和 `toString()`。一般類別讓你完全自由地設計任何行為 。 [stackoverflow](https://stackoverflow.com/questions/71473485/what-is-the-difference-between-a-final-class-and-a-record)

| Feature 特點 | `record` | Regular Class 一般類別 |
|---|---|---|
| Fields 欄位 | Implicitly `private final` 隱含 `private final` | Any modifier 任何修飾符 |
| Mutable? 可變？ | ❌ Immutable by design 設計上不可變 | ✅ Can be mutable 可以可變 |
| Extends other class? 可繼承類別？ | ❌ Implicitly extends `Record` 隱含繼承 `Record` | ✅ Yes 可以 |
| Can be extended? 可被繼承？ | ❌ Implicitly `final` 隱含 `final` | ✅ Unless `final` 除非加 `final` |
| `equals()` / `hashCode()` | ✅ Auto-generated (value-based) 自動生成（值比較） | ❌ Must write manually 必須手動撰寫 |
| `toString()` | ✅ Auto-generated 自動生成 | ❌ Must write manually 必須手動撰寫 |
| Setters 設值器 | ❌ Not supported 不支援 | ✅ Optional 可選 |
| Accessor style 存取器風格 | `name()` not `getName()` | `getName()` convention 慣例 |
| Private internal fields 私有內部欄位 | ❌ All fields are exposed via accessors 所有欄位均透過存取器公開 | ✅ Can hide internal representation 可隱藏內部表示 |
| `abstract` / `sealed` modifier | ❌ Not allowed 不允許 | ✅ Allowed 允許 |
| Instance initialisers 實例初始化器 | ❌ Not allowed 不允許 | ✅ Allowed 允許 |

***

## Code Comparison 程式碼比較

**English:** The difference in verbosity is immediately apparent. Both of the following represent the same concept — a data container for a person's name and age. [ziuma](https://ziuma.com/Thread-What-is-the-difference-between-record-and-a-regular-class-in-Java-and-when-should-I?pid=35788)

**中文（繁體）：** 冗長程度的差異一目了然。以下兩者都表示相同的概念 — 一個儲存姓名和年齡的資料容器 。 [ziuma](https://ziuma.com/Thread-What-is-the-difference-between-record-and-a-regular-class-in-Java-and-when-should-I?pid=35788)

```java
// ❌ Regular class — verbose  一般類別：冗長
public final class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName()  { return name; }
    public int    getAge()   { return age; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person p)) return false;
        return age == p.age && Objects.equals(name, p.name);
    }
    @Override public int    hashCode()  { return Objects.hash(name, age); }
    @Override public String toString()  { return "Person[name=" + name + ", age=" + age + "]"; }
}

// ✅ Record — one line!  Record：一行搞定！
record Person(String name, int age) { }
```

***

## When to Use `record` 何時使用 `record`

**English:** Use `record` when your class's **sole purpose is to carry data** and you have no need for mutability, inheritance, or hidden internal state. [linkedin](https://www.linkedin.com/pulse/java-records-when-use-avoid-them-fabio-ribeiro-wm9of)

**中文（繁體）：** 當你的類別**唯一目的是承載資料**，且不需要可變性、繼承或隱藏的內部狀態時，使用 `record` 。 [linkedin](https://www.linkedin.com/pulse/java-records-when-use-avoid-them-fabio-ribeiro-wm9of)

- **DTOs (Data Transfer Objects)** — transferring data between layers (controller → service → database) 在層與層之間傳輸資料
- **API response/request bodies** — simple containers for JSON payloads JSON 載荷的簡單容器
- **Value objects** — coordinates, ranges, pairs, money amounts 座標、範圍、配對、金額
- **Configuration holders** — grouping related settings together 將相關設定組合在一起
- **Pattern matching** — paired with `sealed` interfaces for exhaustive `switch` 與 `sealed` 介面搭配用於完整 `switch`

```java
// ✅ Great use cases for record  record 的最佳使用案例

record Point(int x, int y) { }                        // value object 值物件
record UserDto(String name, String email) { }          // DTO
record ApiResponse(int status, String message) { }     // API response
record DateRange(LocalDate from, LocalDate to) { }     // configuration 設定
```

***

## When NOT to Use `record` 何時不應使用 `record`

**English:** Avoid `record` when you need any of the following: [reddit](https://www.reddit.com/r/javahelp/comments/1pd6yvg/whats_the_difference_between_record_and_class_in/)

**中文（繁體）：** 當你需要以下任何一項時，避免使用 `record` ： [reddit](https://www.reddit.com/r/javahelp/comments/1pd6yvg/whats_the_difference_between_record_and_class_in/)

```java
// ❌ 1. Mutable state (you need setters)  可變狀態（需要 setter）
// Use a regular class  使用一般類別
class ShoppingCart {
    private List<String> items = new ArrayList<>();
    public void addItem(String item) { items.add(item); } // mutable 可變
}

// ❌ 2. Inheritance hierarchy  繼承架構
class Animal { }
class Dog extends Animal { }  // records cannot extend custom classes
                               // record 不能繼承自訂類別

// ❌ 3. Hiding internal representation  隱藏內部表示
class CachedData {
    private final Map<String, Object> cache = new HashMap<>(); // hidden 隱藏
    public Object get(String key) { return cache.get(key); }
}

// ❌ 4. Complex business logic / behaviour  複雜業務邏輯/行為
class OrderService {
    public Order placeOrder(Cart cart) { /* complex logic 複雜邏輯 */ }
    public void cancelOrder(Order o)   { /* complex logic 複雜邏輯 */ }
}
```

***

## The Relationship Between `record` and `Optional`
## `record` 與 `Optional` 的關係

**English:** This is a nuanced topic. `record` and `Optional` are entirely independent features, but they interact in interesting — and sometimes tricky — ways. The core tension is that a `record`'s constructor parameter type **must exactly match** its accessor return type. This means if you declare `Optional<String>` as a record component, the accessor also returns `Optional<String>`, which goes against the convention that `Optional` should only be used as a **return type**, not as a field type. [stackoverflow](https://stackoverflow.com/questions/73424610/optionalt-as-a-record-parameter-in-java)

**中文（繁體）：** 這是一個微妙的話題。`record` 和 `Optional` 是完全獨立的功能，但它們以有趣且有時棘手的方式互動。核心矛盾在於 `record` 的建構子參數型別必須**完全符合**其存取器的回傳型別。這意味著如果你將 `Optional<String>` 宣告為 record 元件，存取器也會回傳 `Optional<String>`，這違反了 `Optional` 應只作為**回傳型別**而非欄位型別的慣例 。 [stackoverflow](https://stackoverflow.com/questions/73424610/optionalt-as-a-record-parameter-in-java)

### Approach 1: `Optional` as a record component (Not Recommended)
### 方式一：`Optional` 作為 record 元件（不建議）

```java
// ⚠️ Technically works, but bad practice  技術上可行，但不建議
record Request(String name, Optional<String> nickname) { }

Request r = new Request("Alice", Optional.of("Ali"));
r.nickname().ifPresent(System.out::println); // Ali

Request r2 = new Request("Bob", Optional.empty());
// ❌ Problems: Optional is not Serializable, violates record's "transparent" nature
// 問題：Optional 不可序列化，違反 record 的「透明」本質
```

**English:** The issue is philosophical — a `record` is meant to be a **transparent carrier** where every field is directly visible. Wrapping a field in `Optional` adds an extra layer of indirection, making it no longer transparent. Additionally, since record fields are `final`, an empty `Optional` stored in a record will **always remain empty** — there is no value in storing it. [stackoverflow](https://stackoverflow.com/questions/73424610/optionalt-as-a-record-parameter-in-java)

**中文（繁體）：** 問題在於設計理念 — `record` 的目的是作為**透明的承載器**，每個欄位都直接可見。用 `Optional` 包裝欄位增加了一層間接性，使其不再透明。此外，由於 record 欄位是 `final`，儲存在 record 中的空 `Optional` 將**永遠保持空** — 儲存它沒有任何意義 。 [stackoverflow](https://stackoverflow.com/questions/73424610/optionalt-as-a-record-parameter-in-java)

### Approach 2: Use `null` + custom accessor returning `Optional` (Recommended)
### 方式二：使用 `null` + 自訂存取器回傳 `Optional`（建議）

```java
// ✅ Best practice — store raw value, wrap in Optional at accessor level
// 最佳實踐 — 儲存原始值，在存取器層面包裝成 Optional
record Person(String name, String nickname) {

    // Custom constructor to allow null  自訂建構子允許 null
    public Person(String name) {
        this(name, null);  // nickname defaults to null 預設為 null
    }

    // Custom accessor wraps value in Optional
    // 自訂存取器將值包裝成 Optional
    public Optional<String> nickname() {
        return Optional.ofNullable(nickname);
    }
}

Person p1 = new Person("Alice", "Ali");
Person p2 = new Person("Bob");           // no nickname 無暱稱

p1.nickname().ifPresent(n -> System.out.println("Nickname: " + n)); // Nickname: Ali
p2.nickname().ifPresent(n -> System.out.println("Nickname: " + n)); // (nothing printed)
```

### Using `Optional` to Safely Retrieve from a Record
### 使用 `Optional` 安全地從 Record 中取值

**English:** `Optional` is very commonly used **around** records — for example, when searching a collection of records and potentially finding nothing. [infobip](https://www.infobip.com/developers/blog/pragmatic-use-of-java-records-and-optional)

**中文（繁體）：** `Optional` 非常常用在 **record 周圍** — 例如，在搜尋 record 集合時可能找不到結果 。 [infobip](https://www.infobip.com/developers/blog/pragmatic-use-of-java-records-and-optional)

```java
record Product(String name, double price) { }

List<Product> products = List.of(
    new Product("Apple", 0.99),
    new Product("Banana", 0.49),
    new Product("Cherry", 2.99)
);

// Optional used around record retrieval — perfect fit!
// Optional 用於 record 查詢的外層 — 非常合適！
Optional<Product> found = products.stream()
    .filter(p -> p.name().equals("Banana"))
    .findFirst();

found.ifPresent(p -> System.out.println(p.name() + ": £" + p.price()));
// Banana: £0.49

double price = products.stream()
    .filter(p -> p.name().equals("Mango"))
    .findFirst()
    .map(Product::price)
    .orElse(0.0); // not found, default 0.0  找不到，預設 0.0

System.out.println(price); // 0.0
```

***

## Summary 總結

**English:** Think of `record` and `Optional` as tools for different purposes — `record` is for **grouping data together cleanly**, and `Optional` is for **expressing the absence of a value**. They work best when `Optional` wraps a **lookup result** involving records, rather than being stored inside records as fields. [stackoverflow](https://stackoverflow.com/questions/73424610/optionalt-as-a-record-parameter-in-java)

**中文（繁體）：** 將 `record` 和 `Optional` 視為服務不同目的的工具 — `record` 用於**整潔地組合資料**，而 `Optional` 用於**表達值的缺失**。當 `Optional` 包裝涉及 record 的**查詢結果**時效果最好，而不應作為欄位儲存在 record 內部 。 [stackoverflow](https://stackoverflow.com/questions/73424610/optionalt-as-a-record-parameter-in-java)

| Scenario 情境 | Recommendation 建議 |
|---|---|
| Simple immutable data container 簡單不可變資料容器 | ✅ Use `record` |
| Mutable object with setters 有 setter 的可變物件 | ✅ Use regular class 使用一般類別 |
| Object with business logic 有業務邏輯的物件 | ✅ Use regular class 使用一般類別 |
| Inheritance required 需要繼承 | ✅ Use regular class 使用一般類別 |
| Optional field in record record 中的可選欄位 | ✅ Store `null`, return `Optional` from accessor 儲存 `null`，從存取器回傳 `Optional` |
| Search result from a record collection 從 record 集合中的搜尋結果 | ✅ Wrap result in `Optional` 將結果包裝在 `Optional` 中 |