package com.javaupskill;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class WeekOneReview {

    public static void main(String[] args) {

        // Task 1: Create a List containing 5 integers
        // Try using Java 8+ syntax (Hint: List.of())
        List<Integer> l = List.of(31, 12, 3, 24, 45);

        // Task 2: Use the Stream API to filter out all numbers greater than 10
        // and print the result (Hint: .stream().filter().forEach())
        l.stream().filter(i -> i > 10).forEach(System.out::println);

        // Task 3: Use the Stream API to calculate the sum of the 5 numbers
        // and print the result (Hint: .stream().mapToInt().sum())
        System.out.println(l.stream().mapToInt(i -> i).sum());

        // Task 4: Use a lambda expression to sort the 5 numbers from largest to
        // smallest
        // and print the result (Hint: .stream().sorted())
        l.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
    }
}