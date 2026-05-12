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
