package crawl

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.Category
import model.CommentSummary
import model.Product
import org.jsoup.Jsoup
import java.net.URLEncoder
import java.util.function.Consumer


class AsyncCategoryCrawler(
        private var category: Category,
        private val queryerBuilder: (Product) -> AsyncCommentQueryer
) {

    private val urlTemplate = "https://search.jd.com/Search?keyword=%s&enc=utf-8"

    fun update(callback: Consumer<Category>) {
        val encode = URLEncoder.encode(category.name, "UTF-8")
        val url = String.format(urlTemplate, encode)

        val doc = Jsoup.connect(url).get()
        val elements = doc.select(".gl-item")
        elements.forEach {

            val id = if (it.attr("data-pid").isNotEmpty()) {
                it.attr("data-pid").toLong()
            } else {
                it.attr("data-sku").toLong()
            }

            val url = it.select(".p-img img").attr("source-data-lazy-img").toString()
            val name = it.select(".p-name em").text()
            putProduct(id, url, name)

            println("$id $name")
        }

        GlobalScope.launch {
            val products = category.products.map {
                val queryer = queryerBuilder(it)
                val product = queryer.update()
                queryer.destroy()
                product
            }
            category = Category(category.name, products)
            callback.accept(category)
        }

    }

    private fun putProduct(id: Long, url: String, name: String) {
        if (category.products.find { it.id == id } != null) {
            return
        }
        category.products.add(Product(name, id, url, CommentSummary(emptyList(), 0.0)))
    }
}