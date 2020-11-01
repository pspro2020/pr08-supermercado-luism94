package codes;

public class Main {

	public static void main(String[] args) {
		int numOfTills = 4;
		int numOfCustomers = 50;
		Thread[] customerThreads = new Thread[numOfCustomers];
		Supermarket supermarket = new Supermarket(numOfTills);
		
		for (int i = 0; i < numOfCustomers; i++) {
			customerThreads[i] = new Thread(new Customer(supermarket), "Customer " + (i + 1));
		}

		for (int i = 0; i < numOfCustomers; i++) {
			customerThreads[i].start();
		}
	}
}
