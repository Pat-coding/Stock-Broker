import java.util.HashMap;

public class Client implements Runnable{
    private HashMap<Company, Float> shares;
    private float balance;
    private StockExchange stockExchange;
    private String name;

    @Override
    public void run() {
        for(int i = 0; i < 100; i++) {
            buy((Company) stockExchange.getCompanies().keySet().toArray()[0], 1);
        }
    }

    public Client(HashMap<Company, Float> shares, float balance, String name) {
        this.shares = shares;
        this.balance = balance;
        this.name = name;
    }

    //empty  constructor for autograder?
    public Client() {
    }

    public void setStockExchange(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }
    public HashMap<Company, Float> getStocks() {
        return shares;
    }

    public void setStocks(HashMap<Company, Float> shares) {
        this.shares = shares;
    }

    public synchronized boolean buy(Company company, float numberOfShares) {
        if(stockExchange.checkPrice(company, numberOfShares, getBalance())) {
            if(stockExchange.shareStatus(company, numberOfShares)) {
                setBalance(stockExchange.clientShareToPrice(company, numberOfShares, getBalance()));
                System.out.println(name + " has bought " + numberOfShares + " with " + getBalance() + "p left");
                stockExchange.processOrders(company, numberOfShares, StockExchange.orderType.BUY);
                return true;
            } else {
                System.out.println(name + " tried to buy " + company.getName() + " but company is broke!");
                return false;
            }
        } else {
            System.out.println(name + " tried to buy " + company.getName() + " but the client broke!");
            return false;
        }
    }

    public synchronized boolean sell(Company company, float numberOfShares) {
        if(numberOfShares > getStocks().get(company)) {
            setBalance(stockExchange.clientShareToPrice(company, numberOfShares, getBalance()));
            System.out.println(name + " has sold " + numberOfShares + " shares to " + company.getName()
                    + ", current balance now: " + getBalance() + "p");
            stockExchange.processOrders(company, numberOfShares, StockExchange.orderType.SELL);
            return true;
        } else {
            System.out.println(name + " has tried to sell and invalid amount of shares");
            return false;
        }
    }

    //use wait and notify, price limit!
    public synchronized boolean buyLow(Company company, float numberOfShares, float limit) throws InterruptedException {
        while(!stockExchange.checkPriceLimit(company, limit)) {//share status
            System.out.println(name + " has set buy limit: " + limit + "p for " + company.getName());
            wait();
        }
        return buy(company, numberOfShares);
    }

    public synchronized boolean sellHigh(Company company, float numberOfShares, float limit) throws InterruptedException {
        while (!stockExchange.checkPriceLimit(company, limit)) {
            System.out.println(name + " has set sell limit: " + limit + "p for " + company.getName());
            wait();
        }
        return sell(company, numberOfShares);
    }

    public boolean deposit(float amount) {
        setBalance(getBalance() + amount);
        System.out.println(getBalance() + "p has been deposited");
        return true;
    }

    public boolean withdraw(float amount) {
        if (getBalance() - amount > 0) {
            setBalance(getBalance() - amount);
            System.out.println(amount + "p has been withdrawn and " + getBalance() + "p is left");
            return true;
        } else {
            System.out.println("Not enough balance to be withdrawn");
            return false;
        }

    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }


}
