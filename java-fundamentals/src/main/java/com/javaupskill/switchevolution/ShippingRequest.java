package com.javaupskill.switchevolution;

/**
 * Sealed hierarchy for the Java 21 pattern-matching switch example.
 * 密封型別階層（供 Java 21 switch 模式比對範例使用）。
 */
public sealed interface ShippingRequest
        permits ShippingRequest.Standard,
                ShippingRequest.Express,
                ShippingRequest.Overnight,
                ShippingRequest.International {

    double weightKg();

    record Standard(double weightKg) implements ShippingRequest {
    }

    record Express(double weightKg) implements ShippingRequest {
    }

    record Overnight(double weightKg) implements ShippingRequest {
    }

    record International(double weightKg, String countryCode) implements ShippingRequest {
    }
}
