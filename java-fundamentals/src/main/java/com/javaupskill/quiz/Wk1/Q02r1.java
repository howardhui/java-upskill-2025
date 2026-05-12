package com.javaupskill.quiz.Wk1;

import java.util.concurrent.Callable;

/*
 * Q2（Lambda — overload ambiguity: Runnable vs Callable）
 * 
 * EN: Create two overloaded methods runTask(Runnable r) and runTask(Callable<String> c). 
 * Show one lambda call that is ambiguous (won’t compile) and then show two fixed calls using explicit 
 * casting or a typed lambda so each overload is chosen intentionally.
 * 
 * 繁中：建立兩個多載方法 runTask(Runnable r) 與 runTask(Callable<String> c)。示範一個會造成 多載模糊（無法編譯）
 * 的 lambda 呼叫，然後用「明確轉型」或「指定型別的 lambda」各修正一次，讓兩個多載都能被刻意選中。
 */
public class Q02r1 {
    // My Answer
    public String runTask(Runnable r) {
        return "Runnable";
    }

    public String runTask(Callable<String> c) {
        return "Callable";
    }

    public static class Task02 implements Runnable, Callable<String> {
        @Override
        public void run() {
        }

        @Override
        public String call() throws Exception {
            return "call method";
        }
    }

    public static void main(String[] args) {
        Q02r1 q = new Q02r1();
        Task02 task = new Task02();

        // This is ambiguous because `task` is BOTH Runnable and Callable<String>.
        // System.out.println(q.runTask(task)); // won't compile: reference is ambiguous

        // Fix 1: explicit casting (choose overload intentionally)
        System.out.println(q.runTask((Runnable) task));
        System.out.println(q.runTask((Callable<String>) task));

        // Fix 2: typed lambda (choose overload intentionally)
        Runnable r = () -> System.out.println("running");
        Callable<String> c = () -> "calling";
        System.out.println(q.runTask(r));
        System.out.println(q.runTask(c));
    }
}
