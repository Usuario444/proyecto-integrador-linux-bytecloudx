package cpu;

import java.util.List;

/**
 * Contrato común para los algoritmos de planificación de CPU.
 * Permite ejecutar distintas políticas de planificación de forma polimórfica.
 */
public interface Planificador {

    /**
     * Retorna el nombre descriptivo del algoritmo y sus parámetros (ej. quantum).
     */
    String getNombre();

    /**
     * Ejecuta la simulación de planificación sobre una lista de procesos.
     * No modifica la lista original ni los objetos de procesos pasados.
     *
     * @param procesos Lista de procesos a planificar.
     * @return ResultadoPlanificacion con los resultados y métricas calculadas.
     * @throws IllegalArgumentException si la lista es nula, vacía o contiene IDs duplicados.
     */
    ResultadoPlanificacion planificar(List<Proceso> procesos);
}
