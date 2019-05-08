package store;

import model.Product;

public interface DataStore {

    void save(Product product);

    Product load();

}
