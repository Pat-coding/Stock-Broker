import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentTest {
	StockExchange se;
	Company company;
	Client client;
	Client[] clients;

	Thread[] threads;

	@BeforeEach
	public void setUp() {
		company = new Company();
		se = new StockExchange();
		client = new Client();
		se.registerCompany(company, 1_000_000);
		se.addClient(client);
		clients = new Client[100];
		threads = new Thread[100];
		for (int i = 0; i < clients.length;i++) {
			Client c = new Client();
			se.addClient(c);
			c.deposit(1_000_000);
			clients[i] = c;
			threads[i] = new Thread(clients[i]);
		}
	}

	@Test
	public void clientsRushBuy() throws InterruptedException {
		company.setTotalShares(1_000_000);
		for (Client c : clients) {
			c.deposit(1_000);
			se.addClient(c);
			for(int i = 0; i < 20; i++) {
				c.addTasksToRun(company, 10);
			}
		}

		for (Thread t: threads) {
			t.start();
		}

		for (Thread t: threads) {
			t.join();
		}


		for (Client c : clients) {
			assertEquals(200, c.getStocks().get(company));
		}

		assertEquals(company.getTotalShares() - 20000, company.getAvailableShares());



	}

	@Test
	public void clientsRushBuyAndSell() throws InterruptedException {
		Client[] clients = new Client[100];
		Thread[] threads = new Thread[100];
		for (int i = 0; i < clients.length;i++) {
			clients[i] = new Client();
			threads[i] = new Thread(clients[i]);
		}
		for (Client c : clients) {
			c.deposit(1_000);
			se.addClient(c);
			for(int i = 0; i < 20; i++) {
				c.addTasksToRun(company, 10);
				c.addTasksToRun(company, -10);
			}

		}

		for (Thread t: threads) {
			t.start();
		}

		for (Thread t: threads) {
			t.join();
		}


		for (Client c : clients) {
			assertEquals(0, c.getStocks().get(company));
		}

		assertEquals(company.getTotalShares(), company.getAvailableShares());

	}

	@Test
	public void limitSells() throws InterruptedException {
		company.setPrice(90);
		company.setAvailableShares(0);
		//clients = new Client[]{clients[0]};
		for (Client c: clients) {
			c.getStocks().put(company, 10f);
			c.addTasksToRun(company, -10, 100);
		}
		for (Thread t: threads) {
			t.start();
		}

		assertEquals(0, company.getAvailableShares());
		for (Client c : clients) {
			assertEquals(10, c.getStocks().getOrDefault(company, 0f));
		}

		Thread priceChange = new Thread(() -> {
			System.out.println("Changing Price");
			try {
				se.setPrice(company, 110);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		//System.out.println(Client.waitingCount);

		priceChange.start();
		priceChange.join();

		for(Thread t:threads) {
			t.join();
		}
		//System.out.println(Client.waitingCount);

		for (Client c : clients) {
			assertEquals(0, c.getStocks().get(company));
		}

	}

	@Test
	public void limitBuys() throws InterruptedException {
		company.setPrice(110);
		//clients = new Client[]{clients[0]};
		for (Client c: clients) {
			c.addTasksToRun(company, 10, 100);
		}
		for (Thread t: threads) {
			t.start();
		}

		assertEquals(company.getTotalShares(), company.getAvailableShares());
		for (Client c : clients) {
			assertEquals(0, c.getStocks().getOrDefault(company, 0f));
		}

		Thread priceChange = new Thread(() -> {
			System.out.println("Changing Price");
			try {
				se.setPrice(company, 90);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		//System.out.println(Client.waitingCount);

		priceChange.start();
		priceChange.join();

		for(Thread t:threads) {
			t.join();
		}
		//System.out.println(Client.waitingCount);

		for (Client c : clients) {
			assertEquals(10, c.getStocks().get(company));
		}
		assertEquals(company.getTotalShares() - 10 * clients.length, company.getAvailableShares() );

	}

	@Test
	public void crazyPrices() throws InterruptedException {
		clients = new Client[] {clients[0]};
		for(int i = 0; i < 1_000; i++) {
			clients[0].addTasksToRun(company, 1, 90);
			clients[0].addTasksToRun(company, -1, 110);
		}

		Thread thread = new Thread(clients[0]);
		thread.setPriority(1);
		thread.start();
		Thread priceChanges = new Thread(() -> {
			while(thread.isAlive()) {
				company.setPrice(80);
				System.out.println("Price low");
				company.setPrice(120);
				System.out.println("Price high");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//System.out.println(clients[0].tasksLeft());
			}
		});
		priceChanges.setDaemon(true);
		priceChanges.start();

		thread.join();

		assertEquals(20000, clients[0].getBalance());
	}
}
