import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.Character;

public class sequential_main{

    public static char[] readTextFromFile(){

        Path path = Paths.get("src/text.txt");

        try {
            Stream<String> lines = Files.lines(path);
            char[] filestring = lines.collect(Collectors.joining()).replaceAll("[ '();:,.]", "").toCharArray();  //delete punctuation

            for(int i = 0; i < filestring.length-1; i++){
                if(Character.isUpperCase(filestring[i])){
                    filestring[i] = Character.toLowerCase(filestring[i]);
                }
            }
            return filestring;
        }
        catch (IOException e){
            System.out.println(e);
            System.exit(1);
            return null;
        }
    }

    public static void computeNGrams(int n, char[] fileString){
        System.out.println(System.currentTimeMillis());

        for (int i = 0; i < fileString.length - n + 1; i++){
            StringBuilder builder = new StringBuilder();

            for (int j = 0; j < n; j ++){
                builder.append(fileString[i+j]);
            }

            //System.out.println(builder.toString());
        }

        System.out.println(System.currentTimeMillis());

    }

    public static void main(String[] args) {
        char[] text = readTextFromFile();
        System.out.println("Bigrams:");
        computeNGrams(2, text);
        //System.out.println("Trigrams:");
        //computeNGrams(3, text);

    }
}