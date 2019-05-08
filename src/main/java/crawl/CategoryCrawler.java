package crawl;

import model.Product;

import java.util.List;

public interface CategoryCrawler {

    List<Product> crawl(String keyword);

}
