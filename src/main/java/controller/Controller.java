package controller;

import model.Category;
import model.CommentSummary;
import model.Product;
import model.TagWeight;
import scorer.CommentSummer;
import store.CategoryStore;
import store.ProductStore;
import store.TagWeightStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Controller {

    private CategoryStore categoryStore;
    private ProductStore productStore;
    private TagWeightStore tagWeightStore;

    private CommentSummer summer;

    private List<Category> categories = new ArrayList<>();
    private Map<Long, Product> products = new HashMap<>();
    private Map<String, TagWeight> tagWeights = new HashMap<>();

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
    }


    public List<Category> getCategories() {
        return categories;
    }

    public Category getCategory(String name) {
        return categories.stream().filter(it -> it.getName().equals(name)).findFirst().get();
    }


    public List<TagWeight> getTagWeights(List<Product> products) {
        return products.stream()
                .map(Product::getComments)
                .flatMap(it -> it.getQueries().stream())
                .flatMap(it -> it.getTags().stream())
                .map(it -> {
                    tagWeights.putIfAbsent(it.getContent(), new TagWeight(it.getContent(), 1));
                    return tagWeights.get(it.getContent());
                })
                .collect(Collectors.toList());

    }

    public List<TagWeight> getTagWeightsByCategory(String name) {
        return getTagWeights(getCategory(name).getProducts());
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

    private boolean isUpdating = false;

    public void updateProducts(String category) {
        //TODO
    }

    public boolean isUpdating() {
        //TODO
        return false;
    }

}