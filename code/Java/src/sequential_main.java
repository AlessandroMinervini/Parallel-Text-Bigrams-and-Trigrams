import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class sequential_main {
    public sequential_main() {
    }

    public static char[] readTextFromFile() {
        Path path = Paths.get("/Users/alessandrominervini/IdeaProjects/Ngrammi/out/production/Ngrammi/text.txt");

        try {
            Stream<String> lines = Files.lines(path);
            char[] filestring = (lines.collect(Collectors.joining()))
                    .replaceAll("[ '();:,.]", "").toCharArray();

            for(int i = 0; i < filestring.length - 1; ++i) {
                if (Character.isUpperCase(filestring[i])) {
                    filestring[i] = Character.toLowerCase(filestring[i]);
                }
            }

            return filestring;

        }

        catch (IOException e) {
            System.out.println(e);
            System.exit(1);
            return null;
        }
    }

    public static HashMap<String, Integer> computeNGrams(int n, char[] fileString) {
        HashMap<String, Integer> hashMap = new HashMap();

        for(int i = 0; i < fileString.length - n + 1; ++i) {
            StringBuilder builder = new StringBuilder();

            for(int j = 0; j < n; ++j) {
                builder.append(fileString[i + j]);
            }

            String key = builder.toString();

            if (!hashMap.containsKey(key)) {
                hashMap.put(builder.toString(), 1);
            }
            else if (hashMap.containsKey(key)) {
                hashMap.put(builder.toString(), hashMap.get(key) + 1);
            }
        }

        //System.out.println(n + "-grams:");
        return hashMap;
    }

    public static void main(String[] args) {

            char[] text = readTextFromFile();
            long start, end;

            start = System.currentTimeMillis();
            HashMap hmap = computeNGrams(3, text);
            end = System.currentTimeMillis();

/*          Set set = hmap.entrySet();
            Iterator iterator = set.iterator();

            while (iterator.hasNext()) {
                Entry map_entry = (Entry) iterator.next();
                System.out.print("key: " + map_entry.getKey() + " , value: ");
                System.out.println(map_entry.getValue());
            }*/

            System.out.println(end - start);

    }
}
