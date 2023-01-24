import java.util.concurrent.locks.ReentrantLock;

public class Main {

    /*
    // Criação de 10 Threads, cada uma imprime os números até 100
    public static void main(String[] args) throws InterruptedException {
        int i=0,N=10;
        Thread [] at = new Thread[N];

        for (;i<N;i++) {
            at[i] = new Thread(new Increment(100));
            at[i].start();
        }

        for(i=0;i<10;i++) {
            at[i].join();
        }

        System.out.println("Fim");

    }*/

    // Cria 10 Threads e faz-se depositos em um banco, no final escreve-se o total
    public static void main(String[] args) throws InterruptedException {
        int i=0,N=10;
        Thread [] at = new Thread[N];
        Bank b = new Bank();

        for (;i<N;i++) {
            at[i] = new Thread(new Depositor(1000,b));
            at[i].start();
        }

        for(i=0;i<10;i++) {
            at[i].join();
        }

        System.out.println("Balanço " + b.balance());

    }
}