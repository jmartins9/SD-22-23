import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Warehouse {

  private class Product {
    private Condition waitForProducts = lm.newCondition();
    int quantity;

    public Product() {
      this.quantity = 0;
    }

  }

  private Product get(String item) {
    Product p = map.get(item);
    if (p != null) return p;
    p = new Product();
    map.put(item, p);
    return p;
  }

  private ReentrantLock lm = new ReentrantLock();
  private Map<String, Product> map =  new HashMap<String, Product>();

  public Warehouse() {

  }

  public void supply(String item, int quantity) throws InterruptedException{
      this.lm.lock();
      Product p = this.get(item);
      p.quantity += quantity;
      p.waitForProducts.signalAll();
      this.lm.unlock();
  }

  /* // Greedy version
  public void consume(Set<String> items) throws InterruptedException {
      this.lm.lock();
      for (String s : items) {

        Product p = this.get(s);
        while(p.quantity<=0) {
          p.waitForProducts.await();
        }
        p.quantity--;
      }
      this.lm.unlock();
  }
  */
  // Cooperative version
  public void consume(Set<String> items) throws InterruptedException {
      this.lm.lock();
      int i = 0;
      int tam = items.size();
      Product[] products = new Product[tam];

      for (String s : items) {
          products[i++] = this.get(s);
      }

      for (i=0; i < tam; i++) {
          while(products[i].quantity <= 0) {
              products[i].waitForProducts.await();
              i = 0;
          }
      }

      for (i=0; i < tam; i++) {
          products[i].quantity--;
      }

      this.lm.unlock();

  }


}
