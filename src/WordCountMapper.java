import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class WordCountMapper {
    public static Map<String, Integer> map(String fileName) {
        Map<String, Integer> wordCountMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line);
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
                        System.out.println(word);
                        // Supprimer la dernière lettre
                        word = word.substring(0, word.length() - 1);
                        derniereLettre = String.valueOf(word.charAt(word.length() - 1));
                    }
                    wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordCountMap;
    }
}
