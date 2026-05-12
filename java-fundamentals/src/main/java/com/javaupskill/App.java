package com.javaupskill;

import java.util.Map;

public class App {

    public static void main(String[] args) {
        Student alice = new Student("Alice", 1001, Map.of("Math", 80, "Science", 70, "English", 90));
        Student bob = new Student("Bob", 1002, Map.of("Math", 45, "Science", 50, "English", 48));
        Student carol = new Student("Carol", 1003, Map.of("Math", 60, "Science", 65, "English", 55));
        Student dave = new Student("Dave", 1004, Map.of("Math", 92, "Science", 88, "English", 85));
        Student eve = new Student("Eve", 1005, Map.of("Math", 35, "Science", 30, "English", 28));

        StudentManager manager = new StudentManager();
        manager.addStudent(alice);
        manager.addStudent(bob);
        manager.addStudent(carol);
        manager.addStudent(dave);
        manager.addStudent(eve);

        System.out.println("--- Individual students ---");
        for (Student s : new Student[] { alice, bob, carol, dave, eve }) {
            System.out.printf("%s (ID %d): average=%.2f, passing=%s, top subject=%s%n",
                    s.getName(),
                    s.getStudentId(),
                    s.getAverageScore(),
                    s.isPassing(),
                    s.getHighestScoringSubject().orElse("n/a"));
        }

        System.out.println("\n--- Qualified students (passing) ---");
        manager.findQualifiedStudents().forEach(s ->
                System.out.printf("  %s — average %.2f%n", s.getName(), s.getAverageScore()));

        System.out.printf("%nClass average: %.2f%n", manager.getClassAverage());

        System.out.println("\n--- Students by average (highest first) ---");
        manager.sortStudentsByAverageDescending().forEach(s ->
                System.out.printf("  %s — %.2f%n", s.getName(), s.getAverageScore()));

        System.out.println("\n--- Highest score per subject ---");
        for (String subject : new String[] { "Math", "Science", "English" }) {
            manager.findStudentWithHighestScoreInSubject(subject)
                    .ifPresentOrElse(
                            s -> System.out.printf("  %s: %s (%d)%n",
                                    subject, s.getName(), s.getSubjectScores().get(subject)),
                            () -> System.out.printf("  %s: no students%n", subject));
        }
    }
}
