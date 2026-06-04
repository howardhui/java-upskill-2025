package com.javaupskill;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Week 2 Advanced Challenge: IT Incident Management System
 * ========================================================
 * Background: 
 * Your company’s IT Operations team receives system alerts from multiple 
 * sources. You are building a console-based Incident Report Engine that 
 * classifies, analyses, and formats incident data using modern Java features.
 *
 */
public class Week2SundayBeta {
    sealed interface Incident permits CriticalOutage, PerformanceDegradation,
            SecurityBreach, ScheduledMaintenance {
        String category();

        // AI recommended
        String incidentId();
        // My work
        // String getIncidentId();

        // AI recommended
        String affectedSystem();
        // My work
        // String getAffectedSystem();
    }

    record CriticalOutage(String incidentId, String affectedSystem,
            int usersImpacted, boolean escalated) implements Incident {
        @Override
        public String category() {
            return "CRITICAL OUTAGE";
        }

        // My work
        // @Override
        // public String getIncidentId() {
        //     return incidentId;
        // }

        // My work
        // @Override
        // public String getAffectedSystem() {
        //     return affectedSystem;
        // }
    }

    record PerformanceDegradation(String incidentId, String affectedSystem,
            double responseTimeMs, String severity) implements Incident {
        @Override
        public String category() {
            return "PERFORMANCE DEGRADATION";
        }

        // My work
        // @Override
        // public String getIncidentId() {
        //     return incidentId;
        // }

        // My work
        // @Override
        // public String getAffectedSystem() {
        //     return affectedSystem;
        // }
    }

    record SecurityBreach(String incidentId, String affectedSystem,
            String threatLevel, boolean containmentAchieved) implements Incident {
        @Override
        public String category() {
            return "SECURITY BREACH";
        }

        // My work
        // @Override
        // public String getIncidentId() {
        //     return incidentId;
        // }

        // My work
        // @Override
        // public String getAffectedSystem() {
        //     return affectedSystem;
        // }
    }

    record ScheduledMaintenance(String incidentId, String affectedSystem,
            String scheduledBy, int durationMinutes) implements Incident {
        @Override
        public String category() {
            return "SCHEDULED MAINTENANCE";
        }

        // My work
        // @Override
        // public String getIncidentId() {
        //     return incidentId;
        // }

        // My work
        // @Override
        // public String getAffectedSystem() {
        //     return affectedSystem;
        // }
    }

