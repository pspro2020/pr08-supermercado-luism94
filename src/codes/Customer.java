package codes;

import java.time.LocalDateTime;

public class Customer implements Runnable {
	//Objeto Supermercado al que el cliente va a comprar
	private Supermarket supermarket;
	
	public Customer(Supermarket supermarket) {
		this.supermarket = supermarket;
	}
	
	//Comportamiento del hilo secundario
	@Override
	public void run() {
		//El cliente va al sumermercado, recoge su compra y se coloca en la cola del semaforo para
		//escoger luego una caja disponible
		try {
			System.out.println(String.format(Messages.CUSTOMER_ARRIVAL, LocalDateTime.now().format(Messages.format), Thread.currentThread().getName()));
			supermarket.getInSupermarket();
		} catch (InterruptedException e) {
			System.out.println("customer got interrupted while trying to shop");
			return;
		}
		//Cuando el cliente deja la caja libre, se va del supermercado
		exitsSupermarket();
	}

	private void exitsSupermarket() {
		System.out.println(String.format(Messages.CUSTOMER_LEAVES_SUPERMARKET, LocalDateTime.now().format(Messages.format), Thread.currentThread().getName()));
	}

}
