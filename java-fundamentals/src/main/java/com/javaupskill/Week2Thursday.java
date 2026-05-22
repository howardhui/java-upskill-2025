package com.javaupskill;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Week2Thursday {
    public static void main(String[] args) {
        System.out.println("=== Part 1: Switch Syntax Evolution ===\n");
        switchEvolution();

        System.out.println("\n=== Part 2: Pattern Matching for Switch ===\n");
        patternMatchingSwitch();

        System.out.println("\n=== Part 3: Guarded Patterns (when) ===\n");
        guardedPatterns();

        System.out.println("\n=== Part 4: Null Handling in Switch===\n");
        nullHandling();

        System.out.println("\n=== Part 5: Ticketing Scenario Refactoring ===\n");
        ticketRefactoring();
    }

    static void switchEvolution() {
        // Scenario: Calculating allowances based on employee grade level
        String grade = "SENIOR";

        // Version 1: Switch statement in Java 1.4
        // Problem: fall-through (forget a break and execute the next case)
        int oldAllowance;
        switch (grade) {
            case "JUNIOR":
                oldAllowance = 500;
                break; // bug exist if forget to code this line
            case "SENIOR":
                oldAllowance = 1000;
                break;
            case "LEAD":
                oldAllowance = 1500;
                break;
            default:
                oldAllowance = 0;
        }
        System.out.println("Old style Switch (Java 1.4): £" + oldAllowance);

        // Version 2: Java 14 Switch Expression (-> syntax)
        // Improvements:
        // 1. Replace with ->: no more "break" and no fall-through issues
        // 2. switch is an "expression" and assign values directly
        // 3. Multiple cases can be combined (separated by commas)
        int newAllowance = switch (grade) {
            case "JUNIOR" -> 500;
            case "SENIOR" -> 1000;
            case "LEAD", "PRINCIPAL" -> 1500; // combine multiple cases
            default -> 0;
        };
        System.out.println("New style Switch (Java 14): £" + newAllowance);

        // Version 3: Using yield keyword for multi-line logic
        // If case has more than one line logic, using yield to return value
        int complexAllowance = switch (grade) {
            case "JUNIOR" -> {
                System.out.println("   (Calculate JUNIOR allowance...) ");
                int base = 500;
                int bonus = 50;
                yield base + bonus; // replace return with yield
            }
            case "SENIOR" -> {
                System.out.println("   (Calculate SENIOR allowance...) ");
                yield 1000;
            }
            default -> 0;
        };
        System.out.println("Multi-line logic Switch (yield): £" + complexAllowance);

        // Key-point summary
        System.out.println("\nOld-style switch is a statement (executes an action)");
        System.out.println("New-style switch is an expression (produces a value)");
        System.out.println("It can be directly used in assignment, return, and method parameters");
    }

    static void patternMatchingSwitch() {
        // Scenario: Processing different types of API input values
        // Practically, after a REST API receives JSON, the type of some fields may uncertain.

        Object[] inputs = {
                42,
                3.14,
                "Hello",
                true,
                List.of(1, 2, 3),
                null
        };

        System.out.println("Processing different types of input values: ");
        for (Object input : inputs) {
            String result = describeInput(input);
            System.out.println("  Input: " + String.valueOf(input) + " → " + result);
        }
    }

    // Pattern Matching for Switch: processing according to types
    static String describeInput(Object input) {
        // Java 21 or earlier syntax: verify with numerous intstanceof
        // if (input instanceof Integer i) return "Integer: " + i;
        // else if (input instanceof Double d) return "Double: " + d;
        // else if (input instanceof String s) return "String: " + s;
        // ... Very verbose

        return switch (input) {
            case Integer i -> "Integer: " + i + " (" +
                    (i > 0 ? "Positive" : i < 0 ? "Negative" : "Zero") + ")";
            case Double d -> String.format("Double: %.2f", d);
            case String s -> "String: \"" + s + "\" (length: " + s.length() + ")";
            case Boolean b -> "Boolean: " + (b ? "TRUE" : "FALSE");
            case List<?> l -> "List: " + l.size() + " element(s)";
            case null -> "Null value";
            default -> "Unknown type: " + input.getClass().getSimpleName();
        };
    }

    record Ticket(int id, String category, String priority, int waitingMinutes) {
    }

    static void guardedPatterns() {
        // Scenario: IT Ticket priority automatic allocation
        // Determine processing method according to Ticket type and specific attributes

        List<Ticket> tickets = List.of(
                new Ticket(1, "Network", "CRITICAL", 15),
                new Ticket(2, "Software", "HIGH", 120),
                new Ticket(3, "Hardware", "LOW", 480),
                new Ticket(4, "Network", "HIGH", 30),
                new Ticket(5, "Software", "CRITICAL", 2),
                new Ticket(6, "Hardware", "MEDIUM", 60));

        System.out.println("Ticket allocation result: ");
        for (Ticket t : tickets) {
            String action = assignTicket(t);
            System.out.printf("  Ticket #%d (%s/%s, waiting %d minutes) → %s%n",
                    t.id(), t.category(), t.priority(), t.waitingMinutes(), action);
        }
    }

    static String assignTicket(Object ticket) {
        if (ticket instanceof Week2Thursday.Ticket t) {
            return assignTicketInternal(t);
        }
        return "Invalid Ticket";
    }

    static String assignTicketInternal(Ticket t) {
        return switch (t) {
            // Guarded Pattern:
            // CRITICAL and waiting over 10 minutes: escalate immediately and notify management
            case Ticket tk when tk.priority().equals("CRITICAL") &&
                    tk.waitingMinutes() > 10 ->
                "🚨 Urgent escalation: Notify management and process immediately";

            // CRITICAL but short waiting time: normal urgent processing
            case Ticket tk when tk.priority().equals("CRITICAL") ->
                "🔴 Urgent processing: Assign to available technician";

            // HIGH priority for Network category: assign to network expert
            case Ticket tk when tk.priority().equals("HIGH") &&
                    tk.category().equals("Network") ->
                "🟡 Network Expert Priority: Assign a Network Team";

            // Any ticket waiting for over 4 hours: requires follow up
            case Ticket tk when tk.waitingMinutes() > 240 ->
                "⚠️ Long wait: Send reminder, check if forgotten";

            // HIGH priority (other category)
            case Ticket tk when tk.priority().equals("HIGH") ->
                "🟠 High priority: Add to priority processing queue";

            // Other cases: Standard queue
            default -> "📋 General processing: Add to standard queue";
        };
    }

    static void nullHandling() {
        // In Java 14, switch expression encountering null will throw NullPointerException (same as
        // the old switch expression)
        // Starting with Java 21, null can be handled directly in a switch expression

        String[] statuses = { "OPEN", "CLOSED", null, "PENDING" };

        for (String status : statuses) {
            String label = switch (status) {
                case "OPEN" -> "🔵 Open";
                case "PENDING" -> "🟡 Pending";
                case "CLOSED" -> "⚫ Closed";
                case null -> "❓ Status unknown (null)";
                default -> "⚠️ Unknown status: " + status;
            };
            System.out.println("Status " + String.valueOf(status) + ": " + label);
        }
        System.out.println("\nNote: In Java 14, switch expression receiving null will throw NullPointerException");
        System.out.println("Starting in Java 21, NULL can be directly handled using case null for greater safety");
    }

    static void ticketRefactoring() {
        // Refactoring goal: Make output of describeStatus() more detailed
        // Using the Guarded Pattern to provide different messages based on the specific data of the status

        TicketStatus.Status[] statuses = {
                new TicketStatus.Open(
                        java.time.LocalDateTime.now().minusHours(3),
                        "alice@company.com"),
                new TicketStatus.InProgress(
                        java.time.LocalDateTime.now().minusMinutes(45),
                        "bob.tech@company.com"),
                new TicketStatus.Pending(
                        java.time.LocalDateTime.now().minusHours(2),
                        "System screenshots are required",
                        "charlie.tech@company.com"),
                new TicketStatus.Resolved(
                        java.time.LocalDateTime.now().minusMinutes(10),
                        "diana.tech@company.com",
                        "The problem was solved after reinstalling the driver."),
                new TicketStatus.Closed(
                        java.time.LocalDateTime.now(),
                        "system",
                        false)
        };

        System.out.println("Ticket status description (refactored): ");
        for (TicketStatus.Status s : statuses) {
            System.out.println("  " + describeStatusV2(s));
        }
    }

    // Improved version: Combines Guarded Pattern and detailed information
    static String describeStatusV2(TicketStatus.Status status) {
        return switch (status) {
            // Open: display waiting time
            case TicketStatus.Open o -> {
                long waitMins = Duration.between(o.openedAt(), LocalDateTime.now()).toMinutes();
                yield waitMins > 120
                        ? String.format("🔴 OPEN (waiting %d minutes, needs follow up) by %s",
                                waitMins, o.reportedBy())
                        : String.format("🔵 OPEN (waiting %d minutes) by %s",
                                waitMins, o.reportedBy());
            }

            // InProgress: display processing time
            case TicketStatus.InProgress ip -> {
                long processMins = Duration.between(ip.startedAt(), LocalDateTime.now()).toMinutes();
                yield String.format("🟡 IN PROGRESS (processing %d minutes) by %s",
                        processMins, ip.assignedTo());
            }

            // Pending: display detailed information about the pending
            case TicketStatus.Pending p ->
                String.format("🟠 PENDING — waiting: %s (request from %s )",
                        p.waitingFor(), p.requestedBy());

            // Resolved: display the summary of resolution
            case TicketStatus.Resolved r ->
                String.format("🟢 RESOLVED by %s — %s",
                        r.resolvedBy(), r.resolution().length() > 20
                                ? r.resolution().substring(0, 20) + "..."
                                : r.resolution());

            // Closed: distinguish whether user confirmed or system automatic closure
            case TicketStatus.Closed c ->
                c.userConfirmed()
                        ? "⚫ CLOSED (resolved with user confirmation)"
                        : "⚫ CLOSED (system automatic closure)";
        };
    }

}
