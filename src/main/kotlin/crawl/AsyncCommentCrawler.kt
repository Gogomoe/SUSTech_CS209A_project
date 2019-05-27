package crawl

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import model.Comment
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime

class AsyncCommentCrawler(val crawler: CommentCrawler) {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val size = 5

    data class PageResult(val pageId: Int, val comments: List<Comment>)

    fun destroy() {
        scope.cancel()
    }

    private suspend fun crawl(productId: Long, page: Int): List<Comment> {
        return runAtLeast(1000) {
            try {
                val res = crawler.crawl(productId, page) ?: emptyList<Comment>()
                println("get $productId $page $res")
                res
            } catch (e: Exception) {
                println(e.message)
                emptyList()
            }

        }
    }

    suspend fun crawlFrom(productId: Long, startDate: LocalDateTime): List<Comment> {
        val res = scope.async {
            crawlMultiPage(productId) { it.comments.isEmpty() || it.comments.first().time.isBefore(startDate) }
        }
        return res.await()
    }


    suspend fun crawlAll(productId: Long): List<Comment> {
        val res = scope.async {
            crawlMultiPage(productId) { it.comments.isEmpty() }
        }
        return res.await()
    }

    private var isClosed = false

    private suspend fun crawlMultiPage(productId: Long, stopCondition: (PageResult) -> Boolean): List<Comment> = coroutineScope {
        val comments = mutableListOf<Comment>()
        val producer = producePages()
        val resultChannel = Channel<PageResult>(size * 2)

        val jobs = List(size) { launchProcessor(productId, producer, resultChannel) }

        for (result in resultChannel) {
            comments.addAll(result.comments)
            if (!isClosed && stopCondition(result)) {
                isClosed = true
                jobs.forEach {
                    it.join()
                }
                resultChannel.close()
            }
        }
        coroutineContext.cancelChildren()
        comments.distinct()
    }


    private fun CoroutineScope.launchProcessor(productId: Long, pages: ReceiveChannel<Int>, result: SendChannel<PageResult>) = launch {
        for (page in pages) {
            if (isClosed) {
                break
            }
            result.send(PageResult(page, crawl(productId, page)))
        }
    }

    private fun CoroutineScope.producePages() = produce {
        for (i in 0..50) {
            send(i)
        }
        close()
    }


    private suspend inline fun <T> runAtLeast(time: Int, function: () -> T): T {

        val start = Instant.now()
        val result = function()
        val end = Instant.now()

        val mills = time - Duration.between(start, end).toMillis()
        if (mills > 0) {
            delay(mills)
        }

        return result
    }

}