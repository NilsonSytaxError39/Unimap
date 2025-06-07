package com.example.univalle_map;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationUtilsTest {

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    @Test
    public void testValidEmailFormat() {
        // Casos válidos
        assertTrue(isValidEmail("usuario@univalle.edu.co"));
        assertTrue(isValidEmail("test@gmail.com"));

        // Casos inválidos
        assertFalse(isValidEmail("email_invalido"));
        assertFalse(isValidEmail("@univalle.edu.co"));
        assertFalse(isValidEmail("usuario@"));
    }

    @Test
    public void testPasswordStrength() {
        // Contraseñas válidas
        assertTrue(isValidPassword("Password123!"));
        assertTrue(isValidPassword("MiClave2024!"));

        // Contraseñas inválidas
        assertFalse(isValidPassword("123")); // Muy corta
        assertFalse(isValidPassword("password")); // Sin mayúsculas ni números
        assertFalse(isValidPassword("Password")); // Sin número ni símbolo
        assertFalse(isValidPassword("12345678")); // Solo números
    }
}
