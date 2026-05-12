# map() vs flatMap()

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