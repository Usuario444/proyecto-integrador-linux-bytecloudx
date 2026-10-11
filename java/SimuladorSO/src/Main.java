import cpu.SimuladorCPU;
import memoria.SimuladorMemoria;
import util.ConsolaUtil;

import java.util.Scanner;

/**
 * Punto de entrada principal para el Simulador de Sistemas Operativos (SimuladorSO).
 * Gestiona el menú de navegación principal y delega a los módulos correspondientes.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsolaUtil consola = new ConsolaUtil(scanner);
        SimuladorCPU simuladorCPU = new SimuladorCPU(consola);
        SimuladorMemoria simuladorMemoria = new SimuladorMemoria(consola);

        System.out.println("=================================================================");
        System.out.println("          SIMULADOR DE SISTEMAS OPERATIVOS (SimuladorSO)        ");
        System.out.println("        Planificación de CPU | Reemplazo de Páginas             ");
        System.out.println("=================================================================");

        boolean salir = false;
        while (!salir) {
            System.out.println("\n----------------------- MENÚ PRINCIPAL -------------------------");
            System.out.println("1. Planificación de CPU (FCFS y Round Robin)");
            System.out.println("2. Reemplazo de Páginas en Memoria (FIFO)");
            System.out.println("3. Salir del programa");
            System.out.println("----------------------------------------------------------------");

            int opcion = consola.leerEntero("Seleccione una opción (1-3): ", 1, 3);

            switch (opcion) {
                case 1 -> simuladorCPU.iniciar();
                case 2 -> simuladorMemoria.iniciar();
                case 3 -> {
                    System.out.println("\nCerrando SimuladorSO. ¡Hasta luego!");
                    salir = true;
                }
            }
        }

        scanner.close();
    }
}
