import java.util.Map;

public class WordCountMapReduce {
    public static void main(String[] args) {
        // Nom de fichier par défaut
        String inputFileName = "src/The_Fellowship_Of_The_Ring.txt";

        // Phase Map
        Map<String, Integer> map = WordCountMapper.map(inputFileName);

        // Phase Reduce (pas nécessaire dans cet exemple, car nous n'avons qu'un seul map)
        Map<String, Integer> finalWordCount = map;

        // Affichage du résultat final
        System.out.println("Word Count:");
        for (Map.Entry<String, Integer> entry : finalWordCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

    }
}
