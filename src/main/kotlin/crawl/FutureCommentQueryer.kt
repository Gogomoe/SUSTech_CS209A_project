package crawl

import model.CommentQuery
import model.CommentSummary
import model.Product
import nlp.TagAnalyzer
import scorer.CommentScorer
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

class FutureCommentQueryer(
        private var product: Product,
        crawler: CommentCrawler,
        private val analyzer: TagAnalyzer,
        private val scorer: CommentScorer) {

    private val crawler = FutureCommentCrawler(crawler)

    private var allComments = product.comments.queries.flatMap { it.comments }.toList()

    fun update(): CompletableFuture<Product> {
        return query().thenApply {
            if (it.comments.isEmpty()) {
                return@thenApply product
            }

            val queries = ArrayList(product.comments.queries)
            queries.add(it)
            val summary = summary(queries)
            product = product.setSummary(summary)
            allComments = product.comments.queries.flatMap { it.comments }.toList()
            product
        }
    }

    fun query(): CompletableFuture<CommentQuery> {
        return if (product.comments.queries.isEmpty() || allComments.size < 200) {

            crawler.crawlAll(product.id).thenApply {
                val list = it.filter { it !in allComments }
                val tags = analyzer.analyse(list)
                val now = LocalDateTime.now()

                CommentQuery(list, tags, now)
            }

        } else {

            val queries = product.comments.queries
            val time = queries[queries.size - 1].time

            crawler.crawlFrom(product.id, time).thenApply {
                val list = it.filter { it !in allComments }
                val tags = analyzer.analyse(list)
                val now = LocalDateTime.now()
                CommentQuery(list, tags, now)
            }
        }
    }

    fun summary(queries: List<CommentQuery>): CommentSummary {
        return scorer.evaluate(queries)
    }

}
