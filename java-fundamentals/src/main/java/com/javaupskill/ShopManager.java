package com.javaupskill;

import java.util.List;

public class ShopManager {
    record Product(String name, String category, double price) {}

    public static void main(String[] args) {
        List<Product> products = List.of(
            new Product("Laptop",       "Electronics",  999.99),
            new Product("Headphones",   "Electronics",  199.99),
            new Product("USB Cable",    "Electronics",  12.99),
            new Product("Desk Chair",   "Furniture",    349.99),
            new Product("Webcam",       "Electronics",  449.99),
            new Product("Bookshelf",    "Furniture",    129.99),
            new Product("Keyboard",     "Electronics",  850.00),
            new Product("Mouse",        "Electronics",  39.99)
        );

        List<String> result = products.stream()
                                    .filter(p -> p.category().equals("Electronics") && p.price() < 500)
                                    .map(Product::name)
                                    .sorted()
                                    .toList();
        
        System.out.println(result);
    }
}
