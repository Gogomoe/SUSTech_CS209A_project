package crawl;

import model.*;
import nlp.TagAnalyzer;
import scorer.CommentScorer;

import java.time.LocalDateTime;
import java.util.List;

public class BasicQueryer extends CommentQueryer {

    private TagAnalyzer analyzer;
    private CommentCrawler crawler;
    private CommentScorer scorer;

    public BasicQueryer(Product product, TagAnalyzer analyzer, CommentCrawler crawler, CommentScorer scorer) {
        super(product);
        this.analyzer = analyzer;
        this.crawler = crawler;
        this.scorer = scorer;
    }

    @Override
    protected CommentQuery query() {
        List<CommentQuery> queries = product.getComments().getQueries();
        LocalDateTime time = queries.get(queries.size() - 1).getTime();
        List<Comment> comments = crawler.crawFrom(product.getId(), time);
        List<Tag> tags = analyzer.analyse(comments);
        LocalDateTime now = LocalDateTime.now();

        return new CommentQuery(comments, tags, now);
    }

    @Override
    protected CommentSummary summary(List<CommentQuery> queries) {
        return scorer.evaluate(queries);
    }

}
