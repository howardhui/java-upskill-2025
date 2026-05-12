# Explain the use of "record" in Java 15 (and later version if any)with examples in both English and Traditional Chinese together

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