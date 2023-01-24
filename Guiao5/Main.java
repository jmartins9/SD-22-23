import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Warehouse w = new Warehouse();

        Set<String> item0 = new HashSet<>();
        item0.add("Pilhas");
        item0.add("Papel");
        item0.add("Bananas");

        Set<String> item1 = new HashSet<>();
        item1.add("Pilhas");

        Set<String> item2 = new HashSet<>();
        item2.add("Papel");
        item2.add("Bananas");

        Set[] s = new Set[3];
        s[0] = item0;
        s[1] = item1;
        s[2] = item2;

        Thread[] ts = new Thread[14];

        int i;

        for (i=0;i<10;i++) {
            ts[i] = new Thread(new RunnerC(w,s[i%3]));
            ts[i].start();
        }

        ts[i] = new Thread(new RunnerS(w,"Pilhas", 10));
        ts[i++].start();
        ts[i] = new Thread(new RunnerS(w,"Bananas", 5));
        ts[i++].start();
        ts[i] = new Thread(new RunnerS(w,"Papel", 10));
        ts[i++].start();


        System.out.println("Adormece...");
        Thread.sleep(10000);

        ts[i] = new Thread(new RunnerS(w,"Bananas", 5));
        ts[i].start();

        for(i=0;i<14;i++) {
            ts[i].join();
        }

    }
}