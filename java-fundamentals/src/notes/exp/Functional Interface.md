# Functional Interface
## What is a Functional Interface? (Functional Interface 是什麼？）

- **EN**: A **functional interface** is an interface that has **exactly one abstract method** (a “single abstract method”, SAM). It may still have `default` / `static` methods.  
- **繁中**：**Functional Interface（函數式介面）** 是指**只有一個抽象方法**（Single Abstract Method, SAM）的介面；它仍然可以有 `default` / `static` 方法。

---

## Why is it the basis for lambdas?（為什麼它是 Lambda 的基礎？）

- **EN**: A **lambda expression** is basically **an implementation of that one abstract method**. Java can compile a lambda only when it knows **which single method** the lambda is meant to implement—so it needs a **functional interface** as the target type.  
- **繁中**：**Lambda** 本質上就是「幫那個**唯一的抽象方法**提供一個實作」。Java 必須知道這個 lambda 要對應到**哪一個唯一的方法**才能編譯，所以需要有 **functional interface** 當作「目標型別（target type）」。

---

## One-line relationship（最簡單的關係一句話）

- **EN**: **Functional interface = the “shape” (method signature); lambda = the “body” (implementation).**  
- **繁中**：**Functional interface 提供「方法長相/簽名」，lambda 提供「方法內容/實作」。**

---

- **Function (EN)**: When I need to **transform an input into an output** (T → R), I use it.  
  **Function (繁中)**：當我需要**把一個輸入轉換成一個輸出**（T → R）時使用

- **Predicate (EN)**: When I need to **test a condition / filter and get true or false** (T → boolean), I use it.  
  **Predicate (繁中)**：當我需要**做條件判斷/過濾並得到 true/false**（T → boolean）時使用

- **Supplier (EN)**: When I need to **provide/generate a value without any input** (() → T), I use it.  
  **Supplier (繁中)**：當我需要**不接受輸入、只提供/產生一個值**（() → T）時使用
  
- **Consumer (EN)**: When I need to **take an input and perform an action with no return value** (T → void), I use it.  
  **Consumer (繁中)**：當我需要**接收一個輸入並執行動作、但不回傳結果**（T → void）時使用

---

### Quick comparison table
| Interface       | Expression | Input | Output  | Description |
| --------------- | ---------- | ----- | ------- | ----------- |
| `Function<T,R>` | `(T t) -> R` | ✅   | ✅     | Transform an input into an output |
| `Predicate<T>`  | `(T t) -> boolean` | ✅   | boolean | Test condition and answer yes/no |
| `Supplier<T>`   | `() -> T` | ❌   | ✅     | Produce a value without any input |
| `Consumer<T>`   | `(T t) -> { /* no return */ }` | ✅   | ❌     | Take an input without output and cause side effects |
| `Runnable`      | `() -> { /* no input, no output */ }` | ❌   | ❌     | Just execute and cause side effects |
| `Callable<V>`   | `() -> V throws Exception` | ❌   | ✅     | Like Supplier + checked exceptions |

