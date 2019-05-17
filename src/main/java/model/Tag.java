package model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return Objects.equals(content, tag.content);

    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "content='" + content + '\'' +
                '}';
    }
}