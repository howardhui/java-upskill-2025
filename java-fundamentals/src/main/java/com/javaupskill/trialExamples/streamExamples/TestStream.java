package com.javaupskill.trialExamples.streamExamples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestStream {

    private static final Logger LOGGER = Logger.getLogger(TestStream.class.getName());

    public static int divideListElements(List<Integer> values, int divider) {
        return values.stream()
                .reduce(0, (a, b) -> divide(a, divider) + divide(b, divider));
    }

    private static int divide(int value, int factor) {
        int result = 0;
        try {
            result = value / factor;
        } catch (ArithmeticException e) {
            LOGGER.log(Level.INFO, "Arithmetic Exception: Division by ZERO");
        }
        return result;
    }

    private static List<Integer> getEvenNumbers(int offset, int limit) {
        return Stream.iterate(0, i -> i + 1)
                    .filter(i -> i % 2 == 0)
                    .skip(offset)
                    .limit(limit)
                    .collect(Collectors.toList());
    }

    private long counter;

    public long getCounter() {
        return counter;
    }

    private void wasCalled() {
        counter++;
    }

    private List<String> test(List<String> list) {
        counter = 0;
        // Stream<String> stream = list.stream()
        //                             .filter(e -> {
        //                                 wasCalled();
        //                                 return e.contains("2");
        //                             });

        // Optional<String> stream = list.stream()
        //                             .filter(e -> {
        //                                 // wasCalled();
        //                                 LOGGER.info("filter() was called: " + e);
        //                                 return e.contains("2");
        //                             }).map(e -> {
        //                                 LOGGER.info("map() was called");
        //                                 return e.toUpperCase();
        //                             }).findFirst();

        List<String> l = list.stream()
                            .skip(2)
                            .map(e -> {
                                LOGGER.info("map() was called");
                                wasCalled();
                                return e.substring(0, 3);
                            })
                            .collect(Collectors.toList());
        return l;
    }

    public static void main(String[] args) {
        // List<Integer> num = Arrays.asList(1, 2, 3, 4, 5, 6);

        // System.out.println("1 to 6 / 1: " + divideListElements(num, 1));
        // num = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
        // System.out.println("0 to 6 / 1: " + divideListElements(num, 1));
        // num = Arrays.asList(1, 2, 3, 4, 5, 6);
        // System.out.println("1 to 6 / 0: " + divideListElements(num, 0));

        User john = new User("John", 30);
        john.getRating().add(new Review(5, ""));
        john.getRating().add(new Review(3, "Not bad!"));
        User julie = new User("Julie", 35);
        julie.getRating().add(new Review(4, "great!"));
        julie.getRating().add(new Review(2, "terrible experience"));
        julie.getRating().add(new Review(4, ""));
        List<User> users = Arrays.asList(john, julie);

        Rating averageRating = users.stream()
                                    .reduce(new Rating(), 
                                            (rating, user) -> Rating.average(rating, user.getRating()), 
                                            Rating::average);
        System.out.println("averageRating.getPoints() = " + averageRating.getPoints());
        double points = users.stream()
                            .map(User::getRating)
                            .reduce(new Rating(), Rating::average)
                            .getPoints();
        System.out.println("points = " + points);

        IntStream intStream = IntStream.of(3,5,7,8,9,10,13,14,15,17,18).parallel();
        // List<Integer> intList = intStream.filter(i -> i % 3 == 0).boxed().collect(Collectors.toList());
        List<Integer> intList = intStream.filter(i -> i % 3 == 0).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        // intStream = intStream.filter(i -> i % 3 == 0);
        // OptionalInt ans = intStream.findAny();
        // OptionalInt ans = intStream.findFirst();
        // if (ans.isPresent()) System.out.println(ans.getAsInt());
        Optional<Integer> ans1 = intList.stream().findAny();
        Optional<Integer> ans2 = intList.stream().findFirst();
        if (ans1.isPresent()) System.out.println(ans1.get());
        if (ans2.isPresent()) System.out.println(ans2.get());
    
        System.out.println(getEvenNumbers(0, 4));
        System.out.println(getEvenNumbers(8, 4));
    
        List<String> list = Arrays.asList("abc1", "abc2", "abc3");
        // long size = list.stream()
        //                 .skip(1)
        //                 .map(element -> element.substring(0, 3))
        //                 .sorted()
        //                 .count();
        // System.out.println(size);
        TestStream testStream = new TestStream();
        // testStream.test(list);
        System.out.println("new list = " + testStream.test(list));
        System.out.println("counter = " + testStream.getCounter());
    }
}
