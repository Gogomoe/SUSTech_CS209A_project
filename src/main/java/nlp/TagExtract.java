package nlp;

import com.hankcs.hanlp.corpus.document.sentence.word.Word;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import model.Comment;
import model.Tag;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TagExtract implements TagAnalyzer {

    @Override
    public List<Tag> analyse(Comment comment) {
        String sentence = comment.getContent();
        List<Tag> tagList = new LinkedList<>();
        CRFLexicalAnalyzer analyzer = null;
        try {
            analyzer = new CRFLexicalAnalyzer();
            List<Word> terms = analyzer.analyze(sentence).toSimpleWordList();

            for (int i = 0; i < terms.size() - 2; i++) {
                if (terms.get(i).getLabel().equals("a") &&
                        terms.get(i + 1).getLabel().equals("n") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue(), 1, 1));
                    i++;
                } else if (terms.get(i).getLabel().equals("n")
                        && terms.get(i + 1).getLabel().equals("a") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue(), 1, 1));
                    i++;
                } else if (terms.get(i).getLabel().equals("n") &&
                        terms.get(i + 1).getLabel().equals("b") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue(), 1, 1));
                    i++;
                } else if (terms.get(i).getLabel().equals("a") &&
                        terms.get(i + 1).getLabel().equals("n") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue(), 1, 1));
                    i++;
                } else if (terms.get(i).getLabel().equals("a") &&
                        terms.get(i + 1).getLabel().equals("v") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue(), 1, 1));
                    i++;
                } else if (terms.get(i).getLabel().equals("v") &&
                        terms.get(i + 1).getLabel().equals("a") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue(), 1, 1));
                    i++;
                } else if (terms.get(i).getLabel().equals("d") &&
                        terms.get(i + 1).getLabel().equals("v") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue(), 1, 1));
                    i++;
                } else if (terms.get(i).getLabel().equals("d") &&
                        terms.get(i + 1).getLabel().equals("a") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue(), 1, 1));
                    i++;
                } else if (terms.get(i).getLabel().equals("n") &&
                        terms.get(i + 1).getLabel().equals("d") &&
                        terms.get(i + 2).getLabel().equals("a") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue() + terms.get(i + 2).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue() + terms.get(i + 2).getValue(), 1, 1));
                    i = i + 2;
                } else if (terms.get(i).getLabel().equals("n") &&
                        terms.get(i + 1).getLabel().equals("d") &&
                        terms.get(i + 2).getLabel().equals("v") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue() + terms.get(i + 2).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue() + terms.get(i + 2).getValue(), 1, 1));
                    i = i + 2;
                } else if (terms.get(i).getLabel().equals("d") &&
                        terms.get(i + 1).getLabel().equals("a") &&
                        terms.get(i + 2).getLabel().equals("v") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue() + terms.get(i + 2).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue() + terms.get(i + 2).getValue(), 1, 1));
                    i = i + 2;

                } else if (terms.get(i).getLabel().equals("v") &&
                        terms.get(i + 1).getLabel().equals("d") &&
                        terms.get(i + 2).getLabel().equals("a") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue() + terms.get(i + 2).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue() + terms.get(i + 2).getValue(), 1, 1));
                    i = i + 2;
                } else if (terms.get(i).getLabel().equals("b") &&
                        terms.get(i + 1).getLabel().equals("ad") &&
                        terms.get(i + 2).getLabel().equals("v") &&
                        (terms.get(i).getValue() + terms.get(i + 1).getValue() + terms.get(i + 2).getValue()).length() >= 3) {
                    tagList.add(new Tag(terms.get(i).getValue() + terms.get(i + 1).getValue() + terms.get(i + 2).getValue(), 1, 1));
                    i = i + 2;
                }
                return tagList;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
