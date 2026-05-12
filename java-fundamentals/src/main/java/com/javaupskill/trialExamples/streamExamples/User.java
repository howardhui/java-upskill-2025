package com.javaupskill.trialExamples.streamExamples;

public class User {
    private String name;
    private int age;
    private Rating rating;

    // constructor, setters and getters
    public User(String name, int age) {
        this.name = name;
        this.age = age;
        this.rating = new Rating();
    }

    public User(String name, int age, Rating rating) {
        this.name = name;
        this.age = age;
        this.rating = rating;
    }
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Rating getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
