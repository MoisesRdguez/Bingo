import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Bingo {

	static Scanner leer = new Scanner(System.in);

	public static void main(String[] args) throws FileNotFoundException {

		boolean finJuego = false;
		int ganadorLinea = 0; // 0 no detectado Ganador Línea, 1 ganador
								// jugador1, 2 ganador jugador2, 3 ambos
		int ganadorBingo = 0;

		int carton1[][] = null, carton2[][] = null, bombo[];

		String nombre1, nombre2;

		nombre1 = args[0];
		nombre2 = args[1];

		bombo = inicializarBombo();

		do {

			System.out.println();
			System.out.println("Elija una de las siguientes opciones del Bingo:");
			System.out.println("1. Iniciar la partida");
			System.out.println("2. Sacar bola");
			System.out.println("3. Imprimir estado de los cartones");
			System.out.println("4. Imprimir estado del bombo");
			int opcion = leer.nextInt();

			switch (opcion) {
			case 1:
				carton1 = crearCarton();
				carton2 = crearCarton();
				System.out.println("Cartones generados");
				break;
			case 2:

				int numero = generarNuevoNumero(bombo);
				System.out.println("Ha salido el " + numero);
				tacharNumero(numero, carton1);
				tacharNumero(numero, carton2);

				if (ganadorLinea == 0) { // Si no hay ganador línea todavía
					ganadorLinea = comprobarLineas(carton1, carton2);
				}
				boolean ganador1 = comprobarBingo(carton1);
				boolean ganador2 = comprobarBingo(carton2);

				if (ganador1 || ganador2) {
					finJuego = true;
					if (ganador1 && ganador2) {
						ganadorBingo = 3;
					} else if (ganador2) {
						ganadorBingo = 2;
					} else if (ganador1) {
						ganadorBingo = 1;
					}
				}

				break;
			case 3:
				System.out.println("Carton de: " + nombre1);
				imprimirCarton(carton1);
				System.out.println();
				System.out.println("Carton de: " + nombre2);
				imprimirCarton(carton2);
				break;
			case 4:
				imprimirBombo(bombo);
				break;

			default:
				System.out.println("Error, debe introducir un número correcto");
			}

		} while (!finJuego);

		imprimirResultadoFinal(ganadorLinea, ganadorBingo, nombre1, nombre2);
		System.out.print("Introduzca el nombre del fichero donde quiere guardar el resultado del bombo");
		String fichero = leer.nextLine();
		fichero = leer.nextLine();
		imprimirFichero(bombo, fichero);

	}

	private static void imprimirResultadoFinal(int ganadorLinea, int ganadorBingo, String nombre1, String nombre2) {

		switch (ganadorLinea) {
		case 1:
			System.out.println("El ganador de la línea es: " + nombre1);
			break;
		case 2:
			System.out.println("El ganador de la línea es: " + nombre2);
			break;
		case 3:
			System.out.println(nombre1 + " y " + nombre2 + "ganaron la línea");
			break;
		default:
			System.out.println("No hay ganador aun");
		}

		switch (ganadorBingo) {
		case 1:
			System.out.println("El ganador del bingo es: " + nombre1);
			break;
		case 2:
			System.out.println("El ganador del bingo es: " + nombre2);
			break;
		case 3:
			System.out.println(nombre1 + " y " + nombre2 + "ganaron el bingo");
			break;
		default:
			System.out.println("No hay ganador aun");
		}

	}

	/**
	 * Inicializar bombo
	 */
	private static int[] inicializarBombo() {

		int bombo[] = new int[90];
		for (int i = 0; i < bombo.length; i++) {
			bombo[i] = i+1;
		}
		return bombo;
	}

	/**
	 * Imprime el estado del bombo en el momento actual
	 * 
	 * @param bombo
	 */
	private static void imprimirBombo(int[] bombo) {
		for (int i = 0; i < bombo.length; i++) {
			if (i % 10 == 0) {
				System.out.println();
			}
			if (bombo[i] == -1)
				System.out.print(" X " + "\t ");
			else
				System.out.print(bombo[i] + "\t ");
		}
	}

	/**
	 * Imprime el estado del bombo en el momento final
	 * 
	 * @param bombo
	 * @throws FileNotFoundException
	 */
	private static void imprimirFichero(int[] bombo, String nombre) throws FileNotFoundException {
		PrintWriter fichero = new PrintWriter(nombre);
		for (int i = 0; i < bombo.length; i++) {
			/*
			 * if (i%10 == 0) System.out.println(); else { if (bombo[i] == -1)
			 * fichero.println(" X "); else fichero.print(bombo[i]); }
			 */
			if (i % 10 == 0) {
				System.out.println();
				if (bombo[i] == -1)
					System.out.print(" X " + "\t ");
				else
					System.out.print(bombo[i] + "\t ");
			} else {
				if (bombo[i] == -1)
					System.out.print(" X " + "\t ");
				else
					System.out.print(bombo[i] + "\t ");
			}
		}
	}

	/**
	 * Cuando sale un número hay que ver si que canta línea en algún cartón
	 * 
	 * @param carton1
	 * @param carton2
	 * @return
	 */
	private static int comprobarLineas(int[][] carton1, int[][] carton2) {
		int ganadorLinea = 0;
		boolean linea1 = buscarLinea(carton1);
		boolean linea2 = buscarLinea(carton2);
		if (linea1 && linea2) {
			System.out.println("Jugador 1 y 2 línea");
			ganadorLinea = 3;
		} else if (linea1) {
			System.out.println("Jugador 1 línea");
			ganadorLinea = 1;
		} else if (linea2) {
			System.out.println("Jugador 2 línea");
			ganadorLinea = 2;
		}
		return ganadorLinea;
	}

	/**
	 * Comprueba si ha terminado el cartón
	 * 
	 * @param carton
	 * @return
	 */
	private static boolean comprobarBingo(int[][] carton) {
		boolean ganador = true;
		for (int i = 0; i < carton.length && ganador; i++) {
			for (int j = 0; j < carton[i].length && ganador; j++) {
				if (carton[i][j] != -1)
					ganador = false;
			}
		}
		return ganador;
	}

	/**
	 * Busca línea dentro del carton
	 * 
	 * @param carton
	 * @return true si encontró una línea
	 */
	private static boolean buscarLinea(int[][] carton) {
		boolean linea = false, saltarLinea = false;
		for (int i = 0; i < carton.length && !linea; i++) {
			saltarLinea = false;
			for (int j = 0; j < carton[i].length && !linea && !saltarLinea; j++) {
				if (carton[i][j] != -1)
					saltarLinea = true;
				else if (carton[i][j] == -1 && j == carton[i].length - 1)
					linea = true;
			}
		}
		return linea;
	}

	/**
	 * 
	 * @param numero
	 * @param carton
	 */
	private static void tacharNumero(int numero, int[][] carton) {
		boolean salir = false;
		for (int i = 0; i < carton.length && !salir; i++) {
			for (int j = 0; j < carton[i].length && !salir; j++) {
				if (carton[i][j] == numero) {
					carton[i][j] = -1;
					salir = true;
				}
			}
		}
	}

	/**
	 * 
	 * @param bombo
	 * @return
	 */
	private static int generarNuevoNumero(int[] bombo) {

		int numero = generarNumero();

		while (bombo[numero - 1] == -1) {
			numero = generarNumero();
		}
		bombo[numero - 1] = -1;
		return numero;
	}

	private static void imprimirCarton(int[][] carton1) {

		for (int i = 0; i < carton1.length; i++) {
			for (int j = 0; j < carton1[i].length; j++) {
				if (carton1[i][j] == -1) {
					System.out.print(" X ");
				} else {
					System.out.print(carton1[i][j] + " ");
				}
			}
			System.out.println();
		}
	}

	// Problema de numeros repetidos
	private static int[][] crearCarton() {
		int[][] carton = new int[3][5];

		for (int i = 0; i < carton.length; i++) {
			for (int j = 0; j < carton[i].length; j++) {
				carton[i][j] = generarNumeroNoRep(carton);
			}
		}

		return carton;
	}

	public static int generarNumero() {
		return (int) (Math.random() * 90) + 1;
	}

	public static int generarNumeroNoRep(int[][] carton) {
		boolean esta;
		int numero;
		do {
			esta = false;
			numero = generarNumero();
			for (int i = 0; i < carton.length && esta == false; i++) {
				for (int j = 0; j < carton[i].length && esta == false; j++) {
					if (carton[i][j] == numero) {
						esta = true;
					}
				}
			}
		} while (esta == true);
		return numero;
	}
}
