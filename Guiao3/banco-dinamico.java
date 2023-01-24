import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
    5/7 fazem leitura
 */

class Bank {

    private ReentrantReadWriteLock lock_banco = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock read_lock = lock_banco.readLock();
    private ReentrantReadWriteLock.WriteLock write_lock = lock_banco.writeLock();

    private static class Account {

        private ReentrantLock lock_conta = new ReentrantLock();
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
        boolean withdraw(int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    private Map<Integer, Account> map = new HashMap<Integer, Account>();
    private int nextId = 0;

    public Bank(int N) {
        for (int i=0; i<N; i++) {
            this.createAccount(0);
        }
    }

    public int get_n_accounts() {
        this.read_lock.lock();
        try {
            return map.size();
        } finally {
          this.read_lock.unlock();
        }
    }

    // create account and return account id
    public int createAccount(int balance) {
        Account c = new Account(balance);
        this.write_lock.lock();
        try {
            int id = nextId;
            nextId += 1;
            map.put(id, c);
            return id;
        } finally {
            this.write_lock.unlock();
        }
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
        this.write_lock.lock();
        Account c;

        try {
            c = map.remove(id);
            if (c == null)
                return 0;
            c.lock_conta.lock();
        } finally {
            this.write_lock.unlock();
        }

        try {
            return c.balance();
        } finally {
            c.lock_conta.unlock();
        }
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        this.read_lock.lock();
        Account c;
        try {
            c = map.get(id);
            if (c == null)
                return 0;
            c.lock_conta.lock();
        } finally {
            this.read_lock.unlock();
        }

        try {
            return c.balance();
        }
        finally {
            c.lock_conta.unlock();
        }
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        this.read_lock.lock();
        Account c;

        try {
            c = map.get(id);
            if (c == null)
                return false;
            c.lock_conta.lock();
        } finally {
            this.read_lock.unlock();
        }

        try {
            return c.deposit(value);
        }
        finally {
            c.lock_conta.unlock();
        }
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        this.read_lock.lock();
        Account c;
        try {
            c = map.get(id);
            if (c == null)
                return false;
            c.lock_conta.lock();
        } finally {
            this.read_lock.lock();
        }
        try {
            return c.withdraw(value);
        } finally {
            c.lock_conta.lock();
        }
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        Account cfrom, cto;

        this.read_lock.lock();
        try {
            cfrom = map.get(from);
            cto = map.get(to);
            if (cfrom == null || cto == null)
                return false;

            if (from < to) {
                cfrom.lock_conta.lock();
                cto.lock_conta.lock();
            }
            else {
                cto.lock_conta.lock();
                cfrom.lock_conta.lock();
            }
        } finally {
            this.read_lock.unlock();
        }

        try {
            return cfrom.withdraw(value) && cto.deposit(value);
        } finally {
            cfrom.lock_conta.unlock();
            cto.lock_conta.unlock();
        }
    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
        int total = 0;

        this.read_lock.lock();
        Account[] ac = new Account[map.size()];
        try {

            List<Integer> keys = new ArrayList<>(map.keySet().stream().toList());
            keys.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer integer, Integer t1) {
                    return integer.compareTo(t1);
                }
            });

            int i=0;
            for (int key : keys) {
                ac[i] = map.get(key);
                ac[i].lock_conta.lock();
                i++;
            }
        } finally {
            this.read_lock.unlock();
        }

        for (Account c : ac ) {
            total += c.balance();
            c.lock_conta.unlock();
        }

        return total;
    }

}
