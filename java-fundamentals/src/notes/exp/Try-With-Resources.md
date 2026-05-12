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