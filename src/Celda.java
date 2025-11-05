/**
 * Celda blanca del Kakuro: posición y valor.
 *
 * - `fila`, `col`: coordenadas 0-index.
 * - `valor`: 0 si está vacía, 1-9 cuando se asigna.
 */
public class Celda {
    /** Fila de la celda en el tablero (0-indexed) */
    public int fila;
    
    /** Columna de la celda en el tablero (0-indexed) */
    public int col;
    
    /** Valor actual de la celda: 0=vacía, 1-9=completada */
    public int valor;

    /**
     * Constructor de una celda blanca.
     * 
     * @param fila Posición vertical en el tablero
     * @param col Posición horizontal en el tablero
     */
    public Celda(int fila, int col) {
        this.fila = fila;
        this.col = col;
        this.valor = 0; // Inicialmente vacía
    }
}