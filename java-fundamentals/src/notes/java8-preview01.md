## 1. What is a Lambda Expression? / 什麼是 Lambda 表達式？

**English:**  
==A **lambda** is a short way to say “here is a piece of behavior” (a function) without writing a whole named class or method. You pass that behavior where a **functional interface** is expected (one abstract method). Think of it as: *“do this with the input”* in one line.==

**現實比喻（繁體中文）：**  
就像你對外送員說「送到 3 樓 B 室」——你沒有幫他取名字、寫完整職務說明，只交代**要做的事**；Lambda 就是把「要做的事」用很短的寫法交給程式（例如 `x -> x * 2`）。

---

## 2. What is the Stream API? What problem does it solve? / 什麼是 Stream API？它解決什麼問題？

**English:**  
The **Stream API** lets you process collections as a **pipeline**: source → intermediate steps (filter, map, sort…) → one terminal step (collect, count, findFirst…). It encourages **declarative** code (“what we want”) instead of manual loops with indexes and temporary lists. It also supports **lazy** intermediate operations and can help **parallel** processing (`parallelStream()`), though parallel use needs care.

**繁體中文：**  
**Stream** 像一條**流水線**：從資料來源開始，經過篩選、轉換、排序等步驟，最後做一次結果（例如收集成 List、計數）。它減少手寫 `for`、索引、`ArrayList` 暫存等**命令式**細節，讓程式較接近「我要什麼結果」。中間步驟可以**延遲執行**，到最後一步才真正跑；也可以選擇**平行**處理（但要小心使用情境）。

---

## 3. Example: Java 1.4 style `for` vs Java 8 Stream / 範例對比

**Task:** From a `List` of `String` names, keep those longer than 3 characters, uppercase them, and put results in a new list.

**English — Java 1.4–style (imperative):**

```java
List<String> names = Arrays.asList("Ann", "Bob", "Charlie", "Dan");
List<String> result = new ArrayList<String>();
for (int i = 0; i < names.size(); i++) {
    String n = names.get(i);
    if (n.length() > 3) {
        result.add(n.toUpperCase());
    }
}
```

**English — Java 8 Stream (declarative):**

```java
List<String> names = Arrays.asList("Ann", "Bob", "Charlie", "Dan");
List<String> result = names.stream()
    .filter(n -> n.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

**繁體中文說明：**  
兩段程式結果相同；前者逐步控制索引與 `if`、手動 `add`，後者用 `filter` / `map` / `collect` 描述「篩選 → 轉大寫 → 收集」，其中 `n -> n.length() > 3` 就是 Lambda。

---

讀 Baeldung 時可對照：**Lambda** 是「傳行為」的語法；**Stream** 是「用流水線處理集合」的 API，兩者常一起用但不是同一件事。

---

---

# Map.of()是否只回傳empty Map Object?

不是的，`Map.of()` **不只會回傳 empty Map**，而是可以建立「不可變（immutable）」的 Map，是否為空取決於你有沒有傳入 key-value 👇

---

## 一、`Map.of()` 的實際行為

### ✅ 1️⃣ 沒有參數 → 空 Map

```java
Map<String, String> map = Map.of();
```

👉 這時候才是 **empty map**

---

### ✅ 2️⃣ 有參數 → 建立含資料的 Map

```java
Map<String, String> map = Map.of(
    "A", "Apple",
    "B", "Banana"
);

