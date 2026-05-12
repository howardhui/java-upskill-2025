package com.javaupskill.quiz.Wk1;

public class Q03 {
    /*
     * Q3（Default interface method — “evolve without breaking” + optional override）
     * 
     * EN: Create an interface Loggable with a default void log(String msg)
     * implementation. Create a class ConsoleLogger that uses the default
     * implementation, and another class SilentLogger that overrides log to do
     * nothing. Show both being called.
     * 
     * 繁中：建立介面 Loggable，提供 default void log(String msg) 的預設實作。建立 ConsoleLogger
     * 使用預設實作，再建立 SilentLogger override log 讓它什麼都不做。示範兩者都被呼叫。
     */

    // My Answer
    // public interface Loggable {
    // default void log(String msg);
    // }
    // public class ConsoleLogger extends Loggable {}
    // public class SilentLogger extends Loggable {
    // public void log(String msg) {};
    // }
    // ConsoleLogger cl = new ConsoleLogger();
    // SilentLogger sl = new SilentLogger();
    // cl.log();
    // sl.log();

    public static interface Loggable {
        default void log(String msg) {
            System.out.println(msg);
        }
    }

    public static class ConsoleLogger implements Loggable {
        // uses default log()
    }

    public static class SilentLogger implements Loggable {
        @Override
        public void log(String msg) {
            // intentionally empty
        }
    }

    public static void main(String[] args) {
        ConsoleLogger cl = new ConsoleLogger();
        SilentLogger sl = new SilentLogger();

        cl.log("cl.hello");
        sl.log("sl.hello");
    }
}
