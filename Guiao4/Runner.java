public class Runner implements Runnable{

    private Barrier b;

    public Runner(Barrier b) {
        this.b = b;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            b.await();
        } catch (InterruptedException e) {
            System.out.println(e.toString());;
        }

    }
}
