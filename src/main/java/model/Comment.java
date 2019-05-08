package model;

import java.time.LocalDateTime;

public class Comment {

    private long id;

    private String content;

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

}
