import java.util.Map;

public class WordCountReducer {
    public static Map<String, Integer> reduce(Map<String, Integer> map1, Map<String, Integer> map2) {
        for (Map.Entry<String, Integer> entry : map2.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();
            map1.put(word, map1.getOrDefault(word, 0) + count);
        }

        return map1;
    }
}