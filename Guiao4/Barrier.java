import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {

    private int n;
    private ReentrantLock lock = new ReentrantLock();
    private Condition cond = lock.newCondition();
    private int i;
    private int round;

    Barrier (int N) {
        this.n = N;
        this.i = 0;
        this.round = 0;
    }

    public void await() throws InterruptedException {
        lock.lock();
        this.i++;
        if (this.i < this.n) {
            int my_round = this.round;
            while (this.round == my_round) {
                cond.await();
            }
        }
        else {
            i = 0;
            this.round++;
            cond.signalAll();
        }

        lock.unlock();
    }

}