package nlp;

import model.Comment;
import model.Tag;

import java.io.IOException;
import java.util.*;

public interface TagAnalyzer {

    List<Tag> analyse(Comment comment) throws IOException;

    default List<Tag> analyse(List<Comment> comments) {
        Map<String, Tag> tags = new HashMap<>();
        comments.stream()
                .map(comment -> {
                    try {
                        return analyse(comment);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .flatMap(Collection::stream)
                .forEach(it -> {
                    String key = it.getContent();
                    tags.putIfAbsent(key, new Tag(key, 0, it.getScore()));
                    Tag old = tags.get(key);
                    tags.put(key, new Tag(key, old.getCount() + it.getCount(), it.getScore()));
                });
        return new ArrayList<>(tags.values());

    }

}
