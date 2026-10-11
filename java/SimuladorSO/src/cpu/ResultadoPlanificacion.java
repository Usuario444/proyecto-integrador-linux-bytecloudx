package cpu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Almacena los resultados consolidados de una simulación de planificación de CPU.
 * Protege sus colecciones contra modificaciones accidentales y proporciona
 * métricas detalladas por proceso y del sistema en general.
 */
public class ResultadoPlanificacion {

    /**
     * Métrica individual para cada proceso tras la simulación.
     */
    public static class MetricaProceso {
        private final String id;
        private final int tiempoLlegada;
        private final int tiempoEjecucion;
        private final int tiempoFinalizacion;
        private final int tiempoRetorno;
        private final int tiempoEspera;

        public MetricaProceso(String id, int tiempoLlegada, int tiempoEjecucion,
                              int tiempoFinalizacion, int tiempoRetorno, int tiempoEspera) {
            this.id = id;
            this.tiempoLlegada = tiempoLlegada;
            this.tiempoEjecucion = tiempoEjecucion;
            this.tiempoFinalizacion = tiempoFinalizacion;
            this.tiempoRetorno = tiempoRetorno;
            this.tiempoEspera = tiempoEspera;
        }

        public String getId() { return id; }
        public int getTiempoLlegada() { return tiempoLlegada; }
        public int getTiempoEjecucion() { return tiempoEjecucion; }
        public int getTiempoFinalizacion() { return tiempoFinalizacion; }
        public int getTiempoRetorno() { return tiempoRetorno; }
        public int getTiempoEspera() { return tiempoEspera; }
    }

    /**
     * Segmento de ejecución en el diagrama de Gantt (incluye lapsos de CPU inactiva).
     */
    public static class SegmentoGantt {
        private final String idProceso;
        private final int tiempoInicio;
        private final int tiempoFin;
        private final boolean esOcioso;

        public SegmentoGantt(String idProceso, int tiempoInicio, int tiempoFin, boolean esOcioso) {
            this.idProceso = idProceso;
            this.tiempoInicio = tiempoInicio;
            this.tiempoFin = tiempoFin;
            this.esOcioso = esOcioso;
        }

        public String getIdProceso() { return idProceso; }
        public int getTiempoInicio() { return tiempoInicio; }
        public int getTiempoFin() { return tiempoFin; }
        public boolean isEsOcioso() { return esOcioso; }

        public int getDuracion() {
            return tiempoFin - tiempoInicio;
        }
    }

    private final String nombreAlgoritmo;
    private final List<MetricaProceso> metricas;
    private final List<SegmentoGantt> diagramaGantt;
    private final double tiempoEsperaPromedio;
    private final double tiempoRetornoPromedio;

    public ResultadoPlanificacion(String nombreAlgoritmo,
                                  List<MetricaProceso> metricas,
                                  List<SegmentoGantt> diagramaGantt,
                                  double tiempoEsperaPromedio,
                                  double tiempoRetornoPromedio) {
        this.nombreAlgoritmo = nombreAlgoritmo;
        this.metricas = Collections.unmodifiableList(new ArrayList<>(metricas));
        this.diagramaGantt = Collections.unmodifiableList(new ArrayList<>(diagramaGantt));
        this.tiempoEsperaPromedio = tiempoEsperaPromedio;
        this.tiempoRetornoPromedio = tiempoRetornoPromedio;
    }

    public String getNombreAlgoritmo() {
        return nombreAlgoritmo;
    }

    public List<MetricaProceso> getMetricas() {
        return metricas;
    }

    public List<SegmentoGantt> getDiagramaGantt() {
        return diagramaGantt;
    }

    public double getTiempoEsperaPromedio() {
        return tiempoEsperaPromedio;
    }

    public double getTiempoRetornoPromedio() {
        return tiempoRetornoPromedio;
    }
}
