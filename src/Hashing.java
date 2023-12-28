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
        // Utilisation d'une fonction de hachage modulo pour répartir les mots entre les HashMap
        int numMaps = numReducers; // Utilisez le nombre minimum entre le nombre de reducers et le nombre d'entrées

        Map<String, List<Integer>>[] resultMapArray = new HashMap[numMaps];

        for (int i = 0; i < numMaps; i++) {
            resultMapArray[i] = new HashMap<>();
        }

        // Parcours la liste d'entrée (liste de HashMaps)
        for (Map<String, Integer> inputMap : inputList) {
            // Parcours chaque HashMap dans la liste d'entrée
            for (Map.Entry<String, Integer> entry : inputMap.entrySet()) {
                String word = entry.getKey();
                int count = entry.getValue();

                // Utilise la fonction de hachage modulo pour choisir la HashMap
                int mapIndex = Math.abs(word.hashCode() % numMaps);

                // Ajoute le nombre d'occurrences du mot à la liste résultante de la HashMap correspondante
                List<Integer> counts = resultMapArray[mapIndex].getOrDefault(word, new ArrayList<>());
                counts.add(count);
                resultMapArray[mapIndex].put(word, counts);
            }
        }

        // Convertit le tableau de HashMaps en une liste de HashMaps
        List<Map<String, List<Integer>>> resultList = Arrays.asList(resultMapArray);

        for(Map<String, List<Integer>> map : resultList){
            System.out.println(map.size());
        }
        return resultList;
    }

}
