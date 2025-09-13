package utils;

public class ConversorNumerico {

    // Convertir decimal a binario
    public static String decimalABinario(int decimal, int bits) {
        String binario = Integer.toBinaryString(decimal);
        // Ajustar a la longitud especificada
        if (binario.length() < bits) {
            binario = String.format("%" + bits + "s", binario).replace(' ', '0');
        } else if (binario.length() > bits) {
            binario = binario.substring(binario.length() - bits);
        }
        return binario;
    }

    // Convertir binario a decimal
    public static int binarioADecimal(String binario) {
        return Integer.parseInt(binario, 2);
    }

    // Convertir decimal a hexadecimal
    public static String decimalAHexadecimal(int decimal) {
        return Integer.toHexString(decimal).toUpperCase();
    }

    // Convertir hexadecimal a decimal
    public static int hexadecimalADecimal(String hexadecimal) {
        return Integer.parseInt(hexadecimal, 16);
    }

    // Convertir binario a hexadecimal
    public static String binarioAHexadecimal(String binario) {
        int decimal = binarioADecimal(binario);
        return decimalAHexadecimal(decimal);
    }

    // Convertir hexadecimal a binario
    public static String hexadecimalABinario(String hexadecimal, int bits) {
        int decimal = hexadecimalADecimal(hexadecimal);
        return decimalABinario(decimal, bits);
    }

    // Validar si es un número binario
    public static boolean esBinario(String numero) {
        return numero.matches("[01]+");
    }

    // Validar si es un número hexadecimal
    public static boolean esHexadecimal(String numero) {
        return numero.matches("[0-9A-Fa-f]+");
    }

    // Validar si es un número decimal
    public static boolean esDecimal(String numero) {
        return numero.matches("\\d+");
    }
}