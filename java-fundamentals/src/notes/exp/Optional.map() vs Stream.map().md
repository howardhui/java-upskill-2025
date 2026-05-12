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