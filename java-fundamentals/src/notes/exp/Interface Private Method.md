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