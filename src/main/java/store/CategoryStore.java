package store;

import model.Category;
import model.Product;

import java.util.List;
import java.util.stream.Collectors;

public interface CategoryStore {

    void saveSaves(List<CategorySave> save);

    List<CategorySave> loadAllSaves();

    default void saveAll(List<Category> category) {
        List<CategorySave> saves = category.stream().map(it -> {
            List<Long> ids = it.getProducts().stream()
                    .map(Product::getId)
                    .collect(Collectors.toList());
            return new CategorySave(it.getName(), ids);
        }).collect(Collectors.toList());

        saveSaves(saves);
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
