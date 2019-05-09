package crawl;

import model.Category;

public class CrawlTest {//for a complete search list

    public static void main(String[] args) {

        for (String c : args) {

            String keyword = c.trim();
            CategoryCrawler categoryCrawler = new CategoryCrawl();
            Category category = new Category(keyword, categoryCrawler.crawl(keyword));

            System.out.println(category.getProducts().size());

        }

    }

}
