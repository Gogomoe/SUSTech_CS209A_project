package crawl;

import model.Comment;
import model.CommentSummary;
import model.Product;
import model.Tag;
import nlp.TagAnalyzer;
import scorer.CommentScorer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class CommentQuery extends CommentQueryer {

    private TagAnalyzer analyzer;
    private CommentCrawler crawler;
    private CommentScorer scorer;

    public CommentQuery(Product product, TagAnalyzer analyzer, CommentCrawler crawler, CommentScorer scorer) {
        super(product);
        this.analyzer = analyzer;
        this.crawler = crawler;
        this.scorer = scorer;
    }

    @Override
    protected model.CommentQuery query() throws IOException {

        if (product.getComments().getQueries().isEmpty()) {

            List<Comment> comments = crawler.crawlAll(product.getId());
            List<Tag> tags = analyzer.analyse(comments);
            LocalDateTime now = LocalDateTime.now();
            return new model.CommentQuery(comments, tags, now);
        } else {

            List<model.CommentQuery> queries = product.getComments().getQueries();
            LocalDateTime time = queries.get(queries.size() - 1).getTime();
            List<Comment> comments = crawler.crawlFrom(product.getId(), time);
            List<Tag> tags = analyzer.analyse(comments);
            LocalDateTime now = LocalDateTime.now();
            return new model.CommentQuery(comments, tags, now);
        }
    }

    @Override
    protected CommentSummary summary(List<model.CommentQuery> queries) {
        return scorer.evaluate(queries);
    }

}
