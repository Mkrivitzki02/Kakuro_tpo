/**
 * Programa principal para resolver archivos de Kakuro con backtracking.
 *
 * Uso rápido: java Main src/kakuro_ejemplo.txt
 */
public class Main {
    public static void main(String[] args) {
        // Archivo de entrada con el tablero inicial
        // Permitir pasar el archivo como argumento; si no se pasa, intentar
        // el nombre por defecto y buscar también en la carpeta `src/`.
        String archivoEntrada = (args.length > 0) ? args[0] : "kakuro_dificil.txt";
        java.io.File f = new java.io.File(archivoEntrada);
        if (!f.exists()) {
            // Intentar dentro de la carpeta src/ (ruta común en este proyecto)
            String alt = "src/" + archivoEntrada;
            java.io.File f2 = new java.io.File(alt);
            if (f2.exists()) {
                archivoEntrada = alt;
            }
        }


        System.out.println("================================================================");
        System.out.println("       KAKURO - ALGORITMO BACKTRACKING - (Matias Krivitzki)   ");
        System.out.println("================================================================\n");

        // 1. CARGAR TABLERO desde el archivo de entrada (no imprimimos su contenido)
        System.out.println("Archivo de entrada: " + archivoEntrada + "\n");

        Tablero tablero = Tablero.leerDesdeArchivo(archivoEntrada);
        
        if (tablero == null) {
            System.err.println("ERROR: No se pudo construir el tablero desde '" + archivoEntrada + "'.");
            System.err.println("Verifica el formato del archivo de entrada.\n");
            return;
        }
        
        System.out.println("Tablero cargado exitosamente.");


    System.out.println("Tablero obtenido:");
    tablero.imprimirConClaves(false);
    System.out.println();

        // 2. CREAR SOLVER y resolver
    Implementacion solver = new Implementacion(tablero);
        
        System.out.println("Iniciando resolución con Backtracking...\n");
        
        // Medir tiempo de ejecución
        long inicio = System.nanoTime();
        boolean exito = solver.resolver();
        long fin = System.nanoTime();
        
        // 3. MOSTRAR RESULTADOS
        System.out.println("=================================================");
        System.out.println("               RESULTADOS                        ");
        System.out.println("=================================================");
        System.out.println("Llamadas recursivas: " + solver.getContadorLlamadas());
        System.out.println();

        if (exito) {
            System.out.println("✓ SOLUCIÓN ENCONTRADA:\n");
            tablero.imprimirConClaves(true);
        } else {
            System.out.println("✗ No se encontró solución para este Kakuro.");
        }
        
        System.out.println("\n=================================================");
    }
}