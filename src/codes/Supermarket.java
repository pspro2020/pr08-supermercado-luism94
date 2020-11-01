package codes;

import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Supermarket {
	//Cantidad de cajas automaticas del supermercado
	private int numOfTills;
	//Array boolean para mostrar si las cajas estan libres
	private boolean[] availableTills;
	//Objeto Semaforo para impedir que los hilos pasen cuando no haya ninguna caja libre
	private Semaphore semaphore;
	//Objeto Cerrojo que obliga a los hilos a escoger una caja dejando pasar a un solo hilo cada vez
	private ReentrantLock lock = new ReentrantLock(true);
	
	public Supermarket(int numOfTills) {
		//Se asigna la cantidad de cajas al supermercado
		this.numOfTills = numOfTills;
		//Se genera y se asigna el semaforo con modo justo
		semaphore = new Semaphore(numOfTills, true);
		//Se asigna el array boolean y se establece todos sus valores a true desde el inicio
		availableTills = new boolean[numOfTills];
		
		for (int i = 0; i < numOfTills; i++) {
			availableTills[i] = true;
		}
	}

	//Comportamiento del cliente cuando llega al supermercado
	public void getInSupermarket() throws InterruptedException {
		//Realiza su compra
		takeShopping();
		//El hilo actual intenta pasar por el semaforo para escoger una caja
		//Si no quedan cajas libres se quedara suspendido a la espera de que el semaforo le indique otra vez el paso
		semaphore.acquire();
		//Si el semaforo le ha dejado el paso...
		try {
			//El hilo intenta buscar y escoger una caja si queda alguna libre
			//Se devuelve el numero de la caja que ha escogido
			int tillId = chooseTill();
			//Si se recibe un valor positivo el hilo ha tenido exito
			if (tillId >= 0) {
				//El cliente coloca su compra en la caja
				putShoppingInTill(tillId);
				//El cliente recoge su compra, deja libre la caja y se va del supermercado
				finishShopping(tillId);
			} 
		} finally {
			//Cuando el cliente se va de la caja, el semaforo aumenta en 1 las cajas disponibles
			semaphore.release();
		}
	}

	private void takeShopping() throws InterruptedException {
		//El hilo secundario se suspende entre 1 y 3 segundos para "realizar su compra"
		TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3) + 1);
		
	}

	private void finishShopping(int tillId) {
		//El hilo secundario libera la caja en la que estaba y vuelve a establecer su valor boolean en el array a true
		availableTills[tillId] = true;
		System.out.println(String.format(Messages.CUSTOMER_LEAVES_TILL, LocalDateTime.now().format(Messages.format), Thread.currentThread().getName(), tillId + 1));

	}

	private void putShoppingInTill(int tillId) throws InterruptedException {
		//El cliente llega a la caja y pone su compra en la caja automatica
		System.out.println(String.format(Messages.CUSTOMER_ATTENDED, LocalDateTime.now().format(Messages.format), Thread.currentThread().getName(), tillId + 1));
		//El hilo secundario se suspende entre 1 y 4 segundos para "colocar su compra" en la caja
		TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(4) + 1);
	}

	private int chooseTill() {
		//Despues de pasar por el semaforo el hilo actual pone el cerrojo para que pueda
		//elegir una caja sin que otro hilo secundario se lo impida antes
		lock.lock();
		System.out.println(String.format(Messages.CUSTOMER_IN_QUEUE, LocalDateTime.now().format(Messages.format), Thread.currentThread().getName()));
		try {
			for (int i = 0; i < numOfTills; i++) {
				//Se recorre el array boolean en busca de que alguna caja este libre (con valor true)
				if (availableTills[i]) {
					System.out.println(String.format(Messages.CUSTOMER_IN_TILL, LocalDateTime.now().format(Messages.format), Thread.currentThread().getName(), i+1));
					//Se establece el estado de la caja elegida a no disponible
					availableTills[i] = false;
					//Se devuelve el id de la caja (su posicion en el array)
					return i;
				}
			} 
			//Si no queda ninguna caja libre en el momento de elegirla, se devuelve un valor negativo identificativo
			return -1;
		} finally {
			//Se desbloquea el paso de los demas hilos
			lock.unlock();
		}
	}

	
}
