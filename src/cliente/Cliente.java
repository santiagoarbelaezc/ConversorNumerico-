package cliente;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Conectado al servidor: " + SERVER_ADDRESS + ":" + SERVER_PORT);

            while (true) {
                mostrarMenu();
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("0")) {
                    System.out.println("Saliendo...");
                    break;
                }

                if (input.equalsIgnoreCase("")) {
                    continue;
                }

                // Procesar el input en formato: opcion;numero;bits
                String mensaje = procesarInput(input);

                if (mensaje.startsWith("ERROR:")) {
                    System.out.println(mensaje);
                    continue;
                }

                // CAMBIO IMPORTANTE: Enviar el mensaje con ; como separador
                out.println(mensaje);

                // Recibir y mostrar respuesta
                String respuesta = in.readLine();
                System.out.println("Resultado: " + respuesta);
                System.out.println("----------------------------------------");
            }

        } catch (UnknownHostException e) {
            System.err.println("Servidor no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n=== CONVERSOR NUMÉRICO ===");
        System.out.println("1. Convertir decimal a binario");
        System.out.println("2. Convertir binario a decimal");
        System.out.println("3. Convertir decimal a hexadecimal");
        System.out.println("4. Convertir hexadecimal a decimal");
        System.out.println("5. Convertir binario a hexadecimal");
        System.out.println("6. Convertir hexadecimal a binario");
        System.out.println("0. Salir");
        System.out.println("Formato: opcion;numero;bits (bits es opcional para algunas conversiones)");
        System.out.println("Ejemplo: 1;10;8");
        System.out.print("Ingrese su elección: ");
    }

    private static String procesarInput(String input) {
        try {
            String[] partes = input.split(";");

            if (partes.length < 2) {
                return "ERROR: Formato incorrecto. Use: opcion;numero;bits";
            }

            int opcion = Integer.parseInt(partes[0].trim());
            String numero = partes[1].trim();

            // Validar opción
            if (opcion < 1 || opcion > 6) {
                return "ERROR: Opción debe estar entre 1 y 6";
            }

            // Validar número según la opción
            switch (opcion) {
                case 1: case 3:
                    if (!esDecimal(numero)) {
                        return "ERROR: Para opción " + opcion + " debe ingresar un número decimal";
                    }
                    break;
                case 2: case 5:
                    if (!esBinario(numero)) {
                        return "ERROR: Para opción " + opcion + " debe ingresar un número binario";
                    }
                    break;
                case 4: case 6:
                    if (!esHexadecimal(numero)) {
                        return "ERROR: Para opción " + opcion + " debe ingresar un número hexadecimal";
                    }
                    break;
            }

            // Procesar bits (opcional)
            int bits = 0;
            if (partes.length >= 3) {
                try {
                    bits = Integer.parseInt(partes[2].trim());
                    if (bits <= 0) {
                        return "ERROR: La longitud de bits debe ser mayor a 0";
                    }
                } catch (NumberFormatException e) {
                    return "ERROR: Formato de bits incorrecto";
                }
            }

            // Para opciones que requieren bits pero no se proporcionaron
            if ((opcion == 1 || opcion == 6) && bits == 0) {
                return "ERROR: Para opción " + opcion + " debe especificar la longitud de bits";
            }

            // CAMBIO IMPORTANTE: Construir mensaje en formato: opcion;numero;bits
            return opcion + ";" + numero + (bits > 0 ? ";" + bits : "");

        } catch (NumberFormatException e) {
            return "ERROR: Formato de opción incorrecto";
        }
    }

    private static boolean esDecimal(String numero) {
        return numero.matches("\\d+");
    }

    private static boolean esBinario(String numero) {
        return numero.matches("[01]+");
    }

    private static boolean esHexadecimal(String numero) {
        return numero.matches("[0-9A-Fa-f]+");
    }
}