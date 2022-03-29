package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque {

	// Que hay que hacer
	// Salidas
	// Aforo maximo
	// aforo minimo = 0
	// Pre y post condiciones {waitm notify
	// mas invariantes

	private int aforoMax;
	private int aforoMin;
	// TODO hecho?
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;

	public Parque() { // TODO
		this.contadorPersonasTotales = 0;
		this.contadoresPersonasPuerta = new Hashtable<String, Integer>();
		this.aforoMax = 50;
		this.aforoMin = 0;
		// TODO hecho?
	}

	@Override
	public synchronized void entrarAlParque(String puerta) { // TODO

		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null) {
			contadoresPersonasPuerta.put(puerta, 0);
		}

		// TODO

		comprobarAntesDeEntrar();
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta) + 1);

		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");

		checkInvariante();

		notifyAll();

	}

	//
	// TODO Método salirDelParque similar a entrar al parque
	//
	public synchronized void salirDelParque(String puerta) { // TODO

		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null) {
			contadoresPersonasPuerta.put(puerta, 0);
		}

		// TODO
		comprobarAntesDeSalir();

		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta) + 1);

		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");

		checkInvariante();
		
		notifyAll();

	}

	private void imprimirInfo(String puerta, String movimiento) {
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); // + " tiempo medio de estancia: " +
																					// tmedio);

		// Iteramos por todas las puertas e imprimimos sus entradas
		for (String p : contadoresPersonasPuerta.keySet()) {
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}

	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
		Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
		while (iterPuertas.hasMoreElements()) {
			sumaContadoresPuerta += iterPuertas.nextElement();
		}
		return sumaContadoresPuerta;
	}

	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales
				: "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert contadorPersonasTotales <= this.aforoMax : "Hay 50 personas, el aforo esta completo";
		assert contadorPersonasTotales >= this.aforoMin : "Hay algun problema, ¡no puede haber unaforo negativo!";

	}

	protected void comprobarAntesDeEntrar() { // TODO
		// precondiciones
		// TODO
		//
		while (contadorPersonasTotales == aforoMax) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void comprobarAntesDeSalir() { // TODO
		//
		// TODO
		//
		while (contadorPersonasTotales == aforoMin) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
