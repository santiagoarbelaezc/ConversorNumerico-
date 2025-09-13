package servidor;


import utils.ConversorNumerico;

import java.io.*;
import java.net.*;

public class Servidor {
    private static final int PUERTO = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado en el puerto " + PUERTO);
            System.out.println("Esperando conexiones...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Manejar cada cliente en un hilo separado
                new Thread(() -> manejarCliente(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void manejarCliente(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                System.out.println("Mensaje recibido: " + mensaje);
                String respuesta = procesarMensaje(mensaje);
                out.println(respuesta);
                System.out.println("Respuesta enviada: " + respuesta);
            }

        } catch (IOException e) {
            System.err.println("Error con el cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar socket: " + e.getMessage());
            }
        }
    }

    private static String procesarMensaje(String mensaje) {
        try {
            String[] partes = mensaje.split(":");
            if (partes.length < 2) {
                return "ERROR: Formato de mensaje incorrecto";
            }

            int opcion = Integer.parseInt(partes[0]);
            String numero = partes[1];
            int bits = partes.length > 2 ? Integer.parseInt(partes[2]) : 0;

            switch (opcion) {
                case 1: // Decimal a binario
                    if (!ConversorNumerico.esDecimal(numero)) {
                        return "ERROR: No es un número decimal válido";
                    }
                    return ConversorNumerico.decimalABinario(Integer.parseInt(numero), bits);

                case 2: // Binario a decimal
                    if (!ConversorNumerico.esBinario(numero)) {
                        return "ERROR: No es un número binario válido";
                    }
                    return String.valueOf(ConversorNumerico.binarioADecimal(numero));

                case 3: // Decimal a hexadecimal
                    if (!ConversorNumerico.esDecimal(numero)) {
                        return "ERROR: No es un número decimal válido";
                    }
                    return ConversorNumerico.decimalAHexadecimal(Integer.parseInt(numero));

                case 4: // Hexadecimal a decimal
                    if (!ConversorNumerico.esHexadecimal(numero)) {
                        return "ERROR: No es un número hexadecimal válido";
                    }
                    return String.valueOf(ConversorNumerico.hexadecimalADecimal(numero));

                case 5: // Binario a hexadecimal
                    if (!ConversorNumerico.esBinario(numero)) {
                        return "ERROR: No es un número binario válido";
                    }
                    return ConversorNumerico.binarioAHexadecimal(numero);

                case 6: // Hexadecimal a binario
                    if (!ConversorNumerico.esHexadecimal(numero)) {
                        return "ERROR: No es un número hexadecimal válido";
                    }
                    return ConversorNumerico.hexadecimalABinario(numero, bits);

                default:
                    return "ERROR: Opción no válida";
            }
        } catch (NumberFormatException e) {
            return "ERROR: Formato de número incorrecto";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}