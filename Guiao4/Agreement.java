import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Agreement {

        private int n;
        private ReentrantLock lock = new ReentrantLock();
        private Condition cond = lock.newCondition();
        private InstanceA ag;

        Agreement (int N) {
            this.n = N;
            this.ag = new InstanceA();
        }


        int propose(int choice) throws InterruptedException {
            this.lock.lock();

            InstanceA tmp = this.ag;
            tmp.c++;
            tmp.result = Integer.max(tmp.result,choice);

            if (tmp.c < this.n) {
                while (this.ag == tmp) {
                    cond.await();
                }
            }
            else {
                cond.signalAll();
                this.ag = new InstanceA();
            }

            lock.unlock();
            return tmp.result;
        }
}
