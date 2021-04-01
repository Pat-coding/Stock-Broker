import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockExchangeTest {
	StockExchange se;
	Client client;
	Company company;

	@BeforeEach
	void setUp() {
		se = new StockExchange();
		client = new Client();
		company = new Company();
	}

	void connectSystems() {
		client.deposit(100_000);
		se.addClient(client);
		se.registerCompany(company, 100);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void registerCompany() {
		company.setTotalShares(50);
		company.setAvailableShares(50);
		assertEquals(50, company.getAvailableShares());

		assertFalse(se.getCompanies().containsKey(company));
		assertFalse(se.deregisterCompany(company));

		assertTrue(se.registerCompany(company, 100));
		assertTrue(se.getCompanies().containsKey(company));
		company.setAvailableShares(100);
		assertEquals(100, company.getAvailableShares());

		assertFalse(se.registerCompany(company, 50));
		// Check that this doesn't change the number of shares.
		assertEquals(100, company.getAvailableShares());

		se.addClient(client);

		client.buy(company, 10);
		assertEquals(10, client.getStocks().get(company));
		assertTrue(se.deregisterCompany(company));
		assertEquals(0, client.getStocks().getOrDefault(company, 0F));
	}

	@Test
	void addRemoveClient() {
		assertFalse(se.removeClient(client));
		assertTrue(se.addClient(client));
		assertFalse(se.addClient(client));
		assertTrue(se.removeClient(client));


	}

	void removeClient() {
	}

	@Test
	void getClients() {
	}

	@Test
	void getCompanies() {
	}

	@Test
	void setPrice() {
	}

	@Test
	void changePriceBy() {
	}

	@Test
	void placeOrder() {
		connectSystems();
		client.withdraw(100_000);
		client.deposit(1_000);
		company.setPrice(100);
		assertEquals(0, client.getStocks().getOrDefault(company, 0f));
		se.processOrders(company, 10, StockExchange.orderType.BUY);
		assertEquals(10, client.getStocks().get(company));
		se.processOrders(company,10, StockExchange.orderType.SELL);
		assertEquals(0, client.getStocks().get(company));

	}

	@Test
	void testPlaceOrder() {
	}
}