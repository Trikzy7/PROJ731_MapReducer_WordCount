import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class WordCountMapper {
    public static List<Map<String, Integer>> map(String fileName, int numMappers) {
        List<Map<String, Integer>> mapResults = new ArrayList<>();

        // Lecture du contenu du fichier
        String fileContent = readFile(fileName);

        // Division du contenu en morceaux
        List<String> fileParts = splitContent(fileContent, numMappers);

        // Phase Map
        for (String part : fileParts) {
            mapResults.add(mapSinglePart(part));
        }

        return mapResults;
    }

    private static Map<String, Integer> mapSinglePart(String content) {
        Map<String, Integer> wordCountMap = new HashMap<>();

        StringTokenizer tokenizer = new StringTokenizer(content);
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken().toLowerCase();
            String derniereLettre = String.valueOf(word.charAt(word.length() - 1));
            ArrayList<String> ponctuations = new ArrayList<String>();
            ponctuations.add(".");
            ponctuations.add(",");
            ponctuations.add(";");
            ponctuations.add("!");
            ponctuations.add("?");
            ponctuations.add("`");
            ponctuations.add("_");
            ponctuations.add("-");
            ponctuations.add("\"");
            ponctuations.add(":");
            ponctuations.add("'");
            while ((ponctuations.contains(derniereLettre))&&(word.length()!=1)) {
//                System.out.println(word);
                // Supprimer la dernière lettre
                word = word.substring(0, word.length() - 1);
                derniereLettre = String.valueOf(word.charAt(word.length() - 1));
            }
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }

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

        for (int i = 0; i < numParts; i++) {
            int start = i * partSize;
            int end = (i + 1) * partSize;
            if (i == numParts - 1) {
                end = contentLength;
            }

            String partContent = content.substring(start, end);
            fileParts.add(partContent);
        }

        return fileParts;
    }
}
