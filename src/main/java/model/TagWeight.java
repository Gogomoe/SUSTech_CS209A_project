package model;

import java.util.Objects;

public class TagWeight {

    private String content;

    private int weight;

    public TagWeight(String content, int weight) {
        this.content = content;
        this.weight = weight;
    }

    public String getContent() {
        return content;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagWeight tagWeight = (TagWeight) o;

        return Objects.equals(content, tagWeight.content);

    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }
}
