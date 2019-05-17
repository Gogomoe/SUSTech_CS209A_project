package scorer

import model.CommentQuery
import model.CommentSummary

class CommentSummer(private val weights: MutableMap<String, Int>) : CommentScorer {

    override fun evaluate(queries: MutableList<CommentQuery>): CommentSummary {
        var result = 0.0
        queries.flatMap { it.tags }
                .forEach { result += it.count * it.score * (weights[it.content] ?: 1) }
        return CommentSummary(queries, result)
    }

    fun updateWeight(name: String, weight: Int) {
        weights[name] = weight
    }


}