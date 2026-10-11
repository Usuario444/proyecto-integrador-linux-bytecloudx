package memoria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa el estado y evento ocurrido en un paso individual de la simulación
 * de reemplazo de páginas. Es inmutable.
 */
public class PasoReemplazo {

    private final int numeroPaso;
    private final int pagina;
    private final List<Integer> marcos; // null indica un marco libre/vacío
    private final boolean esFallo;
    private final Integer paginaReemplazada; // null si hubo acierto o carga en marco libre

    public PasoReemplazo(int numeroPaso, int pagina, List<Integer> marcos,
                         boolean esFallo, Integer paginaReemplazada) {
        this.numeroPaso = numeroPaso;
        this.pagina = pagina;
        this.marcos = Collections.unmodifiableList(new ArrayList<>(marcos));
        this.esFallo = esFallo;
        this.paginaReemplazada = paginaReemplazada;
    }

    public int getNumeroPaso() {
        return numeroPaso;
    }

    public int getPagina() {
        return pagina;
    }

    public List<Integer> getMarcos() {
        return marcos;
    }

    public boolean isEsFallo() {
        return esFallo;
    }

    public Integer getPaginaReemplazada() {
        return paginaReemplazada;
    }
}
