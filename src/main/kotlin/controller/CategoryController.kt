package controller

import model.Category
import store.CategoryStore
import store.ProductStore

class CategoryController(val store: CategoryStore, productStore: ProductStore) {

    private val categories: MutableList<Category> = mutableListOf()

    init {
        categories.addAll(store.loadAll(productStore))
    }

    fun getCategories(): List<Category> = categories

    fun getCategory(name: String): Category? {
        return categories.find { it.name == name }
    }

}