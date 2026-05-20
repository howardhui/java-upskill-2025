package com.javaupskill;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Week2Wednesday {
        public static void main(String[] args) {
                System.out.println("=== Part 1: For-loop Refactoring to Stream ===\n");
                refactoringExercise();

                System.out.println("\n=== Part 2: Advanced usage of Collectors ===\n");
                AdvancedCollectors();

                System.out.println("\n=== Part 3: Real World Scenario Exercise ===\n");
                realWorldScenario();
        }

        // Data structure of Employee:
        // (String name, String department, double salary, int yearsOfService)
        // record Employee(String name, String department, double salary, int yearsOfService) {
        // }

        static void refactoringExercise() {
                // Test data (Use Record instead of regular class)
                record Employee(String name, String department, double salary, int yearsOfService) {}

                List<Employee> employees = List.of(new Employee("Alice", "Engineering", 75000, 5),
                                new Employee("Bob", "Marketing", 52000, 2),
                                new Employee("Charlie", "Engineering", 88000, 8), new Employee("Diana", "HR", 48000, 1),
                                new Employee("Eve", "Marketing", 61000, 3),
                                new Employee("Frank", "Engineering", 95000, 12), new Employee("Grace", "HR", 51000, 4));

                // Refactoring 1: Find all employee names with salaries higher than 60,000
                // Old style for-loop syntax:
                // List<String> result = new ArrayList<>();
                // for (Employee e : employees) {
                // if (e.salary() > 60000) result.add(e.name());
                // }
                //
                // My Stream version:
                int specAmt = 60000;
                List<String> highEarners = employees.stream().filter(e -> e.salary() > specAmt).map(e -> e.name())
                                .collect(Collectors.toList());
                System.out.println("Salary higher than £" + specAmt + ": " + highEarners);

                // Refactoring 2: Calculate the average salary of Engineering Department
                String dept = "Engineering";
                double avgDeptSalary = employees.stream().filter(e -> e.department().equals(dept))
                                .mapToDouble(e -> e.salary()).average().orElse(0.0);
                System.out.printf("Average salary of " + dept + " Department: £%.2f%n", avgDeptSalary);

                // Refactoring 3: Find the employee with the longest seniority
                Optional<Employee> mostExperienced = employees.stream()
                                .max(Comparator.comparing(Employee::yearsOfService));
                mostExperienced.ifPresent(e -> System.out.println("Employee with the longest seniority: " + e.name()
                                + " (" + e.yearsOfService() + " years)"));

                // Refactoring 4: Sort employees by salary from highest to lowest
                List<Employee> sortBySalaryDescending = employees.stream()
                                .sorted(Comparator.comparing(Employee::salary, (s1, s2) -> s2.compareTo(s1)))
                                // .sorted(Comparator.comparingDouble(Employee::salary).reversed())
                                // 👆🏻 Better solution
                                .collect(Collectors.toList());
                System.out.println("Employees by salary (First 3 employees): ");
                sortBySalaryDescending.stream().limit(3)
                                .forEach(e -> System.out.printf("  %s: £%.0f%n", e.name(), e.salary()));

                // Determine if all employees have more than 1 year of service
                int year = 1;
                boolean allExperienced = !(employees.stream().map(e -> e.yearsOfService() <= year)
                                .reduce((a, b) -> a || b).orElse(true));
                // boolean allExperienced = employees.stream()
                // .allMatch(e -> e.yearsOfService() > year);
                // 👆🏻 Better solution
                System.out.println("All employees have more than " + year + " year of service: " + allExperienced);
        }

        static void AdvancedCollectors() {
                record Employee(String name, String department, double salary, int yearsOfService) {}

                List<Employee> employees = List.of(new Employee("Alice", "Engineering", 75000, 5),
                                new Employee("Bob", "Marketing", 52000, 2),
                                new Employee("Charlie", "Engineering", 88000, 8), new Employee("Diana", "HR", 48000, 1),
                                new Employee("Eve", "Marketing", 61000, 3),
                                new Employee("Frank", "Engineering", 95000, 12), new Employee("Grace", "HR", 51000, 4));

                // === Collectors.groupingBy - Grouping by column ===
                // groupingBy: group by department
                // return Map<String, List<Employee>>
                Map<String, List<Employee>> byDepartment = employees.stream()
                                .collect((Collectors.groupingBy(Employee::department)));

                System.out.println("Group by department: ");
                byDepartment.forEach((dept, emps) -> {
                        System.out.printf(" %-12s: %s%n", dept,
                                        emps.stream().map(Employee::name).collect(Collectors.joining(", ")));
                });

                // groupingBy + counting: number of employees in each department
                Map<String, Long> countByDept = employees.stream()
                                .collect(Collectors.groupingBy(Employee::department, Collectors.counting()));
                System.out.println("\nGroup by department: ");
                countByDept.forEach((dept, count) -> System.out.printf(" %-12s: %d%n", dept, count));

                // groupingBy + averagingDouble: average salary of each department
                Map<String, Double> avgSalaryByDept = employees.stream().collect(Collectors
                                .groupingBy(Employee::department, Collectors.averagingDouble(Employee::salary)));
                System.out.println("\nAverage salary of each department: ");
                avgSalaryByDept.forEach((dept, avg) -> System.out.printf("  %-15s £%.2f%n", dept, avg));

                // Collectors.joining: String concatenation
                String nameList = employees.stream().map(Employee::name).collect(Collectors.joining(", ", "[", "]"));
                System.out.println("\nAll employees: " + nameList);

                // === Collectors.partitioningBy - divides into two groups based on conditions
                // ===
                // partitioningBy: By whether salary is higher than 60,000 divided into two
                // groups
                // return Map<Boolean, List<Employee>> (Keys are only true and false)
                Map<Boolean, List<Employee>> partitioned = employees.stream()
                                .collect(Collectors.partitioningBy(e -> e.salary() > 60000));
                System.out.println("\nSalary > £60,000: ");
                partitioned.get(true).forEach(e -> System.out.println("  ✅ " + e.name() + " (£" + e.salary() + ") "));
                System.out.println("\nSalary ≤ £60,000: ");
                partitioned.get(false).forEach(e -> System.out.println("  ❌ " + e.name() + " (£" + e.salary() + ") "));

                // === Collectors.toMap - transform into Map ===
                // toMap:Transform List into Map
                // Scenario: Create a lookup table for "Employee Name => Salary"
                Map<String, Double> salaryLookup = employees.stream().collect(Collectors.toMap(
                                // key
                                Employee::name,
                                // value
                                Employee::salary));
                System.out.println("\nSalary Lookup: ");
                System.out.println("  Alice's salary: £" + salaryLookup.get("Alice"));
                System.out.println("  Frank's salary: £" + salaryLookup.get("Frank"));

                // toMap + mergeFunction: handling duplicate keys
                // Scenario: Multiple people in the same department and retain the top salary employee
                Map<String, Double> topSalaryByDept = employees.stream().collect(Collectors.toMap(
                                // key
                                Employee::department,
                                // value
                                Employee::salary,
                                // mergeFunction: When key duplicated, retain the largest one
                                (e1, e2) -> Math.max(e1, e2)));
                System.out.println("\nTop Salary in each department: ");
                topSalaryByDept.forEach((dept, salary) -> System.out.printf("  %-15s £%.0f%n", dept, salary));
        }

        static void realWorldScenario() {

                // IT Helpdesk data analysis scenarios
                record Ticket(int id, String category, String priority, String assignee, boolean resolved,
                                int resolutionMinutes) {}

                List<Ticket> tickets = List.of(new Ticket(1, "Network", "HIGH", "Alice", true, 45),
                                new Ticket(2, "Software", "LOW", "Bob", true, 120),
                                new Ticket(3, "Hardware", "MEDIUM", "Alice", false, 0),
                                new Ticket(4, "Network", "CRITICAL", "Charlie", true, 15),
                                new Ticket(5, "Software", "HIGH", "Bob", true, 90),
                                new Ticket(6, "Hardware", "LOW", "Alice", true, 200),
                                new Ticket(7, "Network", "MEDIUM", "Charlie", false, 0),
                                new Ticket(8, "Software", "CRITICAL", "Alice", true, 30),
                                new Ticket(9, "Hardware", "HIGH", "Bob", false, 0),
                                new Ticket(10, "Network", "LOW", "Charlie", true, 75),
                                new Ticket(11, "Network", "CRITICAL", "Charlie", false, 0));

                // Analysis 1: Unresolved Ticket quantity for each category
                System.out.println("--- Unresolved Tickets in each category ---");
                tickets.stream().filter(t -> !t.resolved())
                                .collect(Collectors.groupingBy(Ticket::category, Collectors.counting()))
                                .forEach((cat, count) -> System.out.println("  " + cat + ": " + count));

                // Analysis 2: Resolution rate for each technical staff
                System.out.println("\n--- Resolution rate for each technical staff ---");
                Map<String, Long> totalByAssignee = tickets.stream()
                                .collect(Collectors.groupingBy(Ticket::assignee, Collectors.counting()));

                Map<String, Long> resolvedByAssignee = tickets.stream().filter(Ticket::resolved)
                                .collect(Collectors.groupingBy(Ticket::assignee, Collectors.counting()));

                totalByAssignee.forEach((assignee, total) -> {
                        long resolved = resolvedByAssignee.getOrDefault(assignee, 0L);
                        double rate = (double) resolved / total * 100;
                        System.out.printf("  %-10s %d/%d (%.0f%%) %n", assignee, resolved, total, rate);
                });

                // Analysis 3: Average processing time for resolved tickets (by priority)
                System.out.println("\n--- Average processing time for each priority level ---");
                tickets.stream().filter(Ticket::resolved)
                                .collect(Collectors.groupingBy(Ticket::priority,
                                                Collectors.averagingInt(Ticket::resolutionMinutes)))
                                .entrySet().stream().sorted(Map.Entry.<String, Double> comparingByValue())
                                .forEach(e -> System.out.printf("  %-10s %.1f minutes%n", e.getKey(), e.getValue()));

                // Analysis 4: Whether all CRITICAL priority tickets have been resolved
                boolean allCriticalResolved = tickets.stream().filter(t -> t.priority().equals("CRITICAL"))
                                .allMatch(Ticket::resolved);
                System.out.println("\nAll CRITICAL priority tickets have been resolved: " + allCriticalResolved);

                // Analysis 5: Generating a list of unresolved ticket IDs for each technical staff
                System.out.println("\n--- Unresolved Ticket Allocation ---");
                tickets.stream().filter(t -> !t.resolved())
                                .collect(Collectors.groupingBy(Ticket::assignee,
                                                Collectors.mapping(t -> "#" + t.id(), Collectors.joining(", "))))
                                .forEach((assignee, ids) -> System.out.println(" " + assignee + ": " + ids));
        }
}
