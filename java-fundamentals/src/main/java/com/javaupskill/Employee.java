package com.javaupskill;

/**
 * Employee data holder (Java 1.4-style plain class).
 */
public class Employee {

    private String name;
    private String department;
    private double salary;
    private int yearsOfService;

    public Employee(String name, String department, double salary, int yearsOfService) {
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.yearsOfService = yearsOfService;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public int getYearsOfService() {
        return yearsOfService;
    }

    public String toString() {
        return "Employee[name=" + name
                + ", department=" + department
                + ", salary=" + salary
                + ", yearsOfService=" + yearsOfService
                + "]";
    }
}
