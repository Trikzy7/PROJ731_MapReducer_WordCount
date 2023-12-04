import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCountMapReduce {
    public static void main(String[] args) {
        // Nom de fichier par défaut
        String inputFileName = "src/The_Fellowship_Of_The_Ring.txt";
        int numMappers = 4;  // Remplacez par le nombre de Mappers souhaité

        // Phase Map
        List<Map<String, Integer>> mapResults = WordCountMapper.map(inputFileName, numMappers);

        // Phase Shuffle and Sort : Non nécessaire dans cet exemple simple

        // Phase Reduce (pas nécessaire dans cet exemple, car nous n'avons qu'un seul map)
        Map<String, Integer> finalWordCount = WordCountReducer.reduce(mapResults);

        // Affichage du résultat final
        System.out.println("Word Count:");
        for (Map.Entry<String, Integer> entry : finalWordCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("Répertoire de travail : " + System.getProperty("user.dir"));
    }


}
