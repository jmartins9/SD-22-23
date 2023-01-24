import java.util.Random;

public class RunnerA implements Runnable{

    private Agreement a;

    public RunnerA(Agreement a) {
        this.a = a;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            int r = a.propose((new Random()).nextInt(100));
            System.out.println(r);
        } catch (InterruptedException e) {
            System.out.println(e.toString());;
        }

    }
}


