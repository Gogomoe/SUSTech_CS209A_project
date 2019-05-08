package store;

import model.Category;
import model.Product;

import java.util.List;
import java.util.stream.Collectors;

public interface CategoryStore {

    void save(CategorySave save);

    List<CategorySave> loadAllSaves();

    default void save(Category category) {
        List<Long> ids = category.getProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        save(new CategorySave(category.getName(), ids));
    }

    default List<Category> loadAll(ProductStore store) {
        return loadAllSaves().stream()
                .map(it -> it.toCategoty(store))
                .collect(Collectors.toList());
    }

    class CategorySave {

        private String name;

        private List<Long> products;

        public CategorySave(String name, List<Long> products) {
            this.name = name;
            this.products = products;
        }

        public Category toCategoty(ProductStore store) {
            return new Category(name, products.stream()
                    .map(store::load)
                    .collect(Collectors.toList()));
        }

        public String getName() {
            return name;
        }

        public List<Long> getProducts() {
            return products;
        }
    }

}
