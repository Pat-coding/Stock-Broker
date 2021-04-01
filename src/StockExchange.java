import java.util.ArrayList;
import java.util.HashMap;

/**
 * The stock exchange class where client orders are processed and updated to the company class.
 *
 * @author 1909148 Chinnapat Jongthep
 */
public class StockExchange {
    private final HashMap<Company, Float> companies;
    private final ArrayList<Client> clients;
    private HashMap<Company, Float> sellLimitQueue;

    /**
     * The constructor for stock exchange
     *
     * @param company The company
     * @param shares
     */
    public StockExchange(Company company, float shares) {
        this();
        companies.put(company, shares);
    }

    /**
     * The empty constructor for stock exchange
     */
    public StockExchange() {
        companies = new HashMap<>();
        clients = new ArrayList<>();
    }

    /**
     *
     *
     * @param company
     * @param numberOfShares
     * @return
     */
    public boolean registerCompany(Company company, float numberOfShares) {
        if (!getCompanies().containsKey(company)) {
            getCompanies().put(company, numberOfShares);
            return true;
        } else {
            System.out.println("Company already exist in Stock Exchange!");
            return false;
        }
    }

    /**
     *
     * @param company
     * @return
     */
    public boolean deregisterCompany(Company company) {
        if (getCompanies().containsKey(company)) {
            getCompanies().remove(company);
            return true;
        } else {
            System.out.println("Company does not exist!");
            return false;
        }
    }

    /**
     *
     * @param client
     * @return
     */
    public boolean addClient(Client client) {
        if (!getClients().contains(client)) {
            getClients().add(client);
            client.setStockExchange(this);
            return true;
        } else {
            System.out.println("Client already exist!");
            return false;
        }
    }

    /**
     *
     * @param company
     * @param shares
     * @param typeOfOrder
     */
    public void processOrders(Company company, float shares, orderType typeOfOrder) {
        if (typeOfOrder.equals(orderType.BUY)) {
            company.setLiquidity(-shares);
        } else {
            company.setLiquidity(shares);
        }
        getCompanies().replace(company, company.getAvailableShares());
    }

    /**
     *
     * @param company
     * @param shares
     * @param limit
     * @throws InterruptedException
     */
    public synchronized void queueBuyLimit(Company company, float shares, float limit) throws InterruptedException {
        while (!checkPriceLimit(company, limit, orderType.BUY_LOW)) {//share status
            System.out.println("waiting...");
            wait();
        }
        processOrders(company, shares, orderType.BUY);
        notifyAll();
    }

    /**
     *
     * @param company
     * @param shares
     * @param limit
     * @throws InterruptedException
     */
    public synchronized void queueSellLimit(Company company, float shares, float limit) throws InterruptedException {
        sellLimitQueue.put(company, limit);
        while (sellLimitQueue.size() != 0) {//share status
            System.out.println("waiting...");
            wait();
        }
        processOrders(company, shares, orderType.SELL);
    }

    public synchronized void removeSellQueueElem(Company company) {
        sellLimitQueue.remove(company);
    }


    public synchronized boolean checkSellLimit(Company company, float limit) throws InterruptedException {
        if (company.getPrice() >= limit){
            notifyAll();
            System.out.println("success"); //remove
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param client
     * @return
     */
    public boolean removeClient(Client client) {
        if (getClients().contains(client)) {
            getClients().remove(client);
            return true;
        } else {
            System.out.println("Client does not exist");
            return false;
        }
    }

    /**
     *
     * @return
     */
    public ArrayList<Client> getClients() {
        return clients;
    }

    /**
     *
     * @return
     */
    public HashMap<Company, Float> getCompanies() {
        return companies;
    }

    /**
     *
     * @param company
     * @param sharesToBuy
     * @param clientBalance
     * @return
     */
    public boolean checkPrice(Company company, float sharesToBuy, float clientBalance) {
        return clientShareToPrice(company, sharesToBuy, clientBalance) > 0;
    }

    /**
     *
     * @param company
     * @param limit
     * @param typeOfOrder
     * @return
     */
    public boolean checkPriceLimit(Company company, float limit, orderType typeOfOrder) {
        if (company.getPrice() <= limit && typeOfOrder.equals(orderType.BUY_LOW)) {
            notify();
            return true;
        } else if (company.getPrice() >= limit && typeOfOrder.equals(orderType.SELL_HIGH)){
            notify();
            return true;
        } else {
            return false;
        }
    }


    //convert share to price for client

    /**
     *
     * @param company
     * @param sharesToBuy
     * @param clientBalance
     * @return
     */
    public float clientShareToPrice(Company company, float sharesToBuy, float clientBalance) {
        return clientBalance - (company.getPrice() * sharesToBuy);
    }

    /**
     *
     * @param company
     * @param price
     */
    public void setPrice(Company company, float price) throws InterruptedException {
        if (price >= 0) {
            company.setPrice(price);
        } else {
            System.out.println("Price can not be negative when setting price!");
        }
    }

    /**
     *
     * @param company
     * @param price
     */
    public void changePriceBy(Company company, float price) {
        float priceChangedTo = company.getPrice() + price;
        if (priceChangedTo >= 0) {
            company.setPrice(priceChangedTo);
        } else {
            System.out.println("Price can not be negative when changing price!");
        }
    }

    /**
     *
     */
    public enum orderType {
        BUY,
        SELL,
        BUY_LOW,
        SELL_HIGH
    }
}
