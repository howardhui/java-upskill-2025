package com.javaupskill.switchevolution;

/**
 * Switch evolution demo: shipping fee calculation across three Java eras.
 * Switch 演進示範：以「計算運費」對比三個時代的寫法。
 *
 * <p>Run: {@code mvn -q exec:java -Dexec.mainClass=com.javaupskill.switchevolution.SwitchEvolutionDemo}
 * or run this class from your IDE.
 */
public class SwitchEvolutionDemo {

    private static final double WEIGHT_KG = 10.0;

    public static void main(String[] args) {
        printHeader();
        demoLegacySwitchStatement();
        demoJava14SwitchExpression();
        demoJava21PatternMatchingSwitch();
        printEvolutionSummary();
    }

    private static void printHeader() {
        System.out.println("=== Switch Evolution: Shipping Fee Calculation ===");
        System.out.println("=== Switch 演進：計算運費 ===\n");
        System.out.printf("Weight / 重量: %.1f kg%n%n", WEIGHT_KG);
    }

    // -------------------------------------------------------------------------
    // 1) Java 1.4 — switch statement (fall-through risk)
    // 1) Java 1.4 — switch 陳述式（fall-through 穿透風險）
    // -------------------------------------------------------------------------
    private static void demoLegacySwitchStatement() {
        System.out.println("--- 1) Java 1.4 style: switch statement ---");
        System.out.println("--- 1) Java 1.4 風格：switch 陳述式 ---\n");

        double correct = LegacyShippingCalculator.calculateFee(
                LegacyShippingCalculator.STANDARD, WEIGHT_KG);
        double buggy = LegacyShippingCalculator.calculateFeeWithFallThroughBug(
                LegacyShippingCalculator.STANDARD, WEIGHT_KG);

        System.out.printf(
                "STANDARD (correct, with break) / 標準（正確，有 break）: $%.2f%n",
                correct);
        System.out.printf(
                "STANDARD (fall-through bug) / 標準（穿透錯誤）:          $%.2f%n",
                buggy);
        System.out.println();
        System.out.println(
                "  EN: Without break, STANDARD falls through into EXPRESS and overwrites base/rate.");
        System.out.println(
                "  繁中：缺少 break 時，STANDARD 會穿透到 EXPRESS，覆寫基本費與單價。");
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // 2) Java 14 — switch expression (arrow syntax, no fall-through)
    // 2) Java 14 — switch 表達式（箭頭語法，不會穿透）
    // -------------------------------------------------------------------------
    private static void demoJava14SwitchExpression() {
        System.out.println("--- 2) Java 14: switch expression (->) ---");
        System.out.println("--- 2) Java 14：switch 表達式（->）---\n");

        for (ShippingMethod method : ShippingMethod.values()) {
            double fee = SwitchExpressionShippingCalculator.calculateFee(method, WEIGHT_KG);
            System.out.printf("  %-14s $%.2f%n", method, fee);
        }

        System.out.println();
        System.out.println(
                "  EN: Arrow cases do not fall through; the switch yields a Rate, then we compute the total.");
        System.out.println(
                "  繁中：箭頭 case 不會穿透；switch 直接產出 Rate，再計算總運費。");
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // 3) Java 21 — pattern matching for switch (types + deconstruction + when)
    // 3) Java 21 — switch 模式比對（型別 + 解構 + when）
    // -------------------------------------------------------------------------
    private static void demoJava21PatternMatchingSwitch() {
        System.out.println("--- 3) Java 21: switch + pattern matching ---");
        System.out.println("--- 3) Java 21：switch + 模式比對 ---\n");

        ShippingRequest[] requests = {
                new ShippingRequest.Standard(WEIGHT_KG),
                new ShippingRequest.Express(WEIGHT_KG),
                new ShippingRequest.Overnight(25.0),
                new ShippingRequest.Overnight(35.0), // triggers heavy guard — 觸發超重守衛
                new ShippingRequest.International(WEIGHT_KG, "US"),
                new ShippingRequest.International(WEIGHT_KG, "JP"),
        };

        for (ShippingRequest request : requests) {
            double fee = PatternMatchingShippingCalculator.calculateFee(request);
            System.out.printf("  %-40s $%.2f%n", request, fee);
        }

        System.out.println();
        System.out.println(
                "  EN: Switch matches on sealed subtypes, binds record components, and applies when guards.");
        System.out.println(
                "  繁中：switch 依密封子型別比對，綁定 record 欄位，並以 when 守衛分支。");
        System.out.println();
    }

    private static void printEvolutionSummary() {
        System.out.println("=== Evolution timeline / 演進時間軸 ===");
        System.out.println("  Java 14  — Switch Expression (JEP 361): -> syntax, expression yields a value");
        System.out.println("           — Switch 表達式（JEP 361）：-> 語法，表達式可回傳值");
        System.out.println("  Java 16  — Pattern Matching for instanceof (JEP 394): groundwork for switch");
        System.out.println("           — instanceof 模式比對（JEP 394）：為 switch 鋪路");
        System.out.println("  Java 21  — Pattern Matching for switch (JEP 441): type + deconstruction + when");
        System.out.println("           — switch 模式比對（JEP 441）：型別、解構、when 守衛");
    }
}
