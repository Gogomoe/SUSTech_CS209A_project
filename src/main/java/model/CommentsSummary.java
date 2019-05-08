package model;

import java.util.ArrayList;
import java.util.List;

public class CommentsSummary {

    private List<CommentQuery> querys;

    private double score;

    public CommentsSummary(List<CommentQuery> querys, double score) {
        this.querys = querys;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public List<CommentQuery> getQuerys() {
        return querys;
    }
}
