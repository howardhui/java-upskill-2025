package com.javaupskill;

public final class StringUtils {

    private StringUtils() {
        // Utility class; do not instantiate.
    }

    /**
     * Reverses the substring of {@code input} between {@code startInclusive} (inclusive) and
     * {@code endExclusive} (exclusive).
     *
     * <p>Index semantics match {@link String#substring(int, int)}.
     *
     * @param input the source string
     * @param startInclusive start index (inclusive)
     * @param endExclusive end index (exclusive)
     * @return the reversed substring
     * @throws IllegalArgumentException if {@code input} is {@code null}
     * @throws IndexOutOfBoundsException if indices are out of range
     */
    public static String reverseSubstring(String input, int startInclusive, int endExclusive) {
        if (input == null) {
            throw new IllegalArgumentException("input must not be null");
        }

        String sub = input.substring(startInclusive, endExclusive);
        return new StringBuilder(sub).reverse().toString();
    }

    /**
     * Checks whether {@code input} is a palindrome (reads the same forwards and backwards),
     * using exact character matching.
     *
     * <p>Examples:
     * <ul>
     *   <li>{@code "racecar"} is a palindrome</li>
     *   <li>{@code "Racecar"} is not (case-sensitive)</li>
     *   <li>{@code "a b a"} is a palindrome (spaces are treated as characters)</li>
     * </ul>
     *
     * @param input the string to check
     * @return {@code true} if {@code input} is a palindrome; {@code false} otherwise
     */
    public static boolean isPalindrome(String input) {
        if (input == null) {
            return false;
        }

        int left = 0;
        int right = input.length() - 1;
        while (left < right) {
            if (input.charAt(left) != input.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * Counts how many times {@code target} occurs in the substring of {@code input} between
     * {@code startInclusive} (inclusive) and {@code endExclusive} (exclusive).
     *
     * <p>Index semantics match {@link String#substring(int, int)}.
     *
     * @param input the source string
     * @param target the character to count
     * @param startInclusive start index (inclusive)
     * @param endExclusive end index (exclusive)
     * @return the number of occurrences of {@code target} in the specified substring
     * @throws IllegalArgumentException if {@code input} is {@code null}
     * @throws IndexOutOfBoundsException if indices are out of range
     */
    public static int countCharInSubstring(String input, char target, int startInclusive, int endExclusive) {
        if (input == null) {
            throw new IllegalArgumentException("input must not be null");
        }

        int count = 0;
        for (int i = startInclusive; i < endExclusive; i++) {
            if (input.charAt(i) == target) {
                count++;
            }
        }
        return count;
    }
}
