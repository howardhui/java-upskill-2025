# New methods of String class introduced in **Java 11**

## `isBlank()`

**English:** Returns `true` if the string is **empty or contains only whitespace characters**. Unlike `isEmpty()`, it also returns `true` for strings that contain only spaces, tabs, or newlines. [websparrow](https://websparrow.org/java/java-11-new-methods-of-string-class)

**中文（繁體）：** 如果字串是**空的，或只包含空白字元**（空格、Tab、換行），則回傳 `true`。與 `isEmpty()` 不同，`isBlank()` 對只含空白的字串也會回傳 `true`。

```java
System.out.println("".isBlank());        // true  ← empty string 空字串
System.out.println("   ".isBlank());     // true  ← only spaces 只有空格
System.out.println("\t\n".isBlank());    // true  ← tabs and newline Tab與換行
System.out.println(" Hello ".isBlank()); // false ← has content 有內容
```

***

## `lines()`

**English:** Splits the string by **line terminators** (`\n`, `\r`, `\r\n`) and returns a `Stream<String>`. Useful for processing multi-line text line by line. [cloudhadoop](https://www.cloudhadoop.com/java11-string-methods)

**中文（繁體）：** 根據**換行符號**（`\n`、`\r`、`\r\n`）將字串分割，並回傳一個 `Stream<String>`。適合用來逐行處理多行文字。

```java
String text = "Line 1\nLine 2\nLine 3";

text.lines()
    .forEach(System.out::println);
// Output 輸出:
// Line 1
// Line 2
// Line 3

long count = text.lines().count();
System.out.println(count); // 3
```

***

## `strip()`

**English:** Removes **all leading and trailing whitespace** from a string. It is the **Unicode-aware** upgrade to the old `trim()` method — it recognises Unicode whitespace characters (like `\u2005`) that `trim()` does not handle. [websparrow](https://websparrow.org/java/java-11-new-methods-of-string-class)

**中文（繁體）：** 移除字串**前後兩端所有的空白字元**。這是舊版 `trim()` 的**升級版**，支援 Unicode 空白字元（如 `\u2005`），而 `trim()` 無法處理這些字元。

```java
String str = "\t  Hello World  \u2005";

System.out.println(str.trim());   // "Hello World  \u2005" ← trim() misses Unicode space
                                  // trim() 無法處理 Unicode 空白
System.out.println(str.strip());  // "Hello World"         ← strip() handles it correctly
                                  // strip() 正確處理
```

***

## `stripLeading()`

**English:** Removes whitespace from the **beginning (left side) only**, leaving trailing whitespace untouched. [javadevcentral](https://javadevcentral.com/new-methods-in-the-string-class-java-11-12/)

**中文（繁體）：** 只移除字串**開頭（左側）的空白字元**，尾端的空白保持不變。

```java
String str = "   Hello World   ";

System.out.println(str.stripLeading());
// Output 輸出: "Hello World   "
//               ↑ leading spaces removed, trailing spaces remain
//               開頭空格已移除，尾端空格保留
```

***

## `stripTrailing()`

**English:** Removes whitespace from the **end (right side) only**, leaving leading whitespace untouched. [javadevcentral](https://javadevcentral.com/new-methods-in-the-string-class-java-11-12/)

**中文（繁體）：** 只移除字串**尾端（右側）的空白字元**，開頭的空白保持不變。

```java
String str = "   Hello World   ";

System.out.println(str.stripTrailing());
// Output 輸出: "   Hello World"
//               ↑ leading spaces remain, trailing spaces removed
//               開頭空格保留，尾端空格已移除
```

***

## `repeat(int count)`

**English:** Returns a new string that is the original string **repeated `count` times**. Throws `IllegalArgumentException` if `count` is negative. [cloudhadoop](https://www.cloudhadoop.com/java11-string-methods)

**中文（繁體）：** 回傳一個將原字串**重複 `count` 次**的新字串。若 `count` 為負數，則拋出 `IllegalArgumentException`。

```java
System.out.println("Ha".repeat(3));    // "HaHaHa"
System.out.println("=-".repeat(5));    // "=-=-=-=-=-"
System.out.println("Java".repeat(0));  // ""  ← empty string 空字串
System.out.println("Hi".repeat(-1));   // ❌ IllegalArgumentException
```

***

## Quick Reference 快速參考

| Method 方法 | Action 作用 | Returns 回傳型別 |
|---|---|---|
| `isBlank()` | Check if empty or whitespace only 檢查是否為空或只含空白 | `boolean` |
| `lines()` | Split by line breaks 按換行分割 | `Stream<String>` |
| `strip()` | Remove both-side whitespace (Unicode-aware) 移除兩端空白（支援 Unicode） | `String` |
| `stripLeading()` | Remove leading whitespace only 只移除開頭空白 | `String` |
| `stripTrailing()` | Remove trailing whitespace only 只移除尾端空白 | `String` |
| `repeat(n)` | Repeat string n times 重複字串 n 次 | `String` |

---
---

#readString(), writeString() method from Files Class

Both `Files.readString()` and `Files.writeString()` were introduced in **Java 11** as part of the `java.nio.file.Files` class. They dramatically simplify file I/O by eliminating the need for `BufferedReader`, `BufferedWriter`, or manual byte conversion. [javasleep](https://javasleep.com/lesson-14-new-file-methods-in-java-11-readstring-writestring/)

***

## `Files.readString()`

**English:** Reads the **entire content of a file** into a `String` in one single call. Before Java 11, you needed multiple lines of `BufferedReader` or `new String(Files.readAllBytes(...))` to achieve the same result. It uses **UTF-8** encoding by default, but a second overload accepts a custom `Charset`. [cnblogs](https://www.cnblogs.com/learnfk/p/14763644.html)

**中文（繁體）：** 將**整個檔案的內容**一次性讀取成一個 `String`。在 Java 11 之前，你需要使用 `BufferedReader` 或 `new String(Files.readAllBytes(...))` 才能達到相同效果。預設使用 **UTF-8** 編碼，但也有第二個多載方法可以指定字元集 。 [cnblogs](https://www.cnblogs.com/learnfk/p/14763644.html)

**Method signatures 方法簽名：**

```java
// Default UTF-8 encoding  預設 UTF-8 編碼
public static String readString(Path path) throws IOException

// Custom charset  自訂字元集
public static String readString(Path path, Charset cs) throws IOException
```

**Example 範例：**

```java
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

// Read with default UTF-8  使用預設 UTF-8 讀取
Path path = Path.of("example.txt");
String content = Files.readString(path);
System.out.println(content); // Prints entire file content 印出整個檔案內容

// Read with specific charset  使用指定字元集讀取
String contentISO = Files.readString(path, StandardCharsets.ISO_8859_1);
```

**Before Java 11 vs After 與 Java 11 前的比較：**

```java
// ❌ Before Java 11 — verbose  Java 11 前：冗長
BufferedReader reader = new BufferedReader(new FileReader("example.txt"));
StringBuilder sb = new StringBuilder();
String line;
while ((line = reader.readLine()) != null) sb.append(line).append("\n");
reader.close();
String content = sb.toString();

// ✅ After Java 11 — clean  Java 11 後：簡潔
String content = Files.readString(Path.of("example.txt"));
```

**⚠️ Important 注意：** If the file does not exist, `readString()` throws `NoSuchFileException`. It is also not recommended for **very large files** as the entire content is loaded into memory at once, which may cause `OutOfMemoryError`. [cnblogs](https://www.cnblogs.com/learnfk/p/14763644.html)

**⚠️ 重要：** 若檔案不存在，`readString()` 會拋出 `NoSuchFileException`。也不建議用於**超大型檔案**，因為整個內容會一次載入記憶體，可能導致 `OutOfMemoryError`。

***

## `Files.writeString()`

**English:** Writes a `String` directly to a file in one call. If the file does **not exist**, it is **created automatically**. If the file already exists, its content is **overwritten by default**. Like `readString()`, it uses **UTF-8** by default and accepts optional `OpenOption` flags to control write behaviour. [howtodoinjava](https://howtodoinjava.com/java11/write-string-to-file/)

**中文（繁體）：** 一次呼叫即可將 `String` 直接寫入檔案。若檔案**不存在**，會**自動建立**。若檔案已存在，預設會**覆蓋**其內容。和 `readString()` 一樣預設使用 **UTF-8**，並接受可選的 `OpenOption` 參數來控制寫入行為 。 [howtodoinjava](https://howtodoinjava.com/java11/write-string-to-file/)

**Method signatures 方法簽名：**

```java
// Default UTF-8, overwrites file  預設 UTF-8，覆蓋檔案
public static Path writeString(Path path, CharSequence csq, OpenOption... options) throws IOException

// Custom charset  自訂字元集
public static Path writeString(Path path, CharSequence csq, Charset cs, OpenOption... options) throws IOException
```

Note it returns the `Path` of the written file, which allows **method chaining** 注意它回傳寫入檔案的 `Path`，可用於**方法鏈接**。

**Example 範例：**

```java
import java.nio.file.*;

Path path = Path.of("example.txt");

// Write (overwrites existing content)  寫入（覆蓋現有內容）
Files.writeString(path, "Hello, Java 11!");

// Append to existing file  附加到現有檔案
Files.writeString(path, "\nSecond line", StandardOpenOption.APPEND);

// Verify by reading back  讀回驗證
String content = Files.readString(path);
System.out.println(content);
// Hello, Java 11!
// Second line
```

**Method chaining example 方法鏈接範例：**

```java
// writeString() returns Path, so you can chain readString() directly
// writeString() 回傳 Path，可直接鏈接 readString()
String result = Files.readString(
    Files.writeString(Path.of("hello.txt"), "Hello World")
);
System.out.println(result); // Hello World
```

***

## Comparison 比較

| Feature 特點 | `readString()` | `writeString()` |
|---|---|---|
| Purpose 用途 | Read file → String 讀取檔案成字串 | Write String → file 寫入字串到檔案 |
| Default encoding 預設編碼 | UTF-8 | UTF-8 |
| File not found 檔案不存在 | Throws `NoSuchFileException` ❌ | Creates file automatically 自動建立 ✅ |
| Returns 回傳 | `String` | `Path` (the written file 寫入的檔案) |
| Append mode 附加模式 | N/A | `StandardOpenOption.APPEND` |
| Introduced 引入版本 | Java 11 | Java 11 |

---
---

#Differences between negate() and not() methods from Predicate class

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

---
---

# What is Local Variable Type Inference (LVTI)? 什麼是區域變數型別推斷（LVTI）？

**English:** Introduced in **Java 10** (JEP 286), Local Variable Type Inference lets you replace the explicit type on the left side of a local variable declaration with the keyword `var`. The compiler automatically **infers** the correct type from the initialiser on the right side. It reduces boilerplate without sacrificing Java's static type safety — the type is still fixed at compile time, not at runtime. [baeldung](https://www.baeldung.com/java-10-local-variable-type-inference)

**中文（繁體）：** 在 **Java 10**（JEP 286）中引入，區域變數型別推斷讓你可以用關鍵字 `var` 取代區域變數宣告左側的明確型別。編譯器會自動從右側的初始化式**推斷**正確的型別。它減少了樣板程式碼，同時不犧牲 Java 的靜態型別安全 — 型別仍在編譯時固定，而非執行時 。 [baeldung](https://www.baeldung.com/java-10-local-variable-type-inference)

***

## Basic Usage 基本用法

```java
// Before Java 10  Java 10 之前
String name = "Alice";
List<String> list = new ArrayList<String>();
Map<Integer, String> map = new HashMap<Integer, String>();

// Java 10+ with var  Java 10+ 使用 var
var name = "Alice";                         // inferred as String  推斷為 String
var list = new ArrayList<String>();         // inferred as ArrayList<String>
var map = new HashMap<Integer, String>();   // inferred as HashMap<Integer, String>
```

**English:** Note that `var` is **not a keyword** — it is a **restricted type name**. This means you can still have a variable, method, or package named `var` from old code, and it won't break. [apress](https://www.apress.com/gp/blog/all-blog-posts/local-variable-type-inference-in-java-se-10/15991244)

**中文（繁體）：** 注意 `var` **不是關鍵字** — 它是一個**受限型別名稱**。這意味著舊程式碼中仍可以有名為 `var` 的變數、方法或套件，不會造成衝突 。 [apress](https://www.apress.com/gp/blog/all-blog-posts/local-variable-type-inference-in-java-se-10/15991244)

***

## Where `var` CAN Be Used 可以使用 `var` 的地方

**1. Local variables with an initialiser 有初始化式的區域變數**: [geeksforgeeks](https://www.geeksforgeeks.org/java/local-variable-type-inference-or-lvti-in-java-10/)
```java
var price = 99.99;              // double
var message = "Hello World";   // String
var isValid = true;            // boolean
```

**2. For-each loops 增強型 for 迴圈**: [tutorialspoint](https://www.tutorialspoint.com/java/java10_local_variable_type_inference.htm)
```java
var names = List.of("Alice", "Bob", "Charlie");

for (var name : names) {        // name inferred as String  推斷為 String
    System.out.println(name);
}
```

**3. Traditional for loops 傳統 for 迴圈**: [geeksforgeeks](https://www.geeksforgeeks.org/java/local-variable-type-inference-or-lvti-in-java-10/)
```java
int[] numbers = {1, 2, 3, 4, 5};

for (var i = 0; i < numbers.length; i++) {  // i inferred as int
    System.out.println(numbers[i]);
}
```

**4. Try-with-resources Try-with-resources 中**: [baeldung](https://www.baeldung.com/java-10-local-variable-type-inference)
```java
try (var reader = new BufferedReader(new FileReader("file.txt"))) {
    var line = reader.readLine();  // line inferred as String
    System.out.println(line);
}
```

**5. Static and instance initialisation blocks 靜態與實例初始化區塊**: [geeksforgeeks](https://www.geeksforgeeks.org/java/local-variable-type-inference-or-lvti-in-java-10/)
```java
class MyClass {
    static {
        var greeting = "Hello!";       // valid 有效
        System.out.println(greeting);
    }
}
```

***

## Where `var` CANNOT Be Used不可使用 `var` 的地方

[keyholesoftware](https://keyholesoftware.com/local-variable-type-inference/)

```java
// ❌ 1. Class/instance fields  類別/實例欄位
class MyClass {
    var name = "Alice"; // COMPILE ERROR  編譯錯誤
}

// ❌ 2. Method parameters  方法參數
public void greet(var name) { } // COMPILE ERROR  編譯錯誤

// ❌ 3. Method return types  方法回傳型別
public var getName() { return "Alice"; } // COMPILE ERROR  編譯錯誤

// ❌ 4. Without an initialiser  沒有初始化式
var x; // COMPILE ERROR — compiler can't infer type  無法推斷型別

// ❌ 5. Initialised with null  以 null 初始化
var obj = null; // COMPILE ERROR — type is indeterminate  型別不確定

// ❌ 6. Multiple variable declaration  多變數宣告
var x = 0, y = 0; // COMPILE ERROR  編譯錯誤
```

***

## Java 11 Extension: `var` in Lambda Parameters 在 Lambda 參數中使用 `var`

**English:** Java 11 extended `var` to allow its use in **lambda expression parameters**. This may seem unnecessary since lambda types are already inferred, but the key benefit is that it allows you to add **annotations** to lambda parameters — something you cannot do with plain lambda syntax. [docs.oracle](https://docs.oracle.com/en/java/javase/22/language/local-variable-type-inference.html)

**中文（繁體）：** Java 11 擴展了 `var`，允許在 **lambda 表達式參數**中使用。這看起來似乎多此一舉，因為 lambda 型別本來就會被推斷，但關鍵好處是可以在 lambda 參數上加**注解（annotation）** — 這是純 lambda 語法無法做到的 。 [docs.oracle](https://docs.oracle.com/en/java/javase/22/language/local-variable-type-inference.html)

```java
// Java 11 — var in lambda parameters  lambda 參數中使用 var
var numbers = List.of(1, 2, 3, 4, 5);

// Adding @NonNull annotation to lambda parameter — only possible with var
// 在 lambda 參數上加 @NonNull 注解 — 只有用 var 才能做到
var result = numbers.stream()
    .filter((@NonNull var n) -> n > 2)
    .collect(Collectors.toList());

// Note: mixing var and explicit types in same lambda is NOT allowed
// 注意：同一個 lambda 中不能混用 var 和明確型別
(var x, int y) -> x + y  // ❌ COMPILE ERROR  編譯錯誤
(var x, var y) -> x + y  // ✅ valid  有效
```

***

## Important Behaviour: Type is Fixed at Compile Time 重要行為：型別在編譯時固定

**English:** Once `var` infers a type, that type is **permanent** — you cannot reassign the variable to a different type. `var` is not like JavaScript's `var`; it is fully statically typed. [keyholesoftware](https://keyholesoftware.com/local-variable-type-inference/)

**中文（繁體）：** 一旦 `var` 推斷出型別，該型別就是**固定的** — 你無法將變數重新指派為不同型別。`var` 不像 JavaScript 的 `var`；它是完全靜態型別的 。 [keyholesoftware](https://keyholesoftware.com/local-variable-type-inference/)

```java
var x = 10;      // inferred as int  推斷為 int
x = "hello";     // ❌ COMPILE ERROR — cannot change type to String
                 // 編譯錯誤 — 不能改變型別為 String

var x = 10;
x = 20;          // ✅ fine — still int  沒問題，仍是 int
```

***

## Summary Table 總結表

| Feature 特點 | Java 10 | Java 11 |
|---|---|---|
| Local variables 區域變數 | ✅ | ✅ |
| For / for-each loops 迴圈 | ✅ | ✅ |
| Try-with-resources | ✅ | ✅ |
| Lambda parameters Lambda 參數 | ❌ | ✅ |
| Instance/class fields 實例/類別欄位 | ❌ | ❌ |
| Method parameters 方法參數 | ❌ | ❌ |
| Method return types 方法回傳型別 | ❌ | ❌ |
| Works without initialiser 無初始化式 | ❌ | ❌ |
| `var` is a keyword `var` 是關鍵字嗎 | ❌ Restricted type name 受限型別名稱 | ❌ |

The key philosophy behind `var` is **readability over verbosity** — use it when the type is **obvious from context**, and avoid it when it would make the code harder to understand. For example, `var list = new ArrayList<String>()` is perfectly clear, while `var result = process()` is ambiguous and should be avoided. [azul](https://www.azul.com/blog/local-variable-type-inference-in-java-friend-or-foe/)

`var` 背後的核心理念是**可讀性優於冗長** — 當型別從上下文中**一目了然**時使用它，當使用它會讓程式碼更難理解時避免使用。例如，`var list = new ArrayList<String>()` 非常清楚，而 `var result = process()` 則模糊不清，應該避免 。 [azul](https://www.azul.com/blog/local-variable-type-inference-in-java-friend-or-foe/)

---
---