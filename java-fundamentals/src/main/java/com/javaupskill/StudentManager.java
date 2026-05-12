package com.javaupskill;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StudentManager {

    private final List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(Objects.requireNonNull(student, "student"));
    }

    public List<Student> findQualifiedStudents() {
        return students.stream()
                .filter(Student::isPassing)
                .toList();
    }

    public double getClassAverage() {
        return students.stream()
                .mapToDouble(Student::getAverageScore)
                .average()
                .orElse(0.0);
    }

    public List<Student> sortStudentsByAverageDescending() {
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getAverageScore).reversed())
                .toList();
    }

    public Optional<Student> findStudentWithHighestScoreInSubject(String subjectName) {
        // Objects.requireNonNull(subjectName, "subjectName");
        // return students.stream()
        // .filter(s -> s.getSubjectScores().containsKey(subjectName))
        // .max(Comparator.comparingInt(s -> s.getSubjectScores().get(subjectName)));
        Optional<Student> maxSubjStud = Optional.empty();

        if (subjectName == null) {
            throw new NullPointerException("subjectName is null");
        }

        for (Student s : students) {
            if (s.getSubjectScores().containsKey(subjectName)) {
                if (!maxSubjStud.isPresent() || maxSubjStud.get().getSubjectScores().get(subjectName) < s
                        .getSubjectScores().get(subjectName)) {
                    maxSubjStud = Optional.of(s);
                }

            }
        }
        return maxSubjStud;
    }

    public List<String> getFailingStudents() {
        return students.stream()
                .filter(s -> !s.isPassing())
                .map(Student::getName)
                .toList();
    }
}
