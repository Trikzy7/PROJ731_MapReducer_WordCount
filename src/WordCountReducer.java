import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordCountReducer {

    public static Map<String, Integer> concatenateMaps(List<Map<String, Integer>> listOfMaps) {
        /*
        GOAL : Concatener tous les HashMap de la list dans un seul HashMap
            - INPUT : [{bob=1}, {lait=1, prout=5, theo=1, alice=1, chien=2}, {fraise=2, chat=1, banane=2}]
            - OUTPUT : {lait=1, prout=5, fraise=2, bob=1, chat=1, theo=1, alice=1, banane=2, chien=2}
         */

        Map<String, Integer> concatenatedMap = new HashMap<>();

        // Parcours la liste de HashMap
        for (Map<String, Integer> map : listOfMaps) {
            // Parcours chaque HashMap
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                int value = entry.getValue();

                // Ajoute l'entrée au nouveau HashMap
                concatenatedMap.merge(key, value, Integer::sum);
            }
        }

        return concatenatedMap;
    }

    public static List<Map<String, Integer>> reduceInParallel(List<Map<String, List<Integer>>> inputList) {
        /*
        GOAL : Return une List de HashMap qui a été reduit: [{bob=1}, {lait=1, prout=5, theo=1, alice=1, chien=2}, {fraise=2, chat=1, banane=2}]
            - Count all word of word with reduceSinglePart qui prend en input : {lait=[1], prout=[1, 4], theo=[1], alice=[1], chien=[2]}
            - Execute reduceSinglePart for each word in a thread
         */

        // -- Cela va servire a executer plusieurs threads
        ExecutorService executorService = Executors.newFixedThreadPool(inputList.size());

        // -- Creation d'une list speciale qui va attendre que chaque thread soient terminés
        List<Map<String, Integer>> reduceResult = Collections.synchronizedList(new ArrayList<>());

//        System.out.println(inputList.size());

        try {
            // -- Pour chaque HashMap de shuffleResults
            for (Map<String, List<Integer>> inputMap : inputList) {
//
                // Faire chaque reduce dans un thread différent
                executorService.execute(() -> {
                    reduceResult.add(WordCountReducer.reduceSinglePart(inputMap));
//                    System.out.println("REDUCE THREAD");
                });

            }

            // Attendez que tous les threads se terminent
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return reduceResult;
    }

    private static Map<String, Integer> reduceSinglePart(Map<String, List<Integer>> inputMap) {
        /*
        GOAL : Fournir un HashMap contenant des  mots avec ses valeurs en entrée {prout=[2, 1], chien=[1, 1]}
               et return {prout=3, chien=2}
         */

        // -- Creation du HashMap que l'on va return
        Map<String, Integer> resultMap = new HashMap<>();

        // -- Pour chaque couple word=value du HashMap
        for (Map.Entry<String, List<Integer>> entry : inputMap.entrySet()) {
            String word = entry.getKey();
            List<Integer> counts = entry.getValue();

            // On dditionne les occurrences
            int totalCount = counts.stream().mapToInt(Integer::intValue).sum();

            // On ajoute le résultat à la nouvelle Map
            resultMap.put(word, totalCount);
        }

        return resultMap;
    }

}