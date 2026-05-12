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