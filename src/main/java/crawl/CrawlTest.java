package crawl;

import model.Category;
import model.Product;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CrawlTest {//for a complete search list

    public static void main(String[] args) {

        //search
        //change character into % + utf-8
        //for every search, create a product

        String[] clist = args;

        for (String c : clist) {

            List<Product> list=new ArrayList<>();
            Category category=new Category(c.trim(),list);

        }

    }
}
