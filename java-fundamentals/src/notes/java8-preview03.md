# What is Try-With-Resources? 什麼是 Try-With-Resources？

**English:** Introduced in **Java 7**, try-with-resources automatically closes any resource that implements `java.lang.AutoCloseable` when the `try` block finishes — whether normally or due to an exception. This eliminated the need for verbose `finally` blocks to close resources manually. [runoob](https://www.runoob.com/java/java9-try-with-resources-improvement.html)

**中文（繁體）：** 在 **Java 7** 中引入，try-with-resources 會在 `try` 區塊結束時（無論正常結束或發生例外），自動關閉任何實作 `java.lang.AutoCloseable` 介面的資源，消除了手動在 `finally` 區塊中關閉資源的需要 。 [runoob](https://www.runoob.com/java/java9-try-with-resources-improvement.html)

***

## Java 7 / 8: The Original Syntax 原始語法

**English:** In Java 7 and 8, every resource **must be declared and initialised inside the `try(...)` parentheses**. You could not reuse a variable declared outside. [juejin](https://juejin.cn/post/6844903565589217293)

**中文（繁體）：** 在 Java 7 和 8 中，每個資源**必須在 `try(...)` 括號內宣告並初始化**。你無法重複使用在外部宣告的變數 。 [juejin](https://juejin.cn/post/6844903565589217293)

```java
// ✅ Java 7/8 — resource must be declared inside try()
// Java 7/8 — 資源必須在 try() 內宣告
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    System.out.println(reader.readLine());
} catch (IOException e) {
    e.printStackTrace();
}
// reader.close() is called automatically
// reader.close() 自動被呼叫
```

**The problem with Java 7/8 — redundant variables 問題：產生冗餘變數**

If you already had a resource object declared outside the `try` block, you were **forced to create a second variable** inside `try()` just to satisfy the compiler: [waylau](https://waylau.com/concise-try-with-resources-jdk9/)

如果你已經在 `try` 區塊外宣告了一個資源物件，你被**迫在 `try()` 內再建立第二個變數**才能讓編譯器接受 ： [waylau](https://waylau.com/concise-try-with-resources-jdk9/)

```java
// ❌ Problematic pattern in Java 7/8
// Java 7/8 中的問題模式
BufferedReader reader = new BufferedReader(new FileReader("file.txt"));

// Must redeclare as r — redundant!
// 必須重新宣告為 r — 冗餘！
try (BufferedReader r = reader) {
    System.out.println(r.readLine());
} catch (IOException e) {
    e.printStackTrace();
}
```

***

## Java 9: The Key Improvement — Effectively Final Variables 關鍵改進 — 有效最終變數

**English:** Java 9 allows you to use a variable declared **outside** the `try()` block directly inside it, as long as that variable is **`final` or effectively final** (i.e., never reassigned after initialisation). You no longer need to create a redundant second variable. [blog.csdn](https://blog.csdn.net/LogicWander/article/details/155277384)

**中文（繁體）：** Java 9 允許你將在 **`try()` 區塊外部**宣告的變數直接放入其中使用，只要該變數是 **`final` 或有效最終（effectively final）**（即初始化後從未重新指派）。你不再需要建立冗餘的第二個變數 。 [blog.csdn](https://blog.csdn.net/LogicWander/article/details/155277384)

```java
// ✅ Java 9 — use existing variable directly in try()
// Java 9 — 直接在 try() 中使用已存在的變數

// final variable  final 變數
final BufferedReader reader1 = new BufferedReader(new FileReader("file1.txt"));

// effectively final (never reassigned)  有效最終（從未重新指派）
BufferedReader reader2 = new BufferedReader(new FileReader("file2.txt"));

try (reader1; reader2) {  // ← no new variables needed! 不需要新變數！
    System.out.println(reader1.readLine());
    System.out.println(reader2.readLine());
} catch (IOException e) {
    e.printStackTrace();
}
// Both reader1 and reader2 are closed automatically
// reader1 和 reader2 都會自動被關閉
```

**English:** Note the clean `try (reader1; reader2)` syntax — multiple resources are separated by semicolons, just like in Java 7/8, but without redundant redeclarations. [segmentfault](https://segmentfault.com/a/1190000037478565)

**中文（繁體）：** 注意簡潔的 `try (reader1; reader2)` 語法 — 多個資源以分號分隔，和 Java 7/8 相同，但不再有冗餘的重複宣告 。 [segmentfault](https://segmentfault.com/a/1190000037478565)

***

## What "Effectively Final" Means 什麼是「有效最終（Effectively Final）」

**English:** A variable is **effectively final** if it is never reassigned after its initial assignment — even without the `final` keyword. If you try to reassign the variable and then use it in `try()`, the compiler will reject it. [volcengine](https://www.volcengine.com/article/156113)

**中文（繁體）：** 一個變數若在初始指派後**從未被重新指派**，即使沒有 `final` 關鍵字，它也是**有效最終**的。如果你嘗試重新指派該變數後再將其用於 `try()`，編譯器會拒絕 。 [volcengine](https://www.volcengine.com/article/156113)

```java
BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
reader = new BufferedReader(new FileReader("other.txt")); // reassigned! 重新指派！

try (reader) { // ❌ COMPILE ERROR — reader is no longer effectively final
               // 編譯錯誤 — reader 不再是有效最終
    System.out.println(reader.readLine());
}
```

***

## Full Before/After Comparison 完整前後比較

```java
// ============ JAVA 7/8 ============
Resource resource1 = new Resource("r1");
Resource resource2 = new Resource("r2");

// Must redeclare both inside try() — redundant variables r1, r2
// 必須在 try() 內重新宣告 — 冗餘變數 r1, r2
try (Resource r1 = resource1;
     Resource r2 = resource2) {
    r1.use();
    r2.use();
}


// ============ JAVA 9 ============
Resource resource1 = new Resource("r1");  // effectively final 有效最終
Resource resource2 = new Resource("r2");  // effectively final 有效最終

// Use existing variables directly — no redundancy!
// 直接使用現有變數 — 不再冗餘！
try (resource1; resource2) {
    resource1.use();
    resource2.use();
}
```

***

## Summary Table 總結表

| Feature 特點 | Java 7 / 8 | Java 9+ |
|---|---|---|
| Resource declaration 資源宣告 | Must be inside `try()` 必須在 `try()` 內 | Can be outside `try()` 可在 `try()` 外 |
| Variable requirement 變數要求 | New variable required 需要新變數 | `final` or effectively final 即可 |
| Multiple resources 多個資源 | Separated by `;` 以 `;` 分隔 | Same, but reuse existing 相同，但可重複使用現有變數 |
| Redundant variables 冗餘變數 | ❌ Often required 常常需要 | ✅ No longer needed 不再需要 |
| Code verbosity 程式碼冗長性 | Higher 較高 | Lower 較低 |

The Java 9 enhancement is part of **Project Coin (Milling Project Coin)** — a collection of small but meaningful language refinements. It makes try-with-resources more flexible and eliminates unnecessary boilerplate without changing the core auto-close behaviour. [juejin](https://juejin.cn/post/6844903565589217293)

Java 9 的改進是 **Project Coin（Milling Project Coin）** 的一部分 — 一系列小而有意義的語言改進。它讓 try-with-resources 更靈活，消除不必要的樣板程式碼，同時不改變核心的自動關閉行為 。 [juejin](https://juejin.cn/post/6844903565589217293)

---
---

# What is the Diamond Operator? 背景：什麼是鑽石運算子？

**English:** The diamond operator `<>` was introduced in **Java 7** to reduce verbosity when working with generics. Before Java 7, you had to specify the generic type on **both sides** of an assignment. Java 7 allowed the compiler to **infer** the type on the right side automatically. [baeldung](https://www.baeldung.com/java-diamond-operator)

**中文（繁體）：** 鑽石運算子 `<>` 在 **Java 7** 中引入，用於減少使用泛型時的冗長程式碼。在 Java 7 之前，你必須在指派的**兩側**都指定泛型型別。Java 7 讓編譯器能自動**推斷**右側的型別 。 [baeldung](https://www.baeldung.com/java-diamond-operator)

```java
// Before Java 7  Java 7 之前
List<String> list = new ArrayList<String>(); // type repeated twice 型別重複兩次

// Java 7 onwards — diamond operator infers type
// Java 7 起 — 鑽石運算子推斷型別
List<String> list = new ArrayList<>(); // ✅ clean! 簡潔！
```

***

## The Java 7/8 Limitation: Anonymous Inner Classes 匿名內部類別

**English:** In Java 7 and 8, the diamond operator worked for regular class instantiation but was **not allowed with anonymous inner classes**. The compiler could not infer the type in that context and would throw a compile error. [beginnersbook](https://beginnersbook.com/2018/05/java-9-anonymous-inner-classes-and-diamond-operator/)

**中文（繁體）：** 在 Java 7 和 8 中，鑽石運算子可用於一般類別的實例化，但**不允許用於匿名內部類別**。編譯器在這種情況下無法推斷型別，會拋出編譯錯誤 。 [beginnersbook](https://beginnersbook.com/2018/05/java-9-anonymous-inner-classes-and-diamond-operator/)

```java
abstract class MyClass<T> {
    abstract T process(T input);
}

// ❌ Java 7/8 — COMPILE ERROR with anonymous inner class
// Java 7/8 — 匿名內部類別會編譯錯誤
MyClass<Integer> obj = new MyClass<>() {
    @Override
    Integer process(Integer input) { return input * 2; }
};
// Error: cannot infer type arguments for MyClass
// 錯誤：無法推斷 MyClass 的型別引數

// ✅ Java 7/8 — must explicitly specify the type
// Java 7/8 — 必須明確指定型別
MyClass<Integer> obj = new MyClass<Integer>() {
    @Override
    Integer process(Integer input) { return input * 2; }
};
```

***

## Java 9: Diamond Operator Extended to Anonymous Inner Classes 鑽石運算子擴展至匿名內部類別

**English:** Java 9 removed this restriction. The diamond operator `<>` can now be used with **anonymous inner classes** as long as the inferred type is **denotable** (i.e., expressible as a named type). The compiler is now smart enough to infer the type from the left-hand side declaration. [geeksforgeeks](https://www.geeksforgeeks.org/java/diamond-operator-for-anonymous-inner-class-with-examples-in-java/)

**中文（繁體）：** Java 9 移除了這個限制。鑽石運算子 `<>` 現在可以與**匿名內部類別**一起使用，只要推斷的型別是**可命名的**（即可以用具名型別表達）。編譯器現在夠聰明，可以從左側的宣告推斷型別 。 [geeksforgeeks](https://www.geeksforgeeks.org/java/diamond-operator-for-anonymous-inner-class-with-examples-in-java/)

```java
abstract class MyClass<T> {
    abstract T process(T input);
}

// ✅ Java 9 — diamond operator works with anonymous inner class!
// Java 9 — 鑽石運算子可用於匿名內部類別！
MyClass<Integer> obj = new MyClass<>() {
    @Override
    Integer process(Integer input) { return input * 2; }
};

System.out.println(obj.process(5)); // 10
```

***

## Full Practical Example 完整實際範例

**English:** Below is a realistic example using a generic `Handler` abstract class, demonstrating the three common type usages now all supported with `<>` in Java 9. [tutorialspoint](https://www.tutorialspoint.com/java/java_inner_class_diamond_operator.htm)

**中文（繁體）：** 以下是一個使用泛型 `Handler` 抽象類別的實際範例，展示 Java 9 中 `<>` 現在支援的三種常見型別用法 。 [tutorialspoint](https://www.tutorialspoint.com/java/java_inner_class_diamond_operator.htm)

```java
abstract class Handler<T> {
    public T content;
    public Handler(T content) { this.content = content; }
    abstract void handle();
}

public class Main {
    public static void main(String[] args) {

        // 1. Concrete type  具體型別
        Handler<Integer> h1 = new Handler<>(42) {
            @Override
            public void handle() {
                System.out.println("Integer: " + content); // Integer: 42
            }
        };

        // 2. Bounded wildcard  有界萬用字元
        Handler<? extends Number> h2 = new Handler<>(3.14) {
            @Override
            public void handle() {
                System.out.println("Number: " + content); // Number: 3.14
            }
        };

        // 3. Unbounded wildcard  無界萬用字元
        Handler<?> h3 = new Handler<>("Hello") {
            @Override
            public void handle() {
                System.out.println("Any: " + content); // Any: Hello
            }
        };

        h1.handle();
        h2.handle();
        h3.handle();
    }
}
```

***

## The One Restriction: Non-Denotable Types 唯一限制：不可命名型別

**English:** The diamond operator still **cannot** be used when the inferred type would be **non-denotable** — a type that cannot be expressed as a named type, such as an **intersection type** or a **captured wildcard**. In such cases, you must still specify the type explicitly. [baeldung](https://www.baeldung.com/new-java-9)

**中文（繁體）：** 當推斷的型別是**不可命名型別**（無法用具名型別表達的型別，例如**交集型別**或**捕獲萬用字元**）時，鑽石運算子仍然**不能**使用。在這種情況下，你仍必須明確指定型別 。 [baeldung](https://www.baeldung.com/new-java-9)

```java
// ❌ Still not allowed — inferred type would be non-denotable
// 仍不允許 — 推斷的型別是不可命名型別
var obj = new MyClass<>() {
    // If the anonymous class introduces a new member not in MyClass,
    // the inferred type becomes non-denotable
};
```

***

## Summary Table 總結表

| Feature 特點 | Java 7 | Java 8 | Java 9+ |
|---|---|---|---|
| `<>` with regular classes 用於一般類別 | ✅ | ✅ | ✅ |
| `<>` with anonymous inner classes 用於匿名內部類別 | ❌ | ❌ | ✅ |
| Type explicitly required for anon class 匿名類別需明確指定型別 | ✅ Required 必須 | ✅ Required 必須 | ❌ Not needed 不需要 |
| Bounded wildcard `<? extends T>` with anon class | ❌ | ❌ | ✅ |
| Non-denotable type inference 不可命名型別推斷 | ❌ | ❌ | ❌ Still not supported 仍不支援 |

The Java 9 enhancement is a small but welcome quality-of-life improvement — it removes an inconsistency in the language and makes anonymous inner class syntax consistent with regular class instantiation, reducing unnecessary boilerplate. [beginnersbook](https://beginnersbook.com/2018/05/java-9-anonymous-inner-classes-and-diamond-operator/)

Java 9 的改進雖小，卻是實用的改善 — 它消除了語言中的一個不一致性，使匿名內部類別的語法與一般類別實例化保持一致，減少不必要的樣板程式碼 。 [beginnersbook](https://beginnersbook.com/2018/05/java-9-anonymous-inner-classes-and-diamond-operator/)

---
---

# The Evolution of Interface Methods 介面方法的演進

**English:** To understand Java 9's addition, it helps to see how interface capabilities grew across versions. Each Java version added more power to interfaces. [geeksforgeeks](https://www.geeksforgeeks.org/java/private-methods-java-9-interfaces/)

**中文（繁體）：** 要理解 Java 9 的新增功能，了解介面能力如何跨版本增長很有幫助 。 [geeksforgeeks](https://www.geeksforgeeks.org/java/private-methods-java-9-interfaces/)

| Version 版本 | What interfaces can contain 介面可包含的內容 |
|---|---|
| Java 7 and before 及之前 | `abstract` methods + constants only 抽象方法與常數 |
| Java 8 | + `default` methods + `static` methods |
| Java 9 | + `private` methods + `private static` methods |

***

## The Problem Before Java 9 [Java 9 之前的問題]

**English:** In Java 8, `default` and `static` methods were introduced to allow concrete logic inside interfaces. However, if two `default` methods shared common logic, that logic had to be **duplicated** or exposed as another `public` method — breaking encapsulation. [beginnersbook](https://beginnersbook.com/2018/05/java-9-private-methods-in-interfaces-with-examples/)

**中文（繁體）：** Java 8 引入了 `default` 和 `static` 方法，允許在介面中放入具體邏輯。然而，若兩個 `default` 方法共享相同邏輯，該邏輯必須**重複撰寫**或暴露為另一個 `public` 方法 — 破壞了封裝性 。 [beginnersbook](https://beginnersbook.com/2018/05/java-9-private-methods-in-interfaces-with-examples/)

```java
// ❌ Java 8 — code duplication in default methods
// Java 8 — default 方法中的程式碼重複
interface MyInterfaceJava8 {
    default void method1() {
        System.out.println("Starting method");  // duplicated! 重複！
        System.out.println("Doing something");  // duplicated! 重複！
        System.out.println("This is method1");
    }
    default void method2() {
        System.out.println("Starting method");  // duplicated! 重複！
        System.out.println("Doing something");  // duplicated! 重複！
        System.out.println("This is method2");
    }
}
```

***

## Java 9: Private Methods in Interfaces 介面中的私有方法

**English:** Java 9 introduced **`private`** and **`private static`** methods in interfaces. These methods can only be called from within the interface itself — they are completely invisible to implementing classes and sub-interfaces. This enables true **code reuse and encapsulation** inside an interface. [geeksforgeeks](https://www.geeksforgeeks.org/java/private-methods-java-9-interfaces/)

**中文（繁體）：** Java 9 在介面中引入了 **`private`** 和 **`private static`** 方法。這些方法只能在介面內部被呼叫 — 對實作類別和子介面完全不可見 。這實現了介面內部真正的**程式碼重用與封裝**。 [geeksforgeeks](https://www.geeksforgeeks.org/java/private-methods-java-9-interfaces/)

```java
// ✅ Java 9 — shared logic extracted into private method
// Java 9 — 共用邏輯提取到 private 方法
interface MyInterfaceJava9 {
    default void method1() {
        printLines();                   // call private method 呼叫私有方法
        System.out.println("This is method1");
    }
    default void method2() {
        printLines();                   // reuse — no duplication! 重用，不重複！
        System.out.println("This is method2");
    }

    private void printLines() {         // private method 私有方法
        System.out.println("Starting method");
        System.out.println("Doing something");
    }
}

class MyClass implements MyInterfaceJava9 {
    public static void main(String[] args) {
        MyClass obj = new MyClass();
        obj.method1();
        obj.method2();
        // obj.printLines(); // ❌ COMPILE ERROR — private, not accessible
                             // 編譯錯誤 — 私有方法，無法存取
    }
}
```

**Output 輸出：**
```
Starting method
Doing something
This is method1
Starting method
Doing something
This is method2
```

***

## Both Variants: `private` and `private static` 兩種形式：`private` 與 `private static`

**English:** Java 9 supports two kinds of private methods in interfaces. The key rule is that a **`private static` method can be called from both `static` and `default` methods**, while a **`private` (non-static) method can only be called from `default` methods** — not from `static` methods. [blog.joda](https://blog.joda.org/2016/09/private-methods-in-interfaces-in-java-9.html)

**中文（繁體）：** Java 9 支援兩種介面私有方法。關鍵規則是：**`private static` 方法可以從 `static` 和 `default` 方法中呼叫**，而 **`private`（非靜態）方法只能從 `default` 方法呼叫** — 不能從 `static` 方法呼叫 。 [blog.joda](https://blog.joda.org/2016/09/private-methods-in-interfaces-in-java-9.html)

```java
interface CustomInterface {

    // Abstract method  抽象方法
    public abstract void method1();

    // Default method calling both private methods
    // Default 方法呼叫兩種 private 方法
    public default void method2() {
        method4();  // ✅ private instance method inside default 在 default 內呼叫私有實例方法
        method5();  // ✅ private static method inside non-static 在非靜態方法內呼叫私有靜態方法
        System.out.println("default method");
    }

    // Static method calling only private static method
    // Static 方法只能呼叫 private static 方法
    public static void method3() {
        method5();  // ✅ private static inside static 在靜態方法內呼叫私有靜態方法
        // method4(); // ❌ COMPILE ERROR — cannot call non-static from static
                      // 編譯錯誤 — 不能從靜態方法呼叫非靜態方法
        System.out.println("static method");
    }

    // Private instance method  私有實例方法
    private void method4() {
        System.out.println("private method");
    }

    // Private static method  私有靜態方法
    private static void method5() {
        System.out.println("private static method");
    }
}

class CustomClass implements CustomInterface {
    @Override
    public void method1() {
        System.out.println("abstract method");
    }

    public static void main(String[] args) {
        CustomInterface instance = new CustomClass();
        instance.method1();        // abstract method
        instance.method2();        // default method (+ private methods inside)
        CustomInterface.method3(); // static method
    }
}
```

**Output 輸出：**
```
abstract method
private method
private static method
default method
private static method
static method
```

***

## Rules Summary 規則總結

- `private` methods **must have a body** — `private abstract` is a compile error 私有方法**必須有方法體** — `private abstract` 會編譯錯誤
- `private default` is also a compile error — use just `private` `private default` 也是編譯錯誤 — 只用 `private` 即可
- `private` instance methods can only be called from **`default` methods** 私有實例方法只能從 **`default` 方法**呼叫
- `private static` methods can be called from **both `default` and `static` methods** 私有靜態方法可從 **`default` 和 `static` 方法**呼叫
- Private methods are **not inherited** by implementing classes or sub-interfaces 私有方法**不會被**實作類別或子介面繼承

***

## Valid Method Modifier Combinations 有效修飾符組合

| Modifier 修飾符 | Valid? 有效？ | Notes 說明 |
|---|---|---|
| `public abstract` | ✅ | Traditional abstract method 傳統抽象方法 |
| `public default` | ✅ | Java 8+ concrete instance method Java 8+ 具體實例方法 |
| `public static` | ✅ | Java 8+ static method Java 8+ 靜態方法 |
| `private` | ✅ | Java 9+ private instance method Java 9+ 私有實例方法 |
| `private static` | ✅ | Java 9+ private static method Java 9+ 私有靜態方法 |
| `private abstract` | ❌ | Compile error 編譯錯誤 |
| `private default` | ❌ | Compile error 編譯錯誤 |

The core purpose of this feature is **encapsulation within the interface itself** — shared logic between `default` or `static` methods can be hidden away in private helpers, preventing implementing classes from seeing or accidentally overriding internal implementation details. [baeldung](https://www.baeldung.com/java-interface-private-methods)

此功能的核心目的是**介面本身的封裝** — `default` 或 `static` 方法之間的共用邏輯可以隱藏在私有輔助方法中，防止實作類別看到或意外覆寫內部實作細節 。 [baeldung](https://www.baeldung.com/java-interface-private-methods)

---
---

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
