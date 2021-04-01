import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


class ClientTest {
	Client client;
	StockExchange stockExchange;
	Company company;

	@BeforeEach
	void setUp() {
		client = new Client();
		stockExchange = new MockStockExchange();
		client.setStockExchange(stockExchange);
		company = new Company();
	}

	void addMoneyAndStocks() {
		stockExchange.addClient(client);
		stockExchange.registerCompany(company, 100);

		company.setTotalShares(100f);
		company.setAvailableShares(100f);
		company.setPrice(100f);

		client.deposit(10_000);
	}

	@Test
	void setAndGetStocks() {
		HashMap<Company, Float> stocks = new HashMap<>();
		client.setStocks(stocks);
		assertEquals(stocks, client.getStocks());
	}

	@Test
	void buyAndSell() {
//		assertFalse(client.buy(company, 10));
//		assertFalse(client.sell(company, 10));
		addMoneyAndStocks();
		assertTrue(client.buy(company, 10));
		assertTrue(client.sell(company, 10));

	}

	@Test
	void buyLowAndSellHigh() throws InterruptedException {
		//assertFalse(client.sellHigh(company, 10, 10));
		//assertFalse(client.buyLow(company, 10, 10));
		addMoneyAndStocks();
		assertTrue(client.buyLow(company, 10, 110));
		assertTrue(client.sellHigh(company, 10 ,110));
	}

	@Test
	void sellHigh() {
	}

	@Test
	void depositAndWithdraw() {
		assertFalse(client.withdraw(1));
		assertTrue(client.deposit(100));
		assertFalse(client.withdraw(101));
		assertTrue(client.withdraw(100));
//		assertTrue(client.deposit(Float.MAX_VALUE * .9f));
//		assertTrue(client.deposit(Float.MAX_VALUE * .1f));
//		assertFalse(client.deposit(Float.MAX_VALUE * .1f));
//		assertTrue(client.withdraw(Float.MAX_VALUE * .9f));
	}

	@Test
	void setStockExchange() throws NoSuchFieldException, IllegalAccessException {
		client.setStockExchange(stockExchange);
	}
}

class MockStockExchange extends StockExchange {
	public MockStockExchange() {
		super();
	}

	@Override
	public synchronized boolean registerCompany(Company company, float numberOfShares) {
		return true;
	}

	@Override
	public synchronized boolean deregisterCompany(Company company) {
		return true;
	}

	@Override
	public synchronized boolean addClient(Client client) {
		client.setStockExchange(this);
		return true;
	}

	@Override
	public synchronized boolean removeClient(Client client) {
		return true;
	}

	@Override
	public ArrayList<Client> getClients() {
		return super.getClients();
	}

	@Override
	public HashMap<Company, Float> getCompanies() {
		return super.getCompanies();
	}

	@Override
	public synchronized void setPrice(Company company, float price) throws InterruptedException {
		super.setPrice(company, price);
	}

	@Override
	public synchronized void changePriceBy(Company company, float amount) {
	}

	@Override
	public synchronized void processOrders(Company company, float shares, orderType typeOfOrder) {

	}

	@Override
	public synchronized void queueBuyLimit(Company company, float shares, float limit) {

	}

	@Override
	public synchronized void queueSellLimit(Company company, float shares, float limit) {

	}

}