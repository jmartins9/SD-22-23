public class Main {
    public static void main(String[] args) throws InterruptedException {

        int N=10;
        Bank b = new Bank(10); // Create bank with 10 accounts (all with 0$)

        for (int i=0; i< N; i++)
            b.deposit(i, 1000); // Deposit 1000$ in each account

        int[] ids = {0,1,2,3,4,5,6,7,8,9};



        Thread t1 = new Thread(new Depositor(b,N));
        Thread t2 = new Thread(new Depositor(b,N));

        t1.start(); t2.start();

        new Thread(() -> {
            for (int i=0; i<N; i++)
                b.createAccount(1000); // Create 10 new accounts with 1000$ each
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(b.totalBalance(ids));
            }
        }).start();

        t1.join(); t2.join();


    }
}