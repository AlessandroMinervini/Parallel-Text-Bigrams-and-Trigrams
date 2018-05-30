import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class parallel_thread implements Callable<ConcurrentHashMap<String, Integer>> {

    private int start, stop, n;
    private String id;
    private ConcurrentHashMap<String, Integer> n_grams;
    private char[] fileString;

    StringBuilder builder;

    public parallel_thread(String id, int start, int stop, int n, char[] fileString){   //n is the n-grams dimension

        this.id = id;
        this.start = start;
        this.stop = stop;
        this.n = n;
        this.fileString = fileString;
        this.n_grams = new ConcurrentHashMap();

    }

    public ConcurrentHashMap<String, Integer> call() {

        for (int i = this.start; i < this.stop - n + 1; i++) {

            builder = new StringBuilder();       //builder di bigrammi

            for (int j = 0; j < this.n; j++) {
                builder.append(this.fileString[i + j]);
            }

            if (this.n == 2 || this.n == 3) {

                String key = builder.toString();

                if(!this.n_grams.containsKey(key)){
                    this.n_grams.put(builder.toString(), 1);
                }
                else{
                    if(this.n_grams.containsKey(key)){
                        this.n_grams.put(builder.toString(), this.n_grams.get(key) + 1);
                    }
                }

            }

            else {
                System.out.println("invalid n");
            }
        }
        return n_grams;
    }
}
