import java.util.concurrent.locks.ReentrantLock;

public class EP {
    private int sumT;
    private int countT;
    private ReentrantLock lock = new ReentrantLock();

    public EP() {
        sumT = 0;
        countT = 0;
    }


    public void updateSumT(int value) {
        this.lock.lock();
        this.sumT += value;
        this.countT += 1;
        this.lock.unlock();
    }

    public int getMed() {
        try {
            this.lock.lock();
            return sumT/countT;
        } finally {
            this.lock.unlock();
        }

    }


}
