import java.util.List;

/**
 * Solver de Kakuro usando backtracking simple.
 *
 * Contrato:
 * - Entrada: `Tablero` con celdas y grupos ya construidos.
 * - Salida: boolean indicando si encontró solución (los valores quedan en las celdas).
 */
public class Implementacion {
    private Tablero tablero;
    private int contadorLlamadas = 0;  // Métrica para análisis de complejidad
    private List<Celda> celdasBlancas;

    /**
     * Constructor del solver
     * @param tablero El tablero de Kakuro a resolver
     */
    public Implementacion(Tablero tablero) {
        this.tablero = tablero;
        this.celdasBlancas = tablero.getCeldasBlancas();
    }

    /**
     * Método público para iniciar la resolución
     * @return true si se encontró solución, false en caso contrario
     */
    public boolean resolver() {
        return backtrack(0);
    }

    /**
     * Backtracking recursivo: asigna valores 1..9 a las celdas blancas.
     * Usa `Validador.esValido` para poda temprana.
     */
    private boolean backtrack(int idx) {
        contadorLlamadas++;  // Contar llamada recursiva para análisis
        
        // CASO BASE: Todas las celdas están llenas
        if (idx == celdasBlancas.size()) {
            return tablero.validarSumasCompletas();
        }

        // Obtener la celda actual
        Celda celda = celdasBlancas.get(idx);

        // GENERAR CANDIDATOS: Probar valores del 1 al 9
        for (int val = 1; val <= 9; val++) {
            celda.valor = val;
            
            // PODA: Verificar si el valor es válido antes de continuar
            if (Validador.esValido(tablero, celda)) {
                // RECURSIÓN: Intentar resolver el resto del tablero
                if (backtrack(idx + 1)) {
                    return true;  // Solución encontrada
                }
            }
            
            // BACKTRACK: Deshacer el cambio si no llevó a una solución
            celda.valor = 0;
        }
        
        return false;  // No se encontró solución con esta configuración
    }

    /**
     * Obtiene la cantidad de llamadas recursivas realizadas
     * @return Número total de llamadas a backtrack()
     */
    public int getContadorLlamadas() {
        return contadorLlamadas;
    }
}
