package com.javaupskill;

public class InterfaceDemo {
    // Prior to Java 8, interfaces could only have abstract methods
    // Starting from Java 8, interfaces can have default implementations
    interface Greeting {
        String greet(String name); // abstract method (must be implement)

        // default method: sub-class can either override or inherit it
        default String greetPolitely(String name) {
            return "Good day, " + greet(name);
        }

        // static method: call directly using the interface name
        static String greetAll() {
            return "Hello, everyone!";
        }
    }

    public static void main(String[] args) {
        // Interface implemented with lambda
        Greeting casual = name -> "Hey, " + name + "!";

        System.out.println(casual.greet("Alice"));
        System.out.println(casual.greetPolitely("Bob"));
        System.out.println(Greeting.greetAll());
    }
}
