package servidor;

import utils.ConversorNumerico;
import java.io.*;
import java.net.*;

public class Servidor {
    private static final int PUERTO = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("=== SERVIDOR INICIADO ===");
            System.out.println("Servidor iniciado en el puerto " + PUERTO);
            System.out.println("Esperando conexiones...");
            System.out.println("=========================");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\n=== NUEVA CONEXIÓN ===");
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                System.out.println("Puerto remoto: " + clientSocket.getPort());
                System.out.println("======================");

                // Manejar cada cliente en un hilo separado
                new Thread(() -> manejarCliente(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void manejarCliente(Socket clientSocket) {
        String clientInfo = clientSocket.getInetAddress() + ":" + clientSocket.getPort();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            System.out.println("\n[CLIENTE " + clientInfo + "] Iniciando comunicación");

            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                System.out.println("\n[CLIENTE " + clientInfo + "] Mensaje recibido: '" + mensaje + "'");
                System.out.println("[CLIENTE " + clientInfo + "] Procesando mensaje...");

                String respuesta = procesarMensaje(mensaje, clientInfo);

                System.out.println("[CLIENTE " + clientInfo + "] Enviando respuesta: '" + respuesta + "'");
                out.println(respuesta);
                System.out.println("[CLIENTE " + clientInfo + "] Respuesta enviada exitosamente");
            }

            System.out.println("\n[CLIENTE " + clientInfo + "] Conexión cerrada por el cliente");

        } catch (IOException e) {
            System.err.println("[CLIENTE " + clientInfo + "] Error en la comunicación: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("[CLIENTE " + clientInfo + "] Socket cerrado exitosamente");
            } catch (IOException e) {
                System.err.println("[CLIENTE " + clientInfo + "] Error al cerrar socket: " + e.getMessage());
            }
        }
    }

    private static String procesarMensaje(String mensaje, String clientInfo) {
        System.out.println("[CLIENTE " + clientInfo + "] Descomponiendo mensaje...");

        try {
            // CAMBIO IMPORTANTE: Usar ; como separador en lugar de :
            String[] partes = mensaje.split(";");
            System.out.println("[CLIENTE " + clientInfo + "] Partes del mensaje: " + java.util.Arrays.toString(partes));

            if (partes.length < 2) {
                System.out.println("[CLIENTE " + clientInfo + "] ERROR: Formato de mensaje incorrecto - menos de 2 partes");
                return "ERROR: Formato de mensaje incorrecto. Use: opcion;numero;bits";
            }

            int opcion = Integer.parseInt(partes[0]);
            String numero = partes[1];
            int bits = partes.length > 2 ? Integer.parseInt(partes[2]) : 0;

            System.out.println("[CLIENTE " + clientInfo + "] Opción parseada: " + opcion);
            System.out.println("[CLIENTE " + clientInfo + "] Número parseado: '" + numero + "'");
            System.out.println("[CLIENTE " + clientInfo + "] Bits parseados: " + bits);

            System.out.println("[CLIENTE " + clientInfo + "] Ejecutando conversión para opción " + opcion + "...");

            String resultado;
            switch (opcion) {
                case 1: // Decimal a binario
                    System.out.println("[CLIENTE " + clientInfo + "] Conversión: Decimal a Binario");
                    if (!ConversorNumerico.esDecimal(numero)) {
                        System.out.println("[CLIENTE " + clientInfo + "] ERROR: No es un número decimal válido");
                        return "ERROR: No es un número decimal válido";
                    }
                    resultado = ConversorNumerico.decimalABinario(Integer.parseInt(numero), bits);
                    System.out.println("[CLIENTE " + clientInfo + "] Resultado de conversión: " + resultado);
                    return resultado;

                case 2: // Binario a decimal
                    System.out.println("[CLIENTE " + clientInfo + "] Conversión: Binario a Decimal");
                    if (!ConversorNumerico.esBinario(numero)) {
                        System.out.println("[CLIENTE " + clientInfo + "] ERROR: No es un número binario válido");
                        return "ERROR: No es un número binario válido";
                    }
                    resultado = String.valueOf(ConversorNumerico.binarioADecimal(numero));
                    System.out.println("[CLIENTE " + clientInfo + "] Resultado de conversión: " + resultado);
                    return resultado;

                case 3: // Decimal a hexadecimal
                    System.out.println("[CLIENTE " + clientInfo + "] Conversión: Decimal a Hexadecimal");
                    if (!ConversorNumerico.esDecimal(numero)) {
                        System.out.println("[CLIENTE " + clientInfo + "] ERROR: No es un número decimal válido");
                        return "ERROR: No es un número decimal válido";
                    }
                    resultado = ConversorNumerico.decimalAHexadecimal(Integer.parseInt(numero));
                    System.out.println("[CLIENTE " + clientInfo + "] Resultado de conversión: " + resultado);
                    return resultado;

                case 4: // Hexadecimal a decimal
                    System.out.println("[CLIENTE " + clientInfo + "] Conversión: Hexadecimal a Decimal");
                    if (!ConversorNumerico.esHexadecimal(numero)) {
                        System.out.println("[CLIENTE " + clientInfo + "] ERROR: No es un número hexadecimal válido");
                        return "ERROR: No es un número hexadecimal válido";
                    }
                    resultado = String.valueOf(ConversorNumerico.hexadecimalADecimal(numero));
                    System.out.println("[CLIENTE " + clientInfo + "] Resultado de conversión: " + resultado);
                    return resultado;

                case 5: // Binario a hexadecimal
                    System.out.println("[CLIENTE " + clientInfo + "] Conversión: Binario a Hexadecimal");
                    if (!ConversorNumerico.esBinario(numero)) {
                        System.out.println("[CLIENTE " + clientInfo + "] ERROR: No es un número binario válido");
                        return "ERROR: No es un número binario válido";
                    }
                    resultado = ConversorNumerico.binarioAHexadecimal(numero);
                    System.out.println("[CLIENTE " + clientInfo + "] Resultado de conversión: " + resultado);
                    return resultado;

                case 6: // Hexadecimal a binario
                    System.out.println("[CLIENTE " + clientInfo + "] Conversión: Hexadecimal a Binario");
                    if (!ConversorNumerico.esHexadecimal(numero)) {
                        System.out.println("[CLIENTE " + clientInfo + "] ERROR: No es un número hexadecimal válido");
                        return "ERROR: No es un número hexadecimal válido";
                    }
                    resultado = ConversorNumerico.hexadecimalABinario(numero, bits);
                    System.out.println("[CLIENTE " + clientInfo + "] Resultado de conversión: " + resultado);
                    return resultado;

                default:
                    System.out.println("[CLIENTE " + clientInfo + "] ERROR: Opción no válida: " + opcion);
                    return "ERROR: Opción no válida. Use 1-6";
            }
        } catch (NumberFormatException e) {
            System.out.println("[CLIENTE " + clientInfo + "] ERROR: Formato de número incorrecto - " + e.getMessage());
            return "ERROR: Formato de número incorrecto";
        } catch (Exception e) {
            System.out.println("[CLIENTE " + clientInfo + "] ERROR inesperado: " + e.getMessage());
            e.printStackTrace();
            return "ERROR: Error interno del servidor";
        }
    }
}