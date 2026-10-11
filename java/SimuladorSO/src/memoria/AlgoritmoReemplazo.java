package memoria;

import java.util.List;

/**
 * Contrato común para algoritmos de reemplazo de páginas en memoria virtual.
 * Facilita la incorporación futura de otros algoritmos (LRU, Óptimo, etc.).
 */
public interface AlgoritmoReemplazo {

    /**
     * Retorna el nombre descriptivo del algoritmo de reemplazo.
     */
    String getNombre();

    /**
     * Simula el comportamiento del algoritmo frente a una secuencia de referencias.
     *
     * @param referencias Secuencia de números de páginas solicitadas.
     * @param capacidadMarcos Cantidad de marcos de página disponibles en memoria (> 0).
     * @return ResultadoReemplazo con el desglose paso a paso y las estadísticas finales.
     * @throws IllegalArgumentException si los argumentos son inválidos.
     */
    ResultadoReemplazo simular(List<Integer> referencias, int capacidadMarcos);
}
