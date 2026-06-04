package com.javaupskill;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.javaupskill.Week2Thursday.Ticket;

/*
 * Advanced Challenge: IT Operations Incident Digest
 * =================================================
 * Scenario: 
 * Each hour, the system receives heterogeneous operational signals (metrics, 
 * You are building a small on-call incident digest module for a platform team. 
 * logs, tickets, deployments). Your job is to normalize, classify, aggregate, 
 * and render a human-readable digest for Slack/email—using modern Java features 
 * only (no external libraries beyond the JDK).
 * 
 * Time box (suggested): 
 * 90–120 minutes
 * 
 * Deliverable: 
 * One class (or a small package) with a main that runs against the provided 
 * sample data and prints the digest.
 * 
 */
public class Week2SundayAlpha {
    // - Define a sealed interface (or sealed class) named OpsSignal that 
    // represents any incoming operational event.
    sealed interface OpsSignal permits MetricAlert, LogErrorBurst, ChangeEvent,
            TicketEscalation, SecurityFinding {
        Instant timestamp();
    }

    enum ChangeType {
        DEPLOY, ROLLBACK, CONFIG;
    }

    enum Severity {
        CRITICAL, HIGH, LOW;
    }

    // - DigestBucket is an enum you define with at least: CRITICAL, WARNING, 
    // INFO, SUPPRESSED.
    enum DigestBucket {
        CRITICAL, WARNING, INFO, SUPPRESSED;
    }

    // - It must permit at least four distinct subtypes. Use record 
    // implementations where appropriate.
    // - Add a common accessor for Instant timestamp on every subtype (via the 
    // sealed type or a shared sealed supertype).
    record MetricAlert(String service, String metric, double value,
            double threshold, Instant timestamp) implements OpsSignal {
    }

    record LogErrorBurst(String service, int errorCount, int windowMinutes,
            Instant timestamp) implements OpsSignal {
    }

    record ChangeEvent(String service, ChangeType changeType, String version,
            Instant timestamp) implements OpsSignal {
    }

    record TicketEscalation(String ticketId, String team, String priority,
            Duration slaBreached, int ageHours, Instant timestamp) implements
            OpsSignal {
    }

    record SecurityFinding(String asset, Severity severity,
            Instant timestamp) implements OpsSignal {
    }

    // - EnrichedSignal is a record wrapping the original OpsSignal plus:
    //  * DigestBucket bucket
    //  * String shortSummary (one line, ≤ 80 chars)
    //  * int impactScore (0–100; your formula, but must be documented in a 
    // comment)
    record EnrichedSignal(DigestBucket digestBucket, OpsSignal opssignal,
            String shortSummary, // one line, ≤ 80 chars
            int impactScore // 0–100
    ) {
    }

    static Map<String, Instant> criticalServices;

