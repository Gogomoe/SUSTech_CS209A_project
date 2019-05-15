package model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {

    private long id;

    private String content;

    @SerializedName(value = "time", alternate = {"creationTime"})
    private LocalDateTime time;

    public Comment(long id, String content, LocalDateTime time) {
        this.id = id;
        this.content = content;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }

}
