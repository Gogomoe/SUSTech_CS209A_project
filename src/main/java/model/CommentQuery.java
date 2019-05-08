package model;

import java.time.LocalDateTime;
import java.util.List;

public class CommentQuery {

    private List<Comment> comments;

    private List<Tag> tags;

    private LocalDateTime time;

    public CommentQuery(List<Comment> comments, List<Tag> tags, LocalDateTime time) {
        this.comments = comments;
        this.tags = tags;
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Tag> getTags() {
        return tags;
    }
}
