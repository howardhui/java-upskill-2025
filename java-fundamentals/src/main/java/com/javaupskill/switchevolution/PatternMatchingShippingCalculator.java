package com.javaupskill.switchevolution;

/**
 * Java 21: pattern matching for {@code switch} on sealed types + record deconstruction + guards.
 * Java 21：對密封型別做 switch 模式比對 + record 解構 + {@code when} 守衛。
 */
public final class PatternMatchingShippingCalculator {

    private static final double HEAVY_THRESHOLD_KG = 30.0;
    private static final double HEAVY_SURCHARGE = 25.0;

    private PatternMatchingShippingCalculator() {
    }

    public static double calculateFee(ShippingRequest request) {
        Rate rate = switch (request) {
            case ShippingRequest.Standard(double w) ->
                    new Rate(5.0, 0.50, w);
            case ShippingRequest.Express(double w) ->
                    new Rate(15.0, 1.00, w);
            case ShippingRequest.Overnight(double w) when w > HEAVY_THRESHOLD_KG ->
                    // guarded pattern — heavy overnight surcharge
                    // 守衛模式 — 超重隔夜加收
                    new Rate(30.0 + HEAVY_SURCHARGE, 2.00, w);
            case ShippingRequest.Overnight(double w) ->
                    new Rate(30.0, 2.00, w);
            case ShippingRequest.International(double w, String country)
                    when "US".equals(country) || "CA".equals(country) ->
                    new Rate(50.0, 2.50, w); // North America rate — 北美費率
            case ShippingRequest.International(double w, String country) ->
                    new Rate(50.0, 3.00, w); // other regions — 其他地區
            // no default: sealed permits are exhaustive — 不需 default，編譯器檢查完整性
        };

        return rate.baseFee() + rate.ratePerKg() * rate.weightKg();
    }

    private record Rate(double baseFee, double ratePerKg, double weightKg) {
    }
}
