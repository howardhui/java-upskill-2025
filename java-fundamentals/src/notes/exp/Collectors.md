# Java Collectors

[https://www.baeldung.com/java-collectors](https://www.baeldung.com/java-collectors)

The **Java collect() method** is used to accumulate elements from a stream into a collection, such as a List, Set, or Map. For example, to collect even numbers from a list into a List, you can use: 

```java
List<Integer> evenNumbers = numbers.stream()
                                   .filter(x -> x % 2 == 0)
                                   .collect(Collectors.toList());
```

This will result in a List containing only the even numbers.
