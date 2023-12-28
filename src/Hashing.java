import java.util.*;

public class Hashing {
    public static List<Map<String, List<Integer>>> mergeMaps(List<Map<String, Integer>> inputList) {
        /*
        GOAL : return une liste de HashMap avec le word en key et ses values en values
                - INPUT : [{banane=2}, {prout=2}, {lait=1, fraise=1}, {prout=1, fraise=1}]
                - OUTPUT : [{lait=[1]}, {prout=[2, 1]}, {fraise=[1, 1]}, {banane=[2]}]

        Remarque : Je voulais avoir la list de hashmap suivant :
                   [{lait=1}, {prout=2, prout=1}, {fraise=1, fraise=1}, {banane=2}]

                   Mais le pb c'est que pour un hashMap, il ne peut pas y avoir deux fois la même key
         */

        Map<String, List<Integer>> resultMap = new HashMap<>();

        // Parcours la liste d'entrée (liste de HashMaps)
        for (Map<String, Integer> inputMap : inputList) {
            // Parcours chaque HashMap dans la liste d'entrée
            for (Map.Entry<String, Integer> entry : inputMap.entrySet()) {
                String word = entry.getKey();
                int count = entry.getValue();

                // Ajoute le nombre d'occurrences du mot à la liste résultante
                List<Integer> counts = resultMap.getOrDefault(word, new ArrayList<>());
                counts.add(count);
                resultMap.put(word, counts);
            }
        }

        // Convertit la resultMap en une liste de HashMaps
        List<Map<String, List<Integer>>> resultList = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : resultMap.entrySet()) {
            // Crée une nouvelle HashMap avec une seule clé (le mot) et la valeur correspondante (liste des occurrences)
            Map<String, List<Integer>> wordCountsMap = new HashMap<>();
            wordCountsMap.put(entry.getKey(), entry.getValue());

            // Ajoute la nouvelle HashMap à la liste résultante
            resultList.add(wordCountsMap);
        }

        return resultList;
    }

    public static List<Map<String, List<Integer>>> mergeMapsUniform(List<Map<String, Integer>> inputList, int numReducers) {
        /*
        GOAL : return une liste de HashMap avec pour chaque HashMap les mots répartis de façon uniforme.
        Le nombre de HashMap qu'il y aura est soit le nombre de Mapper soit le parametre numReducers
             - INPUT : [{prout=4}, {lait=1, fraise=1, bob=1, theo=1, alice=1}, {prout=1, fraise=1, banane=2}, {chat=1, chien=2}]
             - OUTPUT : [{lait=[1], prout=[4, 1], bob=[1], fraise=[1, 1], chat=[1], theo=[1], alice=[1], banane=[2], chien=[2]}]

        Remarque : Je voulais avoir la list de hashmap suivant :
                   [{lait=1}, {prout=2, prout=1}, {fraise=1, fraise=1}, {banane=2}]
         */

        // -- On choisit le min entre le nb de Mapper (qui est inputList.size() ) et le paramètre numReducers
        int numMaps = Math.min(numReducers, inputList.size());

        // -- Création de la list de hashMap que l'on va return
        List<Map<String, List<Integer>>> resultList = new ArrayList<>(numMaps);

        // -- Ajout des HashMap vide (pour l'instant) à la list qu'on va return
        for (int i = 0; i < numMaps; i++) {
            resultList.add(new HashMap<>());
        }

        // -- Remplissage des différents HashMap pour les mots qui sont présents dans les HashMap de la list resultante de la phase de Mapping
        // Pour chaque HashMap de la list mapResults
        for (Map<String, Integer> inputMap : inputList) {
            // Pour chaque couple word=value du HashMapa
            for (Map.Entry<String, Integer> entry : inputMap.entrySet()) {
                String word = entry.getKey();
                int count = entry.getValue();

                int mapIndex = (word.hashCode() & Integer.MAX_VALUE) % numMaps;

                resultList.get(mapIndex)
                        .computeIfAbsent(word, k -> new ArrayList<>())
                        .add(count);
            }
        }

//        for (Map<String, List<Integer>> map : resultList) {
//            System.out.println(map);
//            System.out.println(map.size());
//        }

        return resultList;
    }
}
