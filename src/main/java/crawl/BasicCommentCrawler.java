package crawl;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Comment;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class BasicCommentCrawler implements CommentCrawler {

    private final String urlTemplate = "https://sclub.jd.com/comment/productPageComments.action?callback=fetchJSON_comment98vv1973&productId=%s&score=0&sortType=6&page=%d&pageSize=10&isShadowSku=0&fold=1";

    private static final String[] userAgents = {
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
            "Mozilla/5.0 (compatible; Bingbot/2.0; +http://www.bing.com/bingbot.htm)",
            "Mozilla/5.0 (compatible; Yahoo! Slurp; http://help.yahoo.com/help/us/ysearch/slurp)",
            "DuckDuckBot/1.0; (+http://duckduckgo.com/duckduckbot.html)",
            "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)",
            "Mozilla/5.0 (compatible; YandexBot/3.0; +http://yandex.com/bots)",
            "ia_archiver (+http://www.alexa.com/site/help/webmasters; crawler@alexa.com)"
    };
    private final Random generator = new Random();

    private final int[] proxyPorts = {};

    private Proxy getProxy() {
        if (proxyPorts.length != 0) {
            int port = proxyPorts[generator.nextInt(proxyPorts.length)];
            return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", port));
        }
        return null;
    }

    private String randomUserAgent() {
        return userAgents[generator.nextInt(userAgents.length)];
    }

    @Override
    public List<Comment> crawl(long productId, int page) throws IOException {

        URL url = new URL(String.format(urlTemplate, productId, page));
        HttpURLConnection connection;
        Proxy proxy = getProxy();
        if (proxy != null) {
            connection = (HttpURLConnection) url.openConnection(proxy);
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", randomUserAgent());
        connection.setRequestProperty("Referer", "https://item.jd.com/");

        String str;
        try {
            str = new String(connection.getInputStream().readAllBytes(), Charset.forName("GBK"));
        } catch (IOException e) {
            throw new RuntimeException("port:" + ((InetSocketAddress) proxy.address()).getPort(), e);
        }

        if (str.length() == 0) {
            return null;
        }
        str = str.substring(26, str.length() - 2);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(json.getAsString(), formatter);
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
        Deque<Comment> list = new ArrayDeque<>();

        while (list.isEmpty() || list.getLast().getTime().isAfter(startDate)) {

            List<Comment> comment = crawl(productId, page++);
            if (comment.isEmpty()) {
                break;
            }

            list.addAll(comment);

        }

        return list.stream()
                .distinct()
                .filter(it -> it.getTime().isAfter(startDate))
                .collect(Collectors.toList());

    }

    @Override
    public List<Comment> crawlAll(long productId) throws IOException {

        List<Comment> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            List<Comment> page = crawl(productId, i);
            list.addAll(page);

            if (page.isEmpty()) {
                break;
            }
        }

        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }

}