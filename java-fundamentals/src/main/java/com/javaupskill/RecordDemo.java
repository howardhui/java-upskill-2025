package com.javaupskill;

public class RecordDemo {
    // Java 1.4: lots of boilerplate code
    // private final String name;
    // private final int age;
    // public Person(String name, int age) { ... }
    // public String getName() { ... }
    // public int getAge() { ... }
    // public bollean equals(...) { ... }
    // public int hashCode() { ... }
    // public String toString() { ... }

    // Java 16 Record: one line for all fields, constructor and methods above
    record Person(String name, int age) {
    }

    // Custom methods add in the Record
    record Student(String name, int score) {
        // compact constructor (Add checking logic)
        Student {
            if (score < 0 || score > 100)
                throw new IllegalArgumentException(
                        "Score must be between 0 and 100, receive: " + score);
        }

        // Custom methods
        boolean isPassing() {
            return score >= 50;
        }

        String grade() {
            if (score >= 90) return "A";
            if (score >= 80) return "B";
            if (score >= 70) return "C";
            if (score >= 60) return "D";
            return "F";
        }

        public static void main(String[] args) {
            // Create Record instant
            Person alice = new Person("Alice", 30);
            Person bob = new Person("Bob", 25);

            // Auto generated accessor method (not more getXXX but field name)
            System.out.println(alice.name()); // Alice
            System.out.println(alice.age()); // 30

            // Auto generated toString()
            System.out.println(alice); // Person[name=Alice, age=30]

            // Auto generated equals()
            Person alice2 = new Person("Alice", 30);
            System.out.println(alice.equals(alice2)); // true

            // Student Record
            Student s = new Student("Charles", 85);
            System.out.println(s.name() + "'s result: " + s.grade());
            System.out.println("Passed: " + s.isPassing());

            // Test compact constructor
            try {
                Student invalid = new Student("Error", 150);
            } catch (IllegalArgumentException e) {
                System.err.println("Caught exception: " + e.getMessage());
            }
        }
    }
}
