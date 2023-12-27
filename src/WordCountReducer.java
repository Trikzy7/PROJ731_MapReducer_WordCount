import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordCountReducer {
//    public static Map<String, Integer> reduce(List<Map<String, Integer>> mapResults) {
//        // Implémentez la logique de réduction ici
//        // Dans cet exemple, nous combinons simplement les résultats de tous les Mappers
//        Map<String, Integer> finalResult = new HashMap<>();
//        for (Map<String, Integer> resultMap : mapResults) {
//            for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
//                String word = entry.getKey();
//                int count = entry.getValue();
//                finalResult.put(word, finalResult.getOrDefault(word, 0) + count);
//            }
//        }
//        return finalResult;
//    }

    public static Map<String, Integer> reduceInParallel(List<Map<String, List<Integer>>> inputList) {
        /*
        GOAL : Return un hashmap le hashmap final sous cette forme : {lait=1, prout=3, fraise=2, banane=2}
            - Count all word of word with reduceSinglePart : {prout=[2, 1]} -> prout=3
            - Execute reduceSinglePart for each word in a thread
         */
        ExecutorService executorService = Executors.newFixedThreadPool(inputList.size());

        // Utilisation d'une liste synchronisée pour stocker les résultats intermédiaires
        Map<String, Integer> reduceResult = Collections.synchronizedMap(new HashMap<String, Integer>());

        try {
            for (Map<String, List<Integer>> inputMap : inputList) {
                // Faire chaque reduce dans un thread différent
                executorService.execute(() -> reduceResult.put(WordCountReducer.reduceSinglePart(inputMap).getKey(), WordCountReducer.reduceSinglePart(inputMap).getValue()));
            }

            // Attendez que tous les threads se terminent
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return reduceResult;
    }
    private static Map.Entry<String, Integer> reduceSinglePart(Map<String, List<Integer>> inputMap) {
        /*
        GOAL : Fournir un mot avec ses valeurs en entrée {prout=[2, 1]}
                et retourner (prout,3)
         */

        Map<String, Integer> resultMap = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : inputMap.entrySet()) {
            String word = entry.getKey();
            List<Integer> counts = entry.getValue();

            // Additionne les occurrences
            int totalCount = counts.stream().mapToInt(Integer::intValue).sum();

            // Ajoute le résultat à la nouvelle Map
            resultMap.put(word, totalCount);

            // Renvoie le premier couple clé-valeur
            return resultMap.entrySet().iterator().next();
        }

        // Si la Map d'entrée est vide, renvoyer null ou gérer en conséquence
        return null;
    }

}