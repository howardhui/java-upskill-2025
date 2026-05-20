package com.javaupskill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Employee list utilities written in pre-Java 8 style (for-loops and Iterator only).
 */
public final class LegacyEmployeeUtils {

    private LegacyEmployeeUtils() {
    }

    /**
     * Returns names of employees whose salary is strictly greater than {@code minSalary}.
     */
    public static List<String> findNamesWithSalaryAbove(List<Employee> employees, double minSalary) {
        List<String> names = new ArrayList<String>();
        if (employees == null) {
            return names;
        }
        for (Iterator it = employees.iterator(); it.hasNext();) {
            Employee employee = (Employee) it.next();
            if (employee.getSalary() > minSalary) {
                names.add(employee.getName());
            }
        }
        return names;
    }

    /**
     * Returns the average salary for employees in the given department.
     * Returns {@code 0.0} when no employees match the department.
     */
    public static double averageSalaryInDepartment(List<Employee> employees, String department) {
        if (employees == null || department == null) {
            return 0.0;
        }
        double total = 0.0;
        int count = 0;
        for (Iterator it = employees.iterator(); it.hasNext();) {
            Employee employee = (Employee) it.next();
            if (department.equals(employee.getDepartment())) {
                total += employee.getSalary();
                count++;
            }
        }
        if (count == 0) {
            return 0.0;
        }
        return total / count;
    }

    /**
     * Returns the employee with the greatest years of service, or {@code null} if the list is empty.
     */
    public static Employee findMostSeniorEmployee(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return null;
        }
        Employee mostSenior = null;
        int maxYears = Integer.MIN_VALUE;
        for (Iterator it = employees.iterator(); it.hasNext();) {
            Employee employee = (Employee) it.next();
            if (employee.getYearsOfService() > maxYears) {
                maxYears = employee.getYearsOfService();
                mostSenior = employee;
            }
        }
        return mostSenior;
    }

    /**
     * Returns a new list of employees sorted by salary from highest to lowest.
     */
    public static List<Employee> sortBySalaryDescending(List<Employee> employees) {
        List<Employee> sorted = new ArrayList<Employee>();
        if (employees == null) {
            return sorted;
        }
        for (Iterator it = employees.iterator(); it.hasNext();) {
            sorted.add((Employee) it.next());
        }
        Collections.sort(sorted, new Comparator() {
            public int compare(Object first, Object second) {
                Employee left = (Employee) first;
                Employee right = (Employee) second;
                if (left.getSalary() > right.getSalary()) {
                    return -1;
                }
                if (left.getSalary() < right.getSalary()) {
                    return 1;
                }
                return 0;
            }
        });
        return sorted;
    }

    /**
     * Returns {@code true} if every employee has more than one year of service.
     * An empty or {@code null} list is treated as satisfying the condition.
     */
    public static boolean allHaveMoreThanOneYearOfService(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return true;
        }
        for (Iterator it = employees.iterator(); it.hasNext();) {
            Employee employee = (Employee) it.next();
            if (employee.getYearsOfService() <= 1) {
                return false;
            }
        }
        return true;
    }
}
