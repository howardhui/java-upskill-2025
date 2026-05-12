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

## The Three Permitted Subclass Modifiers 三種允許子類別的修飾符

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

## The Real Power: Exhaustive Pattern Matching (Java 17+) 真正的威力：完整的模式比對（Java 17+）

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

## Comparison: `final` vs `sealed` vs Open Class 比較：`final` vs `sealed` vs 開放類別

| Feature 特點 | `final` class | `sealed` class | Open class 開放類別 |
|---|---|---|---|
| Can be extended? 可繼承？ | ❌ None 不可 | ✅ Only listed subclasses 只有列出的子類別 | ✅ Any class 任何類別 |
| Subclass control 子類別控制 | Total 完全 | Fine-grained 細粒度 | None 無 |
| Exhaustive switch 完整 switch | ✅ (only self) | ✅ All subtypes known 所有子型別已知 | ❌ Unknown subclasses 子類別未知 |
| Use case 使用情境 | Utility / value classes | Domain models, ADTs | General-purpose APIs |

Sealed classes, records, and pattern matching together form Java's answer to **Algebraic Data Types (ADTs)** — a powerful pattern from functional programming that allows safe, exhaustive modelling of a fixed set of variants. They are particularly powerful together as a trio. [infoq](https://www.infoq.com/articles/java-sealed-classes/)

密封類別、record 和模式比對共同構成了 Java 對**代數資料型別（ADT）**的回應 — 這是函數式程式設計中的一個強大模式，允許對固定的變體集合進行安全、完整的建模。三者結合使用特別強大 。 [infoq](https://www.infoq.com/articles/java-sealed-classes/)