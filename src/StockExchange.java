import java.util.ArrayList;
import java.util.HashMap;

public class StockExchange {
    private HashMap<Company, Float> companies;
    private ArrayList<Client> clients;

    public enum orderType {
        BUY,
        SELL,
    }

    public StockExchange(Company company, float shares) {
        this();
        companies.put(company, shares);
    }

    public StockExchange() {
        companies = new HashMap<>();
    }
    public boolean registerCompany(Company company, float numberOfShares) {
        if (!getCompanies().containsKey(company)) {
            getCompanies().put(company, numberOfShares);
            return true;
        } else {
            System.out.println("Company already exist in Stock Exchange!");
            return false;
        }
    }

    public boolean deregisterCompany(Company company) {
        if (getCompanies().containsKey(company)) {
            getCompanies().remove(company);
            return true;
        } else {
            System.out.println("Company does not exist!");
            return false;
        }
    }

    public boolean addClient(Client client) {
        if (!getClients().contains(client)) {
            getClients().add(client);
            return true;
        } else {
            System.out.println("Client already exist!");
            return false;
        }
    }

    public synchronized void processOrders(Company company, float shares, orderType typeOfOrder) {
        if (typeOfOrder.equals(orderType.BUY)) {
            company.setLiquidity(-shares);
            getCompanies().replace(company, company.getAvailableShares());
        } else if (typeOfOrder.equals(orderType.SELL)) {
            company.setLiquidity(shares);
            getCompanies().replace(company, company.getAvailableShares());
        }
    }

    public boolean removeClient(Client client) {
        if (getClients().contains(client)) {
            getClients().remove(client);
            return true;
        } else {
            System.out.println("Client does not exist");
            return false;
        }
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public HashMap<Company, Float> getCompanies() {
        return companies;
    }

    //check for the company share price
    public boolean checkPrice(Company company, float sharesToBuy, float clientBalance) {
        return clientShareToPrice(company, sharesToBuy, clientBalance) > 0;
    }

    public boolean checkPriceLimit(Company company, float limit) {
        if(company.getPrice() == limit) {
            notify();
            return true;
        } else {
            return false;
        }
    }

    //convert share to price for client
    public float clientShareToPrice(Company company, float sharesToBuy, float clientBalance) {
        return clientBalance - (company.getPrice() * sharesToBuy);
    }

    //see if there is available shares
    public boolean shareStatus(Company company, float numberOfShares) {
        return company.getAvailableShares() - numberOfShares >= 0;
    }

    public void setPrice(Company company, float price) {
        if (price >= 0) {
            company.setPrice(price);
        } else {
            System.out.println("Price can not be negative when setting price!");
        }
    }

    public void changePriceBy(Company company, float price) {
        float priceChangedTo = company.getPrice() + price;
        if (priceChangedTo >= 0) {
            company.setPrice(priceChangedTo);
        } else {
            System.out.println("Price can not be negative when changing price!");
        }
    }
}
