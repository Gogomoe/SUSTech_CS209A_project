package store

import com.google.gson.Gson
import model.Product
import java.nio.file.Files
import java.nio.file.Path

class JsonProductStore : ProductStore {

    private val gson = Gson()

    override fun save(product: Product) {
        val json = gson.toJson(product)
        Files.writeString(makePath(product.id), json)
    }

    override fun load(productId: Long): Product {
        val path = makePath(productId)
        val json = if (path.toFile().exists()) Files.readString(path) else "{}"
        return gson.fromJson(json, Product::class.java)
    }

    private fun makePath(id: Long): Path {
        return Path.of("json", "p$id.json")
    }

}