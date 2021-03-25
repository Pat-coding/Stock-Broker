import java.util.HashMap;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        HashMap<Company, Float> shares1 = new HashMap<>();
        HashMap<Company, Float> shares2 = new HashMap<>();

        Company Amazon = new Company("Amazon", 9f, 200f, 3f);

        shares2.put(new Company("GME", 100.2232f, 100.0f, 3.1f), 20f);
        Client tom = new Client(shares1, 2000.1f, "tom");
        Client harry = new Client(shares2, 2000.1f, "harry");

        StockExchange newStock = new StockExchange(Amazon, 200f);

        Thread T1 = new Thread(tom);
        tom.setStockExchange(newStock);
        Thread T2 = new Thread(harry);
        harry.setStockExchange(newStock);

        T1.start();
        T2.start();
        T1.join();
        T2.join();
        System.out.println(newStock.getCompanies().get(Amazon));
    }
}
