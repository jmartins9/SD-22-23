public class Main {
    /*
    public static void main(String[] args) throws InterruptedException {

        int N = 10;
        int i;
        Barrier b = new Barrier(N);
        Thread[] threads = new Thread[N];

        for (i = 0; i < N; i++) {
            threads[i] = new Thread(new Runner(b));
            threads[i].start();
        }

        for (i=0; i<N; i++) {
            threads[i].join();
        }


    }
    */

    public static void main(String[] args) throws InterruptedException {

        int N = 10;
        int i;
        Agreement b = new Agreement(N);
        Thread[] threads = new Thread[N];

        for (i = 0; i < N; i++) {
            threads[i] = new Thread(new RunnerA(b));
            threads[i].start();
        }

        for (i=0; i<N; i++) {
            threads[i].join();
        }


    }
}