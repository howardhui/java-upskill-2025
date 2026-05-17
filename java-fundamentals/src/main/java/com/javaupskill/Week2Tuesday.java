package com.javaupskill;

import java.time.Duration;
import java.time.LocalDateTime;

public class Week2Tuesday {

    public static void main(String[] args) {
        System.out.println("=== Pattern Matching Exercise ===\n");
        patternMatchingBasic();

        System.out.println("\n=== Switch Pattern Matching ===\n");
        switchPatternMatching();

        System.out.println("\n=== Ticket Business Logic Demostration ===\n");
        ticketBusinessLogic();
    }

    // -- Level 1: Pattern Matching for instanceof ----------
    static void patternMatchingBasic() {

        // create tickets with different states
        TicketStatus.Status openTicket = new TicketStatus.Open(
                LocalDateTime.now().minusHours(2),
                "alice@company.com");

        TicketStatus.Status inProgressTicket = new TicketStatus.InProgress(
                LocalDateTime.now().minusMinutes(30),
                "bob.tech@company.com");

        TicketStatus.Status resolvedTicket = new TicketStatus.Resolved(
                LocalDateTime.now().minusMinutes(5),
                "bob.tech@company.com",
                "The problem was resolved after reinstalling the VPN client.");

        // Old style syntax (Java 1.4): Explicit Type Casting
        System.out.println("--- Old style instanceof syntax ---");
        if (openTicket instanceof TicketStatus.Open) {
            TicketStatus.Open o = (TicketStatus.Open) openTicket;
            System.out.println("Ticket reported by " + o.reportedBy());
        }

        // New style syntax: Pattern Matching, verification and type casting in one step
        System.out.println("\n--- Pattern Matching new style syntax ---");
        if (openTicket instanceof TicketStatus.Open o) {
            System.out.println("Ticket reported by " + o.reportedBy());
        }

        if (inProgressTicket instanceof TicketStatus.InProgress ip) {
            System.out.println("Assign to process by " + ip.assignedTo());
        }

        if (resolvedTicket instanceof TicketStatus.Resolved r) {
            System.out.println("Resolution: " + r.resolution());
        }
    }

    // -- Level 2: Switch Pattern Matching (Java 21) ----------
    static void switchPatternMatching() {

        // Creates an array of Tickets containing different states
        TicketStatus.Status[] tickets = {
                new TicketStatus.Open(
                        LocalDateTime.now().minusHours(5),
                        "charlie@company.com"),
                new TicketStatus.InProgress(
                        LocalDateTime.now().minusHours(1),
                        "diana.tech@company.com"),
                new TicketStatus.Pending(
                        LocalDateTime.now().minusMinutes(45),
                        "Have to provide the error screenshot.",
                        "diana.tech@company.com"),
                new TicketStatus.Resolved(
                        LocalDateTime.now().minusMinutes(10),
                        "eve.tech@company.com",
                        "Problem resolved after driver software upgraded."),
                new TicketStatus.Closed(
                        LocalDateTime.now(),
                        "system",
                        false)
        };

        // Switch Pattern Matching:
        // Because Status is sealed, the compiler ensures all cases are handled
        // No default case needed
        for (TicketStatus.Status status : tickets) {
            String summary = switch (status) {
                case TicketStatus.Open o ->
                    String.format("🔵 OPEN    | Submitter: %-35s | Waiting time: %d minutes",
                            o.reportedBy(),
                            Duration.between(o.openedAt(),
                                    LocalDateTime.now()).toMinutes());

                case TicketStatus.InProgress ip ->
                    String.format("🟡 IN PROG | Processor: %-35s | Processing time: %d minutes",
                            ip.assignedTo(),
                            Duration.between(ip.startedAt(),
                                    LocalDateTime.now()).toMinutes());

                case TicketStatus.Pending p ->
                    String.format("🟠 PENDING | Waiting: %-37s | Required by %s",
                            p.waitingFor(),
                            p.requestedBy());

                case TicketStatus.Resolved r ->
                    String.format("🟢 RESOLVE | Solver: %-38s | Solution: %s",
                            r.resolvedBy(),
                            r.resolution().substring(0,
                                    Math.min(15, r.resolution().length()))
                                    + "...");

                case TicketStatus.Closed c ->
                    String.format("⚫ CLOSED  | Closer: %-38s | User confirmation: %s",
                            c.closedBy(),
                            c.userConfirmed() ? "Yes" : "No (By System)");
            };
            System.out.println(summary);
        }
    }

    // -- Level 3: Business Logic - State transition verification ----------
    static void ticketBusinessLogic() {

        // Design a method: verify if the state transition is valid
        // Open → InProgress ✅
        // Open → Closed ❌ (Cannot skip state)
        // Resolved → Closed ✅
        // Closed → Open ❌ (Cannot reopen after closed, Need to create new Ticket)

        TicketStatus.Status current = new TicketStatus.Open(
                LocalDateTime.now().minusHours(1),
                "frank@company.com");
        System.out.println("Current State: " + describeStatus(current));

        // Simulate state transition
        TicketStatus.Status next1 = transition(current,
                new TicketStatus.InProgress(
                        LocalDateTime.now(),
                        "grace.tech@company.com"));
        System.out.println("State after transition: " + describeStatus(next1) + "\n");

        // Try the invalid transition: from Open to Closed directly
        TicketStatus.Status next2 = transition(current,
                new TicketStatus.Closed(
                        LocalDateTime.now(),
                        "system",
                        false));
        System.out.println("Invalid transition result: " + describeStatus(next2) + "\n");
    }

    // State description (By Switch Pattern Matching)
    static String describeStatus(TicketStatus.Status status) {
        return switch (status) {
            case TicketStatus.Open o ->
                "OPEN (submitted by " + o.reportedBy() + ")";
            case TicketStatus.InProgress ip ->
                "IN PROGRESS (processed by " + ip.assignedTo() + ")";
            case TicketStatus.Pending p ->
                "PENDING (waiting: " + p.waitingFor() + ")";
            case TicketStatus.Resolved r ->
                "RESOLVED (resolved by " + r.resolvedBy() + ")";
            case TicketStatus.Closed c ->
                "CLOSED (closed by " + c.closedBy() + ")";
        };
    }

    // State transition validator
    static TicketStatus.Status transition(
            TicketStatus.Status current,
            TicketStatus.Status next) {

        // Define the valid transition rule
        boolean isValid = switch (current) {
            case TicketStatus.Open o ->
                // Opened can transit to InProgress or Resolved
                next instanceof TicketStatus.InProgress ||
                        next instanceof TicketStatus.Resolved;

            case TicketStatus.InProgress ip ->
                // InProgress can transit to Pending or Resolved
                next instanceof TicketStatus.Pending ||
                        next instanceof TicketStatus.Resolved;

            case TicketStatus.Pending p ->
                // Pending can transit to InProgress after receiving reply
                next instanceof TicketStatus.InProgress;

            case TicketStatus.Resolved r ->
                // Resolved can only transit to Closed
                next instanceof TicketStatus.Closed;

            case TicketStatus.Closed c ->
                // Closed cannot transit to any state
                false;
        };

        if (isValid) {
            System.out.println("✅ Valid Transition: " +
                    current.getClass().getSimpleName() +
                    " → " + next.getClass().getSimpleName());
            return next;
        } else {
            System.out.println("❌ Invalid Transition: " +
                    current.getClass().getSimpleName() +
                    " → " + next.getClass().getSimpleName() +
                    " (Keep the original state)");
            return current;
        }
    }
}
