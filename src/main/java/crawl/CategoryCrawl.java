package crawl;

import model.Product;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CategoryCrawl implements CategoryCrawler {

    @Override
    public List<Product> crawl(String keyword) {

        List<Product> list = new ArrayList<>();

        return list;
    }

    private String searchPattern(String name) {

        if (name == null || name.equals("")) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        try {

            char c;
            for (int i = 0; i < name.length(); i++) {

                c = name.charAt(i);

                if (c >= 0 && c <= 255) {
                    sb.append(c);
                } else {
                    byte[] b;
                    b = Character.toString(c).getBytes(StandardCharsets.UTF_8);
                    for (int j = 0; j < b.length; j++) {
                        int k = b[j];
                        if (k < 0)
                            k += 256;
                        sb.append("%").append(Integer.toHexString(k).toUpperCase());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