    public static void main(String[] args) {
        // Instant base = LocalDateTime.parse("2026-06-01T14:30")
        Instant base = LocalDateTime.now().withMinute(30)
                .atZone(ZoneId.systemDefault()).toInstant();

        // - Provide a List<OpsSignal> of at least 12 events in main, mixing all
        // five subtypes, at least 3 different service/team/asset values, and 
        // timestamps within the same calendar hour (you may use a fixed Instant
        // base for simplicity)
        List<OpsSignal> opsSignals = List.of(
                new MetricAlert("service001", "metric001", 12.25,
                        10, base.plus(Duration.ofMinutes(5))),
                new MetricAlert("service002", "metric002", 3.5,
                        5, base.plus(Duration.ofMinutes(8))), // SUPPRESSED
                new MetricAlert("service003", "metric003", 1.05,
                        1, base.plus(Duration.ofMinutes(12))),
                new MetricAlert("service003", "metric001", 11.05,
                        10, base.plus(Duration.ofMinutes(12))),
                new MetricAlert("service002", "metric002", 8.5,
                        5, base.plus(Duration.ofMinutes(14))),
                new MetricAlert("service002", "metric003", 1.2,
                        1, base.plus(Duration.ofMinutes(15))),
                new MetricAlert("service001", "metric001", 10.05,
                        10, base.plus(Duration.ofMinutes(18))),
                new LogErrorBurst("service001", 120,
                        10, base),
                new LogErrorBurst("service002", 25,
                        5, base.minus(Duration.ofMinutes(3))),
                new LogErrorBurst("service003", 4,
                        10, base.plus(Duration.ofMinutes(5))),
                new ChangeEvent("service001", ChangeType.ROLLBACK,
                        "1.0.1", base.minus(Duration.ofMinutes(6))),
                new ChangeEvent("service001", ChangeType.DEPLOY,
                        "12.1.5", base.minus(Duration.ofMinutes(20))),
                new ChangeEvent("service002", ChangeType.CONFIG, "3.5.6",
                        base.plus(Duration.ofMinutes(10))),
                new TicketEscalation("IT0001", "Team A", "P3",
                        Duration.ofMinutes(30), 1,
                        base.plus(Duration.ofMinutes(8))),
                new TicketEscalation("IT0002", "Team B", "P4",
                        Duration.ofMinutes(30), 10,
                        base.plus(Duration.ofMinutes(10))),
                new TicketEscalation("IT0001", "Team A", "P2",
                        Duration.ofMinutes(30), 5,
                        base.plus(Duration.ofMinutes(12))),
                new TicketEscalation("IT0003", "Team C", "P1",
                        Duration.ofMinutes(61), 1,
                        base.plus(Duration.ofMinutes(12))),
                new SecurityFinding("asset001", Severity.CRITICAL,
                        base.plus(Duration.ofMinutes(5))),
                new SecurityFinding("asset002", Severity.HIGH,
                        base.minus(Duration.ofMinutes(7))),
                new SecurityFinding("asset003", Severity.LOW,
                        base.plus(Duration.ofMinutes(22))));

        List<OpsSignal> sortedSignals = opsSignals.stream()
                .sorted(Comparator.comparing(OpsSignal::timestamp))
                .collect(Collectors.toList());
        criticalServices = new HashMap<>();
        Map<DigestBucket, List<EnrichedSignal>> digest = buildDigest(sortedSignals);
        String report = renderDigest(digest, sortedSignals);
        System.out.println(report);
    }

    // - For the “deploy during fire” guard, you may precompute a 
    // Set<String> servicesOnFire from the same batch before enriching, or pass 
    // context into enrich—your choice, but behavior must match the table.
    static boolean serviceOnFire(ChangeEvent ce) {
        if (criticalServices.containsKey(ce.service())) {
            return ChronoUnit.MINUTES.between(
                    criticalServices.get(ce.service()), ce.timestamp()) < 15
                            ? true
                            : false;
        }
        return false;
    }

    static Map<DigestBucket, List<EnrichedSignal>> buildDigest(List<OpsSignal> signals) {
        // - Use at least one additional stream operation on the full list 
        // before or after grouping (e.g. filter, map, flatMap, 
        // collectingAndThen) to implement suppression:
        //  * If two or more MetricAlert entries for the same service and metric
        //  exist in the input, keep only the highest value and mark others as 
        //  SUPPRESSED (they must still appear in the final map under SUPPRESSED).
        List<MetricAlert> maMaxList = signals.stream()
                .<MetricAlert>mapMulti((s, consumer) -> {
                    if (s instanceof MetricAlert ma)
                        consumer.accept(ma);
                }).collect(Collectors.toMap(ma -> ma.service() + ", " + ma.metric(),
                        Function.identity(),
                        (ma1, ma2) -> ma2.value() > ma1.value() ? ma2 : ma1))
                .values()
                .stream()
                .toList();

        List<EnrichedSignal> rawEnrichedList = signals.stream()
                .map(s -> enrich(s))
                .map(es -> {
                    if (es.opssignal() instanceof MetricAlert ma && !maMaxList.contains(ma)) {
                        return new EnrichedSignal(DigestBucket.SUPPRESSED, ma,
                                "Suppressed", 25);
                    }
                    return es;
                })
                .toList();

        // - Use Collectors.groupingBy to group enriched signals by DigestBucket.
        // - Inside the grouping collector, use Collectors.mapping (or mapping +
        // another downstream) so each bucket’s value is List<EnrichedSignal> 
        // sorted by impactScore descending.
        return rawEnrichedList.stream()
                .sorted(Comparator.comparing(EnrichedSignal::impactScore).reversed())
                .collect(Collectors.groupingBy(EnrichedSignal::digestBucket,
                        Collectors.mapping(Function.identity(),
                                Collectors.toList())));
    }

