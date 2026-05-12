# Lambda (->) 與 Method Reference (::) 有什麼分別？什麼時候應該用 :: 而不是 Lambda？什麼時候該用 Lambda 而不是 :: ？

這題其實是 Java 函數式寫法的核心觀念之一 👍
我用「差異 → 何時用 → 實例 → 常見陷阱」幫你一次講清楚。

---

## 1. Core Difference: Lambda (`->`) vs Method Reference (`::`) / Lambda (`->`) vs Method Reference (`::`) 本質差異

### ✅ Lambda（anonymous function）

```java
(a, b) -> a + b
```

👉 You **define the logic yourself** / 你**自己定義邏輯**

---

### ✅ Method Reference（方法參考）

```java
Integer::sum
```

👉 You **reuse an existing method** / **直接引用現有方法**

---

## 2. Simple Side-by-Side Example / 最直觀對比

```java
// Lambda
list.forEach(s -> System.out.println(s));

// Method Reference
list.forEach(System.out::println);
```

👉 They are functionally equivalent / 兩者完全等價，但寫法不同

---

## 三、什麼時候用 `::`（Method Reference）？

👉 原則：**When the lambda only calls an existing method** / **當 Lambda 只是「呼叫現成方法」時**

---

### ✅ Case 1: Direct method call / 情境 1：單純轉呼叫

```java
.map(s -> s.length())
```

👉 可改成：

```java
.map(String::length)
```

---

### ✅ Case 2: Passing argument directly / 情境 2：參數直接傳入

```java
.map(x -> Math.abs(x))
```

👉 改成：

```java
.map(Math::abs)
```

---

### ✅ Case 3: Printing / consuming / 情境 3：直接呼叫輸出

```java
.forEach(x -> System.out.println(x))
```

👉 改成：

```java
.forEach(System.out::println)
```

---

### ✅ Case 4: Constructor reference / 情境 4：建構子

```java
.map(s -> new User(s))
```

👉 改成：

```java
.map(User::new)
```

---

## 👉 ✔️ Rule of Thumb / 使用 `::` 的判斷公式

如果 Lambda 長這樣：

```java
x -> someMethod(x)
```

👉 就可以改成：

```java
ClassName::someMethod
```

---

## 4. When you should NOT use `::` (use Lambda instead) / 什麼時候「不能」用 `::`（要用 Lambda）？

👉 When there is **extra logic involved** / 當有「額外邏輯」時

---

### ❌ Case 1: Computation / 情境 1：有運算

```java
.map(x -> x * 2)
```

👉 ❌ 不能寫成 `::`

---

### ❌ Case 2: Multiple steps / 情境 2：多步驟

```java
.map(s -> {
    String upper = s.toUpperCase();
    return upper + "!";
})
```

👉 ❌ 無法用 method reference

---

### ❌ Case 3: Argument reordering / 情境 3：參數重排 / 不同順序

```java
(a, b) -> b.compareTo(a)
```

👉 ❌ 無法用簡單 `::`

---

### ❌ Case 4: Conditional logic / 情境 4：條件邏輯

```java
.map(x -> x > 0 ? x : 0)
```

👉 ❌ 必須用 Lambda

---

## 5. Four Types of Method References (Important) / Method Reference 四種類型（很重要）

### 1️⃣ Static method / 靜態方法

```java
Math::max
```

---

### 2️⃣ Instance method of a specific object / 實例方法（特定物件）

```java
System.out::println
```

---

### 3️⃣ Instance method of a class / 實例方法（類別）

```java
String::toUpperCase
```

👉 等價：

```java
s -> s.toUpperCase()
```

---

### 4️⃣ Constructor reference / 建構子

```java
ArrayList::new
```

---

## 6. Practical Guidelines (Common Interview Topic 🔥) / 實務選擇原則（面試很愛問🔥）

### ✅ Prefer `::` when / 優先用 `::` 當：

- No extra logic, just forwarding a call / 沒有邏輯，只是轉呼叫
- Improves readability / 可讀性更好
- Makes code more concise / 簡潔

👉 例如：

```java
.map(String::length)
```

---

### ✅ Use Lambda when / 用 Lambda 當：

- There is logic / 有邏輯
- Computation is involved / 有運算
- Conditions exist / 有條件
- Multiple steps are needed / 有多步驟

👉 例如：

```java
.map(s -> s.length() * 2)
```

---

## 7. Readability Matters / 可讀性對比（很關鍵）

### 👍 Good use of method reference / 好的 method reference

```java
.map(String::trim)
```

## 👎 Overuse (can hurt readability) / 過度使用（反而難讀）

```java
.map(SomeUtilClass::veryComplexTransformation)
```

👉 有時 Lambda 更清楚：

```java
.map(x -> transform(x))
```

---

## 8. Common Pitfalls / 常見陷阱 ⚠️

### ❌ Not all lambdas can be converted / 不是所有 Lambda 都能轉

```java
(a, b) -> a + b
```

👉 ❌  Cannot directly use `::` (unless a matching method exists) / 不能直接變成 `::`（除非有現成方法）

👉 ✔ 可用：

```java
Integer::sum
```

---

### ❌ Type inference issues / 型別推導問題

Sometimes `::` causes ambiguity / 有時 `::` 會讓編譯器推導失敗：

```java
.map(MyClass::convert) // 可能 ambiguous
```

👉 解法：

```java
.map(x -> MyClass.convert(x))
```

---

## ✔️ Key Takeaway / 最重要總結

👉 Lambda vs Method Reference 本質：

- Lambda = **write logic / 寫邏輯**
- Method Reference = **reuse existing method / 引用現有方法**

---

## ✔️ 一句話記住

👉 **「只是呼叫 → 用 ::」**
👉 **「有邏輯 → 用 ->」**