package cpu;

import util.ConsolaUtil;

import java.util.Scanner;

/**
 * Punto de entrada independiente para el módulo de Planificación de CPU (FCFS y Round Robin).
 * Permite ejecutar la simulación de CPU de forma directa sin pasar por el menú general.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsolaUtil consola = new ConsolaUtil(scanner);
        SimuladorCPU simulador = new SimuladorCPU(consola);

        simulador.iniciar();

        scanner.close();
    }
}
