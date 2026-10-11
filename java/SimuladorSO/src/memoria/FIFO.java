package memoria;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Implementación del algoritmo de reemplazo de páginas FIFO (First-In, First-Out).
 * Reemplaza la página que lleva más tiempo residiendo en memoria principal.
 */
public class FIFO implements AlgoritmoReemplazo {

    @Override
    public String getNombre() {
        return "First-In, First-Out (FIFO)";
    }

    @Override
    public ResultadoReemplazo simular(List<Integer> referencias, int capacidadMarcos) {
        validarParametros(referencias, capacidadMarcos);

        Integer[] marcos = new Integer[capacidadMarcos]; // null representa marco libre
        Queue<Integer> colaAntiguedad = new ArrayDeque<>(); // Registra el orden de llegada a memoria

        int totalFallos = 0;
        int totalAciertos = 0;
        List<PasoReemplazo> pasos = new ArrayList<>();

        for (int i = 0; i < referencias.size(); i++) {
            int pagina = referencias.get(i);
            int pasoActual = i + 1;

            boolean estaEnMemoria = false;
            for (Integer marco : marcos) {
                if (marco != null && marco == pagina) {
                    estaEnMemoria = true;
                    break;
                }
            }

            if (estaEnMemoria) {
                // Acierto (Hit): En FIFO la antigüedad no se renueva al acceder a la página
                totalAciertos++;
                pasos.add(new PasoReemplazo(pasoActual, pagina, Arrays.asList(marcos), false, null));
            } else {
                // Fallo de página (Page Fault)
                totalFallos++;

                // Buscar primer marco libre disponible
                int indiceLibre = -1;
                for (int j = 0; j < capacidadMarcos; j++) {
                    if (marcos[j] == null) {
                        indiceLibre = j;
                        break;
                    }
                }

                if (indiceLibre != -1) {
                    // Hay espacio disponible: se asigna sin expulsar ninguna página
                    marcos[indiceLibre] = pagina;
                    colaAntiguedad.offer(pagina);
                    pasos.add(new PasoReemplazo(pasoActual, pagina, Arrays.asList(marcos), true, null));
                } else {
                    // Memoria llena: se expulsa la página más antigua de acuerdo con la cola FIFO
                    int victima = colaAntiguedad.poll();

                    int indiceVictima = -1;
                    for (int j = 0; j < capacidadMarcos; j++) {
                        if (marcos[j] != null && marcos[j] == victima) {
                            indiceVictima = j;
                            break;
                        }
                    }

                    marcos[indiceVictima] = pagina;
                    colaAntiguedad.offer(pagina);
                    pasos.add(new PasoReemplazo(pasoActual, pagina, Arrays.asList(marcos), true, victima));
                }
            }
        }

        return new ResultadoReemplazo(getNombre(), capacidadMarcos, pasos, totalFallos, totalAciertos);
    }

    private void validarParametros(List<Integer> referencias, int capacidadMarcos) {
        if (referencias == null || referencias.isEmpty()) {
            throw new IllegalArgumentException("La secuencia de referencias no puede ser nula ni vacía.");
        }
        for (Integer ref : referencias) {
            if (ref == null) {
                throw new IllegalArgumentException("La secuencia no puede contener elementos nulos.");
            }
            if (ref < 0) {
                throw new IllegalArgumentException("Los números de página deben ser no negativos (recibido: " + ref + ").");
            }
        }
        if (capacidadMarcos <= 0) {
            throw new IllegalArgumentException("La cantidad de marcos debe ser estrictamente mayor a 0 (recibida: " + capacidadMarcos + ").");
        }
    }
}
