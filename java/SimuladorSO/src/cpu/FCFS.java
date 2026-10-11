package cpu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementación del algoritmo de planificación FCFS (First-Come, First-Served).
 * No apropiativo (non-preemptive): los procesos se ejecutan hasta completarse en orden de llegada.
 */
public class FCFS implements Planificador {

    @Override
    public String getNombre() {
        return "First-Come, First-Served (FCFS)";
    }

    @Override
    public ResultadoPlanificacion planificar(List<Proceso> procesos) {
        validarProcesos(procesos);

        // Envoltorio para preservar el orden original de inserción como criterio de desempate determinista
        record ProcesoConIndice(Proceso proceso, int indiceOriginal) {}

        List<ProcesoConIndice> lista = new ArrayList<>();
        for (int i = 0; i < procesos.size(); i++) {
            lista.add(new ProcesoConIndice(procesos.get(i), i));
        }

        // Ordenar por tiempo de llegada ascendente; en caso de empate, usar el orden de ingreso
        lista.sort(Comparator.comparingInt((ProcesoConIndice p) -> p.proceso().getTiempoLlegada())
                .thenComparingInt(ProcesoConIndice::indiceOriginal));

        List<ResultadoPlanificacion.SegmentoGantt> diagramaGantt = new ArrayList<>();
        ResultadoPlanificacion.MetricaProceso[] metricasPorIndice =
                new ResultadoPlanificacion.MetricaProceso[procesos.size()];

        int tiempoActual = 0;

        for (ProcesoConIndice item : lista) {
            Proceso p = item.proceso();

            // Si el CPU queda ocioso esperando que el proceso llegue
            if (tiempoActual < p.getTiempoLlegada()) {
                diagramaGantt.add(new ResultadoPlanificacion.SegmentoGantt(
                        "OCIOSO", tiempoActual, p.getTiempoLlegada(), true));
                tiempoActual = p.getTiempoLlegada();
            }

            int inicio = tiempoActual;
            int fin = tiempoActual + p.getTiempoEjecucion();
            diagramaGantt.add(new ResultadoPlanificacion.SegmentoGantt(
                    p.getId(), inicio, fin, false));

            int tiempoFinalizacion = fin;
            int tiempoRetorno = tiempoFinalizacion - p.getTiempoLlegada();
            int tiempoEspera = tiempoRetorno - p.getTiempoEjecucion();

            metricasPorIndice[item.indiceOriginal()] = new ResultadoPlanificacion.MetricaProceso(
                    p.getId(),
                    p.getTiempoLlegada(),
                    p.getTiempoEjecucion(),
                    tiempoFinalizacion,
                    tiempoRetorno,
                    tiempoEspera
            );

            tiempoActual = fin;
        }

        // Ensamblar métricas en el orden original de entrada
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
