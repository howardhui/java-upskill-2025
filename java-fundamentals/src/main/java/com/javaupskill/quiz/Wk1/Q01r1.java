package com.javaupskill.quiz.Wk1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Q1（Lambda — capturing variables + “effectively final”）
 * 
 * EN: Write code that uses a lambda to filter List<String> names so only strings starting with
 *  a given prefix remain. Make prefix come from a local variable, and demonstrate (in code) what
 *  “effectively final” means (i.e., the version that compiles).
 * 
 * 繁中：寫一段程式用 lambda 過濾 List<String> names，只留下以 prefix 開頭的字串。prefix 要來自區域變數，
 * 並用代碼展示「effectively final」的正確可編譯寫法。
 */
public class Q01r1 {
    public static void main(String[] args) {
        // My Answer
        String prefix = "a";
        List<String> names = new ArrayList<>(Arrays.asList("aStr01", "aStr02", "bStr01", "cStr01", "cStr02"));

        names = names.stream().filter(s -> s.startsWith(prefix)).collect(Collectors.toList());
        System.out.println(names);

        // "effectively final" demo:
        // The lambda above captures `prefix`. If you uncomment the next line,
        // it will NOT compile because `prefix` would no longer be effectively final.
        // prefix = "b";
    }

}
