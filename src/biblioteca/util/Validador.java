package biblioteca.util;

/**
 * Clase utilitaria para validaciones del sistema
 */
public class Validador {

    /**
     * Valida el formato y dígito verificador del RUN chileno
     * Formato: 12345678-9 o 12345678-K
     */
    public static boolean validarRUN(String run) {
        if (run == null || run.isEmpty()) {
            return false;
        }

        // Remover puntos y guiones
        run = run.replace(".", "").replace("-", "").toUpperCase();

        // Debe tener entre 8 y 9 caracteres (7-8 dígitos + verificador)
        if (run.length() < 8 || run.length() > 9) {
            return false;
        }

        // Extraer número y dígito verificador
        String numero = run.substring(0, run.length() - 1);
        char dvIngresado = run.charAt(run.length() - 1);

        // Validar que el número sea numérico
        try {
            Integer.parseInt(numero);
        } catch (NumberFormatException e) {
            return false;
        }

        // Calcular dígito verificador
        char dvCalculado = calcularDigitoVerificador(numero);

        return dvIngresado == dvCalculado;
    }

    /**
     * Calcula el dígito verificador del RUN
     */
    private static char calcularDigitoVerificador(String run) {
        int suma = 0;
        int multiplicador = 2;

        // Recorrer de derecha a izquierda
        for (int i = run.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(run.charAt(i)) * multiplicador;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
        }

        int resto = 11 - (suma % 11);

        if (resto == 11) {
            return '0';
        } else if (resto == 10) {
            return 'K';
        } else {
            return Character.forDigit(resto, 10);
        }
    }

    /**
     * Valida que el género sea M o F
     */
    public static boolean validarGenero(String genero) {
        if (genero == null || genero.isEmpty()) {
            return false;
        }
        String generoUpper = genero.toUpperCase();
        return generoUpper.equals("M") || generoUpper.equals("F");
    }

    /**
     * Valida que el ISBN no esté vacío
     */
    public static boolean validarISBN(String isbn) {
        return isbn != null && !isbn.trim().isEmpty();
    }

    /**
     * Valida que una cantidad sea mayor a cero
     */
    public static boolean validarCantidadPositiva(int cantidad) {
        return cantidad > 0;
    }

    /**
     * Valida que la cantidad disponible sea coherente con la cantidad total
     */
    public static boolean validarCantidadDisponible(int disponible, int total) {
        return disponible >= 0 && disponible <= total;
    }

    /**
     * Formatea el RUN con puntos y guión
     * Ejemplo: 12345678-9
     */
    public static String formatearRUN(String run) {
        if (run == null || run.isEmpty()) {
            return run;
        }

        // Remover formato previo
        run = run.replace(".", "").replace("-", "").toUpperCase();

        if (run.length() < 2) {
            return run;
        }

        // Separar número y dígito verificador
        String numero = run.substring(0, run.length() - 1);
        String dv = run.substring(run.length() - 1);

        return numero + "-" + dv;
    }
}