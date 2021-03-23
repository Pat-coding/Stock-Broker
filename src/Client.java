import java.util.HashMap;

public class Client {
    private HashMap<Company, Float> shares;
    private float balance;

    public Client() {
    }

    public HashMap<Company, Float> getStocks() {
        return shares;
    }

    public void setStocks(HashMap<Company, Float> shares) {
        this.shares = shares;
    }

    public boolean buy(Company company, float numberOfShares) {
        return true; //for now
    }

    public boolean sell(Company company, float numberOfShares) {
        return false; //for now
    }

    public boolean buyLow(Company company, float numberOfShares, float limit) {
        return false; //for now
    }

    public boolean buyHigh(Company company, float numberOfShares, float limit) {
        return false; //for now
    }

    public boolean deposit(float amount) {
        return false; //for now
    }

    public boolean withdraw(float amount) {
        return false; //for now
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
