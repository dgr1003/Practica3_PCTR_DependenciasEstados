package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque {

	// Que hay que hacer
	// Salidas
	// Aforo maximo hecho
	// aforo minimo = 0
	// Pre y post condiciones {wait, notify} casi hecho
	// mas invariantes hecho

	private int aforoMax;
	private int aforoMin;
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;

	public Parque() {
		this.contadorPersonasTotales = 0;
		this.contadoresPersonasPuerta = new Hashtable<String, Integer>();
		this.aforoMax = 50;
		this.aforoMin = 0;
	}

	@Override
	public synchronized void entrarAlParque(String puerta) {

		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null) {
			contadoresPersonasPuerta.put(puerta, 0);
		}

		// Antes de entrar en el parque, debemos ver que no esté lleno
		comprobarAntesDeEntrar(puerta);

		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta) + 1);

		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");

		checkInvariante();

		notifyAll();

	}

	/**
	 * Método salirDelParque similar a entrar al parque
	 *
	 * @param puerta la puerta por la que sale
	 */
	public synchronized void salirDelParque(String puerta) {

		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null) {
			contadoresPersonasPuerta.put(puerta, 0);
		}

		comprobarAntesDeSalir();

		// Aumentamos el contador total y el individual
		contadorPersonasTotales--;
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta) - 1);

		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");

		checkInvariante();

		notifyAll();

	}

	/**
	 * Método que imprime por pantalla la información de todos los contadores
	 * respecto a las personas que hay en el parque y en cada iteracion muestra si
	 * entra, si sale, por qué puerta, el contador total de personas en el parque y
	 * el contador invidual de dicha puerta.
	 * 
	 * @param puerta     la puerta por la que entra o sale
	 * @param movimiento entrada o salida, depende de lo que realice
	 */
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

	/**
	 * Método usado en el CheckInvariante para conprobar si la suma total de los
	 * contadores de todas las puertas es diferente al contador total que tenemos
	 * dentro de parque.
	 * 
	 * @return la suma de todos los contadores de las puertas
	 */
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
		Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
		while (iterPuertas.hasMoreElements()) {
			sumaContadoresPuerta += iterPuertas.nextElement();
		}
		return sumaContadoresPuerta;
	}

	/**
	 * Checkea con asserts que tengamos todo como queremos.
	 */
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales
				: "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";

		assert contadorPersonasTotales <= aforoMax : "Hay 50 personas, el aforo esta completo";
		assert contadorPersonasTotales >= aforoMin : "Hay algun problema, �no puede haber unaforo negativo!";
	}

	/**
	 * No le ponemos el synchronized porque lo estamos llamando desde un metodo ya
	 * sincronizado por lo que sería una comprobacion extra e innecesaria. Si el
	 * contador de personas totales del parque es igual al aforo máximo, le manda
	 * esperar, ya que no debería ser capaz de entrar al parque si está lleno.
	 * 
	 */
	protected void comprobarAntesDeEntrar(String puerta) { // TODO
		while (contadorPersonasTotales == aforoMax) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(" TEST AFORO MAXIMO ");
			}

		}
	}

	/**
	 * No le ponemos el synchronized porque lo estamos llamando desde un metodo ya
	 * sincronizado por lo que sería una comprobacion extra e innecesaria. Si el
	 * contador de personas totales del parque es igual al aforo minimo, le manda
	 * esperar, ya que no debería ser capaz de salir del parque si está vacío.
	 * 
	 */
	protected void comprobarAntesDeSalir() {
		while (contadorPersonasTotales == aforoMin) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println(" TEST AFORO MINIMO ");
			}

		}
	}

}
