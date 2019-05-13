package crawl;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Comment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommentCrawl implements CommentCrawler {

    private final String urlTemplate = "https://sclub.jd.com/comment/productPageComments.action?callback=fetchJSON_comment98vv1973&productId=%s&score=0&sortType=6&page=%d&pageSize=10&isShadowSku=0&fold=1";

    public static void main(String[] args) throws IOException {

        /*
        List<Comment> list1 = new CommentCrawl().crawl(100005087998L, 50);
        System.out.println(list1);
        */

        /*
        String strDatewithTime = "2019-05-12T10:11:30";
        LocalDateTime aLDT = LocalDateTime.parse(strDatewithTime);

        List<Comment> list2 = new CommentCrawl().crawlFrom(100005087998L, aLDT);
        for (Comment c : list2) {
            System.out.println(c.getTime());
        }
        System.out.println(list2.size());
        */

        List<Comment> list3 = new CommentCrawl().crawlAll(100005087998L);
        System.out.println(list3.size());

    }

    @Override
    public List<Comment> crawl(long productId, int page) throws IOException {

        URL url = new URL(String.format(urlTemplate, productId, page));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
        connection.setRequestProperty("Referer", "https://item.jd.com/");

        String str = new String(connection.getInputStream().readAllBytes(), Charset.forName("GBK"));

        if (str.length() == 0) {
            return null;
        }
        str = str.substring(26, str.length() - 2);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.parse(json.getAsString(), formatter);
            return time;
        }).create();
        JsonObject object = new JsonParser().parse(str).getAsJsonObject();
        String array = object.get("comments").getAsJsonArray().toString();
        if (array.isEmpty()) {
            return null;
        }

        return gson.fromJson(array, new TypeToken<List<Comment>>() {
        }.getType());

    }

    @Override
    public List<Comment> crawlFrom(long productId, LocalDateTime startDate) throws IOException {

        int page = 0;
        List<Comment> list = new ArrayList<>();
        while (list.isEmpty() || list.get(list.size() - 1).getTime().isAfter(startDate)) {

            List<Comment> comment = crawl(productId, page);
            if (comment.isEmpty()) {
                break;
            }

            list.addAll(comment);
            page++;
            while (list.get(list.size() - 1).getTime().isBefore(startDate)) {
                list.remove(list.size() - 1);
            }
        }
        return list;

    }

    @Override
    public List<Comment> crawlAll(long productId) throws IOException {
        List<Comment> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.addAll(crawl(productId, i));
        }

        return list;
    }

}