    static void runReport() {
        // Create a List<Incident> covering all four subtypes, including:
        // At least 2 CriticalOutage — one escalated, one not
        // At least 2 PerformanceDegradation — one "HIGH", one "MEDIUM"
        // At least 2 SecurityBreach — mix of threat levels and containment status
        // At least 2 ScheduledMaintenance
        List<Incident> incidents = List.of(
                new CriticalOutage("IT0001", "Database",
                        6000, true),
                new CriticalOutage("IT0002", "Auth Service",
                        100, true),
                new CriticalOutage("IT0003", "Others",
                        5, false),
                new PerformanceDegradation("IT0004", "SYS01",
                        10000, "HIGH"),
                new PerformanceDegradation("IT0005", "SYS01",
                        20000, "HIGH"),
                new PerformanceDegradation("IT0006", "SYS02",
                        1500, "MEDIUM"),
                new SecurityBreach("IT0007", "VPN",
                        "APT", true),
                new SecurityBreach("IT0008", "SYS03",
                        "PHISHING", false),
                new SecurityBreach("IT0009", "API Gateway",
                        "DDOS", false),
                new ScheduledMaintenance("IT0010", "SYS01",
                        "Alice", 10),
                new ScheduledMaintenance("IT0011", "SYS02",
                        "Bob", 60),
                new ScheduledMaintenance("IT0012", "SYS03",
                        "Charlie", 60));

        // Using Collectors.groupingBy + Collectors.mapping, produce a 
        // Map<String, List<String>> where the key is the category() string and 
        // the value is the list of affectedSystem names in that category.
        Map<String, List<String>> affectedSystemReport = incidents.stream()
                .collect(Collectors.groupingBy(Incident::category,
                        Collectors.collectingAndThen(
                                // AI recommended
                                Collectors.mapping(Incident::affectedSystem,
                                        // My work
                                        // Collectors.mapping(Incident::getAffectedSystem,
                                        Collectors.toCollection(LinkedHashSet::new)),
                                set -> new ArrayList<>(set))));

        // Filter for SecurityBreach incidents where containmentAchieved is 
        // false, and collect their incidentId values into a List<String>.
        List<String> securityBreachList = incidents.stream()
                // AI recommended
                .filter(i -> i instanceof SecurityBreach sb && !sb.containmentAchieved())
                .map(Incident::incidentId)
                // My work
                // .filter(i -> i instanceof SecurityBreach 
                //     && !((SecurityBreach) i).containmentAchieved())
                // .map(Incident::getIncidentId)
                .toList();

        // Filter PerformanceDegradation incidents with severity of "HIGH", 
        // and calculate the average responseTimeMs using a primitive stream.
        double averageResponseTime = incidents.stream()
                // AI recommended
                .<PerformanceDegradation>mapMulti((i, consumer) -> {
                    if (i instanceof PerformanceDegradation pd)
                        consumer.accept(pd);
                })
                // My Work
                // .filter(i -> i instanceof PerformanceDegradation)
                // .map(i -> (PerformanceDegradation) i)
                .filter(p -> p.severity().equals("HIGH"))
                // AI recommended
                .mapToDouble(PerformanceDegradation::responseTimeMs)
                .average()
                .orElse(0);
        // My work
        // .map(PerformanceDegradation::responseTimeMs)
        // .collect(Collectors.averagingDouble(Double::doubleValue));

        // Filter CriticalOutage incidents where escalated is true and sum the 
        // usersImpacted field using a primitive stream.
        int usersImpactedTotal = incidents.stream()
                // AI recommended
                .<CriticalOutage>mapMulti((i, consumer) -> {
                    if (i instanceof CriticalOutage co)
                        consumer.accept(co);
                })
                // My Work
                // .filter(i -> i instanceof CriticalOutage)
                // .map(i -> (CriticalOutage) i)
                .filter(CriticalOutage::escalated)
                // AI recommended
                .mapToInt(CriticalOutage::usersImpacted)
                .sum();
        // My Work
        // .map(CriticalOutage::usersImpacted)
        // .collect(Collectors.summingInt(Integer::intValue));

        System.out.println(formatIncidentReport(affectedSystemReport, securityBreachList,
                averageResponseTime, usersImpactedTotal));
        incidents.forEach(i -> {
            // AI recommended
            System.out.printf("  %-8s→ %s%n", i.incidentId(), triageAction(i));
            // My work
            // System.out.printf("  %-8s→ %s%n", i.getIncidentId(), triageAction(i));
        });
    }

    static String triageAction(Incident incident) {
        return switch (incident) {
            case CriticalOutage co when co.usersImpacted() > 5000 ->
                "Action for Massive User Impact";
            case CriticalOutage co -> "Action for Critical Outage";
            case PerformanceDegradation pd when pd.responseTimeMs() > 3000 ->
                "Action for Excessive Response time";
            case PerformanceDegradation pd when pd.severity().equals("HIGH") ->
                "Action for High Severity Level";
            case PerformanceDegradation pd -> "Action for Performance Degradation";
            case SecurityBreach sb when sb.threatLevel().equals("DDOS") ->
                "Action for DDos Attack";
            case SecurityBreach sb when sb.threatLevel().equals("PHISHING") ->
                "Action for Phishing Attack";
            case SecurityBreach sb -> "Action for General Security Breach";
            case ScheduledMaintenance sm when sm.durationMinutes() > 240 ->
                "Action for Long Duration Maintenance";
            case ScheduledMaintenance sm -> "Action for Scheduled Maintenance";
        };
    }

    static String formatIncidentReport(Map<String, List<String>> affectedSystemReport,
            List<String> securityBreachList, double averageResponseTime,
            int usersImpactedTotal) {
        // AI recommended
        String sysReport = affectedSystemReport.entrySet().stream()
                .map(e -> "  %-24s→ %s".formatted(e.getKey(), e.getValue()))
                .collect(Collectors.joining("\n"));
        // My work
        // String sysReport = affectedSystemReport.keySet().stream()
        //         .map(s -> "  %-24s→ %s".formatted(s, affectedSystemReport.get(s)))
        //         .collect(Collectors.joining("\n"));
        String content = """
                {

                ========================================
                   IT OPERATIONS INCIDENT REPORT
                ========================================
                INCIDENTS BY CATEGORY:
                %s

                UNCONTAINED BREACHES: %s
                AVG RESPONSE TIME (HIGH): %.2f ms
                TOTAL USERS IMPACTED (ESCALATED): %d
                ========================================

                }
                """.formatted(sysReport, securityBreachList, averageResponseTime, usersImpactedTotal);
        return content;
    }

    public static void main(String[] args) {
        runReport();
    }
}
