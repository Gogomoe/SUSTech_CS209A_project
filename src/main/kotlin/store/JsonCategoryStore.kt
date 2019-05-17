package store

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.nio.file.Files
import java.nio.file.Path

class JsonCategoryStore : CategoryStore {

    private val path = Path.of("json/categories.json")
    private val gson = Gson()

    init {
        path.parent.toFile().mkdirs()
    }

    override fun save(save: CategoryStore.CategorySave) {
        val json = gson.toJson(save)
        Files.writeString(path, json)
    }

    override fun loadAllSaves(): MutableList<CategoryStore.CategorySave> {
        val json = if (path.toFile().exists()) Files.readString(path) else "[]"
        return gson.fromJson(json, object : TypeToken<MutableList<CategoryStore.CategorySave>>() {}.type)
    }

}