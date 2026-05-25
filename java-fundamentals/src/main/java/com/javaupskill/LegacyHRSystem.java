package com.javaupskill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyHRSystem {

    // TODO: Use Record instead
    // Employee class
    static class Employee {
        private String name;
        private String department;
        private String employmentType; // FULL_TIME / PART_TIME / CONTRACT
        private double salary;
        private int yearsOfService;

        public Employee(String name, String department, String employmentType,
                double salary, int yearsOfService) {
            this.name = name;
            this.department = department;
            this.employmentType = employmentType;
            this.salary = salary;
            this.yearsOfService = yearsOfService;
        }

        public String getName() {
            return name;
        }

        public String getDepartment() {
            return department;
        }

        public String getEmploymentType() {
            return employmentType;
        }

        public double getSalary() {
            return salary;
        }

        public int getYearsOfService() {
            return yearsOfService;
        }
    }

    // TODO: Use stream + filter + map instead
    // Method 1: Find a list of employees whose salaries exceed a specified amount
    public List<String> findEmployeesAboveSalary(List<Employee> employees, double threshold) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            if (emp.getSalary() > threshold) {
                result.add(emp.getName());
            }
        }
        return result;
    }

    // TODO: Use groupby instead
    // Method 2: Group employees by department
    public Map<String, List<Employee>> groupByDepartment(List<Employee> employees) {
        Map<String, List<Employee>> result = new HashMap<String, List<Employee>>();
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            String dept = emp.getDepartment();
            if (!result.containsKey(dept)) {
                result.put(dept, new ArrayList<Employee>());
            }
            result.get(dept).add(emp);
        }
        return result;
    }

    // TODO: Use switch expression instead
    // Method 3: Calculate allowance amount based on employmentType
    public double calculateAllowance(Employee employee) {
        double allowance = 0.0;
        switch (employee.getEmploymentType()) {
            case "FULL_TIME":
                allowance = employee.getSalary() * 0.15;
                break;
            case "PART_TIME":
                allowance = employee.getSalary() * 0.05;
                break;
            case "CONTRACT":
                allowance = employee.getSalary() * 0.08;
                break;
            default:
                allowance = 0.0;
                break;
        }
        return allowance;
    }

    // TODO: Use text blocks instead
    // Method 4: Generate a JSON string containing employee information
    public String generateEmployeeJson(Employee employee) {
        String json = "{" +
                "\"name\": \"" + employee.getName() + "\", " +
                "\"department\": \"" + employee.getDepartment() + "\", " +
                "\"employmentType\": \"" + employee.getEmploymentType() + "\", " +
                "\"salary\": " + employee.getSalary() + ", " +
                "\"yearsOfService\": " + employee.getYearsOfService() +
                "}";
        return json;
    }

    // TODO: Use Collectors.toMap instead
    // Method 5: Find the highest-paid employee in each department
    public Map<String, Employee> findTopEarnerByDepartment(List<Employee> employees) {
        Map<String, Employee> result = new HashMap<String, Employee>();
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            String dept = emp.getDepartment();
            if (!result.containsKey(dept)) {
                result.put(dept, emp);
            } else {
                if (emp.getSalary() > result.get(dept).getSalary()) {
                    result.put(dept, emp);
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {

        // Create 5 employees test data
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(new Employee("Alice", "Engineering", "FULL_TIME", 85000.0, 5));
        employees.add(new Employee("Bob", "Engineering", "CONTRACT", 60000.0, 2));
        employees.add(new Employee("Carol", "HR", "FULL_TIME", 72000.0, 8));
        employees.add(new Employee("David", "HR", "PART_TIME", 35000.0, 1));
        employees.add(new Employee("Eve", "Finance", "FULL_TIME", 95000.0, 10));

        LegacyHRSystem system = new LegacyHRSystem();

        // Method 1
        System.out.println("=== Method 1: Find employees whose salaries exceed £70,000 ===");
        List<String> highEarners = system.findEmployeesAboveSalary(employees, 70000.0);
        for (int i = 0; i < highEarners.size(); i++) {
            System.out.println("  " + highEarners.get(i));
        }

        // Method 2
        System.out.println("\n=== Method 2: Group employees by department ===");
        Map<String, List<Employee>> byDept = system.groupByDepartment(employees);
        for (String dept : byDept.keySet()) {
            System.out.print("  " + dept + ": ");
            List<Employee> group = byDept.get(dept);
            for (int i = 0; i < group.size(); i++) {
                if (i > 0)
                    System.out.print(", ");
                System.out.print(group.get(i).getName());
            }
            System.out.println();
        }

        // Method 3
        System.out.println("\n=== Method 3: Calculate allowance amount ===");
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            double allowance = system.calculateAllowance(emp);
            System.out.println("  " + emp.getName() + " (" + emp.getEmploymentType()
                    + "): allowance = " + allowance);
        }

        // Method 4
        System.out.println("\n=== Method 4: Generate employee JSON string ===");
        for (int i = 0; i < employees.size(); i++) {
            System.out.println("  " + system.generateEmployeeJson(employees.get(i)));
        }

        // Method 5
        System.out.println("\n=== Method 5: Find the highest-paid employee in each department ===");
        Map<String, Employee> topEarners = system.findTopEarnerByDepartment(employees);
        for (String dept : topEarners.keySet()) {
            Employee top = topEarners.get(dept);
            System.out.println("  " + dept + " -> " + top.getName()
                    + " (£" + top.getSalary() + ")");
        }
    }
}
