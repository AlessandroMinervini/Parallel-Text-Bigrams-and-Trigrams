import java.io.FileNotFoundException;
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

    public static String computeNGrams(int n, char[] fileString){

        String finalNgrams = "";

        for (int i = 0; i < fileString.length - n + 1; i++){
            StringBuilder builder = new StringBuilder();

            for (int j = 0; j < n; j ++){
                builder.append(fileString[i+j]);
            }
            finalNgrams += builder.toString();
        }

        return finalNgrams;

    }

    public static void main(String[] args) {
        char[] text = readTextFromFile();

        System.out.println("Bigrams:");

        long start = 0;
        long end = 0;

        try {
            start = System.currentTimeMillis();
            parallel_main.saveToTxt("n-grams-seq.txt", computeNGrams(2, text), 2);
            end = System.currentTimeMillis();
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }

        System.out.println("Total time: " + (end-start));


    }
}