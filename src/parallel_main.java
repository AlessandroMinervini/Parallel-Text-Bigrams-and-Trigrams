import java.util.ArrayList;
import java.io.FileNotFoundException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
import java.lang.InterruptedException;
import java.io.PrintWriter;
import java.io.FileOutputStream;


public class parallel_main {


    public static void saveToTxt(String fileName, String bigrams, String trigrams) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
        pw.write(bigrams);
        pw.write(trigrams);
        pw.close();
    }


    public static void main(String args[]) {

        char[] fileString = sequential_main.readTextFromFile();

        int fileLen = fileString.length;
        int threads = Runtime.getRuntime().availableProcessors();
        int realThreads = threads/2;

        ArrayList<parallel_thread> threadsArray = new ArrayList<>();

        for (int i = 0; i < realThreads; i++) {
            parallel_thread t = new parallel_thread("" + i, i*fileLen/realThreads, (i+1)*fileLen/realThreads, 2, fileString);
            threadsArray.add(t);

            t.start();

            //int tIndex = Integer.parseInt(t.getIdThread()) + 1;
            //System.out.println("Thread " + tIndex + " starts...");

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
        String finalTrigrams = "";

        for (parallel_thread t: threadsArray) {
            if (t.getN() == 2){
                finalBigrams += t.getBigrams();
            }
            else {
                finalTrigrams += t.getTrigrams();
            }
        }

        try{
            saveToTxt("n-grams.txt", finalBigrams, finalTrigrams);
            System.out.println("Successfully saved n-grams");
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }

    }
}