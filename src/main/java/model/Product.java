package model;

public class Product {

    private String name;

    private long id;

    private String url;

    private CommentSummary comments;

    public Product(String name, long id, String url, CommentSummary comments) {
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

    public CommentSummary getComments() {
        return comments;
    }

    public Product setSummary(CommentSummary summary) {
        return new Product(name, id, url, summary);
    }

}
