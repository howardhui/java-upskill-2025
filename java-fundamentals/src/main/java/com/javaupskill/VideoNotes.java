package com.javaupskill;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import static com.javaupskill.VideoNotes.CustomerRegistrationValidator.*;
import static com.javaupskill.VideoNotes.CustomerRegistrationValidator.ValidationResult.*;

/**
 * Amigoscode — Java Functional Programming
 * Video Notes and follow-up exercises
 * Date: 25/05/2026
 */
public class VideoNotes {
    public static void main(String[] args) {
        // ===== Predicate combination operation =====   
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);
        Predicate<Integer> isEvenOrNegative = isEven.or(isPositive.negate());

        List<Integer> numbers = List.of(-4, -3, -2, -1, 0, 1, 2, 3, 4);
        System.out.println("Even and Positive: " +
                numbers.stream()
                        .filter(isEvenAndPositive)
                        .toList());

        // ===== Function Concatenation: andThen vs compose =====
        Function<Integer, Integer> multiplyBy2 = n -> n * 2;
        Function<Integer, Integer> add10 = n -> n + 10;

        // andThen: execute multiplyBy2 first, and pass the result to execute add10
        // Input 3 → 3 * 2 = 6 → 6 + 10 = 16
        Function<Integer, Integer> multiplyThenAdd = multiplyBy2.andThen(add10);

        // compose: execute add10 first, and pass the result to execute multiplyBy2
        // Input 3 → 3 + 10 = 13 → 13 * 2 = 26
        Function<Integer, Integer> addThenMultiply = multiplyBy2.compose(add10);

        System.out.println("andThen(3): " + multiplyThenAdd.apply(3)); // 16
        System.out.println("compose(3): " + addThenMultiply.apply(3)); // 26

        // ===== Combinator Pattern ======

        var customer = new Customer(
                "Alice",
                "alice@gmail.com",
                "+07770087906",
                LocalDate.of(2000, 1, 1));

        // How combinators work
        ValidationResult result = isEmailValid()
                .and(isPhoneValid())
                .and(isAnAdult())
                .apply(customer);

        System.out.println(result);

        // another example
        var validCustomer = new Customer2("Alice", "alice@email.com",
                "+447911123456", 30);
        var invalidCustomer = new Customer2("Bob", "bob-no-at-sign",
                "07911123456", 15);

        // Chain validators using .and()
        CustomerValidator fullValidation = isCEmailValid()
                .and(isCPhoneNumberValid())
                .and(isCAgeInRange());

        System.out.println("Valid customer:   " + fullValidation.apply(validCustomer));
        System.out.println("Invalid customer: " + fullValidation.apply(invalidCustomer));

        // ===== Callback function as argument ======
        hello("John", null,
                value -> System.out.println("No lastname provided for " + value));
        hello2("John", null,
                () -> System.out.println("No lastname provided"));

        // ===== Rules of pure functional programming =====
        // 1. Pure function -> No state:
        //    function should not depend on global state
        // 2. No side effects:
        //    function should not have side effect outside of the function
        // 3. Higher order functions:
        //    Match either one of below rules:
        //    * The function takes one or more functions as parameters.
        //    * The function returns another function as result.

        // ===== Four types of Method References =====

        // Type 1: Reference to a Static Method（ClassName::staticMethod）
        // Equivalent to: n -> Math.abs(n)
        Function<Integer, Integer> absValue = Math::abs;
        System.out.println("Reference to a Static Method: " + absValue.apply(-5)); // 5

        // Type 2: Reference to an Instance Method of a Particular Object（instance::method）
        String prefix = "Hello, ";
        // Equivalent to: s -> prefix.concat(s)
        Function<String, String> greet = prefix::concat;
        System.out.println("Reference to an Instance Method of a Particular Object: " + greet.apply("Alice"));

        // Type 3：Reference to an Instance Method of an Arbitrary Object of a Particular Type（ClassName::instanceMethod）
        // Equivalent to: s -> s.toUpperCase()
        Function<String, String> toUpper = String::toUpperCase;
        System.out.println("Reference to an Instance Method of an Arbitrary Object of a Particular Type: "
                + toUpper.apply("hello"));

