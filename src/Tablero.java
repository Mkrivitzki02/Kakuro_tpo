// Archivo: Tablero.java
import java.io.*;
import java.util.*;

/**
 * Representa el tablero de Kakuro y gestiona la carga desde archivo.
 * 
 * El tablero almacena:
 * - Matriz de celdas (blancas para completar, negras para claves/pistas)
 * - Runs horizontales y verticales (grupos de celdas que deben sumar un objetivo)
 * - Mapa para acceso rápido a los runs de cada celda
 * 
 * Formato del archivo de entrada (kakuro.txt):
 * - "X" = celda negra (no se completa)
* - "0" = celda blanca (vacía, se debe completar con 1-9)
 * - "n/m" = celda con claves: n=suma vertical (hacia abajo), m=suma horizontal (derecha)
 */

public class Tablero {
    private Celda[][] matriz;
    // Tokens originales leídos del archivo (ej: "X", ".", "12/0")
    private String[][] tokensOriginal;
    private List<Run> runsHorizontales = new ArrayList<>();
    private List<Run> runsVerticales = new ArrayList<>();
    private List<Celda> celdasBlancas = new ArrayList<>();
    private Map<String, Run> mapaRuns = new HashMap<>();

    /**
     * Lee y construye un tablero de Kakuro desde un archivo de texto.
     * 
     * Proceso:
     * 1. Parsea cada línea del archivo separando tokens por espacios
    * 2. Crea celdas blancas para los "0"
     * 3. Identifica celdas con claves (formato "n/m")
     * 4. Construye runs horizontales (hacia la derecha) y verticales (hacia abajo)
     * 5. Valida que cada celda blanca pertenezca exactamente a 1 run H y 1 run V
     * 
    * @param archivo Ruta del archivo con el tablero (ej: "src/kakuro.txt")
     * @return Tablero construido, o null si hay errores de formato/parsing
     */
    public static Tablero leerDesdeArchivo(String archivo) {
        try {
            // PASO 1: Leer todas las líneas del archivo
            List<String[]> lineas = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea.trim().split("\\s+"));
                }
            }
            br.close();

            // PASO 2: Inicializar matriz y crear celdas blancas
            int filas = lineas.size();
            int columnas = lineas.get(0).length;
            Tablero t = new Tablero();
            t.matriz = new Celda[filas][columnas];
            t.tokensOriginal = new String[filas][columnas];

            // Crear solo las celdas blancas (las que se deben completar)
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    String token = lineas.get(i)[j];
                    // Guardar token original para futuras impresiones
                    t.tokensOriginal[i][j] = token;
                    // Ahora el formato admite '.' o '0' para celdas blancas
                        // Ahora el formato admite sólo '0' para celdas blancas
                        if (token.equals("0")) {
                        Celda celda = new Celda(i, j);
                        t.matriz[i][j] = celda;
                        t.celdasBlancas.add(celda);
                    } else {
                        // Celda negra (X) o con claves (n/m)
                        t.matriz[i][j] = null;
                    }
                }
            }

            // PASO 3: Crear runs desde celdas con claves (formato "sumaVertical/sumaHorizontal")
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    String token = lineas.get(i)[j];
                    if (token.contains("/")) {
                        String[] partes = token.split("/");
                        // Permitir '-' como indicador de ausencia (mapear a 0)
                        int sumaV = partes[0].equals("-") ? 0 : Integer.parseInt(partes[0]);
                        int sumaH = partes[1].equals("-") ? 0 : Integer.parseInt(partes[1]);

                        // RUN HORIZONTAL: recolectar celdas hacia la DERECHA
                        if (sumaH > 0) {
                            List<Celda> celdas = new ArrayList<>();
                            int col = j + 1;
                                while (col < columnas && (lineas.get(i)[col].equals("0"))) {
                                Celda celda = t.matriz[i][col];
                                if (celda != null) celdas.add(celda);
                                col++;
                            }
                            if (!celdas.isEmpty()) {
                                Run run = new Run(sumaH, celdas);
                                t.runsHorizontales.add(run);
                                // Mapear cada celda a su run horizontal
                                for (Celda c : celdas) {
                                    t.mapaRuns.put("H" + c.fila + "," + c.col, run);
                                }
                            }
                        }

                        // RUN VERTICAL: recolectar celdas hacia ABAJO
                        if (sumaV > 0) {
                            List<Celda> celdas = new ArrayList<>();
                            int fil = i + 1;
                                while (fil < filas && (lineas.get(fil)[j].equals("0"))) {
                                Celda celda = t.matriz[fil][j];
                                if (celda != null) celdas.add(celda);
                                fil++;
                            }
                            if (!celdas.isEmpty()) {
                                Run run = new Run(sumaV, celdas);
                                t.runsVerticales.add(run);
                                // Mapear cada celda a su run vertical
                                for (Celda c : celdas) {
                                    t.mapaRuns.put("V" + c.fila + "," + c.col, run);
                                }
                            }
                        }
                    }
                }
            }

            // PASO 4: Validar integridad del tablero
            // Cada celda blanca DEBE pertenecer exactamente a 1 run H y 1 run V
            for (Celda c : t.celdasBlancas) {
                String keyH = "H" + c.fila + "," + c.col;
                String keyV = "V" + c.fila + "," + c.col;
                if (!t.mapaRuns.containsKey(keyH) || !t.mapaRuns.containsKey(keyV)) {
                    System.err.println("Error: celda sin run asignada en (" + c.fila + "," + c.col + ")");
                    return null;
                }
            }

            return t;
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene la lista de todas las celdas blancas del tablero.
     * Estas son las celdas que se deben completar con valores 1-9.
     * 
     * @return Lista de celdas blancas (vacías al inicio)
     */
    public List<Celda> getCeldasBlancas() {
        return celdasBlancas;
    }

    /**
     * Obtiene el run horizontal al que pertenece una celda.
     * 
     * @param c Celda de la cual obtener el run horizontal
     * @return Run horizontal que contiene esta celda
     */
    public Run getRunHorizontal(Celda c) {
        return mapaRuns.get("H" + c.fila + "," + c.col);
    }

    /**
     * Obtiene el run vertical al que pertenece una celda.
     * 
     * @param c Celda de la cual obtener el run vertical
     * @return Run vertical que contiene esta celda
     */
    public Run getRunVertical(Celda c) {
        return mapaRuns.get("V" + c.fila + "," + c.col);
    }

    /**
     * Valida que todos los runs del tablero cumplan sus restricciones:
     * - La suma de las celdas debe igualar el objetivo
     * - No puede haber números repetidos en un run
     * 
     * Se usa al final del backtracking para verificar la solución completa.
     * 
     * @return true si todas las sumas son válidas, false en caso contrario
     */
    /**
     * Valida que todos los runs del tablero cumplan sus restricciones:
     * - La suma de las celdas debe igualar el objetivo
     * - No puede haber números repetidos en un run
     * 
     * Se usa al final del backtracking para verificar la solución completa.
     * 
     * @return true si todas las sumas son válidas, false en caso contrario
     */
    public boolean validarSumasCompletas() {
        // Verificar todos los runs horizontales
        for (Run run : runsHorizontales) {
            if (!Validador.sumaEsValida(run)) return false;
        }
        // Verificar todos los runs verticales
        for (Run run : runsVerticales) {
            if (!Validador.sumaEsValida(run)) return false;
        }
        return true;
    }

    /**
     * Imprime el tablero actual en consola.
     * 
     * Formato de salida:
     * - "X" = celda negra
     * - "." = celda blanca vacía (valor 0)
     * - Números 1-9 = celdas completadas
     */
    /**
     * Imprime una representación del tablero en consola mostrando las claves
     * (tokens originales) y, opcionalmente, la solución en las celdas blancas.
     * 
     * - Si `mostrarSolucion` es false: las celdas blancas se muestran como "."
     * - Si `mostrarSolucion` es true: las celdas blancas muestran su valor si
     *   ya fueron resueltas (valor != 0) o "." si siguen vacías.
     * - Las celdas negras con claves se muestran con el token original (ej "12/0").
     */
    public void imprimirConClaves(boolean mostrarSolucion) {
        // Ancho fijo por celda para alinear la impresión
    final int ancho = 6;
        for (int i = 0; i < tokensOriginal.length; i++) {
            StringBuilder fila = new StringBuilder();
            for (int j = 0; j < tokensOriginal[0].length; j++) {
                String tok = tokensOriginal[i][j];
                if (tok == null) tok = "X";

                String contenido;
                if (tok.equals("X")) {
                    contenido = "X";
                } else if (tok.contains("/")) {
                    // Mostrar la clave tal cual (vertical/horizontal)
                    contenido = tok;
                } else {
                    // Celda blanca
                    Celda cel = matriz[i][j];
                    if (mostrarSolucion && cel != null) {
                        int val = cel.valor;
                        contenido = (val == 0 ? "0" : Integer.toString(val));
                    } else {
                        contenido = "0";
                    }
                }

                fila.append(padCenter(contenido, ancho));
            }
            System.out.println(fila.toString());
        }
    }
    public void imprimir() {
    final int ancho = 6;
        for (int i = 0; i < matriz.length; i++) {
            StringBuilder fila = new StringBuilder();
            for (int j = 0; j < matriz[0].length; j++) {
                String contenido;
                if (matriz[i][j] != null) {
                    int val = matriz[i][j].valor;
                    contenido = (val == 0 ? "0" : Integer.toString(val));
                } else {
                    contenido = "X";
                }
                fila.append(padCenter(contenido, ancho));
            }
            System.out.println(fila.toString());
        }
    }

    /**
     * Centra el texto dentro de un campo de ancho fijo. Si el texto es más largo
     * que el ancho, se devuelve truncado a la longitud máxima.
     */
    private String padCenter(String s, int width) {
        if (s == null) s = "";
    if (s.length() >= width) return s.substring(0, width);
        int totalPad = width - s.length();
        int padLeft = totalPad / 2;
        int padRight = totalPad - padLeft;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padLeft; i++) sb.append(' ');
        sb.append(s);
        for (int i = 0; i < padRight; i++) sb.append(' ');
        return sb.toString();
    }
}