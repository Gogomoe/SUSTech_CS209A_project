package crawl

import kotlinx.coroutines.*
import model.CommentQuery
import model.CommentSummary
import model.Product
import nlp.TagAnalyzer
import scorer.CommentScorer
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

class AsyncCommentQueryer(
        private var product: Product,
        crawler: CommentCrawler,
        private val analyzer: TagAnalyzer,
        private val scorer: CommentScorer) {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val crawler = AsyncCommentCrawler(crawler)

    private var allComments = product.comments.queries.flatMap { it.comments }.toList()


    fun destroy() {
        crawler.destroy()
        scope.cancel()
    }

    suspend fun update(): Product {

        val res = scope.async {
            val query = query()

            if (query.comments.isEmpty()) {
                return@async product
            }

            val queries = ArrayList(product.comments.queries)
            queries.add(query)
            val summary = summary(queries)

            product = product.setSummary(summary)
            allComments = product.comments.queries.flatMap { it.comments }.toList()
            product
        }
        return res.await()

    }

    suspend fun query(): CommentQuery = coroutineScope {
        if (product.comments.queries.isEmpty() || allComments.size < 200) {

            val comments = crawler.crawlAll(product.id)
            val list = comments.filter { it !in allComments }
            val tags = analyzer.analyse(list)
            val now = LocalDateTime.now()

            CommentQuery(list, tags, now)

        } else {

            val queries = product.comments.queries
            val time = queries[queries.size - 1].time

            val comments = crawler.crawlFrom(product.id, time)

            val list = comments.filter { it !in allComments }
            val tags = analyzer.analyse(list)
            val now = LocalDateTime.now()

            CommentQuery(list, tags, now)
        }
    }

    fun summary(queries: List<CommentQuery>): CommentSummary {
        return scorer.evaluate(queries)
    }

}
