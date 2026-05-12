# the main motivation behind sealed classes is to have the possibility for a superclass to be widely accessible, but not widely extensible.

This quote from Baeldung is the philosophical heart of sealed classes. Let's break it down deeply. [baeldung](https://www.baeldung.com/java-sealed-classes-interfaces)

***

## What Does "Widely Accessible but Not Widely Extensible" Mean?「廣泛可存取但不廣泛可擴展」是什麼意思？

**English:** Before sealed classes, Java gave you only two choices when designing a superclass hierarchy: [incusdata](https://incusdata.com/blog/sealed-classes-in-java)

1. Make the class `public` → **widely accessible AND widely extensible** — anyone anywhere can subclass it
2. Make the class `final` → **accessible but NOT extensible at all** — nobody can subclass it

Neither option was ideal for domain modelling. Sealed classes introduce a **third option**: a class that is `public` (everyone can see and use it), but its allowed subclasses are **explicitly named** — no outsider can add a new subtype. [infoq](https://www.infoq.com/articles/java-sealed-classes/)

**中文（繁體）：** 在密封類別出現之前，設計超類別繼承架構時 Java 只給你兩個選擇 ： [incusdata](https://incusdata.com/blog/sealed-classes-in-java)

1. 將類別設為 `public` → **廣泛可存取且廣泛可擴展** — 任何地方的任何人都可以建立子類別
2. 將類別設為 `final` → **可存取但完全不可擴展** — 沒有人可以建立子類別

兩種選項對領域建模都不理想。密封類別引入了**第三個選項**：一個 `public` 的類別（所有人都可以看到並使用），但其允許的子類別是**明確指定的** — 外部人員無法新增新的子型別 。 [infoq](https://www.infoq.com/articles/java-sealed-classes/)

***

## The Core Problem Illustrated 核心問題圖解

**English:** Imagine you are a library author designing a `Shape` hierarchy. You want users to be able to **use** `Shape` everywhere — pass it around, use it in method parameters, store it in lists. But you **do not** want external developers creating random new shapes like `Hexagon` or `Blob` that break your library's assumptions. [infoq](https://www.infoq.com/articles/java-sealed-classes/)

**中文（繁體）：** 想像你是一個程式庫作者，正在設計 `Shape` 繼承架構。你想讓使用者能夠**使用** `Shape` — 到處傳遞它、用於方法參數、儲存在列表中。但你**不想**讓外部開發者建立隨機的新形狀，例如 `Hexagon` 或 `Blob`，破壞你程式庫的假設 。 [infoq](https://www.infoq.com/articles/java-sealed-classes/)

```java
// ❌ Option 1: public (open class) — too permissive
// 選項一：public（開放類別）— 過於寬鬆
public abstract class Shape {
    abstract double area();
}

// Problem: Anyone in the world can do this!  問題：世界上任何人都可以這樣做！
public class Blob extends Shape {        // external developer 外部開發者
    double area() { return -999; }       // completely breaks assumptions 完全破壞假設
}


// ❌ Option 2: final — too restrictive
// 選項二：final — 過於限制
public final class Shape { }  // Nobody can extend it, including YOU
                               // 沒有人能繼承它，包括你自己


// ✅ Option 3: sealed — the perfect middle ground
// 選項三：sealed — 完美的中間地帶
public sealed class Shape permits Circle, Rectangle, Triangle { }
// public = widely ACCESSIBLE — anyone can USE Shape  廣泛可存取
// permits = NOT widely EXTENSIBLE — only these 3 can EXTEND Shape  不廣泛可擴展
```

***

## Real World Example: Library Design 真實案例：程式庫設計

**English:** Consider a payment processing library. You want `Payment` to be visible to all client code — they need to accept it as method parameters, return it, and store it. But you cannot allow clients to invent new payment types that your processing engine doesn't understand. [incusdata](https://incusdata.com/blog/sealed-classes-in-java)

**中文（繁體）：** 考慮一個付款處理程式庫。你希望 `Payment` 對所有客戶端程式碼可見 — 它們需要接受它作為方法參數、回傳它並儲存它。但你不能允許客戶端發明你的處理引擎不理解的新付款類型 。 [incusdata](https://incusdata.com/blog/sealed-classes-in-java)

```java
// Library code (accessible to all, extensible only by library itself)
// 程式庫程式碼（所有人可存取，但只有程式庫本身可擴展）
public sealed interface Payment
    permits CreditCard, BankTransfer, Crypto { }

public record CreditCard(String cardNumber, double amount) implements Payment { }
public record BankTransfer(String iban,     double amount) implements Payment { }
public record Crypto(String walletId,       double amount) implements Payment { }
```

```java
// Client code — CAN use Payment widely (accessible)
// 客戶端程式碼 — 可以廣泛使用 Payment（可存取）
public class PaymentService {
    public void process(Payment payment) {  // ✅ using Payment freely 自由使用
        double fee = calculateFee(payment); // ✅ passing it around 到處傳遞
    }

    public List<Payment> getPending() {     // ✅ return type 作為回傳型別
        return List.of(new CreditCard("1234", 99.99));
    }
}

// Client code — CANNOT extend Payment (not widely extensible)
// 客戶端程式碼 — 不能擴展 Payment（不廣泛可擴展）
public class CryptoCheque implements Payment { } // ❌ COMPILE ERROR
// Error: class is not allowed to extend sealed interface Payment
// 錯誤：該類別不允許擴展密封介面 Payment
```

***

## The "Widely Accessible" Part in Detail「廣泛可存取」部分詳解

**English:** "Widely accessible" means client code can freely use the sealed type in all normal ways: [baeldung](https://www.baeldung.com/java-sealed-classes-interfaces)

**中文（繁體）：** 「廣泛可存取」意味著客戶端程式碼可以在所有正常方式中自由使用密封型別 ： [baeldung](https://www.baeldung.com/java-sealed-classes-interfaces)

```java
sealed interface Shape permits Circle, Rectangle, Triangle { }
record Circle(double radius)           implements Shape { }
record Rectangle(double w, double h)   implements Shape { }
record Triangle(double base, double h) implements Shape { }

public class DrawingApp {
    // ✅ Use as field type  用作欄位型別
    private List<Shape> shapes = new ArrayList<>();

    // ✅ Use as method parameter  用作方法參數
    public void draw(Shape shape) { }

    // ✅ Use as return type  用作回傳型別
    public Shape createDefault() { return new Circle(1.0); }

    // ✅ Use as local variable  用作區域變數
    Shape s = new Rectangle(4.0, 6.0);

    // ✅ Use in instanceof checks  用於 instanceof 檢查
    if (s instanceof Circle c) { System.out.println(c.radius()); }
}
```

## The "Not Widely Extensible" Part in Detail「不廣泛可擴展」部分詳解

**English:** "Not widely extensible" means only the **explicitly permitted subclasses** can extend it. The compiler enforces this — no workaround exists: [baeldung](https://www.baeldung.com/java-sealed-classes-interfaces)

**中文（繁體）：** 「不廣泛可擴展」意味著只有**明確允許的子類別**可以繼承它。編譯器強制執行此規則 — 沒有任何繞過的方法 ： [baeldung](https://www.baeldung.com/java-sealed-classes-interfaces)

```java
// ❌ External class attempting to extend — ALWAYS fails
// 外部類別嘗試繼承 — 永遠失敗

class Pentagon extends Shape { }     // ❌ COMPILE ERROR — not in permits list
                                     // 不在 permits 列表中
class Ellipse implements Shape { }   // ❌ COMPILE ERROR — same reason
                                     // 相同原因
```

***

## Why This Matters: Decoupling Accessibility from Extensibility 為什麼這很重要：將可存取性與可擴展性解耦

**English:** The key insight from the quote is that before sealed classes, **accessibility and extensibility were tied together**. If you made something `public`, you had no way to prevent others from extending it (unless you used `final`, which was all-or-nothing). Sealed classes **decouple these two concerns** entirely: [infoq](https://www.infoq.com/articles/java-sealed-classes/)

**中文（繁體）：** 引言中的關鍵洞見是，在密封類別出現之前，**可存取性和可擴展性是綁定在一起的**。如果你將某些東西設為 `public`，你無法阻止其他人繼承它（除非使用 `final`，但那是全有或全無）。密封類別完全將這兩個關注點**解耦** ： [infoq](https://www.infoq.com/articles/java-sealed-classes/)

```
Before 之前:  public  =  accessible + extensible  (tied together 綁定)
              final   =  accessible + NOT extensible (all-or-nothing 全有或全無)

After 之後:   sealed  =  accessible  ← controlled separately 獨立控制
                      +  extensible  ← controlled separately 獨立控制
```

***

## Compiler Exhaustiveness: The Bonus Benefit 編譯器完整性檢查：額外的好處

**English:** Because the compiler knows every possible subtype at compile time (thanks to the `permits` clause), it can enforce **exhaustive pattern matching** — a direct benefit of "not widely extensible": [infoq](https://www.infoq.com/articles/java-sealed-classes/)

**中文（繁體）：** 因為編譯器在編譯時就知道每個可能的子型別（多虧了 `permits` 子句），它可以強制執行**完整的模式比對** — 這是「不廣泛可擴展」的直接好處 ： [infoq](https://www.infoq.com/articles/java-sealed-classes/)

```java
double area(Shape shape) {
    return switch (shape) {
        case Circle c    -> Math.PI * c.radius() * c.radius();
        case Rectangle r -> r.w() * r.h();
        case Triangle t  -> 0.5 * t.base() * t.h();
        // No default needed — compiler KNOWS these are all possible shapes
        // 不需要 default — 編譯器知道這些是所有可能的形狀
        // If a new Shape is added to permits, compiler FORCES you to handle it
        // 若在 permits 中新增形狀，編譯器強制你處理它
    };
}
```

**English:** If you added `record Hexagon(double side) implements Shape { }` to the `permits` clause later and forgot to add a `case Hexagon` in the `switch`, the compiler would give you an error immediately — **the type hierarchy documents and enforces itself**. [incusdata](https://incusdata.com/blog/sealed-classes-in-java)

**中文（繁體）：** 如果你後來在 `permits` 子句中新增了 `record Hexagon(double side) implements Shape { }`，但忘記在 `switch` 中新增 `case Hexagon`，編譯器會立即報錯 — **型別繼承架構自我文件化並自我強制執行** 。 [incusdata](https://incusdata.com/blog/sealed-classes-in-java)

***

## Summary: The Three-Way Comparison 總結：三方比較

| | `public` class 開放類別 | `final` class | `sealed` class 密封類別 |
|---|---|---|---|
| Widely accessible 廣泛可存取 | ✅ Yes | ✅ Yes | ✅ Yes |
| Widely extensible 廣泛可擴展 | ✅ Yes (problem ⚠️) | ❌ Not at all | ❌ Only permitted subtypes 只有允許子型別 |
| Author controls subtypes 作者控制子型別 | ❌ No | ✅ (trivially — none allowed) | ✅ Explicitly named 明確命名 |
| Exhaustive switch 完整 switch | ❌ Unknown subtypes 子型別未知 | ✅ (only self) | ✅ All subtypes known 所有子型別已知 |
| Best for 最適合 | Open APIs | Utility/value classes | Domain models, library types |

The quote elegantly captures sealed classes' core value: they solve the long-standing tension in object-oriented design between **openness for use** and **control over evolution** — letting you publish a stable, visible abstraction while keeping the implementation space firmly under your control. [infoq](https://www.infoq.com/articles/java-sealed-classes/)

這句引言優雅地捕捉了密封類別的核心價值：它們解決了物件導向設計中長期存在的**開放使用**與**演進控制**之間的矛盾 — 讓你發佈穩定、可見的抽象，同時牢牢掌控實作空間 。 [infoq](https://www.infoq.com/articles/java-sealed-classes/)