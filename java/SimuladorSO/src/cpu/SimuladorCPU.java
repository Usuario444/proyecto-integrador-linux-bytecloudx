package cpu;

import util.ConsolaUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Módulo de interfaz y orquestación para la simulación de algoritmos de CPU.
 * Separa la interacción por consola de la lógica algorítmica.
 */
public class SimuladorCPU {

    private final ConsolaUtil consola;

    public SimuladorCPU(ConsolaUtil consola) {
        this.consola = consola;
    }

    public void iniciar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n=======================================================");
            System.out.println("                 PLANIFICACIÓN DE CPU");
            System.out.println("=======================================================");
            System.out.println("1. Ingresar procesos manualmente");
            System.out.println("2. Cargar caso de prueba predefinido (Ejemplo académico)");
            System.out.println("3. Volver al menú principal");
            System.out.println("=======================================================");

            int opcion = consola.leerEntero("Seleccione una opción (1-3): ", 1, 3);

            List<Proceso> procesos = switch (opcion) {
                case 1 -> capturarProcesosManual();
                case 2 -> cargarCasoPredefinido();
                case 3 -> {
                    volver = true;
                    yield null;
                }
                default -> null;
            };

            if (volver || procesos == null || procesos.isEmpty()) {
                continue;
            }

            ejecutarMenuAlgoritmos(procesos);
        }
    }

    private List<Proceso> capturarProcesosManual() {
        System.out.println("\n--- Ingreso Manual de Procesos ---");
        int cantidad = consola.leerEntero("Cantidad de procesos (1-20): ", 1, 20);

        List<Proceso> lista = new ArrayList<>();
        Set<String> idsUsados = new HashSet<>();

        for (int i = 1; i <= cantidad; i++) {
            System.out.printf("%n[Proceso %d de %d]%n", i, cantidad);
            String id;
            while (true) {
                id = consola.leerCadena("  Identificador (ej. P" + i + "): ");
                if (idsUsados.contains(id)) {
                    System.out.println("  [Error] Ya existe un proceso con el identificador '" + id + "'.");
                } else {
                    idsUsados.add(id);
                    break;
                }
            }
            int llegada = consola.leerEntero("  Tiempo de llegada (>= 0): ", 0, 10000);
            int ejecucion = consola.leerEntero("  Tiempo de ejecución / ráfaga (> 0): ", 1, 10000);
            int prioridad = consola.leerEntero("  Prioridad (>= 1, menor número = mayor prioridad): ", 1, 100);

            lista.add(new Proceso(id, llegada, ejecucion, prioridad));
        }
        return lista;
    }

    private List<Proceso> cargarCasoPredefinido() {
        System.out.println("\n--- Casos Académicos Predefinidos ---");
        System.out.println("1. Silberschatz clásico (P1: lleg=0 ráf=24, P2: lleg=0 ráf=3, P3: lleg=0 ráf=3)");
        System.out.println("2. Llegadas escalonadas con CPU ocioso (P1: lleg=0 ráf=3, P2: lleg=2 ráf=6, P3: lleg=10 ráf=4)");
        System.out.println("3. Desempate simultáneo y quantum variable (P1: lleg=0 ráf=5, P2: lleg=1 ráf=4, P3: lleg=2 ráf=2, P4: lleg=4 ráf=1)");
        System.out.println("4. Caso de 5 procesos para informe ByteCloudX (P1 a P5)");

        int caso = consola.leerEntero("Seleccione un caso (1-4): ", 1, 4);
        List<Proceso> procesos = new ArrayList<>();
        switch (caso) {
            case 1 -> {
                procesos.add(new Proceso("P1", 0, 24, 1));
                procesos.add(new Proceso("P2", 0, 3, 2));
                procesos.add(new Proceso("P3", 0, 3, 3));
            }
            case 2 -> {
                procesos.add(new Proceso("P1", 0, 3, 1));
                procesos.add(new Proceso("P2", 2, 6, 2));
                procesos.add(new Proceso("P3", 10, 4, 3));
            }
            case 3 -> {
                procesos.add(new Proceso("P1", 0, 5, 3));
                procesos.add(new Proceso("P2", 1, 4, 1));
                procesos.add(new Proceso("P3", 2, 2, 4));
                procesos.add(new Proceso("P4", 4, 1, 2));
            }
            case 4 -> {
                procesos.add(new Proceso("P1", 0, 6, 3));
                procesos.add(new Proceso("P2", 1, 4, 1));
                procesos.add(new Proceso("P3", 2, 8, 4));
                procesos.add(new Proceso("P4", 3, 3, 2));
                procesos.add(new Proceso("P5", 5, 5, 5));
            }
        }
        mostrarResumenProcesos(procesos);
        return procesos;
    }

    private void mostrarResumenProcesos(List<Proceso> procesos) {
        System.out.println("\nProcesos cargados:");
        System.out.printf("%-10s | %-10s | %-10s | %-10s%n", "Proceso", "Llegada", "Ráfaga", "Prioridad");
        System.out.println("-----------------------------------------------------");
        for (Proceso p : procesos) {
            System.out.printf("%-10s | %-10d | %-10d | %-10d%n",
                    p.getId(), p.getTiempoLlegada(), p.getTiempoEjecucion(), p.getPrioridad());
        }
    }

    private void ejecutarMenuAlgoritmos(List<Proceso> procesos) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Selección de Algoritmo de Planificación ---");
            System.out.println("1. First-Come, First-Served (FCFS)");
            System.out.println("2. Round Robin (RR)");
            System.out.println("3. Comparar ambos algoritmos");
            System.out.println("4. Cambiar lista de procesos / Volver");

            int opcion = consola.leerEntero("Seleccione una opción (1-4): ", 1, 4);

            switch (opcion) {
                case 1 -> {
                    Planificador fcfs = new FCFS();
                    ResultadoPlanificacion res = fcfs.planificar(procesos);
                    presentarResultado(res);
                }
                case 2 -> {
                    int quantum = consola.leerEntero("Ingrese el valor del quantum (> 0): ", 1, 1000);
                    Planificador rr = new RoundRobin(quantum);
                    ResultadoPlanificacion res = rr.planificar(procesos);
                    presentarResultado(res);
                }
                case 3 -> {
                    int quantum = consola.leerEntero("Ingrese el valor del quantum para Round Robin (> 0): ", 1, 1000);
                    ResultadoPlanificacion resFCFS = new FCFS().planificar(procesos);
                    ResultadoPlanificacion resRR = new RoundRobin(quantum).planificar(procesos);

                    System.out.println("\n=======================================================");
                    System.out.println("              RESULTADO: FCFS");
                    System.out.println("=======================================================");
                    presentarResultado(resFCFS);

                    System.out.println("\n=======================================================");
                    System.out.println("           RESULTADO: ROUND ROBIN");
                    System.out.println("=======================================================");
                    presentarResultado(resRR);

                    presentarComparativa(resFCFS, resRR);
                }
                case 4 -> salir = true;
            }

            if (!salir) {
                consola.esperarEnter("\nPresione Enter para continuar...");
            }
        }
    }

    private void presentarResultado(ResultadoPlanificacion res) {
        System.out.println("\nAlgoritmo: " + res.getNombreAlgoritmo());
        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s | %-10s | %-10s | %-10s | %-14s | %-14s | %-12s%n",
                "Proceso", "Llegada", "Ráfaga", "Prioridad", "Finalización", "Retorno (T)", "Espera (W)");
        System.out.println("-------------------------------------------------------------------------------------------------------");

        for (ResultadoPlanificacion.MetricaProceso m : res.getMetricas()) {
            System.out.printf("%-10s | %-10d | %-10d | %-10d | %-14d | %-14d | %-12d%n",
                    m.getId(),
                    m.getTiempoLlegada(),
                    m.getTiempoEjecucion(),
                    m.getPrioridad(),
                    m.getTiempoFinalizacion(),
                    m.getTiempoRetorno(),
                    m.getTiempoEspera());
        }
        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.printf("Tiempo de Retorno Promedio: %.2f%n", res.getTiempoRetornoPromedio());
        System.out.printf("Tiempo de Espera Promedio:  %.2f%n", res.getTiempoEsperaPromedio());

        System.out.println("\nDiagrama de Gantt (Cronología de ejecución):");
        imprimirDiagramaGantt(res.getDiagramaGantt());
    }

    private void imprimirDiagramaGantt(List<ResultadoPlanificacion.SegmentoGantt> segmentos) {
        if (segmentos.isEmpty()) {
            System.out.println("[Sin actividad]");
            return;
        }

        // Barra superior de bloques
        StringBuilder barra = new StringBuilder("|");
        StringBuilder tiempos = new StringBuilder();

        tiempos.append(segmentos.get(0).getTiempoInicio());

        for (ResultadoPlanificacion.SegmentoGantt seg : segmentos) {
            String etiqueta = seg.isEsOcioso() ? "[OCIOSO]" : seg.getIdProceso();
            barra.append(String.format(" %-8s |", etiqueta));

            String finStr = String.valueOf(seg.getTiempoFin());
            int espacio = 11; // Ancho del bloque " %-8s |"
            tiempos.append(" ".repeat(Math.max(1, espacio - finStr.length()))).append(finStr);
        }

        System.out.println(barra);
        System.out.println(tiempos);
    }

    private void presentarComparativa(ResultadoPlanificacion fcfs, ResultadoPlanificacion rr) {
        System.out.println("\n=======================================================");
        System.out.println("               COMPARATIVA DE RENDIMIENTO");
        System.out.println("=======================================================");
        System.out.printf("%-30s | %-18s | %-18s%n", "Algoritmo", "Espera Promedio", "Retorno Promedio");
        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("%-30s | %-18.2f | %-18.2f%n", fcfs.getNombreAlgoritmo(), fcfs.getTiempoEsperaPromedio(), fcfs.getTiempoRetornoPromedio());
        System.out.printf("%-30s | %-18.2f | %-18.2f%n", rr.getNombreAlgoritmo(), rr.getTiempoEsperaPromedio(), rr.getTiempoRetornoPromedio());
        System.out.println("-----------------------------------------------------------------------");
    }
}
