package memoria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Almacena el historial completo de pasos y las métricas consolidadas
 * de una simulación de reemplazo de páginas.
 */
public class ResultadoReemplazo {

    private final String nombreAlgoritmo;
    private final int capacidadMarcos;
    private final List<PasoReemplazo> pasos;
    private final int totalReferencias;
    private final int totalFallos;
    private final int totalAciertos;
    private final double porcentajeFallos;
    private final double porcentajeAciertos;

    public ResultadoReemplazo(String nombreAlgoritmo, int capacidadMarcos,
                              List<PasoReemplazo> pasos, int totalFallos, int totalAciertos) {
        if (pasos == null || pasos.isEmpty()) {
            throw new IllegalArgumentException("La lista de pasos no puede ser nula ni vacía.");
        }
        int total = pasos.size();
        if (totalFallos + totalAciertos != total) {
            throw new IllegalArgumentException(String.format(
                    "Inconsistencia en métricas: fallos (%d) + aciertos (%d) != total referencias (%d)",
                    totalFallos, totalAciertos, total));
        }

        this.nombreAlgoritmo = nombreAlgoritmo;
        this.capacidadMarcos = capacidadMarcos;
        this.pasos = Collections.unmodifiableList(new ArrayList<>(pasos));
        this.totalReferencias = total;
        this.totalFallos = totalFallos;
        this.totalAciertos = totalAciertos;
        this.porcentajeFallos = (total > 0) ? ((double) totalFallos / total) * 100.0 : 0.0;
        this.porcentajeAciertos = (total > 0) ? ((double) totalAciertos / total) * 100.0 : 0.0;
    }

    public String getNombreAlgoritmo() {
        return nombreAlgoritmo;
    }

    public int getCapacidadMarcos() {
        return capacidadMarcos;
    }

    public List<PasoReemplazo> getPasos() {
        return pasos;
    }

    public int getTotalReferencias() {
        return totalReferencias;
    }

    public int getTotalFallos() {
        return totalFallos;
    }

    public int getTotalAciertos() {
        return totalAciertos;
    }

    public double getPorcentajeFallos() {
        return porcentajeFallos;
    }

    public double getPorcentajeAciertos() {
        return porcentajeAciertos;
    }
}
