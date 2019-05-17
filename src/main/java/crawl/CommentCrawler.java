package crawl;

import model.Comment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface CommentCrawler {

    List<Comment> crawl(long productId, int page) throws IOException;

    List<Comment> crawlFrom(long productId, LocalDateTime startDate) throws IOException;

    List<Comment> crawlAll(long productId) throws IOException;

}
