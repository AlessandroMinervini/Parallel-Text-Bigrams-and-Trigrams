import java.util.concurrent.Callable;

public class parallel_thread implements Callable<String> {

    private int start, stop, n;
    private String id;
    private String n_grams;
    private char[] fileString;

    StringBuilder builder;

    public parallel_thread(String id, int start, int stop, int n, char[] fileString){   //n is the n-grams dimension

        this.id = id;
        this.start = start;
        this.stop = stop;
        this.n = n;
        this.fileString = fileString;
        this.n_grams = "";

    }

    public String call() {

        for (int i = this.start; i < this.stop - n + 1; i++) {

            builder = new StringBuilder();       //builder di bigrammi


            for (int j = 0; j < this.n; j++) {
                builder.append(this.fileString[i + j]);
            }


            if (this.n == 2 || this.n == 3) {
                String N_gr = builder.toString();
                n_grams += N_gr;
            }

            else {
                System.out.println("invalid n");
            }
        }
        return n_grams;
    }
}
