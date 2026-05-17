package com.javaupskill;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * State model for IT Helpdesk Tickets
 * Using a Sealed Interface ensures that only predefined states exist
 * Each state carries business data associated with that state
 */
public class TicketStatus {

    // Sealed Interface: declares all possible states explicitly
    sealed interface Status
            permits Open, InProgress, Pending, Resolved, Closed {
    }

    // State 1: Open (Newly created, not process)
    record Open(
            LocalDateTime openedAt,
            String reportedBy) implements Status {
    }

    // State 2: InProgress (Processing)
    record InProgress(
            LocalDateTime startedAt,
            String assignedTo // responsible technical staff
    ) implements Status {
    }

    // State 3: Pending (Waiting for more information from user)
    record Pending(
            LocalDateTime pendingSince,
            String waitingFor, // information waiting for
            String requestedBy // requesting technical staff
    ) implements Status {
    }

    // State 4: Resolved (technical staff considers resolved)
    record Resolved(
            LocalDateTime resolvedAt,
            String resolvedBy,
            String resolution // resolution description
    ) implements Status {
    }

    // State 5: Closed (user confirmed resolution or close automatically after timeout)
    record Closed(
            LocalDateTime closedAt,
            String closedBy,
            boolean userConfirmed // close by user confirmation or by system
    ) implements Status {
    }
}
