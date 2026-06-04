package com.javaupskill;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Week2SundayReinforcement {
    // Reinforcement for comprehensive Stream + Text Blocks design
    record Transaction(String id, String type, double amount, String currency,
            boolean successful) {
    }

    public static void main(String[] args) {
        List<Transaction> transcations = List.of(
                new Transaction("T001", "PAYMENT", 150.0,
                        "GBP", true),
                new Transaction("T002", "REFUND", 50.0,
                        "GBP", true),
                new Transaction("T003", "PAYMENT", 300.0,
                        "USD", false),
                new Transaction("T004", "TRANSFER", 1000.0,
                        "GBP", true),
                new Transaction("T005", "PAYMENT", 75.0,
                        "EUR", true),
                new Transaction("T006", "PAYMENT", 200.0,
                        "GBP", false),
                new Transaction("T007", "TRANSFER", 500.0,
                        "GBP", true));

        // Exercise: Use Stream + Text Blocks to generate a report:
        // 1. Total amount of successful GBP transactions
        // 2. Count of failed transactions
        // 3. Group transactions by type, count each
        // 4. Format the results as a JSON summary using Text Blocks

        // Attempt these yourself before asking AI for help
        // --- Your answer below ---
        double successfulGbpTotal = transcations.stream()
                .filter(Transaction::successful)
                .mapToDouble(Transaction::amount)
                .sum();
        long failedCount = transcations.stream()
                .filter(t -> !t.successful())
                .count();
        Map<String, Long> byType = transcations.stream()
                .collect(Collectors
                        .groupingBy(Transaction::type, Collectors.counting()));

        String report = """
                {
                    "successfulGbpTotal": %.2f,
                    "failedCount": %d,
                    "byType": "%s"
                }
                """.formatted(successfulGbpTotal,
                failedCount,
                byType);

        System.out.println(report);
    }
}
