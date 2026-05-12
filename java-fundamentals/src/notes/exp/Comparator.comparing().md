# Java Comparator.comparing()

[https://www.baeldung.com/java-8-comparator-comparing](https://www.baeldung.com/java-8-comparator-comparing)

## **Overview of the comparingInt() Method**

The `comparingInt()` method in Java is a part of the `Comparator` interface. It is specifically designed to create a comparator that sorts objects based on an integer key extracted from them. This method simplifies the process of sorting collections by integer fields.

## **Syntax**

The basic syntax for using `comparingInt()` is as follows:

```java
Comparator.comparingInt(Function<? super T, ? extends Integer> keyExtractor)
```

Here, `keyExtractor` is a function that extracts the integer value from the object to be compared.

## **Example of Using comparingInt() with the sort() Method**

To illustrate how to use `comparingInt()` with the `sort()` method, consider the following example where we have a list of `Product` objects that we want to sort by their price.

### **Product Class Definition**

```java
import java.util.*;
import java.time.LocalDate;

public class Product {
    private String name;
    private int price; // Price as an integer for this example
    private LocalDate manufactureDate;

    public Product(String name, int price, LocalDate manufactureDate) {
        this.name = name;
        this.price = price;
        this.manufactureDate = manufactureDate;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    @Override
    public String toString() {
        return "Product{name='" + name + "', price=" + price + ", manufactureDate=" + manufactureDate + '}';
    }
}

```

### **Sorting Example**

Here’s how to sort a list of `Product` objects by their price using `comparingInt()`:

```java
public class ProductSortingExample {
    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
            new Product("Laptop", 1200, LocalDate.of(2022, 1, 10)),
            new Product("Smartphone", 800, LocalDate.of(2021, 6, 25)),
            new Product("Tablet", 500, LocalDate.of(2020, 11, 5))
        );

        // Sort products by price in ascending order
        products.sort(Comparator.comparingInt(Product::getPrice));

        // Print sorted products
        products.forEach(System.out::println);
    }
}

```

### **Output**

When you run the above code, the output will display the products sorted by price:

```
Product{name='Tablet', price=500, manufactureDate=2020-11-05}
Product{name='Smartphone', price=800, manufactureDate=2021-06-25}
Product{name='Laptop', price=1200, manufactureDate=2022-01-10}
```

This example demonstrates how to effectively use the `comparingInt()` method to sort a list of objects based on an integer attribute.
