import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class parallel_main {


    public static void saveToTxt(String fileName, String n_grams, int n) throws FileNotFoundException {

        PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));

        for (int i = 1; i <= n_grams.length(); i++){
            char c = n_grams.charAt(i-1);
            pw.write(c);
            if (i % n == 0)
                pw.write("\n");
        }

        pw.close();

    }


    public static void main(String args[]) {

        char[] fileString = sequential_main.readTextFromFile();

        int fileLen = fileString.length;
        int threads = Runtime.getRuntime().availableProcessors();
        int realThreads = threads/2;

        String finalNgrams = "";
        ArrayList<Future> futuresArray = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(realThreads);

        long start;
        start = System.currentTimeMillis();

        for (int i = 0; i < realThreads; i++) {

            Future f = executor.submit(new parallel_thread("t" + i, i*fileLen/realThreads, (i+1)
                    *fileLen/realThreads, 2, fileString));

            futuresArray.add(f);
            
        }

        try{

            for (Future <String> f : futuresArray) {
                String n_grams = f.get();
                finalNgrams += n_grams;
            }

            System.out.println("lenght: " + finalNgrams.length());
            awaitTerminationAfterShutdown(executor);

            System.out.println("Finished all threads");

            long end = System.currentTimeMillis();

            System.out.println("Total time: " + (end-start));

            saveToTxt("n-grams-par.txt", finalNgrams, 2);
            System.out.println("Successfully saved n-grams");

        }

        catch (Exception e){
                System.out.println(e);
        }

    }

    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}