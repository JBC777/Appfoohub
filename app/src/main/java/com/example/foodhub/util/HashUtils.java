package com.example.foodhub.util;

// IMPORTACIONES NECESARIAS PARA TRABAJAR CON CIFRADO Y CONJUNTOS DE CARACTERES
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase de utilidades para operaciones de hash de contraseñas.
 * Usa el algoritmo SHA-256 para cifrado seguro.
 */
public class HashUtils {

    /**
     * Aplica el algoritmo SHA-256 para generar un hash seguro de la contraseña.
     * @param password Contraseña en texto plano a cifrar
     * @return Cadena hexadecimal que representa el hash de la contraseña, o null si hay error
     */
    public static String hashPassword(String password) {
        try {
            // Crea una instancia del algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convierte la contraseña en bytes con codificación UTF-8 y genera el hash
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convierte el resultado en una cadena hexadecimal
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // Captura la excepción si el algoritmo no está disponible (muy poco probable en Android)
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convierte un arreglo de bytes a una representación hexadecimal.
     * @param bytes Arreglo de bytes a convertir
     * @return Cadena hexadecimal equivalente
     */
    private static String bytesToHex(byte[] bytes) {
        // Usa StringBuilder por eficiencia
        StringBuilder hexString = new StringBuilder(2 * bytes.length);

        // Convierte cada byte en su representación hexadecimal
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);

            // Añade un 0 inicial si el valor hexadecimal tiene sólo un dígito
            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * Verifica si una contraseña en texto plano coincide con un hash ya almacenado.
     * @param rawPassword Contraseña original en texto plano
     * @param hashedPassword Hash previamente almacenado (por ejemplo, en la base de datos)
     * @return true si coinciden, false en caso contrario
     */
    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        // Hashea la contraseña ingresada por el usuario
        String hashedInput = hashPassword(rawPassword);

        // Compara el hash generado con el hash almacenado
        return hashedInput != null && hashedInput.equals(hashedPassword);
    }
}
