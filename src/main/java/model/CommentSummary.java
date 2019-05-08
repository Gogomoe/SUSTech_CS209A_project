package model;

import java.util.List;

public class CommentSummary {

    private List<CommentQuery> queries;

    private double score;

    public CommentSummary(List<CommentQuery> queries, double score) {
        this.queries = queries;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public List<CommentQuery> getQueries() {
        return queries;
    }

}
