package controller;

import crawl.AsyncCategoryCrawler;
import crawl.AsyncCommentQueryer;
import crawl.BasicCommentCrawler;
import kotlin.Pair;
import kotlin.jvm.functions.Function1;
import model.*;
import nlp.SimpleAnalyzer;
import nlp.TagAnalyzer;
import org.jetbrains.annotations.NotNull;
import scorer.CommentSummer;
import store.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Controller {

    private CategoryStore categoryStore;
    private ProductStore productStore;
    private TagWeightStore tagWeightStore;

    private TagAnalyzer tagAnalyzer = new SimpleAnalyzer();
    private CommentSummer summer;

    private List<Category> categories = new ArrayList<>();
    private Map<Long, Product> products = new HashMap<>();
    private Map<String, TagWeight> tagWeights = new HashMap<>();

    private Map<String, List<TagWeight>> tagWeightsOfCategory = new HashMap<>();

    public static void reAnalyseTags() {
        Controller c = new Controller(new JsonCategoryStore(), new JsonProductStore(), new JsonTagWeightStore());
        List<Product> newlist = new ArrayList<>();
        for (Product product : c.products.values()) {
            for (CommentQuery query : product.getComments().getQueries()) {
                query.getTags().clear();
                query.getTags().addAll(c.tagAnalyzer.analyse(query.getComments()));
            }
            Product newp = product.setSummary(c.summer.evaluate(product.getComments().getQueries()));
            newlist.add(newp);
            c.productStore.save(newp);
        }
        List<TagWeight> tagWeights = c.updateTagWeights(newlist);
        c.tagWeightStore.save(tagWeights);
    }

    public Controller(CategoryStore categoryStore, ProductStore productStore, TagWeightStore tagWeightStore) {
        this.categoryStore = categoryStore;
        this.productStore = productStore;
        this.tagWeightStore = tagWeightStore;

        categories.addAll(categoryStore.loadAll(productStore));
        getCategories().stream()
                .flatMap(it -> it.getProducts().stream())
                .forEach(it -> products.put(it.getId(), it));

        tagWeightStore.loadAllSaves().forEach(
                it -> tagWeights.put(it.getContent(), new TagWeight(it.getContent(), it.getWeight()))
        );

        Map<String, Integer> map = new HashMap<>();
        tagWeights.forEach((key, value) -> map.put(key, value.getWeight()));
        summer = new CommentSummer(map);

        categories.forEach(it -> updateTagWeightsOfCategory(it.getName()));
    }


    public List<Category> getCategories() {
        return categories;
    }

    public Category getCategory(String name) {
        return categories.stream().filter(it -> it.getName().equals(name)).findFirst().get();
    }


    public void updateTagWeightsOfCategory(String category) {
        List<TagWeight> list = updateTagWeights(getCategory(category).getProducts());
        tagWeightsOfCategory.put(category, list);
    }

    @NotNull
    private List<TagWeight> updateTagWeights(List<Product> products) {
        Map<TagWeight, Integer> map = new HashMap<>();
        products.stream()
                .map(Product::getComments)
                .flatMap(it -> it.getQueries().stream())
                .flatMap(it -> it.getTags().stream())
                .map(it -> {
                    tagWeights.putIfAbsent(it.getContent(), new TagWeight(it.getContent(), 1));
                    return tagWeights.get(it.getContent());
                }).forEach(it -> {
                    map.putIfAbsent(it, 0);
                    map.put(it, map.get(it) + 1);
                }
        );
        return map.entrySet().stream()
                .sorted((a, b) -> -a.getValue() + b.getValue())
                .map(Map.Entry::getKey)
                .limit((int) (products.size() * 2.8))
                .collect(Collectors.toList());
    }

    public List<TagWeight> getTagWeightsByCategory(String name) {
        return tagWeightsOfCategory.get(name);
    }

    public void updateTag(String name, int weight) {
        try {
            tagWeights.put(name, new TagWeight(name, weight));
            tagWeightStore.save(new ArrayList<>(tagWeights.values()));
            summer.updateWeight(name, weight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double calculateScore(long productId) {
        Product p = products.get(productId);
        CommentSummary summary = summer.evaluate(p.getComments().getQueries());
        Product newProduct = p.setSummary(summary);
        products.put(productId, newProduct);
        productStore.save(newProduct);
        return summary.getScore();
    }

    private int filterDate = -1;

    public void setFilterDate(int limit) {
        filterDate = limit;
    }

    public double calculateFilterDateScore(long productId) {
        Product p = products.get(productId);
        if (filterDate == -1) {
            return p.getComments().getScore();
        }
        LocalDateTime time = LocalDateTime.now().minusDays(filterDate);
        int filteredCommentCount = (int) p.getComments().getQueries().stream()
                .flatMap(it -> it.getComments().stream())
                .filter(it -> it.getTime().isAfter(time)).count();
        int commentCount = p.getComments().getQueries().stream()
                .mapToInt(it -> it.getComments().size()).sum();

        if (commentCount == 0) {
            return 0;
        }

        return (int) (p.getComments().getScore() * filteredCommentCount / commentCount);
    }

    private String filterText = "";

    public void setFilterText(String limit) {
        filterText = limit;
    }

    public double calculateFilterTextScore(long productId) {
        Product p = products.get(productId);
        if (filterText.isBlank()) {
            return p.getComments().getScore();
        }

        return (int) p.getComments().getQueries().stream()
                .flatMap(it -> it.getComments().stream())
                .filter(it -> it.getContent().contains(filterText)).count();
    }

    private int filterLengthStart = -1;
    private int filterLengthEnd = -1;

    public void setFilterLength(int start, int end) {
        filterLengthStart = start;
        filterLengthEnd = end;
    }

    public double calculateFilterLengthScore(long productId) {
        Product p = products.get(productId);
        if (filterLengthStart == -1 && filterLengthEnd == -1) {
            return p.getComments().getScore();
        }
        int filteredCommentCount = (int) p.getComments().getQueries().stream()
                .flatMap(it -> it.getComments().stream())
                .filter(it -> {
                    int length = it.getContent().length();
                    return length >= filterLengthStart && length <= filterLengthEnd;
                }).count();
        int commentCount = p.getComments().getQueries().stream()
                .mapToInt(it -> it.getComments().size()).sum();

        if (commentCount == 0) {
            return 0;
        }

        return (int) (p.getComments().getScore() * filteredCommentCount / commentCount);
    }

    private boolean isUpdating = false;

    public void updateProducts(String categoryName) {
        isUpdating = true;
        Category category = categories.stream().filter(it -> it.getName().equals(categoryName)).findFirst().get();
        Function1<Product, AsyncCommentQueryer> generator =
                product -> new AsyncCommentQueryer(product, new BasicCommentCrawler(), tagAnalyzer, summer);
        AsyncCategoryCrawler crawler = new AsyncCategoryCrawler(category, generator);
        crawler.update().thenAcceptAsync(c -> {
            c.getProducts().forEach(p -> {
                products.put(p.getId(), p);
                productStore.save(p);
            });
            categories.remove(c);
            categories.add(c);
            categoryStore.saveAll(categories);
            updateTagWeightsOfCategory(categoryName);
            isUpdating = false;
        });
    }

    public boolean isUpdating() {
        return isUpdating;
    }

    public List<Pair<String, Integer>> getCategoryHistory(String category) {
        return getCategory(category).getProducts().stream()
                .flatMap(it -> it.getComments().getQueries().stream())
                .flatMap(it -> it.getComments().stream())
                .map(it -> it.getTime().toLocalDate().toString())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .map(it -> new Pair<>(it.getKey(), it.getValue().intValue()))
                .sorted(Comparator.comparing(Pair::getFirst))
                .collect(Collectors.toList());
    }

}