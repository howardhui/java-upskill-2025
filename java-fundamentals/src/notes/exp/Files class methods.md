# readString(), writeString() method from Files Class

Both `Files.readString()` and `Files.writeString()` were introduced in **Java 11** as part of the `java.nio.file.Files` class. They dramatically simplify file I/O by eliminating the need for `BufferedReader`, `BufferedWriter`, or manual byte conversion. [javasleep](https://javasleep.com/lesson-14-new-file-methods-in-java-11-readstring-writestring/)

***

## `Files.readString()`

**English:** Reads the **entire content of a file** into a `String` in one single call. Before Java 11, you needed multiple lines of `BufferedReader` or `new String(Files.readAllBytes(...))` to achieve the same result. It uses **UTF-8** encoding by default, but a second overload accepts a custom `Charset`. [cnblogs](https://www.cnblogs.com/learnfk/p/14763644.html)

**中文（繁體）：** 將**整個檔案的內容**一次性讀取成一個 `String`。在 Java 11 之前，你需要使用 `BufferedReader` 或 `new String(Files.readAllBytes(...))` 才能達到相同效果。預設使用 **UTF-8** 編碼，但也有第二個多載方法可以指定字元集 。 [cnblogs](https://www.cnblogs.com/learnfk/p/14763644.html)

**Method signatures 方法簽名：**

```java
// Default UTF-8 encoding  預設 UTF-8 編碼
public static String readString(Path path) throws IOException

// Custom charset  自訂字元集
public static String readString(Path path, Charset cs) throws IOException
```

**Example 範例：**

```java
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

// Read with default UTF-8  使用預設 UTF-8 讀取
Path path = Path.of("example.txt");
String content = Files.readString(path);
System.out.println(content); // Prints entire file content 印出整個檔案內容

// Read with specific charset  使用指定字元集讀取
String contentISO = Files.readString(path, StandardCharsets.ISO_8859_1);
```

**Before Java 11 vs After 與 Java 11 前的比較：**

```java
// ❌ Before Java 11 — verbose  Java 11 前：冗長
BufferedReader reader = new BufferedReader(new FileReader("example.txt"));
StringBuilder sb = new StringBuilder();
String line;
while ((line = reader.readLine()) != null) sb.append(line).append("\n");
reader.close();
String content = sb.toString();

// ✅ After Java 11 — clean  Java 11 後：簡潔
String content = Files.readString(Path.of("example.txt"));
```

**⚠️ Important 注意：** If the file does not exist, `readString()` throws `NoSuchFileException`. It is also not recommended for **very large files** as the entire content is loaded into memory at once, which may cause `OutOfMemoryError`. [cnblogs](https://www.cnblogs.com/learnfk/p/14763644.html)

**⚠️ 重要：** 若檔案不存在，`readString()` 會拋出 `NoSuchFileException`。也不建議用於**超大型檔案**，因為整個內容會一次載入記憶體，可能導致 `OutOfMemoryError`。

***

## `Files.writeString()`

**English:** Writes a `String` directly to a file in one call. If the file does **not exist**, it is **created automatically**. If the file already exists, its content is **overwritten by default**. Like `readString()`, it uses **UTF-8** by default and accepts optional `OpenOption` flags to control write behaviour. [howtodoinjava](https://howtodoinjava.com/java11/write-string-to-file/)

**中文（繁體）：** 一次呼叫即可將 `String` 直接寫入檔案。若檔案**不存在**，會**自動建立**。若檔案已存在，預設會**覆蓋**其內容。和 `readString()` 一樣預設使用 **UTF-8**，並接受可選的 `OpenOption` 參數來控制寫入行為 。 [howtodoinjava](https://howtodoinjava.com/java11/write-string-to-file/)

**Method signatures 方法簽名：**

```java
// Default UTF-8, overwrites file  預設 UTF-8，覆蓋檔案
public static Path writeString(Path path, CharSequence csq, OpenOption... options) throws IOException

// Custom charset  自訂字元集
public static Path writeString(Path path, CharSequence csq, Charset cs, OpenOption... options) throws IOException
```

Note it returns the `Path` of the written file, which allows **method chaining** 注意它回傳寫入檔案的 `Path`，可用於**方法鏈接**。

**Example 範例：**

```java
import java.nio.file.*;

Path path = Path.of("example.txt");

// Write (overwrites existing content)  寫入（覆蓋現有內容）
Files.writeString(path, "Hello, Java 11!");

// Append to existing file  附加到現有檔案
Files.writeString(path, "\nSecond line", StandardOpenOption.APPEND);

// Verify by reading back  讀回驗證
String content = Files.readString(path);
System.out.println(content);
// Hello, Java 11!
// Second line
```

**Method chaining example 方法鏈接範例：**

```java
// writeString() returns Path, so you can chain readString() directly
// writeString() 回傳 Path，可直接鏈接 readString()
String result = Files.readString(
    Files.writeString(Path.of("hello.txt"), "Hello World")
);
System.out.println(result); // Hello World
```

***

## Comparison 比較

| Feature 特點 | `readString()` | `writeString()` |
|---|---|---|
| Purpose 用途 | Read file → String 讀取檔案成字串 | Write String → file 寫入字串到檔案 |
| Default encoding 預設編碼 | UTF-8 | UTF-8 |
| File not found 檔案不存在 | Throws `NoSuchFileException` ❌ | Creates file automatically 自動建立 ✅ |
| Returns 回傳 | `String` | `Path` (the written file 寫入的檔案) |
| Append mode 附加模式 | N/A | `StandardOpenOption.APPEND` |
| Introduced 引入版本 | Java 11 | Java 11 |
