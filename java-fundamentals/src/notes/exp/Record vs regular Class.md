# Record vs regular Class

## Major Differences: Record vs Regular Class 主要差異：Record 與一般類別

**English:** A `record` is fundamentally a **restricted, specialised form of a class** — it is always implicitly `final`, all its fields are implicitly `private final`, and it automatically generates a canonical constructor, accessors, `equals()`, `hashCode()`, and `toString()`. A regular class gives you complete freedom to design any kind of behaviour. [stackoverflow](https://stackoverflow.com/questions/71473485/what-is-the-difference-between-a-final-class-and-a-record)

**中文（繁體）：** `record` 本質上是一種**受限的特殊類別** — 它始終是隱含的 `final`，所有欄位都是隱含的 `private final`，並自動生成標準建構子、存取器、`equals()`、`hashCode()` 和 `toString()`。一般類別讓你完全自由地設計任何行為 。 [stackoverflow](https://stackoverflow.com/questions/71473485/what-is-the-difference-between-a-final-class-and-a-record)

| Feature 特點 | `record` | Regular Class 一般類別 |
|---|---|---|
| Fields 欄位 | Implicitly `private final` 隱含 `private final` | Any modifier 任何修飾符 |
| Mutable? 可變？ | ❌ Immutable by design 設計上不可變 | ✅ Can be mutable 可以可變 |
| Extends other class? 可繼承類別？ | ❌ Implicitly extends `Record` 隱含繼承 `Record` | ✅ Yes 可以 |
| Can be extended? 可被繼承？ | ❌ Implicitly `final` 隱含 `final` | ✅ Unless `final` 除非加 `final` |
| `equals()` / `hashCode()` | ✅ Auto-generated (value-based) 自動生成（值比較） | ❌ Must write manually 必須手動撰寫 |
| `toString()` | ✅ Auto-generated 自動生成 | ❌ Must write manually 必須手動撰寫 |
| Setters 設值器 | ❌ Not supported 不支援 | ✅ Optional 可選 |
| Accessor style 存取器風格 | `name()` not `getName()` | `getName()` convention 慣例 |
| Private internal fields 私有內部欄位 | ❌ All fields are exposed via accessors 所有欄位均透過存取器公開 | ✅ Can hide internal representation 可隱藏內部表示 |
| `abstract` / `sealed` modifier | ❌ Not allowed 不允許 | ✅ Allowed 允許 |
| Instance initialisers 實例初始化器 | ❌ Not allowed 不允許 | ✅ Allowed 允許 |

***

## Code Comparison 程式碼比較

**English:** The difference in verbosity is immediately apparent. Both of the following represent the same concept — a data container for a person's name and age. [ziuma](https://ziuma.com/Thread-What-is-the-difference-between-record-and-a-regular-class-in-Java-and-when-should-I?pid=35788)

**中文（繁體）：** 冗長程度的差異一目了然。以下兩者都表示相同的概念 — 一個儲存姓名和年齡的資料容器 。 [ziuma](https://ziuma.com/Thread-What-is-the-difference-between-record-and-a-regular-class-in-Java-and-when-should-I?pid=35788)

```java
// ❌ Regular class — verbose  一般類別：冗長
public final class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName()  { return name; }
    public int    getAge()   { return age; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person p)) return false;
        return age == p.age && Objects.equals(name, p.name);
    }
    @Override public int    hashCode()  { return Objects.hash(name, age); }
    @Override public String toString()  { return "Person[name=" + name + ", age=" + age + "]"; }
}

// ✅ Record — one line!  Record：一行搞定！
record Person(String name, int age) { }
```

***

## When to Use `record` 何時使用 `record`

**English:** Use `record` when your class's **sole purpose is to carry data** and you have no need for mutability, inheritance, or hidden internal state. [linkedin](https://www.linkedin.com/pulse/java-records-when-use-avoid-them-fabio-ribeiro-wm9of)

**中文（繁體）：** 當你的類別**唯一目的是承載資料**，且不需要可變性、繼承或隱藏的內部狀態時，使用 `record` 。 [linkedin](https://www.linkedin.com/pulse/java-records-when-use-avoid-them-fabio-ribeiro-wm9of)

- **DTOs (Data Transfer Objects)** — transferring data between layers (controller → service → database) 在層與層之間傳輸資料
- **API response/request bodies** — simple containers for JSON payloads JSON 載荷的簡單容器
- **Value objects** — coordinates, ranges, pairs, money amounts 座標、範圍、配對、金額
- **Configuration holders** — grouping related settings together 將相關設定組合在一起
- **Pattern matching** — paired with `sealed` interfaces for exhaustive `switch` 與 `sealed` 介面搭配用於完整 `switch`

```java
// ✅ Great use cases for record  record 的最佳使用案例

record Point(int x, int y) { }                        // value object 值物件
record UserDto(String name, String email) { }          // DTO
record ApiResponse(int status, String message) { }     // API response
record DateRange(LocalDate from, LocalDate to) { }     // configuration 設定
```

***

## When NOT to Use `record` 何時不應使用 `record`

**English:** Avoid `record` when you need any of the following: [reddit](https://www.reddit.com/r/javahelp/comments/1pd6yvg/whats_the_difference_between_record_and_class_in/)

**中文（繁體）：** 當你需要以下任何一項時，避免使用 `record` ： [reddit](https://www.reddit.com/r/javahelp/comments/1pd6yvg/whats_the_difference_between_record_and_class_in/)

```java
// ❌ 1. Mutable state (you need setters)  可變狀態（需要 setter）
// Use a regular class  使用一般類別
class ShoppingCart {
    private List<String> items = new ArrayList<>();
    public void addItem(String item) { items.add(item); } // mutable 可變
}

// ❌ 2. Inheritance hierarchy  繼承架構
class Animal { }
class Dog extends Animal { }  // records cannot extend custom classes
                               // record 不能繼承自訂類別

// ❌ 3. Hiding internal representation  隱藏內部表示
class CachedData {
    private final Map<String, Object> cache = new HashMap<>(); // hidden 隱藏
    public Object get(String key) { return cache.get(key); }
}

// ❌ 4. Complex business logic / behaviour  複雜業務邏輯/行為
class OrderService {
    public Order placeOrder(Cart cart) { /* complex logic 複雜邏輯 */ }
    public void cancelOrder(Order o)   { /* complex logic 複雜邏輯 */ }
}
```

***

## The Relationship Between `record` and `Optional`(`record` 與 `Optional` 的關係)

**English:** This is a nuanced topic. `record` and `Optional` are entirely independent features, but they interact in interesting — and sometimes tricky — ways. The core tension is that a `record`'s constructor parameter type **must exactly match** its accessor return type. This means if you declare `Optional<String>` as a record component, the accessor also returns `Optional<String>`, which goes against the convention that `Optional` should only be used as a **return type**, not as a field type. [stackoverflow](https://stackoverflow.com/questions/73424610/optionalt-as-a-record-parameter-in-java)

**中文（繁體）：** 這是一個微妙的話題。`record` 和 `Optional` 是完全獨立的功能，但它們以有趣且有時棘手的方式互動。核心矛盾在於 `record` 的建構子參數型別必須**完全符合**其存取器的回傳型別。這意味著如果你將 `Optional<String>` 宣告為 record 元件，存取器也會回傳 `Optional<String>`，這違反了 `Optional` 應只作為**回傳型別**而非欄位型別的慣例 。 [stackoverflow](https://stackoverflow.com/questions/73424610/optionalt-as-a-record-parameter-in-java)

### Approach 1: `Optional` as a record component (Not Recommended)［方式一：`Optional` 作為 record 元件（不建議）］

```java
// ⚠️ Technically works, but bad practice  技術上可行，但不建議
record Request(String name, Optional<String> nickname) { }

Request r = new Request("Alice", Optional.of("Ali"));
r.nickname().ifPresent(System.out::println); // Ali

Request r2 = new Request("Bob", Optional.empty());
// ❌ Problems: Optional is not Serializable, violates record's "transparent" nature
// 問題：Optional 不可序列化，違反 record 的「透明」本質
```

**English:** The issue is philosophical — a `record` is meant to be a **transparent carrier** where every field is directly visible. Wrapping a field in `Optional` adds an extra layer of indirection, making it no longer transparent. Additionally, since record fields are `final`, an empty `Optional` stored in a record will **always remain empty** — there is no value in storing it. [stackoverflow](https://stackoverflow.com/questions/73424610/optionalt-as-a-record-parameter-in-java)

**中文（繁體）：** 問題在於設計理念 — `record` 的目的是作為**透明的承載器**，每個欄位都直接可見。用 `Optional` 包裝欄位增加了一層間接性，使其不再透明。此外，由於 record 欄位是 `final`，儲存在 record 中的空 `Optional` 將**永遠保持空** — 儲存它沒有任何意義 。 [stackoverflow](https://stackoverflow.com/questions/73424610/optionalt-as-a-record-parameter-in-java)

### Approach 2: Use `null` + custom accessor returning `Optional` (Recommended) ［方式二：使用 `null` + 自訂存取器回傳 `Optional`（建議）］

```java
// ✅ Best practice — store raw value, wrap in Optional at accessor level
// 最佳實踐 — 儲存原始值，在存取器層面包裝成 Optional
record Person(String name, String nickname) {

    // Custom constructor to allow null  自訂建構子允許 null
    public Person(String name) {
        this(name, null);  // nickname defaults to null 預設為 null
    }

    // Custom accessor wraps value in Optional
    // 自訂存取器將值包裝成 Optional
    public Optional<String> nickname() {
        return Optional.ofNullable(nickname);
    }
}

Person p1 = new Person("Alice", "Ali");
Person p2 = new Person("Bob");           // no nickname 無暱稱

p1.nickname().ifPresent(n -> System.out.println("Nickname: " + n)); // Nickname: Ali
p2.nickname().ifPresent(n -> System.out.println("Nickname: " + n)); // (nothing printed)
```

### Using `Optional` to Safely Retrieve from a Record ［使用 `Optional` 安全地從 Record 中取值］

**English:** `Optional` is very commonly used **around** records — for example, when searching a collection of records and potentially finding nothing. [infobip](https://www.infobip.com/developers/blog/pragmatic-use-of-java-records-and-optional)

**中文（繁體）：** `Optional` 非常常用在 **record 周圍** — 例如，在搜尋 record 集合時可能找不到結果 。 [infobip](https://www.infobip.com/developers/blog/pragmatic-use-of-java-records-and-optional)

```java
record Product(String name, double price) { }

List<Product> products = List.of(
    new Product("Apple", 0.99),
    new Product("Banana", 0.49),
    new Product("Cherry", 2.99)
);

// Optional used around record retrieval — perfect fit!
// Optional 用於 record 查詢的外層 — 非常合適！
Optional<Product> found = products.stream()
    .filter(p -> p.name().equals("Banana"))
    .findFirst();

found.ifPresent(p -> System.out.println(p.name() + ": £" + p.price()));
// Banana: £0.49

double price = products.stream()
    .filter(p -> p.name().equals("Mango"))
    .findFirst()
    .map(Product::price)
    .orElse(0.0); // not found, default 0.0  找不到，預設 0.0

System.out.println(price); // 0.0
```

***

## Summary 總結

**English:** Think of `record` and `Optional` as tools for different purposes — `record` is for **grouping data together cleanly**, and `Optional` is for **expressing the absence of a value**. They work best when `Optional` wraps a **lookup result** involving records, rather than being stored inside records as fields. [stackoverflow](https://stackoverflow.com/questions/73424610/optionalt-as-a-record-parameter-in-java)

**中文（繁體）：** 將 `record` 和 `Optional` 視為服務不同目的的工具 — `record` 用於**整潔地組合資料**，而 `Optional` 用於**表達值的缺失**。當 `Optional` 包裝涉及 record 的**查詢結果**時效果最好，而不應作為欄位儲存在 record 內部 。 [stackoverflow](https://stackoverflow.com/questions/73424610/optionalt-as-a-record-parameter-in-java)

| Scenario 情境 | Recommendation 建議 |
|---|---|
| Simple immutable data container 簡單不可變資料容器 | ✅ Use `record` |
| Mutable object with setters 有 setter 的可變物件 | ✅ Use regular class 使用一般類別 |
| Object with business logic 有業務邏輯的物件 | ✅ Use regular class 使用一般類別 |
| Inheritance required 需要繼承 | ✅ Use regular class 使用一般類別 |
| Optional field in record record 中的可選欄位 | ✅ Store `null`, return `Optional` from accessor 儲存 `null`，從存取器回傳 `Optional` |
| Search result from a record collection 從 record 集合中的搜尋結果 | ✅ Wrap result in `Optional` 將結果包裝在 `Optional` 中 |