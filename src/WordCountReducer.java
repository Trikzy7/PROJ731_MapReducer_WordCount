import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCountReducer {
    public static Map<String, Integer> reduce(List<Map<String, Integer>> mapResults) {
        // Implémentez la logique de réduction ici
        // Dans cet exemple, nous combinons simplement les résultats de tous les Mappers
        Map<String, Integer> finalResult = new HashMap<>();
        for (Map<String, Integer> resultMap : mapResults) {
            for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
                String word = entry.getKey();
                int count = entry.getValue();
                finalResult.put(word, finalResult.getOrDefault(word, 0) + count);
            }
        }
        return finalResult;
    }
}