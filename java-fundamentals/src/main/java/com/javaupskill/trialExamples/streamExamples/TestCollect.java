package com.javaupskill.trialExamples.streamExamples;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestCollect {
    public static void main(String[] args) {
        List<Product> productList = Arrays.asList(
          new Product(23, "potates"),
          new Product(14, "orange"),
          new Product(13, "lemon"),
          new Product(23, "bread"),
          new Product(23, "sugar")
        );

        List<String> collectorCollection = productList.stream()
                                                    .map(Product::getName)
                                                    .collect(Collectors.toList());
        System.out.println(collectorCollection.toString());

        String listToString = productList.stream()
                                        .map(Product::getName)
                                        .collect(Collectors.joining(", ", "[", "]"));
        System.out.println(listToString);

        double averagePrice = productList.stream()
                                        .collect(Collectors.averagingInt(Product::getPrice));
        int summingPrice = productList.stream()
                                        .collect(Collectors.summingInt(Product::getPrice));
        Map<Integer, List<Product>> collectorMapOfLists = productList.stream()
                                        .collect(Collectors.groupingBy(Product::getPrice));
        Map<Boolean, List<Product>> mapPartioned = productList.stream()
                                        .collect(Collectors.partitioningBy(e -> e.getPrice() > 15));

        System.out.println("averagePrice: " + String.valueOf(averagePrice));
        System.out.println("summingPrice: " + summingPrice);
        collectorMapOfLists.forEach((k, v) -> System.out.println("Key :" + k + " Value: " + v.stream().map(Product::getName).collect(Collectors.joining(", ", "[", "]"))));
        mapPartioned.forEach((k, v) -> System.out.println("Key :" + k + " Value: " + v.stream().map(Product::getName).collect(Collectors.joining(", ", "[", "]"))));

        Stream<Product> streamOfCollection = productList.parallelStream();
        System.out.println("isParallel = " + streamOfCollection.isParallel());
        // boolean bigPrice = streamOfCollection.map(p -> p.getPrice() * 12)
        //                                     .anyMatch(price -> price > 200);
        List<Product> bigPriceList = streamOfCollection.collect(Collectors.toList());
        // System.out.println("bigPrice = " + bigPrice);
        bigPriceList.forEach((p -> System.out.println(p.getPrice() + ", " + p.getName())));
    }
}
