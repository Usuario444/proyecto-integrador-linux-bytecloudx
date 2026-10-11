package util;

import java.util.Scanner;

/**
 * Centraliza la lectura y validación de datos desde la consola.
 * Evita duplicaciones y maneja entradas inválidas sin cerrar la aplicación.
 */
public class ConsolaUtil {

    private final Scanner scanner;

    public ConsolaUtil(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Lee un entero en el rango [min, max]. Repite si la entrada es inválida.
     */
    public int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                System.out.println("  [Error] La entrada no puede estar vacía.");
                continue;
            }
            try {
                int valor = Integer.parseInt(linea);
                if (valor < min || valor > max) {
                    System.out.printf("  [Error] El valor debe estar entre %d y %d.%n", min, max);
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("  [Error] Se esperaba un número entero.");
            }
        }
    }

    /**
     * Lee un entero sin límite superior (usa Integer.MAX_VALUE como techo).
     */
    public int leerEnteroPositivo(String mensaje, int min) {
        return leerEntero(mensaje, min, Integer.MAX_VALUE);
    }

    /**
     * Lee una cadena no vacía.
     */
    public String leerCadena(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            if (!linea.isEmpty()) {
                return linea;
            }
            System.out.println("  [Error] La entrada no puede estar vacía.");
        }
    }

    /**
     * Lee una secuencia de enteros separados por espacios o comas.
     * Devuelve un arreglo con los valores leídos.
     */
    public int[] leerSecuenciaEnteros(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                System.out.println("  [Error] Debe ingresar al menos un valor.");
                continue;
            }
            // Acepta separadores: espacio, coma o punto y coma
            String[] partes = linea.split("[,;\\s]+");
            int[] valores = new int[partes.length];
            boolean valido = true;
            for (int i = 0; i < partes.length; i++) {
                try {
                    int v = Integer.parseInt(partes[i].trim());
                    if (v < min || v > max) {
                        System.out.printf("  [Error] Cada valor debe estar entre %d y %d. Valor inválido: %d%n", min, max, v);
                        valido = false;
                        break;
                    }
                    valores[i] = v;
                } catch (NumberFormatException e) {
                    System.out.printf("  [Error] '%s' no es un número entero válido.%n", partes[i].trim());
                    valido = false;
                    break;
                }
            }
            if (valido) {
                return valores;
            }
        }
    }

    /**
     * Pausa y espera que el usuario presione Enter para continuar.
     */
    public void esperarEnter(String mensaje) {
        System.out.print(mensaje);
        scanner.nextLine();
    }
}
