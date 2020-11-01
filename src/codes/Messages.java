package codes;

import java.time.format.DateTimeFormatter;

public class Messages {
	//Clase estatica que contiene los mensajes del programa para mostrar por pantalla
	
	//Formato de la hora
	static DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	static final String CUSTOMER_ARRIVAL = "Time: %s -- %s arrives at supermarket";
	static final String CUSTOMER_IN_QUEUE = "Time: %s -- %s waits for a free till";
	static final String CUSTOMER_IN_TILL = "Time: %s -- %s gets into till %d .............";
	static final String CUSTOMER_ATTENDED = "Time: %s -- %s is paying his shopping in till %d";
	static final String CUSTOMER_LEAVES_TILL = "Time: %s -- %s leaves till %d ............";
	static final String CUSTOMER_LEAVES_SUPERMARKET = "Time: %s -- %s exits supermarket";
}
