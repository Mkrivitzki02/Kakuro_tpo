import java.util.List;

/**
 * Representa un grupo de celdas consecutivas (antes llamado "Run").
 *
 * Un grupo puede ser horizontal o vertical y tiene una suma objetivo.
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
