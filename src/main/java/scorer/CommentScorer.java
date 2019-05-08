package scorer;

import model.CommentQuery;
import model.CommentSummary;

import java.util.List;

public interface CommentScorer {

    CommentSummary evaluate(List<CommentQuery> queries);

}