    // - Use a switch expression on the signal with pattern matching (case 
    // MetricAlert ma -> ..., etc.).
    static EnrichedSignal enrich(OpsSignal signal) {
        // The granting rules of impactScore points :
        // MetricAlert:         25
        // LogErrorBurst:       20
        // ChangeEvent:         15
        // TicketEscalation:    10
        // SecurityFinding:     5
        //
        // DigestBucket.CRITICAL:   75
        // DigestBucket.WARNING:    50
        // DigestBucket.INFO:       25
        // DigestBucket.SUPPRESSED: 0
        return switch (signal) {
            case MetricAlert ma when ma.value() >= ma.threshold() * 1.2 -> {
                criticalServices.put(ma.service(), ma.timestamp());
                yield new EnrichedSignal(DigestBucket.CRITICAL, signal,
                        "CRITICAL - Metric Alert", 75 + 25);
            }
            case MetricAlert ma when ma.value() >= ma.threshold() ->
                new EnrichedSignal(DigestBucket.WARNING, signal,
                        "WARNING - Metric Alert", 50 + 25);
            case MetricAlert ma ->
                new EnrichedSignal(DigestBucket.INFO, signal,
                        "INFO - Metric Alert", 25 + 25);
            case LogErrorBurst le when le.errorCount() >= 100 -> {
                criticalServices.put(le.service(), le.timestamp());
                yield new EnrichedSignal(DigestBucket.CRITICAL, signal,
                        "CRITICAL - Log Error Burst", 75 + 20);
            }
            case LogErrorBurst le when le.errorCount() >= 20 ->
                new EnrichedSignal(DigestBucket.WARNING, signal,
                        "WARNING - Log Error Burst", 50 + 20);
            case LogErrorBurst le ->
                new EnrichedSignal(DigestBucket.INFO, signal,
                        "INFO - Log Error Burst", 25 + 20);
            case ChangeEvent ce when ce.changeType() == ChangeType.ROLLBACK -> {
                criticalServices.put(ce.service(), ce.timestamp());
                yield new EnrichedSignal(DigestBucket.CRITICAL, signal,
                        "CRITICAL - Change Event", 75 + 15);
            }
            case ChangeEvent ce when ce.changeType() == ChangeType.DEPLOY
                    && serviceOnFire(ce) -> {
                criticalServices.put(ce.service(), ce.timestamp());
                yield new EnrichedSignal(DigestBucket.CRITICAL, signal,
                        "CRITICAL - Change Event", 75 + 15);
            }
            case ChangeEvent ce -> new EnrichedSignal(DigestBucket.INFO, signal,
                    "INFO - Change Event", 25 + 15);
            case TicketEscalation te when List.of("P1", "P2").contains(te.priority())
                    && te.ageHours() > 4 ->
                new EnrichedSignal(DigestBucket.CRITICAL, signal,
                        "CRITICAL - Ticket Escalation", 75 + 10);
            case TicketEscalation te when te.priority().equals("P1") &&
                    te.slaBreached().compareTo(Duration.ofMinutes(60)) > 0 ->
                new EnrichedSignal(DigestBucket.CRITICAL, signal,
                        "CRITICAL - Ticket Escalation", 75 + 10);
            case TicketEscalation te when te.ageHours() > 2 ->
                new EnrichedSignal(DigestBucket.WARNING, signal,
                        "WARNING - Ticket Escalation", 50 + 10);
            case TicketEscalation te -> new EnrichedSignal(DigestBucket.INFO, signal,
                    "INFO - Ticket Escalation", 25 + 10);
            case SecurityFinding sf when sf.severity() == Severity.CRITICAL ->
                new EnrichedSignal(DigestBucket.CRITICAL, signal,
                        "CRITICAL - Security Finding", 75 + 5);
            case SecurityFinding sf when sf.severity() == Severity.HIGH ->
                new EnrichedSignal(DigestBucket.WARNING, signal,
                        "WARNING - Security Finding", 50 + 5);
            case SecurityFinding sf -> new EnrichedSignal(DigestBucket.INFO, signal,
                    "INFO - Security Finding", 25 + 5);
        };
    }

