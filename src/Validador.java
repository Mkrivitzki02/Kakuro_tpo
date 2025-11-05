import java.util.HashSet;

/**
 * Clase que implementa las funciones de validación de restricciones del Kakuro.
 * 
 * Las restricciones principales son:
 * 1. Los números en cada grupo deben sumar el valor objetivo
 * 2. No puede haber números repetidos dentro de un grupo
 * 3. La suma parcial no debe exceder el valor objetivo (poda temprana)
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
    * Verifica si un grupo es válido parcialmente.
     * 
     * Restricciones verificadas:
     * - No hay números repetidos
     * - La suma parcial no excede el objetivo (permite poda temprana)
     * - Si está completo, la suma debe ser exactamente igual al objetivo
     * 
    * @param grupo El grupo a validar
     * @return true si el grupo es válido (o potencialmente válido), false si viola restricciones
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
     * @return true si el grupo está completo y es válido
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