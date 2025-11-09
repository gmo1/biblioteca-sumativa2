package biblioteca.modelo;

/**
 * Clase Estudiante que hereda de Usuario
 * Tiene periodo de préstamo de 10 días
 */
public class Estudiante extends Usuario {
    private String carrera;
    public static final int DIAS_PRESTAMO = 10;

    // Constructor
    public Estudiante(String nombreCompleto, String run, String genero, String carrera) {
        super(nombreCompleto, run, genero);
        this.carrera = carrera;
    }

    // Getters y Setters
    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getDiasPrestamo() {
        return DIAS_PRESTAMO;
    }

    @Override
    public String getTipo() {
        return "Estudiante";
    }

    @Override
    public String getInfoAdicional() {
        return "Carrera: " + carrera;
    }

    @Override
    public String toString() {
        return super.toString() + " | Carrera: " + carrera + " | Días préstamo: " + DIAS_PRESTAMO;
    }
}