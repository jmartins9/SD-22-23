import java.util.Set;

public class RunnerC implements Runnable{

    private Warehouse w;
    private Set<String> item;

    public RunnerC(Warehouse w, Set<String> item) {
        this.w = w;
        this.item = item;
    }

    @Override
    public void run() {
        try{
            System.out.println("Entrou para consumir...");
            this.w.consume(this.item);
            System.out.println("Consumiu.");
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }


}
