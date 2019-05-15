package store;

import model.TagWeight;

import java.util.List;

public interface TagWeightStore {

    void save(List<TagWeight> tags);

    List<TagWeight> loadAllSaves();

}