    static String renderDigest(Map<DigestBucket, List<EnrichedSignal>> digest, List<OpsSignal> signals) {
        String header = """
                ====================================================================================================
                Hour windows: %tT - %tT

                No. of signals: %d
                    CRITICAL: %-8d      WARNING: %-8d         INFO: %-8d   SUPPRESSED: %-8d
                """.formatted(Date.from(signals.getFirst().timestamp()),
                Date.from(signals.getLast().timestamp()),
                signals.size(),
                digest.get(DigestBucket.CRITICAL).size(),
                digest.get(DigestBucket.WARNING).size(),
                digest.get(DigestBucket.INFO).size(),
                digest.get(DigestBucket.SUPPRESSED).size());

        String footer;
        String sections = Arrays.stream(DigestBucket.values()).map(db -> {
            String sectionHeader = """
                    ====================================================================================================
                    %s:
                    """.formatted(db);
            String sectionContent = digest.get(db).stream().map(es -> {
                String generalData = """
                        Impact Score: %d
                        Summary: %s
                        """.formatted(es.impactScore(), es.shortSummary());
                String specificData = switch (es.opssignal()) {
                    case MetricAlert ma -> """
                            Type                Service        Metric         Value          Threshold
                            Metric Alert        %-14s %-14s %-14.2f %-14.2f
                            """.formatted(ma.service(), ma.metric(), ma.value(), ma.threshold());
                    case LogErrorBurst le -> """
                            Type                Service        Error Count    Window Minutes
                            Log Error Burst     %-14s %-14d %-14d
                            """.formatted(le.service(), le.errorCount(), le.windowMinutes());
                    case ChangeEvent ce -> """
                            Type                Service        Change Type    Version
                            Change Event        %-14s %-14s %-14s
                            """.formatted(ce.service(), ce.changeType(), ce.version());
                    case TicketEscalation te -> """
                            Type                Ticket ID      Team           Priority       Age Hours
                            Ticket Escalation   %-14s %-14s %-14s %-14d
                            """.formatted(te.ticketId(), te.team(), te.priority(), te.ageHours());
                    case SecurityFinding sf -> """
                            Type                Asset          Severity
                            Change Event        %-14s %-14s
                            """.formatted(sf.asset(), sf.severity());
                };
                return generalData.concat(specificData);
            }).collect(Collectors.joining("\n"));
            return sectionHeader.concat(sectionContent);
        }).collect(Collectors.joining("\n"));
        if (digest.containsKey(DigestBucket.CRITICAL))
            footer = """

                    ====================================================================================================
                    ACTION: Page on-call immediately.
                    """;
        else if (digest.containsKey(DigestBucket.WARNING))
            footer = """

                    ====================================================================================================
                    ACTION: Review within 30 minutes.
                    """;
        else
            footer = """

                    ====================================================================================================
                    ACTION: No immediate action required.
                    """;
        return header.concat(sections).concat(footer);
    }
}
