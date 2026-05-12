package com.javaupskill.trialExamples;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class TestOptional {
    public static void main(String[] args) {
        String expected = "properValue";
        // String defaultString = "default";

        // Optional<String> value = Optional.of(expected);
        // Optional<String> defaultValue = Optional.of("default");
        // Optional<String> value = Optional.empty();
        // Optional<String> defaultValue = Optional.of(defaultString);

        // Optional<String> result = value.or(() -> defaultValue);

        // System.out.println("check: " + result.get().equals(expected));
        // System.out.println("check: " + result.get().equals(defaultString));

        Optional<String> value = Optional.of(expected);
        AtomicInteger successCounter = new AtomicInteger(0);
        AtomicInteger onEmptyOptionalCounter = new AtomicInteger(0);

        value.ifPresentOrElse(
                v -> successCounter.incrementAndGet(),
                onEmptyOptionalCounter::incrementAndGet);

        System.out.println("sucessCounter.get() ==  1: " + String.valueOf(successCounter.get() == 1));
        System.out.println("onEmptyOptionalCounter.get() ==  0: " + String.valueOf(onEmptyOptionalCounter.get() == 0));
    }
}
