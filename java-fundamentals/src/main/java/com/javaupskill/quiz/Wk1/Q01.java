package com.javaupskill.quiz.Wk1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Q01 {
    /*
     * Q1（Lambda — functional interface + behavior parameter）
     * 
     * EN: Write a method filter(List<Integer> xs, Predicate<Integer> p)
     * that returns only numbers matching p, then call it with a lambda to keep only
     * even numbers.
     * 
     * 繁中：寫一個方法 filter(List<Integer> xs, Predicate<Integer> p) 回傳符合 p 的數字，
     * 並用 lambda 呼叫它，只保留偶數。
     */

    // My Answer
    // Integer filter(List<Integer> xs, Predicate<Integer> p) {
    // xs.stream().filter(i -> i % 2 == 0).forEach(i ->
    // System.out.println(p.test(i));
    // }

    // AI Answer
    static List<Integer> filter(List<Integer> xs, Predicate<Integer> p) {
        return xs.stream()
                .filter(p)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Integer> xInt = new ArrayList<>(Arrays.asList(21, 384, 232, 121));
        List<Integer> evens = filter(xInt, i -> i % 2 == 0);

        System.out.println(evens);
    }
}
