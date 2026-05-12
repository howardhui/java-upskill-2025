package com.javaupskill;

/**
 * Utility class for mathematical operations.
 */
public class MathUtils {

    /**
     * Checks if a given number is prime.
     *
     * <p>Primes are defined for integers greater than 1. Values {@code 0} and {@code 1} are not
     * prime and this method returns {@code false} for them. Negative values are not valid for this
     * check and are rejected.</p>
     *
     * @param number a non-negative integer to check
     * @return {@code true} if the number is prime, {@code false} if it is 0, 1, or composite
     * @throws IllegalArgumentException if {@code number} is negative
     */
    public boolean isPrime(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number must be non-negative");
        }
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates the nth Fibonacci number.
     *
     * <p>Underlying principle: Fibonacci has <em>overlapping subproblems</em> (the same intermediate
     * values are recomputed many times in naive recursion). This implementation uses memoization
     * via a {@code long[]} cache so each Fibonacci value from {@code 0..n} is computed once and
     * reused, reducing time complexity from exponential to linear.</p>
     *
     * <p>Complexity: \(O(n)\) time and \(O(n)\) space for the memoization array.</p>
     *
     * <p>Note: results can overflow {@code long} for sufficiently large {@code n}.</p>
     *
     * @param n the index of the Fibonacci number to calculate
     * @return the nth Fibonacci number
     */
    public long fibonacci(int n) {
        if (n <= 0) {
            return 0;
        }
        long[] memo = new long[n + 1];
        memo[0] = 0L;
        memo[1] = 1L;
        for (int i = 2; i <= n; i++) {
            memo[i] = memo[i - 1] + memo[i - 2];
        }
        return memo[n];
    }
}