System.out.println(map);
// {A=Apple, B=Banana}
```

👉 所以：
❌ 不是只回傳 empty
✅ 是「依參數建立 Map」

---

## 二、重要特性（很關鍵⚠️）

### 🔒 1️⃣ Immutable（不可變）

```java
map.put("C", "Cat"); // ❌ 會丟例外
```

👉 會拋出：

```
UnsupportedOperationException
```

---

### 🚫 2️⃣ 不允許 null

```java
Map.of("A", null); // ❌ NullPointerException
```

👉 key / value 都不能是 null

---

### ⚠️ 3️⃣ 不允許重複 key

```java
Map.of("A", "Apple", "A", "Ant"); // ❌ IllegalArgumentException
```

---

### 🔢 4️⃣ 最多 10 組（Map.of）

```java
Map.of(k1,v1, ..., k10,v10);
```

👉 超過 10 個要用：

```java
Map.ofEntries(
    Map.entry("A", "Apple"),
    Map.entry("B", "Banana"),
    ...
);
```

---

## 三、和傳統寫法差異

### 🆚 傳統 HashMap

```java
Map<String, String> map = new HashMap<>();
map.put("A", "Apple");
map.put("B", "Banana");
```

👉 可變（mutable）

---

### 🆚 Map.of

```java
Map<String, String> map = Map.of("A", "Apple", "B", "Banana");
```

👉 不可變（immutable）
👉 更簡潔（常用於初始化）

---

## 四、底層補充（進階）

`Map.of()` 回傳的其實不是一般的 `HashMap`，而是：

👉 `java.util.ImmutableCollections.MapN`（或 Map1、Map2...）

特性：

- 記憶體更省
- 查詢更快（小集合優化）
- 完全不可變

---

## ✔️ 總結

👉 `Map.of()`：

- ✔ 可建立空 Map（無參數）
- ✔ 可建立含資料 Map（有參數）
- ❌ 不是只回傳 empty
- 🔒 永遠 immutable

---

---

# 解釋如何使用Optional.map()

`Optional.map()` 是 Java 中用來「**在值存在時做轉換**」的核心方法之一。理解它的關鍵可以用一句話概括：

👉 **有值就轉換，沒值就保持 empty**

---

## 一、基本概念

```java
Optional<T> map(Function<? super T, ? extends U> mapper)
```

👉 做的事情是：

- 如果 Optional 有值 → 套用 `mapper`
- 如果沒有值 → 回傳 `Optional.empty()`

---

## 二、最簡單例子

### ✅ 字串轉長度

```java
Optional<String> name = Optional.of("Alice");

Optional<Integer> length = name.map(s -> s.length());

System.out.println(length.get()); // 5
```

👉 流程：

```
"Alice" → map → 5 → Optional<Integer>
```

---

## 三、空值情況

```java
Optional<String> name = Optional.empty();

Optional<Integer> length = name.map(s -> s.length());

System.out.println(length); // Optional.empty
```

👉 重點：

- `map()` 不會執行 lambda
- 直接回傳 empty

---

## 四、常見使用場景

### ✅ 1️⃣ 取物件屬性（避免 NPE）

```java
Optional<User> user = Optional.of(new User("John"));

Optional<String> username =
    user.map(u -> u.getName());
```

👉 等價於：

```java
if (user != null) {
    return user.getName();
}
```

---

### ✅ 2️⃣ 連續轉換（鏈式操作）

```java
Optional<String> result =
    Optional.of("hello")
        .map(s -> s.toUpperCase())
        .map(s -> s + " WORLD");

System.out.println(result.get()); // HELLO WORLD
```

👉 很像 Stream pipeline

---

### ✅ 3️⃣ 搭配 method reference

```java
Optional<String> name = Optional.of("Bob");

Optional<Integer> length = name.map(String::length);
```

---

## 五、map vs flatMap（超重要⚠️）

很多人會卡在這裡👇

---

### ❌ 使用 map（錯誤情境）

```java
Optional<String> name = Optional.of("Alice");

Optional<Optional<Integer>> result =
    name.map(s -> Optional.of(s.length()));
```

👉 結果變成：

```
Optional<Optional<Integer>>
```

---

### ✅ 使用 flatMap（正確）

```java
Optional<Integer> result =
    name.flatMap(s -> Optional.of(s.length()));
```

👉 `flatMap` 會幫你「攤平」

---

## 六、搭配 orElse / orElseGet

```java
String result =
    Optional.of("hello")
        .map(String::toUpperCase)
        .orElse("default");

System.out.println(result); // HELLO
```

---

## 七、實務常見寫法

### ✅ 安全取巢狀屬性

```java
Optional<String> city =
    Optional.of(user)
        .map(User::getAddress)
        .map(Address::getCity);
