import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordCountMapper {

    public static List<Map<String, Integer>> mapInParallel(String fileName, int numMappers) {
        ExecutorService executorService = Executors.newFixedThreadPool(numMappers);
        List<Map<String, Integer>> mapResults = new ArrayList<>();

        try {
            // Lecture du contenu du fichier
            String fileContent = WordCountMapper.readFile(fileName);

            // String fileContent = "banane banane fraise prout prout prout fraise lait";
            // Division du contenu en morceaux
            List<String> fileParts = WordCountMapper.splitContent(fileContent, numMappers);

            for (String part : fileParts) {
                // Exécute chaque Mapper dans un thread différent
                executorService.execute(() -> mapResults.add(WordCountMapper.mapSinglePart(part)));
            }

            // Attendre que tous les threads se terminent
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return mapResults;
    }

    private static Map<String, Integer> mapSinglePart(String content) {
        Map<String, Integer> wordCountMap = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(content);
        while (tokenizer.hasMoreTokens()) {
            // recuperation du mot et mis en minuscule
            String word = tokenizer.nextToken().toLowerCase();

            // ponctuation à supprimer
            ArrayList<String> ponctuations = new ArrayList<>(Arrays.asList(".", ",", ";", "!", "?", "`", "_", "-", "\"", ":", "'", "(", ")", "[", "]"));

            // derniere lettre du mot
            String derniereLettre = String.valueOf(word.charAt(word.length() - 1));

            // boucle pour supprimer tous les caracteres de fin qui ne sont pas bons
            while ((ponctuations.contains(derniereLettre))&&(word.length()!=1)) {
                // Supprimer la dernière lettre
                word = word.substring(0, word.length() - 1);
                // recuperer la derniere lettre
                derniereLettre = String.valueOf(word.charAt(word.length() - 1));
            }

            // premiere lettre du mot
            String premiereLettre = String.valueOf(word.charAt(0));

            // boucle pour supprimer tous les caracteres de debut qui ne sont pas bons
            while ((ponctuations.contains(premiereLettre))&&(word.length()!=1)) {
                // Supprimer la première lettre
                word = word.substring(1);
                // recuperer la premier lettre
                premiereLettre = String.valueOf(word.charAt(0));
            }


            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }

        return wordCountMap;
    }

    // lecture du fichier
    private static String readFile(String fileName) {
        // Lire le contenu du fichier et le retourner sous forme de chaîne
        // Utilisation de FileReader
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder content = new StringBuilder();
            String line;

            // parcoure des lignes
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            // renvoie le contenu
            return content.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    // Split du contenu
    private static List<String> splitContent(String content, int numParts) {
        List<String> fileParts = new ArrayList<>();
        int contentLength = content.length();
        int partSize = contentLength / numParts;

        int startIndex = 0;
        int endIndex;

        // parcoure le contenu pour le séparer
        for (int i = 0; i < numParts; i++) {
            endIndex = findEndIndex(content, startIndex + partSize);

            String partContent = content.substring(startIndex, endIndex).trim();
            fileParts.add(partContent);

            startIndex = endIndex;
        }

        return fileParts;
    }

    // Trouver l'index de fin
    private static int findEndIndex(String content, int startIndex) {
        // Trouver la position du prochain espace à partir de startIndex
        int index = content.indexOf(' ', startIndex);

        // S'il n'y a pas d'espace, ou si l'espace suivant est à la fin du contenu, retournez la fin du contenu
        return (index != -1) ? index : content.length();
    }

}
