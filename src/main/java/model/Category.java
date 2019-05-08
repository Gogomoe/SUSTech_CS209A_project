package model;

import java.util.List;

public class Category {

    private String name;

    private List<Product> products;

    public Category(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }

}
