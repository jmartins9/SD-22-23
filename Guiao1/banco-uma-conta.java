import java.util.concurrent.locks.ReentrantLock;

class Bank {

  private static class Account {

    private int balance;

    Account(int balance) {
      this.balance = balance;
    }

    int balance() {
      return balance;
    }

    boolean deposit(int value) {
      balance += value;
      return true;
    }
  }

  private ReentrantLock cadeado = new ReentrantLock();

  // Our single account, for now
  private Account savings = new Account(0);

  // Account balance
  public int balance() {
    return savings.balance();
  }

  // Deposit
  boolean deposit(int value) {
    cadeado.lock();
    try {
      return savings.deposit(value);
    } finally {
      cadeado.unlock();
    }
  }

}

class Depositor implements Runnable{

  private final int I;
  private final Bank B;

  Depositor(int i,Bank b) {
    this.I = i;
    this.B = b;
  }

  public void run(){
    for(int i=0;i<this.I;i++) {
      this.B.deposit(100);
    }
  }


}
