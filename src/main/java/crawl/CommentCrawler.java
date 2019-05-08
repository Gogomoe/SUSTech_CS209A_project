package crawl;

import model.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentCrawler {

    List<Comment> crawl(long productId, int pageSize, int page);

    List<Comment> crawFrom(long productId, LocalDateTime startDate);

    List<Comment> crawAll(long productId);

}
