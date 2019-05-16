package nlp

import model.Comment
import model.Tag

class FakeTagAnalyzer : TagAnalyzer {

    override fun analyse(comment: Comment): MutableList<Tag> {
        return mutableListOf(Tag(comment.content.first().toString(), 1, 1.0));
    }

}