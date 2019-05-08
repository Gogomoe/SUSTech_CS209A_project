package model;

public class Tag {

    private String content;

    private int count;

    private double score;

    public Tag(String content, int count, double score) {
        this.content = content;
        this.count = count;
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public int getCount() {
        return count;
    }

    public double getScore() {
        return score;
    }

}