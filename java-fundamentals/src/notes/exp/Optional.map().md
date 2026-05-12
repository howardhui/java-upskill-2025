# Optional.map()

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

## ✔️ 總結

👉 `map()` 的核心：

- 有值 → 套用函數轉換
- 沒值 → 保持 empty
- 回傳仍是 Optional

---

## ✔️ 一句話記住

👉 `map()` = **「轉換內容，不拆盒子」**
👉 `flatMap()` = **「轉換 + 拆盒子」**
