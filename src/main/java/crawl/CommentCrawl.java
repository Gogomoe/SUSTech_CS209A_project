package crawl;

import model.Comment;
import model.CommentQuery;
import model.Product;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class CommentCrawl implements CommentCrawler{

    private String name;
    private String url;

    public CommentCrawl(String name) {
        this.name = name;
    }

    public void getItem() throws IOException {

        String url = "https://search.jd.com/Search?keyword=" + searchPattern(name) + "&enc=utf-8";
        this.url = url;

        Document doc = Jsoup.connect(url).maxBodySize(0).get();
        Elements ulList = doc.select("ul[class='gl-warp clearfix']");
        Elements liList = ulList.select("li[class='gl-item']");

        for (Element item : liList) {

            if (!item.select("span[class='p-promo-flag']").text().trim().equals("广告")) {

                //System.out.println(item.select("div[class='p-name p-name-type-2']").select("em").text());
                String purl = item.select("div[class='p-name p-name-type-2']").select("a").attr("href");
                //System.out.println(purl);
                getComment(purl);

            }
        }

    }

    private void getComment(String url) throws IOException {

        url = "https:" + url;
        Document doc = Jsoup.connect(url).maxBodySize(0).get();
        Elements tab = doc.select("div[class=tab-con]");
        Elements cmts = doc.select("div[id=comment-0]");
        System.out.println(cmts);
        //Proxy needed

        CommentQuery commentQuery=new CommentQuery();

        for (Element e : cmts) {


            Comment comment = new Comment();
            comment.setId();
            comment.setContent();
            comment.setTime();

            commentQuery.comments.add(comment);

        }

        Product product=new Product();


    }

    @Override
    public List<Comment> crawl(long productId, int pageSize, int page) {
        return null;
    }

    @Override
    public List<Comment> crawFrom(long productId, LocalDateTime startDate) {
        return null;
    }

    @Override
    public List<Comment> crawAll(long productId) {
        return null;
    }

}
