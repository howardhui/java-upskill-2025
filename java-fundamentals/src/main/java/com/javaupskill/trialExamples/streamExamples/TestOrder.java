package com.javaupskill.trialExamples.streamExamples;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TestOrder {
    private static final Logger LOGGER = Logger.getLogger(TestStream.class.getName());

    private long counter;

    public long getCounter() {
        return counter;
    }

    private void wasCalled() {
        counter++;
    }

    private List<String> test(List<String> list) {
        counter = 0;
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
        List<String> list = Arrays.asList("abc1", "abc2", "abc3");
        TestOrder testOrder = new TestOrder();

        System.out.println("new list = " + testOrder.test(list));
        System.out.println("counter = " + testOrder.getCounter());
    }
}
