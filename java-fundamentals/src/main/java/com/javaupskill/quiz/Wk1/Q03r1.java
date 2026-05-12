package com.javaupskill.quiz.Wk1;

/*
 * Q3（Default interface method — diamond conflict + resolving with InterfaceName.super）
 * 
 * EN: Define two interfaces A and B, each with a default String id() returning different strings. 
 * Create a class C implements A, B and resolve the conflict by overriding id() and delegating 
 * to exactly one parent default using A.super.id() or B.super.id(). Print the result.
 * 
 * 繁中：定義兩個介面 A、B，各自有 default String id() 且回傳不同字串。建立 class C implements A, B，
 * 並用 override id() 解決衝突，且在 override 內用 A.super.id() 或 B.super.id() 委派到其中一個預設實作。印出結果。
 */
public class Q03r1 {
    // My Answer
    public interface A {
        default String id() {
            return "Interface A";
        }
    }

    public interface B {
        default String id() {
            return "Interface B";
        }
    }

    public static class C implements A, B {
        @Override
        public String id() {
            return A.super.id();
        }
    }

    public static void main(String[] args) {
        C c = new C();
        System.out.println(c.id());
    }
}
