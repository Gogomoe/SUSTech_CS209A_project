package model;

public class Product {

    private String name;

    private long id;

    private String url;

    private CommentsSummary comments;

    public Product(String name, long id, String url, CommentsSummary comments) {
        this.name = name;
        this.id = id;
        this.url = url;
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public CommentsSummary getComments() {
        return comments;
    }

}
