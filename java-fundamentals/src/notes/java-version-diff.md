## 1) Generics（泛型，Java 5 起）

**EN**: Generics add compile-time type safety; you avoid most casts and many `ClassCastException`s.  
**繁中**：泛型提供編譯期型別安全，減少轉型與執行期的型別錯誤。

```java
// Java 1.4 (no generics)
java.util.List list = new java.util.ArrayList();
list.add("hello");
String s = (String) list.get(0); // must cast

// Java 21
java.util.List<String> list2 = new java.util.ArrayList<>();
list2.add("hello");
String s2 = list2.get(0); // no cast
```

## 2) Lambda + Stream（匿名函式 + 串流處理，Java 8 起）

**EN**: Lambdas let you pass behavior; Streams make collection processing concise and composable.  
**繁中**：Lambda 讓你把「行為」當參數傳遞；Stream 讓集合處理更精簡、可組合。

```java
// Java 1.4
java.util.List nums = java.util.Arrays.asList(new Integer[]{1,2,3,4});
int sum = 0;
for (java.util.Iterator it = nums.iterator(); it.hasNext();) {
    Integer n = (Integer) it.next();
    if (n.intValue() % 2 == 0) sum += n.intValue();
}

// Java 21
var nums2 = java.util.List.of(1,2,3,4);
int sum2 = nums2.stream()
                .filter(n -> n % 2 == 0)
                .mapToInt(n -> n)
                .sum();
```

## 3) `var` (Local Variable Type Inference)（區域變數型別推斷，Java 10 起）

**EN**: `var` reduces noise when the initializer already makes the type obvious.  
**繁中**：當右側初始化已經很清楚型別時，用 `var` 降低冗長度。

```java
// Java 1.4
java.util.Map map = new java.util.HashMap();
java.util.List list = new java.util.ArrayList();

// Java 21
var map2 = new java.util.HashMap<String, Integer>();
var list2 = new java.util.ArrayList<String>();
```

## 4) Switch Expressions + Pattern Matching（switch 表達式 + 模式比對，Java 14/17 起逐步成熟）

**EN**: `switch` can return a value; pattern matching lets you test and bind types cleanly.  
**繁中**：`switch` 可直接回傳值；模式比對能更乾淨地判斷型別並綁定變數。

```java
// Java 1.4
Object o = "hi";
String kind;
if (o instanceof String) {
    kind = "string";
} else if (o instanceof Integer) {
    kind = "int";
} else {
    kind = "other";
}

// Java 21
Object o2 = "hi";
String kind2 = switch (o2) {
    case String s  -> "string: " + s;
    case Integer i -> "int: " + i;
    default        -> "other";
};
```

## 5) Records（資料載體類型，Java 16 起）+（更少樣板碼）

**EN**: Records are concise immutable data carriers (fields + ctor + accessors + `equals/hashCode/toString`).  
**繁中**：Record 是精簡的不可變資料類型（自動生成欄位、建構子、存取器與常用方法）。

```java
// Java 1.4
public class Point {
    private final int x;
    private final int y;
    public Point(int x, int y) { this.x = x; this.y = y; }
    public int getX() { return x; }
    public int getY() { return y; }
}

// Java 21
public record Point(int x, int y) {}
```
---
## Cursor AI 使用指引（個人版）

| 情況 | 使用哪個功能 | 快捷鍵 |
|------|------------|--------|
| 知道要寫什麼，想打少點字 | Tab 自動補全 | 自動觸發 |
| 想改進/修正現有代碼 | Inline Chat | Command + I |
| 想理解概念或從零生成 | Chat 面板 | Command + L |
| 需要 AI 參考特定檔案 | Chat + @ 引用 | Command + L 後輸入 @ |