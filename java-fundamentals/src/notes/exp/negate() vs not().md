# Differences between negate() and not() methods from Predicate class

Both `negate()` and `not()` achieve the same goal — **inverting a `Predicate`** — but they differ in where they were introduced, how they are called, and most importantly, their **readability and practical use cases**.

***

## `negate()` — Instance Method

**English:** `negate()` is a **default instance method** on the `Predicate<T>` interface, introduced in **Java 8**. You call it **on an existing `Predicate` object** to return a new `Predicate` that produces the logical opposite result. [blog.csdn](https://blog.csdn.net/weixin_31451617/article/details/115071817)

**中文（繁體）：** `negate()` 是 `Predicate<T>` 介面上的一個**預設實例方法**，在 **Java 8** 中引入。你需要在**一個已存在的 `Predicate` 物件上呼叫它**，它會回傳一個產生邏輯相反結果的新 `Predicate`。

**Method signature 方法簽名：**

```java
// Instance method on Predicate  Predicate 上的實例方法
default Predicate<T> negate()
```

**Example 範例：**

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

Predicate<Integer> isEven = n -> n % 2 == 0;
Predicate<Integer> isOdd = isEven.negate(); // ← called on the instance 在實例上呼叫

List<Integer> evenNumbers = numbers.stream()
    .filter(isEven)
    .collect(Collectors.toList());
System.out.println(evenNumbers); // [2, 4, 6, 8, 10]

List<Integer> oddNumbers = numbers.stream()
    .filter(isOdd)
    .collect(Collectors.toList());
System.out.println(oddNumbers);  // [1, 3, 5, 7, 9]
```

**The key limitation 關鍵限制：** `negate()` requires you to first assign the predicate to a variable before you can negate it. You **cannot** chain it directly on a lambda:

**關鍵限制：** `negate()` 要求你先將述詞指派給一個變數才能取反。你**無法**直接在 lambda 上鏈接它：

```java
// ❌ Does NOT compile — lambda has no type yet
// 無法編譯 — lambda 尚未有型別
.filter((String s -> s.isBlank()).negate())

// ✅ Must assign first  必須先指派
Predicate<String> isBlank = String::isBlank;
.filter(isBlank.negate())
```

***

## `not()` — Static Method

**English:** `not()` is a **static method** on the `Predicate<T>` interface, introduced in **Java 11**. It was created specifically to solve the readability problem of `negate()` — you can now **wrap any predicate or method reference directly** without needing to assign it to a variable first. [tutorialspoint](https://www.tutorialspoint.com/java/java11_not_predicate.htm)

**中文（繁體）：** `not()` 是 `Predicate<T>` 介面上的一個**靜態方法**，在 **Java 11** 中引入。它的設計目的正是為了解決 `negate()` 的可讀性問題 — 你現在可以**直接包裝任何述詞或方法參考**，不需要先將其指派給變數 。 [tutorialspoint](https://www.tutorialspoint.com/java/java11_not_predicate.htm)

**Method signature 方法簽名：**

```java
// Static method on Predicate  Predicate 上的靜態方法
static <T> Predicate<T> not(Predicate<T> target)
```

**Example 範例：**

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

List<String> strings = Arrays.asList("hello", "", "world", "  ", "java");

// ✅ Works directly with method reference — clean and readable
// 直接使用方法參考 — 簡潔易讀
List<String> nonBlank = strings.stream()
    .filter(Predicate.not(String::isBlank))
    .collect(Collectors.toList());
System.out.println(nonBlank); // [hello, world, java]

// ✅ Also works with a lambda directly
// 也可以直接使用 lambda
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
List<Integer> oddNumbers = numbers.stream()
    .filter(Predicate.not(n -> n % 2 == 0))
    .collect(Collectors.toList());
System.out.println(oddNumbers); // [1, 3, 5, 7, 9]
```

***

## The Key Practical Difference 核心實際差異

The most important difference is how cleanly each method handles **method references** like `String::isBlank`:

最重要的差異在於兩種方法處理像 `String::isBlank` 這樣的**方法參考**時的簡潔程度：

```java
List<String> strings = Arrays.asList("hello", "", "  ", "world");

// ❌ With negate() — verbose, must declare variable first
// 使用 negate() — 冗長，必須先宣告變數
Predicate<String> isBlank = String::isBlank;
List<String> result1 = strings.stream()
    .filter(isBlank.negate())
    .collect(Collectors.toList());

// ✅ With not() — concise, no variable needed
// 使用 not() — 簡潔，不需要變數
List<String> result2 = strings.stream()
    .filter(Predicate.not(String::isBlank))
    .collect(Collectors.toList());

// Both produce: [hello, world]
// 兩者都產生：[hello, world]
```

***

## Comparison Table 比較表

| Feature 特點 | `negate()` | `not()` |
|---|---|---|
| Type 類型 | Instance method 實例方法 | Static method 靜態方法 | 
| Introduced 引入版本 | Java 8 | Java 11 |
| How to call 呼叫方式 | `predicate.negate()` | `Predicate.not(predicate)` |
| Works directly on method reference 可直接用於方法參考 | ❌ Requires variable first 需先宣告變數 | ✅ Yes, directly 可直接使用 |
| Works on lambda | ✅ (after assignment 指派後) | ✅ Directly 直接 |
| Readability 可讀性 | Less readable in streams 在 Stream 中較不易讀 | More readable 較易讀 |

**In summary 總結：** Both methods produce identical results. If you are on **Java 11+**, prefer `Predicate.not()` for its cleaner, more readable syntax — especially when working with method references in stream pipelines. Use `negate()` when you already have a `Predicate` stored in a variable and want to invert it inline. [blog.csdn](https://blog.csdn.net/weixin_31451617/article/details/115071817)

**總結：** 兩種方法產生相同的結果。如果你使用 **Java 11+**，建議優先使用 `Predicate.not()`，因為它的語法更簡潔易讀，特別是在 Stream 管道中使用方法參考時。當你已經有一個存在變數中的 `Predicate` 並想在行內取反時，使用 `negate()`。
