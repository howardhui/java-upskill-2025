package com.javaupskill;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Week2Saturday {
    // Question 1:
    // Create a BiFunction that accepts two Strings,
    // Return the longer substring
    // If the lengths are the same, return the first one
    static BiFunction<String, String, String> longerString = (s1,
            s2) -> s1.length() >= s2.length() ? s1 : s2;

    // Question 2:
    // Given the following order data, using Stream() method to complete four analyses
    record Order(String orderId,
            String customer,
            String product,
            double amount,
            boolean delivered) {
    }

    static void analyzeOrders() {
        List<Order> orders = List.of(
                new Order("O001", "Alice", "Laptop", 1200.0, true),
                new Order("O002", "Bob", "Phone", 800.0, false),
                new Order("O003", "Alice", "Tablet", 500.0, true),
                new Order("O004", "Charlie", "Laptop", 1200.0, true),
                new Order("O005", "Bob", "Headphones", 150.0, true),
                new Order("O006", "Alice", "Phone", 800.0, false));

        // Analysis 1: Find the orderId of all undelivered orders
        List<String> pendingOrderIds = orders.stream()
                .filter(o -> !o.delivered())
                .map(Order::orderId)
                .toList();
        System.out.println("Undelivered: " + pendingOrderIds);

        // Analysis 2: Calculate Alice's total spending
        double aliceTotal = orders.stream()
                .filter(o -> o.customer().equals("Alice"))
                .mapToDouble(Order::amount)
                .sum();
        System.out.printf("Alice's total spending: £%.2f%n", aliceTotal);

        // Analysis 3: Group by customer and calculate the number of orders per person
        Map<String, Long> orderCountByCustomer = orders.stream()
                .collect(Collectors.groupingBy(Order::customer, Collectors.counting()));
        System.out.println("Number of orders: " + orderCountByCustomer);

        // Analysis 4: Find the delivered order with the highest amount
        Optional<Order> topDelivered = orders.stream()
                .filter(Order::delivered)
                .max((o1, o2) -> Double.compare(o1.amount(), o2.amount()));
        // AI suggeston: 
        // .max(Comparator.comparingDouble(Order::amount));
        topDelivered.ifPresent(o -> System.out.println("Highest amount delivered: " +
                o.orderId() + "（£" + o.amount() + ")"));
    }

    // Problem 3:
    // Design a simple "Payment Method" system
    // Requirements:
    // Use a Sealed Interface to restrict to only three types of payment methods:
    // - CreditCard: Requires cardNumber (String) and expiryDate (String)
    // - BankTransfer: Requires accountNumber (String) and sortCode (String)
    // - Cash: Requires amount (double)
    // Write a method to return the transaction fee (double) based on the payment method
    // - CreditCard: 1.5% of amount
    // - BankTransfer: £0.50 fixed fee
    // - Cash: Free

    /* Your answer: Define the Sealed Interface and three Records here */
    sealed interface PaymentMethod permits CreditCard, BankTransfer, Cash {
        double transactionFee(double total);
    }

    record CreditCard(String cardNumber, String expiryDate) implements PaymentMethod {
        public double transactionFee(double total) {
            return 0.015 * total;
        }
    }

    record BankTransfer(String accountNumber, String sortCode) implements PaymentMethod {
        public double transactionFee(double total) {
            return 0.50;
        }
    }

    record Cash(double amount) implements PaymentMethod {
        public double transactionFee(double total) {
            return 0;
        }
    }

    // Question 4:
    // Complete the method generateTicketSummary(String status)
    // - Use the switch expression to return a JSON-formatted summary string
    //   according to the status (OPEN/IN_PROGRESS/RESOLVED/CLOSED)
    // - The JSON must include three fields: status, emoji, and description
    // - Text Blocks must be used

    static String generateTicketSummary(String status) {
        return switch (status) {
            case "OPEN" -> """
                    {
                        "status":"OPEN",
                        "emoji":"🆕",
                        "description":"Case is opened"
                    }
                    """;
            case "IN_PROGRESS" -> """
                    {
                        "status":"IN_PROGRESS",
                        "emoji":"⏳",
                        "description":"Case is in progress"
                    }
                    """;
            case "RESOLVED" -> """
                    {
                        "status":"RESOLVED",
                        "emoji":"🎯",
                        "description":"Case is resolved"
                    }
                    """;
            case "CLOSED" -> """
                    {
                        "status":"CLOSED",
                        "emoji":"✅",
                        "description":"Case is closed"
                    }
                    """;
            default -> """
                    {
                        "status":"ERROR",
                        "emoji":"⚠️",
                        "description":"Invalid status, no ticket summary is generated"
                    }
                    """;
        };
    }

    // Question 5:
    // Given a List<String>, each string is formatted as "name:score"
    // For example: ["Alice:85", "Bob:42", "Charlie:90", "Diana:55"]
    //
    // Create a method that returns a formatted report string (using Text Blocks):
    // - List the passed (>=50) students, sorted by score from highest to lowest
    // - List the failed (<50) students
    // - Calculate the class average score
    // Complete with Stream() method instead of using for-loop
    //
    static String generateClassReport(List<String> rawData) {
        // AI suggeston:
        // record Student(String name, int score) {}
        // List<Student> students = rawData.stream()
        //         .map(s -> new Student(s.split(":")[0], Integer.parseInt(s.split(":")[1])))
        //         .toList();
        Map<String, Integer> mapData = rawData.stream().collect(Collectors.toMap(
                s -> s.split(":")[0],
                s -> Integer.parseInt(s.split(":")[1])));

        // List<Student> passed = students.stream()
        //         .filter(s -> s.score() >= 50)
        //         .sorted(Comparator.comparingInt(Student::score).reversed())
        //         .toList();
        List<String> passed = mapData.keySet().stream()
                .filter(k -> mapData.get(k) >= 50)
                .sorted(Comparator.comparingInt(mapData::get).reversed())
                .toList();

        // List<Student> failed = students.stream()
        //         .filter(s -> s.score() < 50)
        //         .toList();
        List<String> failed = mapData.keySet().stream()
                .filter(k -> mapData.get(k) < 50)
                .toList();

        // AI suggestion (with Student class)
        // OptionalDouble classAverage = students.stream()
        //         .mapToInt(s -> s.score())
        //         .average();
        double classAverage = mapData.values().stream()
                // AI suggestion:
                // .mapToInt(Integer::intValue)
                // .average();
                .collect(Collectors.averagingInt(Integer::intValue));

        return """
                Passed students: %s
                Failed students: %s
                Class average score: %.2f
                """.formatted(passed, failed, classAverage);
        // AI suggestion:
        // """.formatted(
        // passed.stream()
        //         .map(s -> s.name() + " (" + s.score() + ")")
        //         .collect(Collectors.joining(", ")),
        // failed.stream()
        //         .map(s -> s.name() + " (" + s.score() + ")")
        //         .collect(Collectors.joining(", ")),
        // classAverage.orElse(0.0));
    }

    public static void main(String[] args) {
        // Question 1
        System.out.println("=== Question 1 ===");
        System.out.println(longerString.apply("Hello", "Hi"));
        System.out.println(longerString.apply("Java", "Python"));
        System.out.println(longerString.apply("Alice", "Peter"));

        // Question 2
        System.out.println("\n=== Question 2 ===");
        analyzeOrders();

        // Test Question 3
        /* Create three payment methods and calculate the handling fee */
        System.out.println("\n=== Question 3 ===");
        List<PaymentMethod> payments = List.of(
                new CreditCard("1234567890123456", "06/2029"),
                new BankTransfer("12345678", "12-34-56"),
                new Cash(2000));
        double amount = 1000;
        payments.forEach(payment -> {
            System.out.println(
                    switch (payment) {
                        case CreditCard cc -> "Total handling fee (Credit Card): "
                                + cc.transactionFee(amount);
                        case BankTransfer bt -> "Total handling fee (Bank Transfer): "
                                + bt.transactionFee(amount);
                        case Cash c -> "Total handling fee (Cash): " + c.transactionFee(amount);
                    });
        });

        // Test Question 4
        System.out.println("\n=== Question 4 ===");
        System.out.println(generateTicketSummary("OPEN"));
        System.out.println(generateTicketSummary("RESOLVED"));
        System.out.println(generateTicketSummary("EXPLAIN"));

        // Test Question 5
        System.out.println("\n=== Question 5 ===");
        List<String> classData = List.of(
                "Alice:85", "Bob:42", "Charlie:90",
                "Diana:55", "Eve:38", "Frank:73");
        System.out.println(generateClassReport(classData));
    }
}
