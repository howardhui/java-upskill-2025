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
