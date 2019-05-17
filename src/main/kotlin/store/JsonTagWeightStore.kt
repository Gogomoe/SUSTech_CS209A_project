package store

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import model.TagWeight
import java.nio.file.Files
import java.nio.file.Path

class JsonTagWeightStore : TagWeightStore {

    private val path = Path.of("json/tags.json")
    private val gson = Gson()

    init {
        path.parent.toFile().mkdirs()
    }

    override fun save(tags: MutableList<TagWeight>?) {
        Files.writeString(path, gson.toJson(tags!!))
    }

    override fun loadAllSaves(): MutableList<TagWeight> {
        if (!path.toFile().exists()) {
            return emptyList<TagWeight>().toMutableList()
        }
        return gson.fromJson(Files.readString(path), object : TypeToken<ArrayList<TagWeight>>() {}.type)
    }

}