import java.util.Random;

public class Depositor implements Runnable{

    Bank bank;
    int ncontas;

    public Depositor(Bank bank, int ncontas) {
        this.bank = bank;
        this.ncontas = ncontas;
    }

    @Override
    public void run() {
        final int moves=100000;
        int from, to;
        Random rand = new Random();

        for (int m=0; m<moves; m++) {
            from=rand.nextInt(this.ncontas); // Get one
            while ((to=rand.nextInt(this.ncontas))==from); // Slow way to get distinct
            this.bank.transfer(from,to,1);
        }

    }
}
