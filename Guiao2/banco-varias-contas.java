import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Bank {

  /*
  *  Acrescentar lock em cada conta e em cada momento que acerder a uma conta ir busca-lo e fazer um writeLock -- deposit e withdraw
  *  Para ir buscar o balance apenas obter um readLock
  *  trasnfer dois writerLock
  *  totalBalance um Intentional writeLock do array
  */

  private static class Account {
    private int balance;
    Lock l = new ReentrantLock();
    Account(int balance) { this.balance = balance; }
    int balance() { return balance; }
    boolean deposit(int value) {
      balance += value;
      return true;
    }
    boolean withdraw(int value) {
      if (value > balance)
        return false;
      balance -= value;
      return true;
    }
  }

  /* Bank slots and vector of accounts */
  private final int slots;
  private Account[] av;

  public Bank(int n) {
    slots=n;
    av=new Account[slots];
    for (int i=0; i<slots; i++) av[i]=new Account(0);
  }

  // Account balance
  public int balance(int id) {
    if (id < 0 || id >= slots)
      return 0;
    Account c = av[id];
    c.l.lock();
    try {
      return av[id].balance();
    } finally {
      c.l.unlock();
    }
  }

  /* Deposit */
  boolean deposit(int id, int value) {
    if (id < 0 || id >= slots)
      return false;
    Account c = av[id];
    c.l.lock();
    try {
      return av[id].deposit(value);
    } finally {
      c.l.unlock();
    }
  }

  /* Withdraw; fails if no such account or insufficient balance */
  public boolean withdraw(int id, int value) {
    if (id < 0 || id >= slots)
      return false;
    Account c = av[id];
    c.l.lock();
    try {
      return av[id].withdraw(value);
    }
    finally {
      c.l.unlock();
    }
  }

  boolean transfer (int from, int to, int value) {
    if (from < 0 || from >= slots || to < 0 || to >= slots)
      return false;

    Account f = av[from];
    Account t = av[to];

    if (from < to) {
      f.l.lock();
      t.l.lock();
    }
    else {
      t.l.lock();
      f.l.lock();
    }

    try {
      if (f.withdraw(value)) {
        f.l.unlock();
        return t.deposit(value);
      }
      else
        return false;
    } finally {
      t.l.unlock();
    }
  }
  int totalBalance() {
    int r = 0;
    int i;

    for ( i = 0; i < slots; i++)
      av[i].l.lock();

    for (i = 0; i < slots; i++) {
      r += av[i].balance();
      av[i].l.unlock();
    }

    return r;
  }

}
