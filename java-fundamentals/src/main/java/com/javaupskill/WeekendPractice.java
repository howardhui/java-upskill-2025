package com.javaupskill;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;

public class WeekendPractice {

        // Method may return null in old implementation
        private static String findUserById(int id) {
                if (id == 1)
                        return "Alice";
                return null; // Null checking may be missed in the method call
        }

        // Method will return whether value is existed in new implementation
        private static Optional<String> findUserByIdSafely(int id) {
                if (id == 1)
                        return Optional.of("Alice");
                return Optional.empty(); // clear expression of no value
        }

        public static void main(String[] args) {

                // ===== Lambda Exercise =====

                // Java 1.4 old method: sorting by anonynous classes
                List<String> names = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob"));

                // Old syntax
                Collections.sort(names, new Comparator<String>() {
                        @Override
                        public int compare(String a, String b) {
                                return a.compareTo(b);
                        }
                });
                System.out.println("Old sorting syntax: " + names);

                // Java 8 Lambda syntax
                List<String> names2 = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob"));
                names2.sort((a, b) -> a.compareTo(b));
                System.out.println("Lambda sorting: " + names2);

                // More concise Method Reference syntax
                List<String> names3 = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob"));
                names3.sort(String::compareTo);
                System.out.println("Method Reference sorting: " + names3);

                // ===== Function<T, R> Exercise =====
                // A function is like a converter: input class T, output class R

                // Example 1: String to Upper Case
                Function<String, String> toUpperCase = str -> str.toUpperCase();
                System.out.println("toUpperCase: hello world => " + toUpperCase.apply("hello world"));

                // Example 2: String to Integer
                Function<String, Integer> strLength = str -> str.length();
                System.out.println("String length: hello world => " + strLength.apply("hello world"));

                // andThen: Linking two functions
                Function<String, String> addExclamation = str -> str + "!";
                Function<String, String> shout = toUpperCase.andThen(addExclamation);
                System.out.println("andThen test: " + shout.apply("hello world"));

                // ===== Predicate<T> Exercise =====
                // Predicate interface returns a condition for evaluation

                Predicate<String> isLong = str -> str.length() > 5;
                Predicate<String> startsWithH = str -> str.startsWith("H");
                System.out.println("isLong: Hi => " + isLong.test("Hi")); // false
                System.out.println("isLong: Hello World => " + isLong.test("Hello World")); // true

                // and / or / negate: combined condition
                Predicate<String> isLongAndStartsWithH = isLong.and(startsWithH);
                System.out.println("isLongAndStartsWithH: Hello => " + isLongAndStartsWithH.test("Hello")); // false
                System.out.println("isLongAndStartsWithH: Hello World => " + isLongAndStartsWithH.test("Hello World")); // true

                // ===== Consumer<T> Exercise =====
                // Consumer interface takes arguement and performs action without return value

                Consumer<String> printer = str -> System.out.println(">> " + str);
                printer.accept("Hello from Consumer");

                // forEach is designed to work with the Consumer interface
                List<String> fruits = List.of("Apple", "Banana", "Cherry");
                fruits.forEach(fruit -> System.out.println("Fruit: " + fruit));
                // the same:
                fruits.forEach(printer);

                // ===== Supplier<T> Exercise =====
                // Supplier interface takes no arguement and return value like a "factory"

                Supplier<String> greeting = () -> "Hello, World!";
                System.out.println("Supplier.greeting: " + greeting.get());

                Supplier<List<String>> listFactory = () -> new ArrayList<>();
                List<String> newList = listFactory.get();
                newList.add("Item 1");
                newList.add("Item 2");
                System.out.println(newList);

                // ===== Optional Exercise =====
                // Old implementation: Cause NullPointerExcetion
                String name = findUserById(1); // Assume this method may return null
                if (name != null) {
                        System.out.println(name.toUpperCase());
                }

                // Java 8 Optional Implementation
                Optional<String> optName = findUserByIdSafely(1);
                optName.ifPresent(n -> System.out.println(n.toUpperCase()));

                // Optional.of(): value exists
                Optional<String> definite = Optional.of("Hello");

                // Optional.empty: value does not exist
                Optional<String> empty = Optional.empty();

                // Optional.ofNullable(): value may be null (Most commonly use)
                String maybeNull = null;
                Optional<String> maybe = Optional.ofNullable(maybeNull);

                // isPresent(): check if value exists
                System.out.println(definite.isPresent()); // true
                System.out.println(empty.isPresent()); // false

                // get(): return value (throw exception if value not exist)
                System.out.println(definite.get());

                // orElse(): return value if exists, return default if null
                System.out.println(empty.orElse("Default"));

                // orElseGet(): return value if exists, run supplier if null
                System.out.println(empty.orElseGet(() -> "Gerenate default"));

                // orElseThrow(): return value if exists, throw exception if null
                try {
                        empty.orElseThrow(() -> new RuntimeException("No data"));
                } catch (RuntimeException e) {
                        System.out.println("exception was thrown: " + e.getMessage());
                }

                // ifPresent(): execute only if value exists
                definite.ifPresent(v -> System.out.println("Value: " + v));
                empty.ifPresent(v -> System.out.println("No execution"));

                // map(): transform only if value exists
                Optional<Integer> len = definite.map(s -> s.length());
                System.out.println(len.orElse(0));

                // ===== Stream API Exercise =====

                // List to Stream
                List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

                // Array to Stream
                int[] arr = { 1, 2, 3, 4, 5 };
                IntStream fromArray = Arrays.stream(arr);

                // Create directly with Stream.of()
                Stream<String> fromOf = Stream.of("A", "B", "C");

                // Infinite Stream (should work with limit() method)
                Stream<Integer> infinite = Stream.iterate(0, n -> n + 1);

                // filter() method
                List<Integer> evens = numbers.stream()
                                .filter(n -> n % 2 == 0) // Keep even numbers only
                                .collect(Collectors.toList());
                System.out.println("Even numbers: " + evens); // [2, 4, 6, 8, 10]

                // maps() method
                List<String> numStrings = numbers.stream()
                                .map(n -> "Number " + n) // Add prefix for each number
                                .collect(Collectors.toList());
                System.out.println("After transformation: " + numStrings.subList(0, 3)); // Display first 3 items

                // sorted() method
                List<Integer> sorted = numbers.stream()
                                .sorted((a, b) -> b - a) // from large number to small number
                                .collect(Collectors.toList());
                System.out.println("Sorted: " + sorted); // [1, 2, 3, 4]

                // distinct() method
                List<Integer> withDup = List.of(1, 2, 2, 3, 3, 3, 4);
                List<Integer> uniqle = withDup.stream()
                                .distinct()
                                .collect(Collectors.toList());
                System.out.println("Distinct: " + uniqle);

                // limit() and skip() methods
                List<Integer> limited = numbers.stream()
                                .skip(2)
                                .limit(3)
                                .collect(Collectors.toList());
                System.out.println("skip(2).limit(3): " + limited);

                // collect() method
                List<Integer> collected = numbers.stream()
                                .filter(n -> n > 5)
                                .collect(Collectors.toList());
                System.out.println("collect: " + collected);

                // forEach() method
                System.out.println("forEach: ");
                numbers.stream()
                                .filter(n -> n <= 3)
                                .forEach(n -> System.out.println(n + " "));
                System.out.println();

                // count() method
                long count = numbers.stream()
                                .filter(n -> n % 3 == 0)
                                .count();
                System.out.println("number of multiple of 3: " + count);

                // reduce() method
                int sum = numbers.stream()
                                .reduce(0, (a, b) -> a + b);
                System.out.println("Total: " + sum); // 55

                // min() / max() methods
                Optional<Integer> max = numbers.stream()
                                .max(Integer::compareTo);
                Optional<Integer> min = numbers.stream()
                                .min(Integer::compareTo);
                System.out.println("Max: " + max.orElse(-1));
                System.out.println("Min: " + min.orElse(-1));

                // anyMatch() / allMatch() / noneMatch() methods
                boolean anyBig = numbers.stream().anyMatch(n -> n > 8);
                boolean anyPositive = numbers.stream().allMatch(n -> n > 0);
                boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
                System.out.println("number > 8: " + anyBig);
                System.out.println("All positive: " + anyPositive);
                System.out.println("No negative: " + noneNegative);

                // Real-world scenario: Find the names of qualified students from
                // the student grade list and sort them by name.
                List<String> studentNames = List.of("Diana:38", "Bob:42", "Charlie:90",
                                "Alice:85", "Eve:75");

                List<String> passingStudents = studentNames.stream()
                                .filter(s -> Integer.parseInt(s.split(":")[1]) > 50)
                                .map(s -> s.split(":")[0])
                                .sorted()
                                .collect(Collectors.toList());
                System.out.println("Passing Students: " + passingStudents);

                // ===== Java 11 String new methods exercise =====

                String text = "  Hello, World!  ";

                // isBlank(): Check if the string is empty or contains only whitespace
                // characters.
                System.out.println("isBlank: " + "   ".isBlank()); // true
                System.out.println("isBlank: " + "hi!".isBlank()); // false

                // strip(): Removes all leading and trailing whitespace from a string
                System.out.println("strip: '" + text.strip() + "'");

                // stripLeading() / stripTrailing()
                System.out.println("stripLeading: '" + text.stripLeading() + "'");
                System.out.println("stripLeading: '" + text.stripTrailing() + "'");

                // lines(): Splits string by line terminators (\n, \r, \r\n) and returns
                // Stream<String>
                String multiLine = "Line 1\nLine 2\nLine 3";
                multiLine.lines().forEach(ln -> System.out.println("Line: " + ln));

                // repeat(): Returns new string that is the original string repeated count times
                System.out.println("Ha".repeat(3));
        }
}
