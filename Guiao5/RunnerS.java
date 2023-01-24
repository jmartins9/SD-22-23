
public class RunnerS implements Runnable{

    private Warehouse w;
    private String item;
    private int quantity;

    public RunnerS(Warehouse w, String item, int quantity) {
        this.w = w;
        this.item = item;
        this.quantity = quantity;
    }

    @Override
    public void run() {
        try{
            this.w.supply(item,quantity);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }


}


