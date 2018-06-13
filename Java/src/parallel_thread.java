import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import oracle.jrockit.jfr.events.Bits;

public class parallel_thread implements Callable<ConcurrentHashMap<String, Integer>> {

    private int n;
    private double start, stop;
    private String id;
    private ConcurrentHashMap<String, Integer> n_grams;
    private char[] fileString;

    StringBuilder builder;

    public parallel_thread(String id, double start, double stop, int n, char[] fileString){   //n is the n-grams dimension

        this.id = id;
        this.start = start;
        this.stop = stop;
        this.n = n;
        this.fileString = fileString;
        this.n_grams = new ConcurrentHashMap();

    }

    public ConcurrentHashMap<String, Integer> call() {

        if (stop > fileString.length-1){
            stop = fileString.length-1;
        }

        for (double i = this.start + n - 1; i <= this.stop; i++) {

            builder = new StringBuilder();       //builder di bigrammi

            for (double j = n - 1; j >= 0; j--) {
                builder.append(this.fileString[Bits.intValue(i - j)]);
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
