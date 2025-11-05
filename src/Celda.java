/**
 * Representa una celda blanca del tablero de Kakuro.
 * 
 * Cada celda tiene:
 * - Posición: fila y columna en el tablero
 * - Valor: número del 1 al 9, o 0 si está vacía
 * 
 * Las celdas blancas son las que el algoritmo de backtracking debe completar.
 * Cada celda blanca pertenece exactamente a un grupo horizontal y uno vertical.
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