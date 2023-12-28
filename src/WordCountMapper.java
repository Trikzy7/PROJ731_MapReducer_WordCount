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
        List<Map<String, Integer>> mapResults = Collections.synchronizedList(new ArrayList<>());

        try {
            // Lecture du contenu du fichier
            String fileContent = WordCountMapper.readFile(fileName);

//            String fileContent = "banane banane fraise prout prout prout prout prout fraise lait bob alice theo chien chat chien";

            // Division du contenu en morceaux
            List<String> fileParts = WordCountMapper.splitContent(fileContent, numMappers);

            for (String part : fileParts) {
                // Exécute chaque Mapper dans un thread différent
//                System.out.println(part);
                executorService.execute(() -> mapResults.add(WordCountMapper.mapSinglePart(part)));
//                mapResults.add(WordCountMapper.mapSinglePart(part));
            }

            // Attendez que tous les threads se terminent
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
            String word = tokenizer.nextToken().toLowerCase();
            String derniereLettre = String.valueOf(word.charAt(word.length() - 1));
            ArrayList<String> ponctuations = new ArrayList<>(Arrays.asList(".", ",", ";", "!", "?", "`", "_", "-", "\"", ":", "'", "(", ")", "[", "]"));

            while ((ponctuations.contains(derniereLettre))&&(word.length()!=1)) {
                // Supprimer la dernière lettre
                word = word.substring(0, word.length() - 1);
                derniereLettre = String.valueOf(word.charAt(word.length() - 1));
            }

            String premiereLettre = String.valueOf(word.charAt(0));

            while ((ponctuations.contains(premiereLettre))&&(word.length()!=1)) {
                // Supprimer la première lettre
                word = word.substring(1);
                premiereLettre = String.valueOf(word.charAt(0));
            }
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }

//        System.out.println(wordCountMap);

        return wordCountMap;
    }

    private static String readFile(String fileName) {
        // Lisez le contenu du fichier et retournez-le sous forme de chaîne
        // Vous pouvez utiliser différentes méthodes pour cela, par exemple FileReader, Files.readAllBytes, etc.
        // Voici une version simple pour les besoins de l'exemple :
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static List<String> splitContent(String content, int numParts) {
        List<String> fileParts = new ArrayList<>();
        int contentLength = content.length();
        int partSize = contentLength / numParts;

        int startIndex = 0;
        int endIndex;

        for (int i = 0; i < numParts; i++) {
            endIndex = findEndIndex(content, startIndex + partSize);

            String partContent = content.substring(startIndex, endIndex).trim();
            fileParts.add(partContent);

            startIndex = endIndex;
        }

        return fileParts;
    }

    private static int findEndIndex(String content, int startIndex) {
        // Trouver la position du prochain espace à partir de startIndex
        int index = content.indexOf(' ', startIndex);

        // S'il n'y a pas d'espace, ou si l'espace suivant est à la fin du contenu, retournez la fin du contenu
        return (index != -1) ? index : content.length();
    }

}