```

👉 避免：

```java
user.getAddress().getCity() // NPE 風險
```

---

## 八、心智模型（超重要）

你可以這樣理解：

```
Optional<T>
   ↓ map
Optional<U>
```

👉 map 就是：
👉 **「包在 Optional 裡的值做 transformation」**

---

# ✔️ 總結

👉 `map()` 的核心：

- 有值 → 套用函數轉換
- 沒值 → 保持 empty
- 回傳仍是 Optional

---

# ✔️ 一句話記住

👉 `map()` = **「轉換內容，不拆盒子」**
👉 `flatMap()` = **「轉換 + 拆盒子」**

---

---

# 以下兩行代碼有何分別？

```java
Optional<Integer> length = name.map(String::length);
...
Optional<Integer> length = name.flatMap(s -> Optional.of(s.length()));
```

這兩行代碼乍看很像，但 **差別在於回傳的 Optional 結構**，這是 `map()` 與 `flatMap()` 的核心差異。讓我們逐步拆解。

---

### 代碼：

```java
Optional<String> name = Optional.of("Alice");
```

## 1️⃣ 使用 `map`：

```java
Optional<Integer> length = name.map(String::length);
```

- `map` 的 lambda / method reference 返回 **普通型別**（不是 Optional）
- `String::length` 回傳 `int`（自動裝箱成 `Integer`）
- `map` 會把這個值「包進新的 Optional」

**結果：**

```java
length = Optional[5]
```

結構是 **Optional****

---

## 2️⃣ 使用 `flatMap`：

```java
Optional<Integer> length = name.flatMap(s -> Optional.of(s.length()));
```

- `flatMap` 的 lambda 必須回傳 **Optional**
- lambda 回傳 `Optional.of(s.length())`
- `flatMap` 會「攤平」這個 Optional，不會變成 Optional

**結果：**

```java
length = Optional[5]
```

結構同樣是 **Optional****

---

## 🔑 差異精髓


| 特性        | map                           | flatMap            |
| --------- | ----------------------------- | ------------------ |
| lambda 回傳 | T                             | Optional           |
| 回傳結果      | Optional                      | Optional（攤平）       |
| 會發生什麼     | 自動包裝值                         | 自動拆掉多餘的 Optional   |
| 錯誤使用情況    | lambda 回傳 Optional → Optional | lambda 回傳 T → 編譯錯誤 |


---

## ⚠️ 舉例易錯誤情境：

```java
Optional<Optional<Integer>> wrong = name.map(s -> Optional.of(s.length()));
```

- map → lambda 回傳 Optional → 變成 **Optional<Optional****>**
- 這通常不是你想要的
- 如果想要攤平 → 用 flatMap

---

## ✅ 心智模型

- `map()` = 「轉換值，保留盒子」
- `flatMap()` = 「轉換值，如果有盒子就拆掉再包成新的」

---

---

# Optional.map() vs Stream.map() 差異

`Optional.map()` 和 `Stream.map()` 在語法上很像，但概念和使用場景完全不同。以下我幫你整理得清楚、直觀、連帶實例說明。

---

## 一、核心差異概念


| 特性        | Optional.map()                | Stream.map()       |
| --------- | ----------------------------- | ------------------ |
| 對象        | **單一值可能存在**（Optional）         | **多個值**（Stream）    |
| Lambda 輸入 | Optional 裡的單一值                | Stream 中的每個元素      |
| Lambda 回傳 | 單一值                           | 單一值（每個元素映射）        |
| 回傳結果      | Optional 包裝的單一值               | Stream（映射後的元素集合）   |
| 空值處理      | Optional.empty() → lambda 不執行 | 空 Stream → map 不執行 |


---

## 二、Optional.map() 實例

```java
Optional<String> name = Optional.of("Alice");

Optional<Integer> length = name.map(String::length);

