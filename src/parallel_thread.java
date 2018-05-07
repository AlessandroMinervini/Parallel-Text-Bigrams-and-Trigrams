import java.util.ArrayList;
import java.util.concurrent.Callable;

public class parallel_thread implements Callable<ArrayList<String>> {

    private int start, stop, n;
    private String id;
    private ArrayList<String> n_grams;
    private char[] fileString;

    public parallel_thread(String id, int start, int stop, int n, char[] fileString){   //n is the n-grams dimension

        this.id = id;
        this.start = start;
        this.stop = stop;
        this.n = n;
        this.fileString = fileString;
        this.n_grams = new ArrayList<>();

    }

    public ArrayList<String> call() {

        for (int i = this.start; i < this.stop - this.n + 1; i++) {
            StringBuilder builder = new StringBuilder();       //builder di bigrammi

            for (int j = 0; j < this.n; j++) {
                builder.append(this.fileString[i + j]);
            }

            if (this.n == 2 || this.n == 3) {
                String N_gr = builder.toString();
                this.n_grams.add(N_gr);
            }

            else {
                System.out.println("invalid n");
            }

        }
        return n_grams;
    }
}
