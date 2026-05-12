package com.javaupskill;

public class Calculator {
    // Addition method
    public double add(double a, double b) {
        return a + b;
    }

    // Subtraction method
    public double subtract(double a, double b) {
        return a - b;
    }

    // Multiplication method
    public double multiply(double a, double b) {
        return a * b;
    }

    // Division method
    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return a / b;
    }
}
