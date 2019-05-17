package nlp

import com.hankcs.hanlp.tokenizer.NLPTokenizer
import model.Comment
import model.Tag
import java.time.LocalDateTime

class SimpleAnalyazer : TagAnalyzer {

    override fun analyse(comment: Comment): MutableList<Tag> {
        val list = NLPTokenizer.segment(comment.getContent())
        val result = mutableListOf<Tag>()

        for (i in 1 until list.size) {
            val last = list[i - 1]
            val now = list[i]

            if (last.nature.toString().startsWith("v") && now.nature.toString().startsWith("a")) {
                result.add(Tag(last.word + now.word, 1, 1.0))
            }
        }
        return result

    }

}