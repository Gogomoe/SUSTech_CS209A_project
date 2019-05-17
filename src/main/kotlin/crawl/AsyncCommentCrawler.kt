package crawl

import model.Comment
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.function.Supplier


class AsyncCommentCrawler(val crawler: CommentCrawler) {

    private val size = 5

    private val pool = Executors.newFixedThreadPool(size) as ThreadPoolExecutor
    private val cached = Executors.newCachedThreadPool() as ThreadPoolExecutor

    private fun crawl(productId: Long, page: Int): CompletableFuture<List<Comment>> {
        return CompletableFuture.supplyAsync(Supplier {
            runAtLeast(1000) {

                try {
                    val res = crawler.crawl(productId, page) ?: emptyList<Comment>()
                    println("get $productId $page $res")
                    res
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList<Comment>()
                }

            }
        }, pool)
    }

    fun crawlFrom(productId: Long, startDate: LocalDateTime): CompletableFuture<List<Comment>> {
        return crawlMultiPage(productId) { it.isEmpty() || it.first().time.isBefore(startDate) }
                .thenApply { list ->
                    list.filter { it.time.isAfter(startDate) }
                }

    }


    fun crawlAll(productId: Long): CompletableFuture<List<Comment>> {
        return crawlMultiPage(productId) { it.isEmpty() }
    }

    private inline fun crawlMultiPage(productId: Long, crossinline stopCondition: (List<Comment>) -> Boolean): CompletableFuture<List<Comment>> {
        return CompletableFuture.supplyAsync(Supplier {
            var page = 0
            val list = mutableListOf<Comment>()
            val tasks = mutableSetOf<CompletableFuture<List<Comment>>>()
            var shouldRun = true

            while (shouldRun) {
                if (page >= 60) {
                    shouldRun = false
                }
                if (tasks.size < size) {
                    val task = crawl(productId, page++)
                    tasks.add(task)
                    task.thenAccept {
                        if (task.isCancelled) {
                            return@thenAccept
                        }
                        tasks.remove(task)
                        list.addAll(it)
                        if (stopCondition(it)) {
                            shouldRun = false
                            tasks.forEach { task -> task.cancel(false) }
                        }
                    }
                }
                Thread.sleep(10)
            }

            list
                    .distinct()
                    .toList()
        }, cached)
    }

    private inline fun <T> runAtLeast(time: Int, function: () -> T): T {
        val start = Instant.now()
        val result = function()
        val end = Instant.now()

        val mills = time - Duration.between(start, end).toMillis()
        if (mills > 0) {
            Thread.sleep(mills)
        }

        return result
    }

}