package crawl

import model.Category
import model.CommentSummary
import model.Product
import org.jsoup.Jsoup
import java.util.concurrent.CompletableFuture
import java.net.URLEncoder
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.function.Supplier


class AsyncCategoryCrawler(
        private var category: Category,
        private val queryerBuilder: (Product) -> AsyncCommentQueryer
) {

    private val cached = Executors.newCachedThreadPool() as ThreadPoolExecutor

    private val urlTemplate = "https://search.jd.com/Search?keyword=%s&enc=utf-8"

    fun update(): CompletableFuture<Category> {
        val encode = URLEncoder.encode(category.name, "UTF-8")
        val url = String.format(urlTemplate, encode)

        return CompletableFuture.supplyAsync(Supplier {
            val doc = Jsoup.connect(url).get()
            val elements = doc.select(".gl-item")
            elements.forEach {
                val id = it.attr("data-pid").toLong()
                val url = it.select(".p-img img").attr("source-data-lazy-img").toString()
                val name = it.select(".p-name em").text()
                putProduct(id, url, name)
            }
            val products = category.products.map {
                queryerBuilder(it).upate().get()
            }
            category = Category(category.name, products)
            category
        }, cached)
    }

    private fun putProduct(id: Long, url: String, name: String) {
        if (category.products.find { it.id == id } != null) {
            return
        }
        category.products.add(Product(name, id, url, CommentSummary(emptyList(), 0.0)))
    }
}