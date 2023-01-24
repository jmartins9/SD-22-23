class Increment implements Runnable {

  private final int I;

  Increment(int I) {
    this.I = I;
  }

  public void run() {
    for (long i = 0; i < this.I; i++)
      System.out.println(i);
  }
}