System.out.println(length); // Optional[5]
```

- 如果 `name` 是空的 (`Optional.empty()`) → map 不執行 → 回傳 `Optional.empty()`
- 用途：對單一可能存在的值做安全轉換

---

## 三、Stream.map() 實例

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

List<Integer> lengths = names.stream()
    .map(String::length)
    .toList();

System.out.println(lengths); // [5, 3, 7]
```

- Lambda 會對每個元素都執行
- 回傳的 Stream 元素數量 = 原本 Stream 元素數量
- 用途：對集合的每個元素做批次轉換

---

## 四、直覺心智模型

- `Optional.map()` = 「單個值轉換，如果有值就轉」
- `Stream.map()` = 「對集合裡的每個元素做轉換」

> 兩者都是 **函數式轉換**，但一個是單值，一個是多值。

---

## 五、空值 / 空集合處理差異


| 狀況       | Optional.map()                | Stream.map()             |
| -------- | ----------------------------- | ------------------------ |
| 空值 / 空集合 | Optional.empty() → lambda 不執行 | Stream.empty() → map 不執行 |
| 安全性      | 自動避免 NPE                      | 需要 stream 本身存在（null 會出錯） |


---

## 六、鏈式操作差異

```java
// Optional
Optional<Integer> result = Optional.of("Hello")
    .map(String::length)
    .map(x -> x * 2); // 對單一值鏈式轉換

// Stream
List<Integer> results = List.of("Hello", "World").stream()
    .map(String::length)
    .map(x -> x * 2)
    .toList(); // 對多個元素鏈式轉換
```

- Optional.map() 只對**一個可能存在的值**
- Stream.map() 對**多個元素**

---

## 七、map vs flatMap 差異也要注意

- Optional.flatMap() → 拆 Optional
- Stream.flatMap() → 拆 Stream（多對一或一對多的關係）

---

簡單一句話總結：

> **Optional.map() = 對單個可能存在的值做轉換**
> **Stream.map() = 對多個元素做轉換**

---

---

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

---

---

# 第1週學習總結

## 環境設定（已完成）

- Java 21 JDK（Temurin）✅
- VS Code + Java Extension Pack ✅
- Cursor（主力 IDE）✅
- Maven 項目結構 ✅

## 本週建立的項目

- java-fundamentals（Maven 項目）
  - Calculator.java
  - StringUtils.java
  - MathUtils.java
  - Student.java / StudentManager.java

## 本週接觸的新語法

### Lambda Expression（->）[用自己的話解釋，並貼一個你理解的例子]

In Java, lambda expression is an implementation of single abstract method of a functional interface that can be executed, taking input parameters optionally to return value or void.

### Stream API [用自己的話解釋，並貼一個你理解的例子]

Stream API is used to process collections of data in a clean and functional style. Instead of looping, different operations like `filter`, `map`, `collect`, `forEach` are used to describe what you want to do with the data. Streams do not store data, but provide a pipeline of operations on existing data sources. The operations can be chained together that makes the code shorter and easier to read.

常用方法：filter、map、collect、forEach、mapToInt、sorted

### Method Reference（::）[用自己的話解釋，並貼一個你理解的例子]

Method reference is a shorthand way of referring an existing method, using `::` syntax. It was passed as an argument to a function, instead of writing a lambda expression. There are four types of method references: static method, instance method of particular object, instance method of arbitrary object and constructor. 

### Optional（如有接觸）

[用自己的話解釋]

## Cursor AI 四種功能


| 功能          | 快捷鍵               | 最適用場景        |
| ----------- | ----------------- | ------------ |
| Tab 自動補全    | 自動觸發              | 加速輸入熟悉的代碼    |
| Inline Chat | Command + I       | 修改現有代碼       |
| Chat 面板     | Command + L       | 概念問答、從零生成    |
| @ 引用        | Command + L 後輸入 @ | 需要 AI 參考特定檔案 |


## 週一小測試自評

- WeekOneReview.java AI 評分：[X] / 10
- 最薄弱的地方：[記錄]

## 仍然不太明白的地方

- [誠實列出，週末閱讀時重點解決]

---
---