 package com.javaupskill.switchevolution;

/**
 * Java 14: switch expression with arrow {@code ->} — no fall-through, returns a value.
 * Java 14：switch 表達式與箭頭 {@code ->} — 不會穿透，可直接回傳值。
 */
public final class SwitchExpressionShippingCalculator {

    private SwitchExpressionShippingCalculator() {
    }

    public static double calculateFee(ShippingMethod method, double weightKg) {
        Rate rate = switch (method) {
            case STANDARD -> new Rate(5.0, 0.50);
            case EXPRESS -> new Rate(15.0, 1.00);
            case OVERNIGHT -> new Rate(30.0, 2.00);
            case INTERNATIONAL -> new Rate(50.0, 3.00);
            // exhaustive over enum — compiler error if a case is missing
            // enum 窮舉 — 漏寫 case 會編譯失敗
        };

        return rate.baseFee() + rate.ratePerKg() * weightKg;
    }

    private record Rate(double baseFee, double ratePerKg) {
    }
}
