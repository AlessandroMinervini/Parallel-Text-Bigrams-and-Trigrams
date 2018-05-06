import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class parallel_main {


    public static void saveToTxt(String fileName, ArrayList<String> n_grams) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
        pw.write(n_grams.toString());
        pw.close();
    }


    public static void main(String args[]) {

        char[] fileString = sequential_main.readTextFromFile();

        int fileLen = fileString.length;
        int threads = Runtime.getRuntime().availableProcessors();
        int realThreads = threads/2;

        ArrayList<String> finalNgrams = new ArrayList<>();

        long start = System.currentTimeMillis();

        ArrayList<Future> futuresArray = new ArrayList<>();

        try{
            for (int i = 0; i < realThreads; i++) {

                ExecutorService executor = Executors.newFixedThreadPool(realThreads);

                futuresArray.add(executor.submit(new parallel_thread("t" + i, i*fileLen/realThreads, (i+1)*fileLen/realThreads, 2, fileString)));     //questo solo se si fa implement runnable
            }

            for (Future <ArrayList<String>> f : futuresArray) {
                ArrayList<String> n_grams = f.get();
                finalNgrams.addAll(n_grams);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

        System.out.println("Finished all threads");

        long end = System.currentTimeMillis();
        System.out.println(finalNgrams);
        System.out.println(end-start);

        try{
            saveToTxt("n-grams.txt", finalNgrams);
            System.out.println("Successfully saved n-grams");
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }

    }
}