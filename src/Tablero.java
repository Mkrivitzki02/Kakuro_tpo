// Archivo: Tablero.java
import java.io.*;
import java.util.*;

/**
 * Tablero de Kakuro: carga, estructura y utilidades de impresión.
 *
 * Contrato corto:
 * - Entrada: archivo con tokens ("X", "0", "n/m").
 * - Salida: estructura con celdas blancas y grupos horizontales/verticales.
 *
 * Ejemplo de uso: Tablero t = Tablero.leerDesdeArchivo("src/kakuro_ejemplo.txt");
 */

public class Tablero {
    private Celda[][] matriz;
    // Tokens originales leídos del archivo (ej: "X", ".", "12/0")
    private String[][] tokensOriginal;
    private List<GrupoSuma> gruposHorizontales = new ArrayList<>();
    private List<GrupoSuma> gruposVerticales = new ArrayList<>();
    private List<Celda> celdasBlancas = new ArrayList<>();
    private Map<String, GrupoSuma> mapaGrupos = new HashMap<>();

    /**
     * Lee y construye un tablero de Kakuro desde un archivo de texto.
     * 
     * Proceso:
     * 1. Parsea cada línea del archivo separando tokens por espacios
    * 2. Crea celdas blancas para los "0"
     * 3. Identifica celdas con claves (formato "n/m")
    * 4. Construye grupos horizontales (hacia la derecha) y verticales (hacia abajo)
    * 5. Valida que cada celda blanca pertenezca exactamente a 1 grupo H y 1 grupo V
     * 
    * @param archivo Ruta del archivo con el tablero (ej: "src/kakuro.txt").
    * @return Tablero construido o null si hay error de formato.
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

            // PASO 3: Crear grupos desde celdas con claves (formato "sumaVertical/sumaHorizontal")
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    String token = lineas.get(i)[j];
                    if (token.contains("/")) {
                        String[] partes = token.split("/");
                        // Permitir '-' como indicador de ausencia (mapear a 0)
                        int sumaV = partes[0].equals("-") ? 0 : Integer.parseInt(partes[0]);
                        int sumaH = partes[1].equals("-") ? 0 : Integer.parseInt(partes[1]);

                        // GRUPO HORIZONTAL: recolectar celdas hacia la DERECHA
                        if (sumaH > 0) {
                            List<Celda> celdas = new ArrayList<>();
                            int col = j + 1;
                                while (col < columnas && (lineas.get(i)[col].equals("0"))) {
                                Celda celda = t.matriz[i][col];
                                if (celda != null) celdas.add(celda);
                                col++;
                            }
                            if (!celdas.isEmpty()) {
                                GrupoSuma grupo = new GrupoSuma(sumaH, celdas);
                                t.gruposHorizontales.add(grupo);
                                // Mapear cada celda a su grupo horizontal
                                for (Celda c : celdas) {
                                    t.mapaGrupos.put("H" + c.fila + "," + c.col, grupo);
                                }
                            }
                        }

                        // GRUPO VERTICAL: recolectar celdas hacia ABAJO
                        if (sumaV > 0) {
                            List<Celda> celdas = new ArrayList<>();
                            int fil = i + 1;
                                while (fil < filas && (lineas.get(fil)[j].equals("0"))) {
                                Celda celda = t.matriz[fil][j];
                                if (celda != null) celdas.add(celda);
                                fil++;
                            }
                            if (!celdas.isEmpty()) {
                                GrupoSuma grupo = new GrupoSuma(sumaV, celdas);
                                t.gruposVerticales.add(grupo);
                                // Mapear cada celda a su grupo vertical
                                for (Celda c : celdas) {
                                    t.mapaGrupos.put("V" + c.fila + "," + c.col, grupo);
                                }
                            }
                        }
                    }
                }
            }

            // PASO 4: Validar integridad del tablero
            // Cada celda blanca DEBE pertenecer exactamente a 1 grupo H y 1 grupo V
            for (Celda c : t.celdasBlancas) {
                String keyH = "H" + c.fila + "," + c.col;
                String keyV = "V" + c.fila + "," + c.col;
                if (!t.mapaGrupos.containsKey(keyH) || !t.mapaGrupos.containsKey(keyV)) {
                    System.err.println("Error: celda sin grupo asignado en (" + c.fila + "," + c.col + ")");
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
     * Obtiene el grupo horizontal al que pertenece una celda.
     * 
     * @param c Celda de la cual obtener el grupo horizontal
     * @return Grupo horizontal que contiene esta celda
     */
    public GrupoSuma getGrupoHorizontal(Celda c) {
        return mapaGrupos.get("H" + c.fila + "," + c.col);
    }

    /**
     * Obtiene el grupo vertical al que pertenece una celda.
     * 
     * @param c Celda de la cual obtener el grupo vertical
     * @return Grupo vertical que contiene esta celda
     */
    public GrupoSuma getGrupoVertical(Celda c) {
        return mapaGrupos.get("V" + c.fila + "," + c.col);
    }

    /**
     * Verifica que todos los grupos (H y V) cumplan sus restricciones finales.
     * - Cada grupo debe estar completo y sumar exactamente su objetivo.
     * - No se permiten números repetidos en un grupo.
     *
     * @return true si el tablero es una solución válida.
     */
    public boolean validarSumasCompletas() {
        // Verificar todos los grupos horizontales
        for (GrupoSuma grupo : gruposHorizontales) {
            if (!Validador.sumaEsValida(grupo)) return false;
        }
        // Verificar todos los grupos verticales
        for (GrupoSuma grupo : gruposVerticales) {
            if (!Validador.sumaEsValida(grupo)) return false;
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
     * Imprime el tablero mostrando las claves (tokens originales).
     *
     * @param mostrarSolucion Si true muestra los valores de las celdas blancas.
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
     * Centra un texto en un campo de ancho fijo (utilidad para impresión).
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