package crawl

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

    private val crawler = AsyncCommentCrawler(crawler)

    fun upate(): CompletableFuture<Product> {
        return query().thenApply {
            val queries = ArrayList(product.comments.queries)
            queries.add(it)
            val summary = summary(queries)
            product = product.setSummary(summary)
            product
        }
    }

    fun query(): CompletableFuture<CommentQuery> {
        return if (product.comments.queries.isEmpty()) {

            crawler.crawlAll(product.id).thenApply {
                val tags = analyzer.analyse(it)
                val now = LocalDateTime.now()
                CommentQuery(it, tags, now)
            }

        } else {
            
            val queries = product.comments.queries
            val time = queries[queries.size - 1].time

            crawler.crawlFrom(product.id, time).thenApply {
                val tags = analyzer.analyse(it)
                val now = LocalDateTime.now()
                CommentQuery(it, tags, now)
            }
        }
    }

    fun summary(queries: List<CommentQuery>): CommentSummary {
        return scorer.evaluate(queries)
    }

}
