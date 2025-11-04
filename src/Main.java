/**
 * Programa principal para resolver tableros de Kakuro usando Backtracking.
 * 
 * Trabajo Práctico Obligatorio - Programación III
 * Tema 2: KAKURO
 * 
 * El programa lee un tablero desde un archivo, aplica el algoritmo de backtracking
 * para encontrar una solución válida, y muestra estadísticas de ejecución.
 */
public class Main {
    public static void main(String[] args) {
        // Archivo de entrada con el tablero inicial
        // Permitir pasar el archivo como argumento; si no se pasa, intentar
        // el nombre por defecto y buscar también en la carpeta `src/`.
        String archivoEntrada = (args.length > 0) ? args[0] : "kakuro_ejemplo.txt";
        java.io.File f = new java.io.File(archivoEntrada);
        if (!f.exists()) {
            // Intentar dentro de la carpeta src/ (ruta común en este proyecto)
            String alt = "src/" + archivoEntrada;
            java.io.File f2 = new java.io.File(alt);
            if (f2.exists()) {
                archivoEntrada = alt;
            }
        }

        System.out.println("=================================================");
        System.out.println("    SOLVER DE KAKURO - ALGORITMO BACKTRACKING    ");
        System.out.println("=================================================\n");

        // 1. MOSTRAR contenido del archivo de entrada y CARGAR TABLERO
        System.out.println("Archivo de entrada: " + archivoEntrada + "\n");
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(archivoEntrada))) {
            String l;
            while ((l = br.readLine()) != null) {
                System.out.println(l);
            }
        } catch (java.io.IOException e) {
            System.err.println("No se pudo leer el archivo para imprimir su contenido: " + e.getMessage());
        }

        System.out.println();
        System.out.println("Cargando tablero desde: " + archivoEntrada);
        Tablero tablero = Tablero.leerDesdeArchivo(archivoEntrada);
        
        if (tablero == null) {
            System.err.println("ERROR: No se pudo construir el tablero desde '" + archivoEntrada + "'.");
            System.err.println("Verifica el formato del archivo de entrada.\n");
            return;
        }
        
        System.out.println("Tablero cargado exitosamente.");
        System.out.println("Celdas blancas a completar: " + tablero.getCeldasBlancas().size() + "\n");

    System.out.println("Tablero parseado (claves y celdas vacías):");
    tablero.imprimirConClaves(false);
    System.out.println();

        // 2. CREAR SOLVER y resolver
        KakuroSolver solver = new KakuroSolver(tablero);
        
        System.out.println("Iniciando resolución con Backtracking...\n");
        
        // Medir tiempo de ejecución
        long inicio = System.nanoTime();
        boolean exito = solver.resolver();
        long fin = System.nanoTime();
        
        // 3. MOSTRAR RESULTADOS
        System.out.println("=================================================");
        System.out.println("               RESULTADOS                        ");
        System.out.println("=================================================");
        System.out.println("Tiempo de ejecución: " + (fin - inicio) / 1e6 + " ms");
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