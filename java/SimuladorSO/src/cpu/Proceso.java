package cpu;

import java.util.Objects;

/**
 * Representa un proceso dentro del sistema operativo para simulación de planificación.
 * Es inmutable para evitar que los algoritmos de simulación modifiquen los datos originales.
 */
public class Proceso {

    private final String id;
    private final int tiempoLlegada;
    private final int tiempoEjecucion;
    private final int prioridad;

    /**
     * Construye y valida un proceso con prioridad.
     *
     * @param id Identificador único del proceso (no nulo ni en blanco).
     * @param tiempoLlegada Tiempo de arribo a la cola de listos (>= 0).
     * @param tiempoEjecucion Tiempo de ráfaga de CPU necesario para completar (> 0).
     * @param prioridad Prioridad asignada (>= 1, donde menor número indica mayor prioridad).
     */
    public Proceso(String id, int tiempoLlegada, int tiempoEjecucion, int prioridad) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador del proceso no puede ser nulo ni estar vacío.");
        }
        if (tiempoLlegada < 0) {
            throw new IllegalArgumentException("El tiempo de llegada no puede ser negativo (recibido: " + tiempoLlegada + ").");
        }
        if (tiempoEjecucion <= 0) {
            throw new IllegalArgumentException("El tiempo de ejecución debe ser estrictamente mayor a 0 (recibido: " + tiempoEjecucion + ").");
        }
        if (prioridad <= 0) {
            throw new IllegalArgumentException("La prioridad debe ser estrictamente mayor a 0 (recibido: " + prioridad + ").");
        }
        this.id = id.trim();
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoEjecucion = tiempoEjecucion;
        this.prioridad = prioridad;
    }

    /**
     * Constructor de conveniencia (prioridad por defecto = 1).
     */
    public Proceso(String id, int tiempoLlegada, int tiempoEjecucion) {
        this(id, tiempoLlegada, tiempoEjecucion, 1);
    }

    public String getId() {
        return id;
    }

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }

    public int getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public int getPrioridad() {
        return prioridad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proceso proceso = (Proceso) o;
        return Objects.equals(id, proceso.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Proceso[ID=%s, Llegada=%d, Ráfaga=%d, Prioridad=%d]",
                id, tiempoLlegada, tiempoEjecucion, prioridad);
    }
}
