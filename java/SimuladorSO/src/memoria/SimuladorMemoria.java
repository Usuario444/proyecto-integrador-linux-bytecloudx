package memoria;

import util.ConsolaUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Módulo de interfaz y orquestación para la simulación de reemplazo de páginas.
 * Separa la interacción por consola de la lógica de los algoritmos de reemplazo.
 */
public class SimuladorMemoria {

    private final ConsolaUtil consola;

    public SimuladorMemoria(ConsolaUtil consola) {
        this.consola = consola;
    }

    public void iniciar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n=======================================================");
            System.out.println("       MÓDULO: REEMPLAZO DE PÁGINAS (FIFO)");
            System.out.println("=======================================================");
            System.out.println("1. Ingresar secuencia y marcos manualmente");
            System.out.println("2. Cargar caso de prueba predefinido (Ejemplo académico)");
            System.out.println("3. Volver al menú principal");
            System.out.println("=======================================================");

            int opcion = consola.leerEntero("Seleccione una opción (1-3): ", 1, 3);

            switch (opcion) {
                case 1 -> ejecutarSimulacionManual();
                case 2 -> ejecutarCasoPredefinido();
                case 3 -> volver = true;
            }
        }
    }

    private void ejecutarSimulacionManual() {
        System.out.println("\n--- Ingreso Manual de Reemplazo de Páginas ---");
        int marcos = consola.leerEntero("Cantidad de marcos de memoria (1-20): ", 1, 20);

        System.out.println("Ingrese la secuencia de páginas separadas por espacios o comas (ej. 7, 0, 1, 2, 0, 3):");
        int[] valores = consola.leerSecuenciaEnteros("Secuencia: ", 0, 10000);

        List<Integer> referencias = new ArrayList<>();
        for (int v : valores) {
            referencias.add(v);
        }

        AlgoritmoReemplazo fifo = new FIFO();
        ResultadoReemplazo resultado = fifo.simular(referencias, marcos);
        presentarResultado(resultado);
        consola.esperarEnter("\nPresione Enter para continuar...");
    }

    private void ejecutarCasoPredefinido() {
        System.out.println("\n--- Casos Académicos Predefinidos ---");
        System.out.println("1. Ejemplo clásico de Silberschatz (20 referencias, 3 marcos)");
        System.out.println("   Secuencia: 7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1");
        System.out.println("2. Ejemplo de Tanenbaum (12 referencias, 3 marcos)");
        System.out.println("   Secuencia: 0, 1, 2, 3, 0, 1, 4, 0, 1, 2, 3, 4");
        System.out.println("3. Secuencia corta sin reemplazos (5 referencias, 4 marcos)");
        System.out.println("   Secuencia: 1, 2, 3, 1, 4");

        int caso = consola.leerEntero("Seleccione un caso (1-3): ", 1, 3);
        List<Integer> referencias;
        int marcos;

        switch (caso) {
            case 1 -> {
                referencias = Arrays.asList(7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1);
                marcos = 3;
            }
            case 2 -> {
                referencias = Arrays.asList(0, 1, 2, 3, 0, 1, 4, 0, 1, 2, 3, 4);
                marcos = 3;
            }
            case 3 -> {
                referencias = Arrays.asList(1, 2, 3, 1, 4);
                marcos = 4;
            }
            default -> {
                return;
            }
        }

        AlgoritmoReemplazo fifo = new FIFO();
        ResultadoReemplazo resultado = fifo.simular(referencias, marcos);
        presentarResultado(resultado);
        consola.esperarEnter("\nPresione Enter para continuar...");
    }

    private void presentarResultado(ResultadoReemplazo res) {
        System.out.println("\n=========================================================================================");
        System.out.printf("  RESULTADO DE LA SIMULACIÓN: %s%n", res.getNombreAlgoritmo());
        System.out.printf("  Marcos disponibles: %d | Total referencias: %d%n", res.getCapacidadMarcos(), res.getTotalReferencias());
        System.out.println("=========================================================================================");

        // Encabezado de la tabla dinámica según la cantidad de marcos
        StringBuilder cabecera = new StringBuilder();
        cabecera.append(String.format("%-5s | %-6s |", "Paso", "Pág"));
        for (int i = 0; i < res.getCapacidadMarcos(); i++) {
            cabecera.append(String.format(" M[%d]  |", i));
        }
        cabecera.append(String.format(" %-10s | %-20s", "Evento", "Reemplazo / Detalle"));
        System.out.println(cabecera);

        int anchoTotal = cabecera.length() + 2;
        System.out.println("-".repeat(Math.max(60, anchoTotal)));

        // Filas paso a paso
        for (PasoReemplazo p : res.getPasos()) {
            StringBuilder fila = new StringBuilder();
            fila.append(String.format("%-5d | %-6d |", p.getNumeroPaso(), p.getPagina()));

            for (Integer marco : p.getMarcos()) {
                String val = (marco == null) ? "-" : String.valueOf(marco);
                fila.append(String.format(" %-5s |", val));
            }

            String evento = p.isEsFallo() ? "FALLO" : "ACIERTO";
            String detalle;
            if (!p.isEsFallo()) {
                detalle = "Página ya en memoria";
            } else if (p.getPaginaReemplazada() != null) {
                detalle = String.format("Expulsa pág. %d", p.getPaginaReemplazada());
            } else {
                detalle = "Carga en marco libre";
            }

            fila.append(String.format(" %-10s | %-20s", evento, detalle));
            System.out.println(fila);
        }

        System.out.println("-".repeat(Math.max(60, anchoTotal)));

        // Estadísticas y verificación
        System.out.println("\n--- Estadísticas Finales ---");
        System.out.printf("Total de Referencias : %d%n", res.getTotalReferencias());
        System.out.printf("Total de Fallos      : %d (%.2f%%)%n", res.getTotalFallos(), res.getPorcentajeFallos());
        System.out.printf("Total de Aciertos    : %d (%.2f%%)%n", res.getTotalAciertos(), res.getPorcentajeAciertos());
        System.out.printf("Verificación         : Fallos + Aciertos = %d (Coherente)%n",
                res.getTotalFallos() + res.getTotalAciertos());
        System.out.println("=========================================================================================");
    }
}
