import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WordCountMapReduce {
    public static void main(String[] args) {

        long startTime = System.nanoTime();

        /* … Le code mesuré commence … */


        // Nom de fichier par défaut
        String inputFileName = "src/The_Fellowship_Of_The_Ring.txt";
        int numMappers = 6;  // Remplacez par le nombre de Mappers souhaité

        // Phase Map
        List<Map<String, Integer>> mapResults = WordCountMapper.mapInParallel(inputFileName, numMappers);

        // Phase Shuffle and Sort : Non nécessaire dans cet exemple simple

        // Phase Reduce (pas nécessaire dans cet exemple, car nous n'avons qu'un seul map)
        Map<String, Integer> finalWordCount = WordCountReducer.reduce(mapResults);

        // Affichage du résultat final
        System.out.println("Word Count:");
        for (Map.Entry<String, Integer> entry : finalWordCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }



        /*--------------------- … Le code mesuré se termine … */

        long endTime = System.nanoTime();

        // obtenir la différence entre les deux valeurs de temps nano
        long timeElapsed = endTime - startTime;

        System.out.println("Execution time in nanoseconds: " + timeElapsed);
        System.out.println("Execution time in milliseconds: " + timeElapsed / 1000000);

    }
}
