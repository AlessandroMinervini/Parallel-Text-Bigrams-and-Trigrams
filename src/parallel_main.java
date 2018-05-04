import java.util.ArrayList;
import java.io.FileNotFoundException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;
import java.io.PrintWriter;
import java.io.FileOutputStream;


public class parallel_main {


    public static void saveToTxt(String fileName, String bigrams) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
        pw.write(bigrams);
        pw.close();
    }


    public static void main(String args[]) {

        int fileLen = sequential_main.readTextFromFile().length;
        int threads = Runtime.getRuntime().availableProcessors();
        int realThreads = threads/2;

        ArrayList<parallel_thread> threadsArray = new ArrayList<>();

        for (int i = 0; i < realThreads; i++) {
            parallel_thread t = new parallel_thread("" + i, i*fileLen/realThreads, (i+1)*fileLen/realThreads, 2);
            t.start();

            int tIndex = Integer.parseInt(t.getIdThread()) + 1;
            System.out.println("Thread " + tIndex + " starts...");

            threadsArray.add(t);

            /*Runnable worker = new parallel_thread("t" + i, i*fileLen/realThreads, (i+1)*fileLen/realThreads, 2);
            executor.execute(worker);*/     //questo solo se si fa implement runnable
        }

        for (parallel_thread t : threadsArray){
            try{
                t.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Finished all threads");

        String finalBigrams = "";
        for (parallel_thread t: threadsArray) {
            finalBigrams += t.getBigrams();
        }

        try{
            saveToTxt("bigrams.txt", finalBigrams);
            System.out.println("Successfully saved n-grams");
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }

    }
}