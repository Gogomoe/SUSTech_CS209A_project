package store;

import model.Product;

public interface ProductStore {

    void save(Product product);

    Product load(long productId);

}
