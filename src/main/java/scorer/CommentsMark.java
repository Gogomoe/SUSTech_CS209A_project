package scorer;

import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import model.Comment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;


public class CommentsMark {

    public static void main(String[] args) {

        try {
//这里读入json文件

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static TreeMap<Word, Integer> tagMark(TreeMap<Word, Integer> TreeMap, Comment cm) {
        List<Word> words = NLPTokenizer
                .segment(cm.getContent())
                .stream()
                .map(w -> new Word(w.word, ""))
                .map(x -> WordDistinguish(x))
                .filter(x -> !x.getType().equals(""))
                .collect(Collectors.toList());
        for (int i = 1; i < words.size() - 1; i++) {
            if (words.get(i).getType().equals("positive") /*|| words.get(i).getType().equals("negative")*/
                    && (words.get(i - 1).getType().equals("ish") || words.get(i - 1).getType().equals("insufficient")
                    || words.get(i - 1).getType().equals("more") || words.get(i - 1).getType().equals("most")
                    || words.get(i - 1).getType().equals("very"))) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(words.get(i - 1));
                stringBuffer.append(words.get(i));

                String string = stringBuffer.toString();
                Word word = new Word(string, "positive");

                if (TreeMap.get(word) == null) {
                    TreeMap.put(word, 1);
                } else {
                    int temp = TreeMap.get(word);
                    temp++;
                    TreeMap.put(word, temp);
                }

            } else if (words.get(i).getType().equals("negative")
                    && (words.get(i - 1).getType().equals("ish") || words.get(i - 1).getType().equals("insufficient")
                    || words.get(i - 1).getType().equals("more") || words.get(i - 1).getType().equals("most")
                    || words.get(i - 1).getType().equals("very"))) {

                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(words.get(i - 1));
                stringBuffer.append(words.get(i));

                String string = stringBuffer.toString();
                Word word = new Word(string, "positive");

                if (TreeMap.get(word) == null) {
                    TreeMap.put(word, 1);
                } else {
                    int temp = TreeMap.get(word);
                    temp++;
                    TreeMap.put(word, temp);
                }
            }

        }

        return TreeMap;
    }


    public static double commentMark(Comment cm) {
        try {

            //Comment cm = null;
            //  String comment = cm.getContent();
            String comment = "超级喜欢，真的太香了，要是纠结买x还是xr的朋友，别犹豫了，xr性能完爆x，待机完爆x，xr就是x的升级版，性价比太高了，骚黄太好看了，炒鸡喜欢，屏幕方面别和xsmax比较就行，能接受屏幕的买它绝对不后悔，屏幕虽然分辨率不高但是很护眼，而且是最省电的，电池非常大，待机时间爆表，目前苹果手机里待机时间最长的一款，支持双卡双待，非常满意nice！";
            List<Word> words = NLPTokenizer
                    .segment(comment)
                    .stream()
                    .map(w -> new Word(w.word, ""))
                    .map(x -> WordDistinguish(x))
                    .filter(x -> !x.getType().equals(""))
                    .collect(Collectors.toList());
            double score = 0;
            for (int i = 1; i < words.size() - 1; i++) {
                if (words.get(i).getType().equals("fouding")) {
                    score--;
                } else if (words.get(i).getType().equals("positive")) {
                    if (words.get(i - 1).getType().equals("insufficient")) {
                        score = score + 1.5;
                    } else if (words.get(i - 1).getType().equals("ish")) {
                        score = score + 2;
                    } else if (words.get(i - 1).getType().equals("more")) {
                        score = score + 3;
                    } else if (words.get(i - 1).getType().equals("very")) {
                        score = score + 4;
                    } else if (words.get(i - 1).getType().equals("most")) {
                        score = score + 5;
                    } else if (words.get(i - 1).getType().equals("fouding") || words.get(i - 1).getType().equals("negative") || words.get(i + 1).getType().equals("negative")) {
                        score--;
                    }

                } else if (words.get(i).getType().equals("negative")) {
                    if (words.get(i - 1).getType().equals("insufficient")) {
                        score = score - 1.5;
                    } else if (words.get(i - 1).getType().equals("ish")) {
                        score = score - 2;
                    } else if (words.get(i - 1).getType().equals("more")) {
                        score = score - 3;
                    } else if (words.get(i - 1).getType().equals("very")) {
                        score = score - 4;
                    } else if (words.get(i - 1).getType().equals("most")) {
                        score = score - 5;
                    } else if (words.get(i - 1).getType().equals("negative")) {
                        score--;
                    }

                }


            }
            return score;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Word WordDistinguish(Word word) {
        try {
            InputStreamReader reader1 = new InputStreamReader(new FileInputStream("C:\\Users\\瓜皮\\CS209A_project\\dict\\insufficient.txt"));
            BufferedReader br = new BufferedReader(reader1);
            String line = "";

            while (line != null) {
                line = br.readLine();
                if (word.equals(line)) {
                    word.setType("insufficient");
                }
            }
            InputStreamReader reader2 = new InputStreamReader(new FileInputStream("dict\\inverse.txt"));
            BufferedReader br2 = new BufferedReader(reader2);
            String line2 = "";
            while (line2 != null) {
                line2 = br2.readLine();
                if (word.equals(line2)) {
                    word.setType("inverse");
                }
            }
            InputStreamReader reader3 = new InputStreamReader(new FileInputStream("dict\\ish.txt"));
            BufferedReader br3 = new BufferedReader(reader3);
            String line3 = "";
            while (line3 != null) {
                line3 = br3.readLine();
                if (word.equals(line3)) {
                    word.setType("ish");
                }
            }
            InputStreamReader reader4 = new InputStreamReader(new FileInputStream("dict\\more.txt"));
            BufferedReader br4 = new BufferedReader(reader4);
            String line4 = "";
            while (line4 != null) {
                line4 = br4.readLine();
                if (word.equals(line4)) {
                    word.setType("more");
                }
            }
            InputStreamReader reader5 = new InputStreamReader(new FileInputStream("dict\\most.txt"));
            BufferedReader br5 = new BufferedReader(reader5);
            String line5 = "";
            while (line5 != null) {
                line5 = br5.readLine();
                if (word.equals(line5)) {
                    word.setType("most");
                }
            }
            InputStreamReader reader6 = new InputStreamReader(new FileInputStream("dict\\negative.txt"));
            BufferedReader br6 = new BufferedReader(reader6);
            String line6 = "";
            while (line6 != null) {
                line6 = br6.readLine();
                if (word.equals(line6)) {
                    word.setType("negative");
                }
            }
            InputStreamReader reader7 = new InputStreamReader(new FileInputStream("positive.txt"));
            BufferedReader br7 = new BufferedReader(reader7);
            String line7 = "";
            while (line7 != null) {
                line7 = br7.readLine();
                if (word.equals(line7)) {
                    word.setType("positive");
                }
            }

            InputStreamReader reader8 = new InputStreamReader(new FileInputStream("dict\\very.txt"));
            BufferedReader br8 = new BufferedReader(reader8);
            String line8 = "";
            while (line8 != null) {
                line8 = br8.readLine();
                if (word.equals(line8)) {
                    word.setType("very");
                }
            }

            InputStreamReader reader9 = new InputStreamReader(new FileInputStream("dict\\fouding.txt"));
            BufferedReader br9 = new BufferedReader(reader9);
            String line9 = "";
            while (line9 != null) {
                line9 = br9.readLine();
                if (word.equals(line9)) {
                    word.setType("fouding");
                }
            }

            System.out.println(word.getType());
            return word;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}