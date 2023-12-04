import java.util.Map;

public class WordCountMapReduce {
    public static void main(String[] args) {
        // Exemple de documents
        String document1 = "Hello world, hello Java";
        String document2 = "Java is a programming language";
        String document3 = "World of Java programming";

        // Phase Map
        Map<String, Integer> map1 = WordCountMapper.map(document1);
        Map<String, Integer> map2 = WordCountMapper.map(document2);
        Map<String, Integer> map3 = WordCountMapper.map(document3);

        // Phase Shuffle and Sort : Non nécessaire dans cet exemple simple

        // Phase Reduce
        Map<String, Integer> finalWordCount = WordCountReducer.reduce(map1, WordCountReducer.reduce(map2, map3));

        // Affichage du résultat final
        System.out.println("Word Count:");
        for (Map.Entry<String, Integer> entry : finalWordCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
