package cpu;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Implementación del algoritmo de planificación Round Robin (RR).
 * Apropiativo (preemptive): asigna a cada proceso una fracción de CPU (quantum).
 */
public class RoundRobin implements Planificador {

    private final int quantum;

    /**
     * @param quantum Tamaño del intervalo de tiempo asignado a cada proceso (> 0).
     */
    public RoundRobin(int quantum) {
        if (quantum <= 0) {
            throw new IllegalArgumentException("El quantum debe ser estrictamente mayor a 0 (recibido: " + quantum + ").");
        }
        this.quantum = quantum;
    }

    public int getQuantum() {
        return quantum;
    }

    @Override
    public String getNombre() {
        return "Round Robin (Quantum = " + quantum + ")";
    }

    private static class EstadoProcesoRR {
        final Proceso proceso;
        final int indiceOriginal;
        int tiempoRestante;
        int tiempoFinalizacion;

        EstadoProcesoRR(Proceso proceso, int indiceOriginal) {
            this.proceso = proceso;
            this.indiceOriginal = indiceOriginal;
            this.tiempoRestante = proceso.getTiempoEjecucion();
        }
    }

    @Override
    public ResultadoPlanificacion planificar(List<Proceso> procesos) {
        validarProcesos(procesos);

        List<EstadoProcesoRR> listaOrdenada = new ArrayList<>();
        for (int i = 0; i < procesos.size(); i++) {
            listaOrdenada.add(new EstadoProcesoRR(procesos.get(i), i));
        }

        // Orden de llegada determinista; en empate, orden original de ingreso
        listaOrdenada.sort(Comparator.comparingInt((EstadoProcesoRR e) -> e.proceso.getTiempoLlegada())
                .thenComparingInt(e -> e.indiceOriginal));

        Queue<EstadoProcesoRR> colaListos = new ArrayDeque<>();
        List<ResultadoPlanificacion.SegmentoGantt> diagramaGantt = new ArrayList<>();
        ResultadoPlanificacion.MetricaProceso[] metricasPorIndice =
                new ResultadoPlanificacion.MetricaProceso[procesos.size()];

        int tiempoActual = 0;
        int pendientesIdx = 0;
        int completados = 0;
        int totalProcesos = procesos.size();

        while (completados < totalProcesos) {
            // Si la cola de listos está vacía, avanzar el tiempo hasta el próximo arribo
            if (colaListos.isEmpty()) {
                EstadoProcesoRR proximo = listaOrdenada.get(pendientesIdx);
                if (tiempoActual < proximo.proceso.getTiempoLlegada()) {
                    diagramaGantt.add(new ResultadoPlanificacion.SegmentoGantt(
                            "OCIOSO", tiempoActual, proximo.proceso.getTiempoLlegada(), true));
                    tiempoActual = proximo.proceso.getTiempoLlegada();
                }
                // Encolar todos los procesos que ya hayan llegado al tiempo actual
                while (pendientesIdx < totalProcesos &&
                        listaOrdenada.get(pendientesIdx).proceso.getTiempoLlegada() <= tiempoActual) {
                    colaListos.offer(listaOrdenada.get(pendientesIdx++));
                }
            }

            EstadoProcesoRR actual = colaListos.poll();
            int tiempoEjecutado = Math.min(quantum, actual.tiempoRestante);
            int inicioTurno = tiempoActual;
            tiempoActual += tiempoEjecutado;
            actual.tiempoRestante -= tiempoEjecutado;

            diagramaGantt.add(new ResultadoPlanificacion.SegmentoGantt(
                    actual.proceso.getId(), inicioTurno, tiempoActual, false));

            // Incorporar a la cola los procesos nuevos que llegaron durante este turno
            // (regla estándar: los nuevos arribos entran antes de que el proceso actual regrese al final)
            while (pendientesIdx < totalProcesos &&
                    listaOrdenada.get(pendientesIdx).proceso.getTiempoLlegada() <= tiempoActual) {
                colaListos.offer(listaOrdenada.get(pendientesIdx++));
            }

            if (actual.tiempoRestante > 0) {
                colaListos.offer(actual);
            } else {
                actual.tiempoFinalizacion = tiempoActual;
                int retorno = actual.tiempoFinalizacion - actual.proceso.getTiempoLlegada();
                int espera = retorno - actual.proceso.getTiempoEjecucion();

                metricasPorIndice[actual.indiceOriginal] = new ResultadoPlanificacion.MetricaProceso(
                        actual.proceso.getId(),
                        actual.proceso.getTiempoLlegada(),
                        actual.proceso.getTiempoEjecucion(),
                        actual.tiempoFinalizacion,
                        retorno,
                        espera
                );
                completados++;
            }
        }

        List<ResultadoPlanificacion.MetricaProceso> metricasFinales = new ArrayList<>();
        long sumaEspera = 0;
        long sumaRetorno = 0;
        for (ResultadoPlanificacion.MetricaProceso m : metricasPorIndice) {
            metricasFinales.add(m);
            sumaEspera += m.getTiempoEspera();
            sumaRetorno += m.getTiempoRetorno();
        }

        double promedioEspera = (double) sumaEspera / metricasFinales.size();
        double promedioRetorno = (double) sumaRetorno / metricasFinales.size();

        return new ResultadoPlanificacion(
                getNombre(),
                metricasFinales,
                diagramaGantt,
                promedioEspera,
                promedioRetorno
        );
    }

    private void validarProcesos(List<Proceso> procesos) {
        if (procesos == null || procesos.isEmpty()) {
            throw new IllegalArgumentException("La lista de procesos no puede ser nula ni vacía.");
        }
        Set<String> idsUnicos = new HashSet<>();
        for (Proceso p : procesos) {
            if (p == null) {
                throw new IllegalArgumentException("La lista contiene elementos nulos.");
            }
            if (!idsUnicos.add(p.getId())) {
                throw new IllegalArgumentException("Identificador de proceso duplicado: " + p.getId());
            }
        }
    }
}
