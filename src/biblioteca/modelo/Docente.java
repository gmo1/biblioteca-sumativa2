package biblioteca.modelo;

/**
 * Clase Docente que hereda de Usuario
 * Tiene periodo de préstamo de 20 días
 */
public class Docente extends Usuario {
    private String profesion;
    private String grados; // magíster y/o doctor
    public static final int DIAS_PRESTAMO = 20;

    // Constructor
    public Docente(String nombreCompleto, String run, String genero, String profesion, String grados) {
        super(nombreCompleto, run, genero);
        this.profesion = profesion;
        this.grados = grados;
    }

    // Getters y Setters
    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getGrados() {
        return grados;
    }

    public void setGrados(String grados) {
        this.grados = grados;
    }

    public int getDiasPrestamo() {
        return DIAS_PRESTAMO;
    }

    @Override
    public String getTipo() {
        return "Docente";
    }

    @Override
    public String getInfoAdicional() {
        return "Profesión: " + profesion + " | Grados: " + grados;
    }

    @Override
    public String toString() {
        return super.toString() + " | Profesión: " + profesion +
                " | Grados: " + grados + " | Días préstamo: " + DIAS_PRESTAMO;
    }
}