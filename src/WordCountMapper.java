import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Map;

public class WordCountMapper {
    public static Map<String, Integer> map(String document) {
        Map<String, Integer> wordCountMap = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(document);

        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken().toLowerCase();
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }

        return wordCountMap;
    }
}