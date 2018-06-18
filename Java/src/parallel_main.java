import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;



public class parallel_main {

    public static ConcurrentHashMap<String, Integer> HashMerge(ConcurrentHashMap<String, Integer> n_grams, ConcurrentHashMap<String, Integer> finalNgrams) {
        for (ConcurrentHashMap.Entry<String, Integer> entry : n_grams.entrySet()) {
            int newValue = entry.getValue();
            Integer existingValue = finalNgrams.get(entry.getKey());
            if (existingValue != null) {
                newValue = newValue + existingValue;
            }
            finalNgrams.put(entry.getKey(), newValue);
        }
        return finalNgrams;
    }

    public static void main(String args[]) {

        char[] fileString = sequential_main.readTextFromFile();

        int fileLen = fileString.length;
        int threads = Runtime.getRuntime().availableProcessors();
        int realThreads = threads/2;

        ConcurrentHashMap<String, Integer> finalNgrams = new ConcurrentHashMap();

        ArrayList<Future> futuresArray = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(realThreads);

        long start, end;
        start = System.currentTimeMillis();

        int n = 3;

        double k = Math.floor(fileLen/realThreads);

        for (int i = 0; i < realThreads; i++) {

            Future f = executor.submit(new parallel_thread("t" + i, i * k, ((i+1) * k) + (n - 1) - 1, n, fileString));
            futuresArray.add(f);

        }

        try{
            for (Future <ConcurrentHashMap<String, Integer>> f : futuresArray) {
                ConcurrentHashMap<String, Integer> n_grams = f.get();
                HashMerge(n_grams,finalNgrams);
            }

            awaitTerminationAfterShutdown(executor);

            System.out.println("Finished all threads");

            end = System.currentTimeMillis();

            Set set = finalNgrams.entrySet();

            Iterator iterator = set.iterator();

            while(iterator.hasNext()) {
                Map.Entry map_entry = (Map.Entry)iterator.next();
                System.out.print("key: "+ map_entry.getKey() + " , value: ");
                System.out.println(map_entry.getValue());
            }

            System.out.println("Total time: " + (end-start));

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

