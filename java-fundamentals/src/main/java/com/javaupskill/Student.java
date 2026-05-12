package com.javaupskill;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Student {

    private final String name;
    private final int studentId;
    private final Map<String, Integer> subjectScores;

    /*
     * Constructor for Student class
     * @param name - name of the student
     * @param studentId - id of the student
     * @param subjectScores - map of subject scores
     */
    public Student(String name, int studentId, Map<String, Integer> subjectScores) {
        this.name = Objects.requireNonNull(name, "name");
        this.studentId = studentId;
        this.subjectScores = Map.copyOf(Objects.requireNonNullElse(subjectScores, Map.of()));
    }

    public String getName() {
        return name;
    }

    public int getStudentId() {
        return studentId;
    }

    public Map<String, Integer> getSubjectScores() {
        return subjectScores;
    }

    /*
     * Get the average score of the student
     * @return average score of the student
     */
    public double getAverageScore() {
        return subjectScores.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    /*
     * Get the highest scoring subject of the student
     * @return highest scoring subject of the student
     */
    public Optional<String> getHighestScoringSubject() {
        return subjectScores.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }

    /*
     * Check if the student is passing
     * @return true if the student is passing, false otherwise
     */
    public boolean isPassing() {
        return getAverageScore() >= 50.0;
    }
}
