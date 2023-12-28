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
        int numMappers = 4;  // Remplacez par le nombre de Mappers souhaité

        // Phase Map : with multiple thread (=numMappers)
        List<Map<String, Integer>> mapResults = WordCountMapper.mapInParallel(inputFileName, numMappers);
//        System.out.println(mapResults);

        // Phase Shuffle : On regroupe par word
        List<Map<String, List<Integer>>> shuffleResults = Hashing.mergeMapsUniform(mapResults, 10);
        System.out.println(shuffleResults);

        // Phase Reduce
        List<Map<String, Integer>> reduceResult = WordCountReducer.reduceInParallel(shuffleResults);
        System.out.println(reduceResult);

        // Phase Reduce OUTPUT FILE:
//        Map<String, Integer> outputFile = WordCountReducer.concatenateMaps(reduceResult);
//        System.out.println(outputFile);

        // Phase Reduce (pas nécessaire dans cet exemple, car nous n'avons qu'un seul map)
//        List<Map<String, Integer>> finalWordCount = WordCountReducer.reduceInParallel(shuffleResults);



        // Affichage du résultat final
//        System.out.println("---------- Word Count:");
//        for (Map.Entry<String, Integer> entry : finalWordCount.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }



        /*--------------------- … Le code mesuré se termine … */

        long endTime = System.nanoTime();

        // obtenir la différence entre les deux valeurs de temps nano
        long timeElapsed = endTime - startTime;

        System.out.println("Execution time in nanoseconds: " + timeElapsed);
        System.out.println("Execution time in milliseconds: " + timeElapsed / 1000000);

    }
}
