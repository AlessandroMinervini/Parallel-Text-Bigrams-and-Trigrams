import java.util.ArrayList;

public class parallel_thread extends Thread {

    private int start, stop, n;
    private String id;
    private ArrayList<String> bigrams;
    private ArrayList<String> trigrams;
    private char[] fileString;

    public parallel_thread(String id, int start, int stop, int n, char[] fileString){   //n is the n-grams dimension

        this.id = id;
        this.start = start;
        this.stop = stop;
        this.n = n;
        this.fileString = fileString;
        this.bigrams = new ArrayList<>();
        this.trigrams = new ArrayList<>();

    }

    public void run() {
        System.out.println("start" + this.id + ":");
        System.out.println(System.currentTimeMillis());

        for (int i = this.start; i < this.stop - this.n + 1; i++) {
            StringBuilder builder = new StringBuilder();       //builder di bigrammi

            for (int j = 0; j < this.n; j++) {
                builder.append(this.fileString[i + j]);
            }

            if (this.n == 2) {
                String bigr = builder.toString();
                this.bigrams.add(bigr);
            }
            else{
                if (this.n == 3){
                    String trigr = builder.toString();
                    this.trigrams.add(trigr);
                }
                else {
                    System.out.println("invalid n");
                }
            }
        }

        System.out.println("end" + this.id + ":");
        System.out.println(System.currentTimeMillis());

    }

    public String getIdThread() {

        return this.id;

    }

    public ArrayList<String> getBigrams(){

        return this.bigrams;

    }

    public ArrayList<String> getTrigrams(){

        return this.trigrams;

    }

    public int getN() {
        return n;
    }
}