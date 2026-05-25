package com.javaupskill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Week2Friday {

    // Refactoring 1: Change the old-style Employee class to Record
    // Originally required constructor, getter, toString, equals, hashCode
    // Record automatically provides all of these
    // === AI generated ===
    // enum EmploymentType { FULL_TIME, PART_TIME, CONTRACT }
    // === AI generated ===
    public enum EmployType {
        FULL_TIME(0.15), PART_TIME(0.05), CONTRACT(0.08);

        private final double rate;

        public double getAllowance(double salary) {
            return salary * rate;
        }

        EmployType(double rate) {
            this.rate = rate;
        }
    }

    // === AI generated ===
    // record Employee(String name, String department, EmploymentType employmentType,
    //         double salary, int yearsOfService) {
    // }
    // === AI generated ===
    record Employee(String name, String department, EmployType employmentType,
            double salary, int yearsOfService) {
    }

    // Refactoring 2: Find a list of employee names whose salaries exceed a specified amount
    // Old-style：for-loop + if + result.add()
    // New-style：stream + filter + map + collect
    // === AI generated ===
    // static List<String> getHighEarners(List<Employee> employees, double threshold) {
    //     return employees.stream().filter(e -> e.salary() > threshold)
    //             .map(Employee::name).sorted().collect(Collectors.toList());
    // }
    // === AI generated ===
    public List<String> findEmployeesAboveSalary(List<Employee> employees, double threshold) {
        return employees.stream().filter(e -> e.salary() > threshold)
                .map(Employee::name).toList();
    }

    // Refactoring 3: Group employees by department
    // Old-style：for-loop + map.containsKey + map.get + list.add
    // New-style：Collectors.groupingBy
    // === AI generated ===
    // static Map<String, List<Employee>> groupByDepartment(List<Employee> employees) {
    //     return employees.stream().collect(Collectors.groupingBy(Employee::department));
    // }
    // === AI generated ===
    public Map<String, List<Employee>> groupByDepartment(List<Employee> employees) {
        return employees.stream().collect(Collectors.groupingBy(Employee::department,
                Collectors.toList()));
    }

    // Refactoring 4: Calculate allowance amount based on employmentType
    // Old-style: switch statement with break, prone to fall-through
    // New-style: switch expression, arrow syntax, no fall-through risk
    // === AI generated ===
    // static double calculateAllowance(Employee employee) {
    //     return switch (employee.employmentType()) {
    //         case FULL_TIME -> employee.salary() * 0.15;
    //         case PART_TIME -> employee.salary() * 0.05;
    //         case CONTRACT -> employee.salary() * 0.08;
    //     };
    // }
    // === AI generated ===
    public double calculateAllowance(Employee employee) {
        return employee.employmentType().getAllowance(employee.salary());
        // return switch (employee.employmentType()) {
        //     case FULL_TIME -> employee.salary() * 0.15;
        //     case PART_TIME -> employee.salary() * 0.05;
        //     case CONTRACT -> employee.salary() * 0.08;
        // };
    }

    // Refactoring 5: Generate a JSON string containing employee information
    // Old-style: Numerous + signs used for concatenation, difficult to read
    // New-style: Text Blocks + formatted()
    // === AI generated ===
    // static String generateEmployeeJson(Employee e) {
    //     return """
    //             {
    //             "name": "%s",
    //             "department": "%s",
    //             "employmentType": "%s",
    //             "salary": %.2f,
    //             "yearsOfService": %d,
    //             "allowance": %.2f
    //             }"""
    //             .formatted(e.name(), e.department(), e.employmentType(), e.salary(),
    //                     e.yearsOfService(), calculateAllowance(e));
    // }
    // === AI generated ===
    public String generateEmployeeJson(Employee employee) {
        return """
                {
                    "name": "%s",
                    "department": "%s",
                    "employmentType": "%s",
                    "salary": %.1f,
                    "yearsOfService": %d
                }
                """.formatted(employee.name(), employee.department(),
                employee.employmentType(), employee.salary(),
                employee.yearsOfService());
    }

    // Refactoring 6: Find the highest-paid employee in each department
    // Old-style: Double for-loop, comparison and replacement
    // New-style: Collectors.toMap combined with mergeFunction
    // === AI generated ===
    // static Map<String, Employee> getTopEarnerByDept(List<Employee> employees) {
    //     return employees.stream().collect(Collectors.toMap(Employee::department, e -> e,
    //             // mergeFunction: Two employees in the same department, retain the higher paid employee
    //             (e1, e2) -> e1.salary() >= e2.salary() ? e1 : e2));
    // }
    // === AI generated ===
    public Map<String, Employee> findTopEarnerByDepartment(List<Employee> employees) {
        return employees.stream().collect(Collectors.toMap(Employee::department, Function.identity(),
                (e1, e2) -> e1.salary() > e2.salary() ? e1 : e2));
    }

    public static void main(String[] args) {

        // Create 5 employees test data
        List<Employee> employees = new ArrayList<Employee>();
        employees.add(new Employee("Alice", "Engineering", EmployType.FULL_TIME, 85000.0, 5));
        employees.add(new Employee("Bob", "Engineering", EmployType.CONTRACT, 60000.0, 2));
        employees.add(new Employee("Carol", "HR", EmployType.FULL_TIME, 72000.0, 8));
        employees.add(new Employee("David", "HR", EmployType.PART_TIME, 35000.0, 1));
        employees.add(new Employee("Eve", "Finance", EmployType.FULL_TIME, 95000.0, 10));

        Week2Friday system = new Week2Friday();

        // Method 1
        System.out.println("=== Method 1: Find employees whose salaries exceed £70,000 ===");
        system.findEmployeesAboveSalary(employees, 70000)
                .forEach(s -> System.out.println(("  " + s)));

        // Method 2
        // === AI generated ===
        // System.out.println("\n=== Method 2: Group employees by department ===");
        // groupByDepartment(employees).forEach((d, l) -> {
        //     System.out.println("  " + d + ": " +
        //             l.stream().map(Employee::name).collect(
        //                     Collectors.joining(", ")));
        // });
        // === AI generated ===
        System.out.println("\n=== Method 2: Group employees by department ===");
        system.groupByDepartment(employees).forEach((d, l) -> {
            System.out.print("  " + d + ": ");
            IntStream.range(0, l.size()).forEach(i -> {
                if (i > 0)
                    System.out.print(", ");
                System.out.print(l.get(i).name());
            });
            System.out.println();
        });

        // Method 3
        // === AI generated ===
        // System.out.println("\n=== Method 3: Calculate allowance amount ===");
        // employees.forEach(e -> System.out.printf("  %s (%s): allowance = %.1f%n",
        //         e.name(), e.employmentType(), calculateAllowance(e)));
        // === AI generated ===
        System.out.println("\n=== Method 3: Calculate allowance amount ===");
        employees.forEach(e -> System.out.println("  " + e.name() + " (" +
                e.employmentType() + "): allowance = " + system.calculateAllowance(e)));

        // Method 4
        // === AI generated ===
        // System.out.println("\n=== Method 4: Generate employee JSON string ===");
        // employees.stream().map(Week2Friday::generateEmployeeJson)
        //         .forEach(System.out::println);
        // === AI generated ===
        System.out.println("\n=== Method 4: Generate employee JSON string ===");
        employees.forEach(e -> System.out.println(system.generateEmployeeJson(e)));

        // Method 5
        // === AI generated ===
        // System.out.println("\n=== Method 5: Find the highest-paid employee in each department ===");
        // findTopEarnerByDepartment(employees).forEach((dept, top) -> System.out.printf("  %s -> %s (£%.1f)%n",
        //         dept, top.name(), top.salary()));
        // === AI generated ===
        System.out.println("\n=== Method 5: Find the highest-paid employee in each department ===");
        system.findTopEarnerByDepartment(employees).forEach(
                (dept, top) -> System.out.println("  " + dept + " -> " + top.name() + " (£" + top.salary() + ")"));
    }
}
