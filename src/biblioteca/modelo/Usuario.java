package biblioteca.modelo;

/**
 * Clase base para representar un usuario de la biblioteca
 * Incluye atributos comunes y métodos básicos
 */
public abstract class Usuario {
    private String nombreCompleto;
    private String run;
    private String genero; // M o F
    private String prestamo; // ISBN del libro prestado o null si no tiene

    // Constructor
    public Usuario(String nombreCompleto, String run, String genero) {
        this.nombreCompleto = nombreCompleto;
        this.run = run;
        this.genero = genero;
        this.prestamo = null; // Por defecto no tiene préstamos
    }

    // Getters y Setters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(String prestamo) {
        this.prestamo = prestamo;
    }

    // Métodos auxiliares
    public boolean tienePrestamo() {
        return prestamo != null && !prestamo.isEmpty();
    }

    // Método abstracto para obtener información específica del tipo de usuario
    public abstract String getTipo();
    public abstract String getInfoAdicional();

    @Override
    public String toString() {
        return "Usuario: " + nombreCompleto +
                " | RUN: " + run +
                " | Género: " + genero +
                " | Préstamo activo: " + (tienePrestamo() ? prestamo : "No");
    }
}