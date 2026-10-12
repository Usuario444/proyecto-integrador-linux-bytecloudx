package memoria;

import util.ConsolaUtil;

import java.util.Scanner;

/**
 * Punto de entrada independiente para el módulo de Reemplazo de Páginas (FIFO).
 * Permite ejecutar la simulación de memoria de forma directa sin pasar por el menú general.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsolaUtil consola = new ConsolaUtil(scanner);
        SimuladorMemoria simulador = new SimuladorMemoria(consola);

        simulador.iniciar();

        scanner.close();
    }
}
