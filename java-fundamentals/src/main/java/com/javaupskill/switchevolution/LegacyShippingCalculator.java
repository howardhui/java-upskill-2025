package com.javaupskill.switchevolution;

/**
 * Java 1.4 era: classic {@code switch} statement with {@code break} and fall-through risk.
 * Java 1.4 時代：傳統 {@code switch} 陳述式，需 {@code break}，否則會 fall-through（穿透）。
 *
 * <p>Fee model (same across all three demos): base fee + rate × weight (kg).
 * 運費模型（三個版本相同）：基本費 + 單價 × 重量（公斤）。
 */
public final class LegacyShippingCalculator {

    // Java 1.4 had no enum in switch — int constants were typical.
    // Java 1.4 的 switch 尚無 enum，常以 int 常數表示方案。
    public static final int STANDARD = 1;
    public static final int EXPRESS = 2;
    public static final int OVERNIGHT = 3;
    public static final int INTERNATIONAL = 4;

    private LegacyShippingCalculator() {
    }

    /**
     * Correct style: every {@code case} ends with {@code break} to avoid fall-through.
     * 正確寫法：每個 {@code case} 都以 {@code break} 結束，避免穿透。
     */
    public static double calculateFee(int method, double weightKg) {
        double baseFee;
        double ratePerKg;

        switch (method) {
            case STANDARD:
                baseFee = 5.0;
                ratePerKg = 0.50;
                break; // required — 必須，否則會落到下一個 case
            case EXPRESS:
                baseFee = 15.0;
                ratePerKg = 1.00;
                break;
            case OVERNIGHT:
                baseFee = 30.0;
                ratePerKg = 2.00;
                break;
            case INTERNATIONAL:
                baseFee = 50.0;
                ratePerKg = 3.00;
                break;
            default:
                throw new IllegalArgumentException("Unknown shipping method: " + method);
        }

        return baseFee + ratePerKg * weightKg;
    }

    /**
     * Intentionally buggy: missing {@code break} after STANDARD causes fall-through into EXPRESS.
     * 故意錯誤：STANDARD 後缺少 {@code break}，會穿透到 EXPRESS，覆寫費率。
     */
    public static double calculateFeeWithFallThroughBug(int method, double weightKg) {
        double baseFee = 0;
        double ratePerKg = 0;

        switch (method) {
            case STANDARD:
                baseFee = 5.0;
                ratePerKg = 0.50;
                // BUG: no break — execution falls through to EXPRESS
                // 錯誤：沒有 break — 執行會繼續進入 EXPRESS
            case EXPRESS:
                baseFee = 15.0;
                ratePerKg = 1.00;
                break;
            case OVERNIGHT:
                baseFee = 30.0;
                ratePerKg = 2.00;
                break;
            case INTERNATIONAL:
                baseFee = 50.0;
                ratePerKg = 3.00;
                break;
            default:
                throw new IllegalArgumentException("Unknown shipping method: " + method);
        }

        return baseFee + ratePerKg * weightKg;
    }
}
