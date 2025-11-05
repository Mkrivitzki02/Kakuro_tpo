import java.util.List;

/**
 * Grupo de celdas que deben sumar un objetivo (horizontal o vertical).
 *
 * - `sumaObjetivo`: valor que deben sumar las celdas del grupo.
 * - `celdas`: lista de `Celda` consecutivas.
 */
public class GrupoSuma {
    /** Suma objetivo que deben alcanzar las celdas del grupo */
    public int sumaObjetivo;
    
    /** Lista de celdas blancas que conforman este grupo */
    public List<Celda> celdas;

    /**
     * Constructor del grupo.
     * @param suma Suma objetivo
     * @param celdas Lista de celdas consecutivas
     */
    public GrupoSuma(int suma, List<Celda> celdas) {
        this.sumaObjetivo = suma;
        this.celdas = celdas;
    }
}
