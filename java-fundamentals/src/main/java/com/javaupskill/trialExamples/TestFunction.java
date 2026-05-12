package com.javaupskill.trialExamples;

public class TestFunction {

    public static <T> void assertArrayEquals(T expectedArray, T actualArray) {
        if (expectedArray == actualArray)
            return;
        if (expectedArray == null || actualArray == null) {
            throw new AssertionError("One of the arrays is null");
        }
        if (!expectedArray.getClass().isArray() || !actualArray.getClass().isArray()) {
            throw new AssertionError("Arguments must be arrays");
        }

        int expectedLen = java.lang.reflect.Array.getLength(expectedArray);
        int actualLen = java.lang.reflect.Array.getLength(actualArray);
        if (expectedLen != actualLen) {
            throw new AssertionError("Array lengths differ: expected " + expectedLen + " but was " + actualLen);
        }

        for (int i = 0; i < expectedLen; i++) {
            Object e = java.lang.reflect.Array.get(expectedArray, i);
            Object a = java.lang.reflect.Array.get(actualArray, i);
            if (!java.util.Objects.equals(e, a)) {
                throw new AssertionError("Arrays differ at index " + i + ": expected " + e + " but was " + a);
            }
        }
        System.out.println("\nassertArrayEquals: Equals");
    }

    public static byte[] transformArray(short[] array, ShortToByteFunction function) {
        byte[] transformedArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            transformedArray[i] = function.applyAsByte(array[i]);
        }
        return transformedArray;
    }

    public static void main(String[] args) {
        short[] array = { (short) 1, (short) 2, (short) 3 };
        byte[] transformedArray = transformArray(array, s -> (byte) (s * 2));

        byte[] expectedArray = { (byte) 2, (byte) 4, (byte) 6 };
        assertArrayEquals(expectedArray, transformedArray);
    }
}
