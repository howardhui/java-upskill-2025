package com.javaupskill;

public class SealedDemo {

    // Sealed Interface: only permitted Class can be implemented
    // This solve the issue: If you have a Shape class and you want to allow
    // only three subclasses: Circle, Rectangle, and Triangle
    // This cannot be restricted before Java 17

    sealed interface Shape permits Circle, Rectangle, Triangle {
    }

    record Circle(double radius) implements Shape {
        double area() {
            return Math.PI * radius * radius;
        }
    }

    record Rectangle(double width, double height) implements Shape {
        double area() {
            return width * height;
        }
    }

    // final: no more extension
    final class Triangle implements Shape {
        private final double base, height;

        Triangle(double base, double height) {
            this.base = base;
            this.height = height;
        }

        double area() {
            return 0.5 * base * height;
        }
    }

    public static void main(String[] args) {
        Shape circle = new Circle(5);
        Shape rect = new Rectangle(4, 6);

        // Pattern Matching for instanceof (Java 16+)
        // In old Java 1.4 way:
        // if (circle instanceof Circle) {
        // Circle c = (Circle) circle;
        // System.out.println(c.area());
        // }

        // New way: instanceof can verify and transform simultaneously
        if (circle instanceof Circle c) {
            System.out.println("Area of Circle: " + c.area());
        }

        if (rect instanceof Rectangle r) {
            System.out.println("Area of Rectangle: " + r.area());
        }

        // Using switch with Sealed Class (Java 21)
        printShapeInfo(circle);
        printShapeInfo(rect);
    }

    static void printShapeInfo(Shape shape) {
        String info = switch (shape) {
            case Circle c -> "Circle, radius " + c.radius();
            case Rectangle r -> "Rectangle, " + r.width() + " x " + r.height();
            case Triangle t -> "Triangle";
        };
        System.out.println(info);
    }
}
