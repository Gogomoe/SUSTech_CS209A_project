package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentQuery {

    public final List<Comment> comments = new ArrayList<>();

    public final List<Tag> tags = new ArrayList<>();

    private LocalDateTime time;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
