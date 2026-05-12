package com.javaupskill.trialExamples;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Person {
    private String name;
    private int age;
    private String password;

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<Integer> getAge() {
        return Optional.ofNullable(age);
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public Person(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    public Person(String name, int age) {
        this(name, age, null);
    }

    public Person(String name) {
        this(name, 0, null);
    }

    public Person() {
        this(null, 0, null);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static List<Person> search(List<Person> people, String name, Optional<Integer> age) {
        // final Integer ageFilter = age != null ? age : 0;

        return people.stream()
                .filter(p -> p.getName().orElse(null).equals(name))
                .filter(p -> p.getAge().orElse(0) >= age.orElse(0))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Person> people = List.of(
                new Person("Peter", 30, "pass"),
                new Person("John", 29, "pass"),
                new Person("Mary", 30, "pass"));

        List<Person> resultList = search(people, "Peter", null);
        System.out.println(resultList.size());

    }
}
