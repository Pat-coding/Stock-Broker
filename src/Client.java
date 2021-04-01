import java.util.ArrayList;
import java.util.HashMap;

/**
 * Client class controls the client side of the program containing the interactions such as buying and selling stocks.
 *
 * @author 1909148 Chinnapat Jongthep
 */
public class Client implements Runnable {
    ArrayList<Company> compToBuy;
    ArrayList<Float> amountToBuy;
    ArrayList<Float> limitToBuy;
    private HashMap<Company, Float> shares;
    private float balance;
    private StockExchange stockExchange;
    private String name;

    /**
     * The constructor of client.
     *
     * @param shares  Shares of the client.
     * @param balance Balance of the client.
     * @param name    Name of the client.
     */
    public Client(HashMap<Company, Float> shares, float balance, String name) {
        this();
        this.shares = shares;
        this.balance = balance;
        this.name = name;
    }

    /**
     * The empty constructor of client.
     */
    public Client() {
        shares = new HashMap<>();
        //delete later
        compToBuy = new ArrayList<>();
        amountToBuy = new ArrayList<>();
        limitToBuy = new ArrayList<>();
    }

    //@Override
    public void run2() {


        for (int i = 0; i < 10; i++) {
            buy((Company) stockExchange.getCompanies().keySet().toArray()[0], 1);
            stockExchange.changePriceBy((Company) stockExchange.getCompanies().keySet().toArray()[0], -0.05f);
        }
        try {
            buyLow((Company) stockExchange.getCompanies().keySet().toArray()[0], 3, 2.1f);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(getBalance());
    }

    /**
     * The setter to set each client with a stock exchange.
     *
     * @param stockExchange The stock exchange each client will use.
     */
    public void setStockExchange(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    /**
     * The getter to return how much shares of a company the client has owned.
     *
     * @return The shares of the company the client owns.
     */
    public HashMap<Company, Float> getStocks() {
        return shares;
    }

    /**
     * The setter to set shares of a company for a client.
     *
     * @param shares The shares to set of the company.
     */
    public void setStocks(HashMap<Company, Float> shares) {
        this.shares = shares;
    }

    /**
     * The method to buy stocks for the client.
     *
     * @param company        The company the client wants to buy stocks in.
     * @param numberOfShares The number of shares the client wants to buy.
     * @return Returns true or false when buy goes through or not.
     */
    public boolean buy(Company company, float numberOfShares) {
                setBalance(stockExchange.clientShareToPrice(company, numberOfShares, getBalance()));
                System.out.println(name + " has bought " + numberOfShares + " with " + getBalance() + "p left");
                stockExchange.processOrders(company, numberOfShares, StockExchange.orderType.BUY);
                return true;
    }

    /**
     * The method to sell stocks.
     *
     * @param company        The company the client wants to sell their shares of.
     * @param numberOfShares The number of shares the client wants to sell.
     * @return Returns true or false when the sell goes through or not.
     */
    public boolean sell(Company company, float numberOfShares) {
            setBalance(stockExchange.clientShareToPrice(company, numberOfShares, getBalance()));
            System.out.println(name + " has sold " + numberOfShares + " shares to " + company.getName()
                    + ", current balance now: " + getBalance() + "p");
            stockExchange.processOrders(company, numberOfShares, StockExchange.orderType.SELL);
            return true;
    }

    /**
     * The method to buy stocks with a given limit.
     *
     * @param company        The company the client wants to buy shares in.
     * @param numberOfShares The number of shares the client wants to buy.
     * @param limit          The limit price of the shares the client is willing to buy at.
     * @return Returns true or false when the buy goes through or not.
     * @throws InterruptedException The exception to throw when method gets interrupted.
     */
    public boolean buyLow(Company company, float numberOfShares, float limit) throws InterruptedException {
        System.out.println(name + " has set buy limit: " + limit + "p for " + company.getName());
        stockExchange.queueBuyLimit(company, numberOfShares, limit);
        return true;
    }

    /**
     * The method to sell stocks with a given limit.
     *
     * @param company        The company the client wants to sell shares in.
     * @param numberOfShares The number of shares the client wants to sell.
     * @param limit          The limit price of the shares the client is willing to sell at.
     * @return Returns true or false when the buy goes through or not.
     * @throws InterruptedException The exception to throw when method gets interrupted.
     */
    public boolean sellHigh(Company company, float numberOfShares, float limit) throws InterruptedException {
        System.out.println(name + " has set sell limit: " + limit + "p for " + company.getName());
        stockExchange.queueSellLimit(company, numberOfShares, limit);
        return true;
    }

    /**
     * The method to deposit currency to the client's account.
     *
     * @param amount The amount of currency the client wants to deposit.
     * @return Returns true or false when the currency goes through or not.
     */
    public boolean deposit(float amount) {
        setBalance(getBalance() + amount);
        System.out.println(getBalance() + "p has been deposited");
        return true;
    }


    /// Delete me just for testing

    /**
     * The method to withdraw currency from the client's account.
     *
     * @param amount The amount of currency the client wants to withdraw.
     * @return Returns true or false when the withdraw goes through or not.
     */
    public boolean withdraw(float amount) {
        if (getBalance() - amount >= 0) {
            setBalance(getBalance() - amount);
            System.out.println(amount + "p has been withdrawn and " + getBalance() + "p is left");
            return true;
        } else {
            System.out.println("Not enough balance to be withdrawn");
            return false;
        }

    }

    /**
     * The getter for the balance of the client's account.
     *
     * @return The balance of the client's account.
     */
    public float getBalance() {
        return balance;
    }

    /**
     * The setter for the balance of the client's account.
     *
     * @param balance The amount of currency in the client's account.
     */
    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void addTasksToRun(Company company, float amount) {
        compToBuy.add(company);
        amountToBuy.add(amount);
        limitToBuy.add(null);
    }

    public void addTasksToRun(Company company, float amount, float limit) {
        compToBuy.add(company);
        amountToBuy.add(amount);
        limitToBuy.add(limit);
    }

    @Override
    public void run() {
        while (!compToBuy.isEmpty()) {
            Company company = compToBuy.get(0);
            float amount = amountToBuy.get(0);
            Float limitCheck = limitToBuy.get(0);
            if (limitCheck == null) {
                // Market Order;
                if (amount >= 0) {
                    // buy Market Order
                    buy(company, amount);
                } else {
                    // sell market Order
                    sell(company, -amount);
                }
            } else {
                // Limit Orders
                float limit = limitCheck;
                try {
                    if (amount >= 0) {
                        buyLow(company, amount, limit);

                    } else {
                        sellHigh(company, -amount, limit);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Sleep
        }

        // No more orders to do

    }


}
