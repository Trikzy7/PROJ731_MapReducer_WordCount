import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordCountReducer {

    public static Map<String, Integer> concatenateMaps(List<Map<String, Integer>> listOfMaps) {
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
        GOAL : Return un hashmap le hashmap final sous cette forme : {lait=1, prout=3, fraise=2, banane=2}
            - Count all word of word with reduceSinglePart : {prout=[2, 1]} -> prout=3
            - Execute reduceSinglePart for each word in a thread
         */
        ExecutorService executorService = Executors.newFixedThreadPool(inputList.size());

        // Utilisation d'une liste synchronisée pour stocker les résultats intermédiaires
        List<Map<String, Integer>> reduceResult = Collections.synchronizedList(new ArrayList<>());

        try {
            for (Map<String, List<Integer>> inputMap : inputList) {
//                System.out.println(inputMap);
                // Faire chaque reduce dans un thread différent
//                executorService.execute(() -> reduceResult.add(WordCountReducer.reduceSinglePart(inputMap)));
                reduceResult.add(WordCountReducer.reduceSinglePart(inputMap));
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
                et retourner (prout=3, chien=2)
         */

        Map<String, Integer> resultMap = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : inputMap.entrySet()) {
//            System.out.println(entry);
            String word = entry.getKey();
            List<Integer> counts = entry.getValue();

            // Additionne les occurrences
            int totalCount = counts.stream().mapToInt(Integer::intValue).sum();

            // Ajoute le résultat à la nouvelle Map
            resultMap.put(word, totalCount);

        }

//        System.out.println(resultMap);

        // Si la Map d'entrée est vide, renvoyer null ou gérer en conséquence
        return resultMap;
    }

}