package com.javaupskill.quiz.Wk1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Q02 {
    /*
     * Q2（Lambda + method reference）
     * 
     * EN: Given List<String> names, sort it by string length ascending using a
     * lambda, then rewrite the sort using a method reference (or comparator
     * factory) without changing behavior.
     * 
     * 繁中：給你 List<String> names，先用 lambda 依字串長度由小到大排序，接著在不改變行為下，
     * 把排序改寫成 method reference（或 comparator factory）的寫法。
     */

    public static void main(String[] args) {
        // My Answer
        // List<String> names = new ArrayList<>(Arrays.asList("Charlie", "Alice",
        // "Bob"));
        // names.sort(a, b -> Integer.compare(a.length(), b.length()));
        // names.sort(Integer::compare);

        // AI Answer
        List<String> names1 = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob", "Daniel"));
        List<String> names2 = new ArrayList<>(Arrays.asList("Yanny", "Zen", "Xavier", "William"));

        // lambda
        names1.sort((a, b) -> Integer.compare(a.length(), b.length()));

        // method reference / factory (same behaviour)
        names2.sort(Comparator.comparingInt(String::length));

        System.out.println("\nnames1: " + names1);
        System.out.println("\nnames2: " + names2);
    }

}
