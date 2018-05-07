import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.PrintWriter;
import java.io.FileOutputStream;
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
        ArrayList<Future> futuresArray = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(realThreads);

        long start,end;
        start = System.nanoTime();

        for (int i = 0; i < realThreads; i++) {

            Future f = executor.submit(new parallel_thread("t" + i, i*fileLen/realThreads, (i+1)
                    *fileLen/realThreads, 3, fileString));
            futuresArray.add(f);
            
        }

        executor.shutdown();

        while (!executor.isTerminated()){}

        end = System.nanoTime();

        System.out.println(end-start);

        try{

            for (Future <ArrayList<String>> f : futuresArray) {
                ArrayList<String> n_grams = f.get();
                finalNgrams.addAll(n_grams);
            }

            System.out.println("Finished all threads");
            //System.out.println(finalNgrams);

            saveToTxt("n-grams.txt", finalNgrams);
            System.out.println("Successfully saved n-grams");

        }

        catch (Exception e){
                System.out.println(e);
        }

    }
}