        // Type 4: Reference to a Constructor（ClassName::new）
        // Equivalent to: s -> new StringBuilder(s)
        Function<String, StringBuilder> sbFactory = StringBuilder::new;
        StringBuilder sb = sbFactory.apply("Initial content");
        System.out.println("Reference to a Constructor: " + sb);
    }

    record Customer(String name, String email, String phoneNumber, LocalDate dob) {
    }

    // Implementation of combinators
    interface CustomerRegistrationValidator
            extends Function<Customer, ValidationResult> {

        static CustomerRegistrationValidator isEmailValid() {
            return customer -> customer.email().contains("@")
                    ? SUCCESS
                    : EMAIL_INVALID;
        }

        static CustomerRegistrationValidator isPhoneValid() {
            return customer -> customer.phoneNumber().startsWith("+0")
                    ? SUCCESS
                    : PHONE_NUMBER_INVALID;
        }

        static CustomerRegistrationValidator isAnAdult() {
            return customer -> Period.between(customer.dob(), LocalDate.now()).getYears() > 16
                    ? SUCCESS
                    : IS_NOT_AN_ADULT;
        }

        default CustomerRegistrationValidator and(CustomerRegistrationValidator other) {
            return customer -> {
                ValidationResult result = this.apply(customer);
                return result == SUCCESS
                        ? other.apply(customer)
                        : result;
            };
        }

        enum ValidationResult {
            SUCCESS, PHONE_NUMBER_INVALID, EMAIL_INVALID, IS_NOT_AN_ADULT
        }
    }

    // Implementation of callback function as argument
    static void hello(String firstName, String lastName, Consumer<String> callback) {
        System.out.println(firstName);
        if (lastName != null) {
            System.out.println(lastName);
        } else {
            callback.accept(firstName);
        }
    }

    static void hello2(String firstName, String lastName, Runnable callback) {
        System.out.println(firstName);
        if (lastName != null) {
            System.out.println(lastName);
        } else {
            callback.run();
        }
    }

    // ===== Combinator Pattern =====
    // A functional approach to combining validation rules
    // Each validator is a Function that returns a ValidationResult

    // Validation result type
    enum ValidationResult2 {
        SUCCESS, PHONE_NUMBER_NOT_VALID, EMAIL_NOT_VALID, AGE_NOT_IN_RANGE
    }

    // Customer to validate
    record Customer2(String name, String email, String phoneNumber, int age) {
    }

    // Functional interface for customer validation
    @FunctionalInterface
    interface CustomerValidator extends Function<Customer2, ValidationResult2> {
        // Combine two validators: run this, then run other if success
        default CustomerValidator and(CustomerValidator other) {
            return customer -> {
                ValidationResult2 result = this.apply(customer);
                return result == ValidationResult2.SUCCESS
                        ? other.apply(customer)
                        : result;
            };
        }
    }

    // Individual validators defined as lambdas
    static CustomerValidator isCEmailValid() {
        return customer -> customer.email().contains("@")
                ? ValidationResult2.SUCCESS
                : ValidationResult2.EMAIL_NOT_VALID;
    }

    static CustomerValidator isCPhoneNumberValid() {
        return customer -> customer.phoneNumber().contains("+44")
                ? ValidationResult2.SUCCESS
                : ValidationResult2.PHONE_NUMBER_NOT_VALID;
    }

    static CustomerValidator isCAgeInRange() {
        return customer -> customer.age() >= 18 && customer.age() <= 100
                ? ValidationResult2.SUCCESS
                : ValidationResult2.AGE_NOT_IN_RANGE;
    }

    // ===== Callback Functions =====
    // Passing behaviour (function) as a parameter
    // This is the foundation of event-driven programming

    // Without callback: rigid, does one fixed thing
    static void processDataOldWay(List<Integer> data) {
        // Always prints — cannot change behaviour
        data.forEach(System.out::println);
    }

    // With callback: flexible, caller decides what to do
    static void processData(
            List<Integer> data,
            Consumer<Integer> callback) {
        data.forEach(callback);
    }

}
