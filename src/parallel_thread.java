import java.util.ArrayList;

public class parallel_thread extends Thread {

    private int start, stop, n;
    private String id;
    private ArrayList<String> bigrams;


    public parallel_thread(String id, int start, int stop, int n){   //n is the n-grams dimension

        this.id = id;
        this.start = start;
        this.stop = stop;
        this.n = n;
        this.bigrams = new ArrayList<>();

    }

    public void run() {

        char[] fileString = sequential_main.readTextFromFile();

        for (int i = this.start; i < this.stop-this.n+1; i++) {
            StringBuilder builder = new StringBuilder();       //builder di bigrammi

            for (int j = 0; j < this.n; j++) {
                builder.append(fileString[i + j]);
            }

            String bigr = builder.toString();

            this.bigrams.add(bigr);

        }

    }

    public String getIdThread() {

        return this.id;

    }

    public ArrayList<String> getBigrams(){

        return this.bigrams;

    }

}
