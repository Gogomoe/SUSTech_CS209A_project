package crawl;

import model.CommentQuery;
import model.CommentSummary;
import model.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CommentQueryer {

    protected Product product;

    public CommentQueryer(Product product) {
        this.product = product;
    }

    void upate() throws IOException {
        CommentQuery query = query();
        List<CommentQuery> queries = new ArrayList<>(product.getComments().getQueries());
        queries.add(query);
        CommentSummary summary = summary(queries);
        product = product.setSummary(summary);
    }

    protected abstract CommentQuery query() throws IOException;

    protected abstract CommentSummary summary(List<CommentQuery> queries);

    public Product getProduct() {
        return product;
    }

}
