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
        System.out.println("------- MAP");
        System.out.println(mapResults);
        System.out.println("NB de hashMap in list map: " + mapResults.size());


        // Phase Shuffle : On Repartir tous les mots dans des HashMap de façon uniform
        List<Map<String, List<Integer>>> shuffleResults = Hashing.mergeMapsUniform(mapResults, 3);
        System.out.println("------- SHUFFLE");
        System.out.println(shuffleResults);
        System.out.println("Nb de hashMap in list shuffle: " + shuffleResults.size());

//        // Phase Reduce
        List<Map<String, Integer>> reduceResult = WordCountReducer.reduceInParallel(shuffleResults);
        System.out.println("------- REDUCE");
        System.out.println(reduceResult);
        System.out.println("Nb de reducer in list reduce: " + reduceResult.size());

//        // Phase Reduce OUTPUT FILE:
        Map<String, Integer> outputFile = WordCountReducer.concatenateMaps(reduceResult);
        System.out.println("------- OUTPUT FILE");
        System.out.println(outputFile);


        // Affichage du résultat final
//        System.out.println("---------- Word Count:");
//        for (Map.Entry<String, Integer> entry : outputFile.entrySet()) {
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
