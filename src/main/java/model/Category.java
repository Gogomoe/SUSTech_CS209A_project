package model;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return Objects.equals(name, category.name);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
