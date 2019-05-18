package scorer

import model.CommentQuery
import model.CommentSummary
import kotlin.math.ln

class CommentSummer(private val weights: MutableMap<String, Int>) : CommentScorer {

    override fun evaluate(queries: MutableList<CommentQuery>): CommentSummary {
        var result = 0.0
        queries.flatMap { it.tags }
                .forEach { result += it.count * it.score * (weights[it.content] ?: 1) }
        //result = calculateDensity(queries, result)
        return CommentSummary(queries, result)
    }

    private fun calculateDensity(queries: MutableList<CommentQuery>, result: Double): Double {
        var result1 = result
        val count = queries.flatMap { it.comments }.count()
        result1 = if (count != 0) {
            result1 / count * ln(count.toDouble())
        } else {
            0.0
        }
        return result1
    }

    fun updateWeight(name: String, weight: Int) {
        weights[name] = weight
    }


}