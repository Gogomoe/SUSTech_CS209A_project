package store;

import model.Product;

import java.io.IOException;

public interface ProductStore {

    void save(Product product) throws IOException;

    Product load(long productId) throws IOException;

}
