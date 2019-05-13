package crawl;

import model.CommentSummary;
import model.Product;

import java.util.List;

public class BasicCommentQuery extends CommentQueryer {

    public BasicCommentQuery(Product product) {
        super(product);
    }

    @Override
    protected model.CommentQuery query() {
        return null;
    }

    @Override
    protected CommentSummary summary(List<model.CommentQuery> queries) {
        return null;
    }
}
