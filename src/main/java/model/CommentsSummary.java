package model;

import java.util.ArrayList;
import java.util.List;

public class CommentsSummary {

    public final List<CommentQuery> querys = new ArrayList<>();

    private double score;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
