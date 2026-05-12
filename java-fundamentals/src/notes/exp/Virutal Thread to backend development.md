# Virutal Thread to backend development

## What Virtual Threads Mean for You as a Backend Developer / Virtual Threads 對後端開發者的實際意義

**English:** Virtual Threads (Project Loom, finalised in Java 21) are arguably the most impactful concurrency change in Java's history. For you as a Spring Boot backend developer, the single biggest takeaway is this: you can now write **simple, readable, blocking code** and still achieve the throughput that previously required complex reactive frameworks like Project Reactor or RxJava.

**繁體中文：** Virtual Threads（Project Loom，在 Java 21 正式定案）可以說是 Java 歷史上最重要的並發性變革。對你這位 Spring Boot 後端開發者而言，最重要的一點是：你現在可以寫出**簡單、可讀、阻塞式的程式碼**，同時仍能達到以前需要 Project Reactor 或 RxJava 等複雜響應式框架才能實現的吞吐量。

***

## The Root Problem They Solve / 它們解決的根本問題

**English:** Every traditional Java thread maps 1-to-1 to an **OS thread** — a heavyweight resource that consumes roughly 1–2 MB of memory each. When your backend handles 10,000 concurrent requests, each waiting on a database or HTTP call, you need 10,000 OS threads. The OS cannot manage this many efficiently, so servers hit a **thread scalability wall**: CPU and memory look fine, but throughput collapses because threads are exhausted. [linkedin](https://www.linkedin.com/pulse/boosting-java-performance-virtual-threads-21-jay-saraiya-ewejc)

**繁體中文：** 每個傳統 Java 執行緒都與一個 **OS 執行緒**一對一對應——這是一個重量級資源，每個大約消耗 1–2 MB 記憶體。當你的後端同時處理 10,000 個請求，每個都在等待資料庫或 HTTP 呼叫時，你需要 10,000 個 OS 執行緒。作業系統無法高效管理這麼多執行緒，伺服器就會撞上**執行緒可擴展性瓶頸**：CPU 和記憶體看起來沒問題，但吞吐量卻崩潰了，因為執行緒已耗盡 。 [linkedin](https://www.linkedin.com/pulse/boosting-java-performance-virtual-threads-21-jay-saraiya-ewejc)

***

## How Virtual Threads Work / Virtual Threads 的運作原理

**English:** Virtual threads are **JVM-managed objects**, not OS threads. A small fixed pool of real "carrier" OS threads runs them. The key mechanism is **mounting and unmounting**: when a virtual thread hits a blocking call (a DB query, a REST call, `Thread.sleep()`), the JVM **automatically unmounts it** from its carrier thread, parks it aside, and lets the carrier thread pick up another virtual thread immediately. When the blocking operation completes, the virtual thread is **remounted and resumes exactly where it left off**. [linkedin](https://www.linkedin.com/pulse/boosting-java-performance-virtual-threads-21-jay-saraiya-ewejc)

**繁體中文：** Virtual threads 是 **JVM 管理的物件**，不是 OS 執行緒。一個小型固定的真實「載體」OS 執行緒池來運行它們。關鍵機制是**掛載與卸載**：當一個 virtual thread 碰到阻塞呼叫（DB 查詢、REST 呼叫、`Thread.sleep()`），JVM 會**自動將它從載體執行緒卸載**，暫停在旁邊，讓載體執行緒立即接起另一個 virtual thread。當阻塞操作完成，virtual thread 會被**重新掛載並從中斷的地方繼續執行** 。 [linkedin](https://www.linkedin.com/pulse/boosting-java-performance-virtual-threads-21-jay-saraiya-ewejc)

```
Platform Thread Model:          Virtual Thread Model:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Request → OS Thread (blocked)   Request → Virtual Thread (parked)
          ⬇ waiting...                    ⬇ carrier picks up another task
          ⬇ wasted resource               ⬇ carrier comes back when IO is done
Response ← OS Thread            Response ← Virtual Thread (resumed)
```

You can create **millions** of virtual threads — they are as cheap as creating a plain Java object. [javascript.plainenglish](https://javascript.plainenglish.io/how-javas-virtual-threads-transformed-backend-development-and-why-they-ll-change-your-career-1325b2c5ea59)

***

## Practical Impact in Spring Boot / 在 Spring Boot 中的實際影響

**English:** Spring Boot 3.2+ supports virtual threads with a **single configuration line**. This is the most actionable thing you can do today: [linkedin](https://www.linkedin.com/pulse/boosting-java-performance-virtual-threads-21-jay-saraiya-ewejc)

**繁體中文：** Spring Boot 3.2+ 只需**一行設定**即可支援 virtual threads。這是你現在最可以立即行動的事 ： [linkedin](https://www.linkedin.com/pulse/boosting-java-performance-virtual-threads-21-jay-saraiya-ewejc)

```yaml
# application.properties / application.yml
spring.threads.virtual.enabled=true
```

**English:** That single property switches Tomcat's request-handling thread pool from platform threads to virtual threads. Real-world benchmarks show this can deliver a **30x reduction in thread usage** under high load while maintaining near-flat response times. Your existing blocking `RestTemplate`, `JdbcTemplate`, or `@Transactional` code works immediately — no rewrite required. [linkedin](https://www.linkedin.com/pulse/boosting-java-performance-virtual-threads-21-jay-saraiya-ewejc)

**繁體中文：** 這一個屬性就能將 Tomcat 的請求處理執行緒池從 platform threads 切換到 virtual threads。真實世界的基準測試顯示，在高負載下這可以帶來**執行緒使用量減少 30 倍**，同時維持幾乎平穩的回應時間 。你現有的阻塞式 `RestTemplate`、`JdbcTemplate` 或 `@Transactional` 程式碼立即就能運作——不需要改寫。 [linkedin](https://www.linkedin.com/pulse/boosting-java-performance-virtual-threads-21-jay-saraiya-ewejc)

***

## Virtual Threads vs. Reactive Programming / Virtual Threads vs. 響應式程式設計

| | **Virtual Threads** | **Reactive (Project Reactor / RxJava)** |
|---|---|---|
| Code style / 程式碼風格 | Blocking, sequential (readable) 阻塞、循序（可讀性高） | Non-blocking, callback chains (complex) 非阻塞、回呼鏈（複雜） |
| Learning curve / 學習曲線 | Low — same as normal threads 低——與一般執行緒相同 | High — requires reactive mindset 高——需要響應式思維 |
| Debugging / 除錯 | Normal stack traces 正常堆疊追蹤 | Complex, hard to follow 複雜，難以追蹤 |
| Best for / 最適合 | IO-bound workloads (DB, HTTP) IO 密集型工作 | Same IO-bound workloads IO 密集型工作 |
| CPU-bound? / CPU 密集型？ | ❌ No benefit — use `parallelStream()` or `ForkJoinPool` 無益 | ❌ No benefit either 同樣無益 |

**English:** Virtual threads do **not replace** reactive programming for teams already invested in it, but they eliminate the *need* to adopt it for new projects. Netflix's engineering blog noted that virtual threads introduced some subtle pitfalls around `synchronized` blocks that teams should understand. [netflixtechblog](https://netflixtechblog.com/java-21-virtual-threads-dude-wheres-my-lock-3052540e231d)

**繁體中文：** Virtual threads **不會取代**已深度投入響應式框架的團隊的現有選擇，但它們消除了新專案*採用*響應式框架的*必要性*。Netflix 的工程部落格指出，virtual threads 在 `synchronized` 區塊上引入了一些微妙的陷阱，團隊應該了解 。 [netflixtechblog](https://netflixtechblog.com/java-21-virtual-threads-dude-wheres-my-lock-3052540e231d)

***

## ⚠️ Key Pitfall — `synchronized` Pinning / 關鍵陷阱——`synchronized` 釘住問題

**English:** If a virtual thread executes code inside a `synchronized` block and then hits a blocking call, the JVM **cannot unmount it** — the virtual thread is "pinned" to its carrier OS thread, blocking it. This kills the scalability benefit. The fix is to replace `synchronized` with `ReentrantLock`, which virtual threads handle correctly. [dev](https://dev.to/dhellano_castro_c5aba0c56/virtual-threads-in-java-21-the-end-of-the-scarcity-era-and-the-pitfalls-that-can-take-you-down-4bml)

**繁體中文：** 如果一個 virtual thread 在 `synchronized` 區塊內執行並碰到阻塞呼叫，JVM **無法將其卸載**——virtual thread 被「釘住」在它的載體 OS 執行緒上，阻塞了它。這會破壞可擴展性的優勢。解決方法是用 `ReentrantLock` 取代 `synchronized`，virtual threads 能正確處理它 。 [dev](https://dev.to/dhellano_castro_c5aba0c56/virtual-threads-in-java-21-the-end-of-the-scarcity-era-and-the-pitfalls-that-can-take-you-down-4bml)

```java
// ⚠️ Pitfall — pins the carrier thread / 陷阱——會釘住載體執行緒
synchronized (lock) {
    result = database.query(...);  // blocks here, carrier is stuck
}

// ✅ Fix — ReentrantLock allows unmounting / 修正——ReentrantLock 允許卸載
lock.lock();
try {
    result = database.query(...);  // virtual thread parks, carrier is free
} finally {
    lock.unlock();
}
```

***

## 🎤 Interview Cheat Sheet / 面試重點清單

When asked about Virtual Threads in an interview, structure your answer around these six points: [reddit](https://www.reddit.com/r/java/comments/1clkz4d/how_effective_are_java_21_virtual_threads/)

面試被問到 Virtual Threads 時，圍繞以下六個重點組織你的答案 ： [dzone](https://dzone.com/articles/java-concurrency-evolution-virtual-threads-java21)

1. **Define it clearly / 清楚定義：** "Virtual threads are JVM-managed lightweight threads from Project Loom, finalized in Java 21. They map many virtual threads onto a small pool of OS carrier threads." / 「Virtual threads 是 Project Loom 的 JVM 管理輕量執行緒，在 Java 21 正式定案。它們將大量 virtual threads 映射到少量 OS 載體執行緒上。」

2. **State the problem it solves / 說明它解決的問題：** "Traditional platform threads are OS-thread-backed — expensive, limited in number. IO-bound servers hit a thread wall even when CPU is idle." / 「傳統 platform threads 由 OS 執行緒支撐——昂貴且數量有限。IO 密集型伺服器即使 CPU 閒置也會撞上執行緒瓶頸。」

3. **Explain mount/unmount / 解釋掛載/卸載：** "When a virtual thread blocks on IO, the JVM automatically parks it and frees the carrier thread for other work — no thread is wasted waiting." / 「當 virtual thread 在 IO 上阻塞時，JVM 自動將其暫停並釋放載體執行緒給其他工作——沒有執行緒在等待中被浪費。」

4. **Spring Boot integration / Spring Boot 整合：** "In Spring Boot 3.2+, one property — `spring.threads.virtual.enabled=true` — enables virtual threads across the entire Tomcat layer." / 「在 Spring Boot 3.2+ 中，一個屬性就能在整個 Tomcat 層啟用 virtual threads。」

5. **Know the limits / 了解限制：** "Virtual threads don't help CPU-bound tasks — use `ForkJoinPool` or `parallelStream()` for those. And `synchronized` blocks can cause thread pinning — prefer `ReentrantLock`." / 「Virtual threads 對 CPU 密集型任務沒有幫助——那種情況用 `ForkJoinPool` 或 `parallelStream()`。而且 `synchronized` 區塊可能造成執行緒釘住——優先使用 `ReentrantLock`。」

6. **Compare to reactive / 與響應式比較：** "Virtual threads achieve similar IO throughput to reactive frameworks but with blocking-style code that is far easier to write, debug, and maintain." / 「Virtual threads 達到與響應式框架相似的 IO 吞吐量，但使用阻塞式程式碼，遠比響應式程式碼更易撰寫、除錯和維護。」