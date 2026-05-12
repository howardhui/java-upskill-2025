package com.javaupskill.trialExamples;

import java.util.function.Function;

public class UseFoo {

    public String add(String string, Foo foo) {
        return foo.method(string);
    }

    // same as the above without interface Foo
    public String add(String string, Function<String, String> fn) {
        return fn.apply(string);
    }

    private String value = "Enclosing scope value";

    public String scopeExperiment() {
        Foo fooIC = new Foo() {
            String value = "Inner class value";

            @Override
            public String method(String string) {
                return this.value; // return "Inner class value"
            }
        };
        String resultIC = fooIC.method("");

        Foo fooLambda = parameter -> {
            String value = "Lambda value";
            return value + this.value; // return "Enclosing scope value"
        };
        String resultLambda = fooLambda.method("");

        return "Results: resultIC = " + resultIC + ", resultLambda = " + resultLambda;
    }

    public static void main(String[] args) {
        UseFoo useFoo = new UseFoo();
        Foo foo = parameter -> parameter + " from lambda";
        String result = useFoo.add("Message ", foo);
        System.out.println(result);

        Function<String, String> fn = parameter -> parameter + " from lambda";
        String result2 = useFoo.add("Message", fn);
        System.out.println(result2);

        System.out.println(useFoo.scopeExperiment());
    }
}
