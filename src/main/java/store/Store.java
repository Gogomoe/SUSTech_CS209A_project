package store;

import com.google.gson.Gson;
import model.CommentQuery;
import model.CommentSummary;
import model.Product;
import scorer.CommentScorer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Store implements ProductStore {

    private final String urlTemplate = "https://item.jd.com/%l.html";

    @Override
    public void save(Product product) throws IOException {

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        s = s + "\\json" + product.getId();

        Gson gson = new Gson();
        gson.toJson(product, new FileWriter(new File(s)));

    }

    @Override
    public Product load(long productId) throws IOException {

        URL url = new URL(String.format(urlTemplate, productId));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        String name = "";//TODO
        String productURL = String.format(urlTemplate, productId);
        List<CommentQuery> queries = new ArrayList<>();


        CommentScorer commentScorer = new CommentScorer() {
            @Override
            public CommentSummary evaluate(List<CommentQuery> queries) {
                return null;
            }
        };


        return new Product(name, productId, productURL, commentScorer.evaluate(queries));
    }

}
