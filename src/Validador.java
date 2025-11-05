import java.util.HashSet;

/**
 * Validaciones para Kakuro: comprueba restricciones por grupo.
 *
 * - Evita duplicados en un grupo.
 * - Prueba poda por suma parcial (suma parcial <= objetivo).
 */
public class Validador {
    
    /**
     * Verifica si una celda con un valor asignado es válida según las restricciones.
     * 
     * @param t El tablero de Kakuro
     * @param c La celda a validar
     * @return true si el valor en la celda es válido, false en caso contrario
     */
    public static boolean esValido(Tablero t, Celda c) {
        GrupoSuma grupoH = t.getGrupoHorizontal(c);
        GrupoSuma grupoV = t.getGrupoVertical(c);
        return grupoEsValido(grupoH) && grupoEsValido(grupoV);
    }

    /**
     * Valida un grupo de forma parcial o completa.
     * - Si hay celdas vacías: asegura que no haya repetidos y que la suma parcial
     *   no exceda el objetivo (para podar en backtracking).
     * - Si está completo: exige que la suma sea exactamente la objetivo.
     *
     * @param grupo Grupo a validar
     * @return true si el grupo cumple las reglas o aún puede cumplirse.
     */
    private static boolean grupoEsValido(GrupoSuma grupo) {
        HashSet<Integer> usados = new HashSet<>();
        int suma = 0;
        int vacias = 0;

        // Recorrer todas las celdas del grupo
        for (Celda c : grupo.celdas) {
            if (c.valor == 0) {
                vacias++;  // Contar celdas vacías
                continue;
            }
            
            // RESTRICCIÓN 1: No repetir números
            if (usados.contains(c.valor)) {
                return false;  // Número repetido -> inválido
            }
            usados.add(c.valor);
            suma += c.valor;
        }

        // Si el grupo está completo, verificar suma exacta
        if (vacias == 0) {
            return suma == grupo.sumaObjetivo;
        }
        
        // PODA TEMPRANA: Si la suma parcial ya excede el objetivo, es inválido
        return suma <= grupo.sumaObjetivo;
    }

    /**
     * Verifica si un grupo está completo y cumple todas las restricciones.
     * 
    * @param grupo El grupo a validar
    * @return true si el grupo está completo y su suma coincide con el objetivo
     */
    public static boolean sumaEsValida(GrupoSuma grupo) {
        int suma = 0;
        HashSet<Integer> usados = new HashSet<>();

        for (Celda c : grupo.celdas) {
            // Todas las celdas deben estar llenas
            if (c.valor == 0) {
                return false;
            }
            
            // No debe haber números repetidos
            if (usados.contains(c.valor)) {
                return false;
            }
            
            usados.add(c.valor);
            suma += c.valor;
        }
        
        // La suma debe ser exactamente igual al objetivo
        return suma == grupo.sumaObjetivo;
    }
}