package biblioteca.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Clase para representar un préstamo activo
 */
public class Prestamo {
    private String runUsuario;
    private String isbnLibro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private int diasPrestamo;

    // Constructor
    public Prestamo(String runUsuario, String isbnLibro, LocalDate fechaPrestamo, LocalDate fechaDevolucion, int diasPrestamo) {
        this.runUsuario = runUsuario;
        this.isbnLibro = isbnLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.diasPrestamo = diasPrestamo;
    }

    // Getters y Setters
    public String getRunUsuario() {
        return runUsuario;
    }

    public void setRunUsuario(String runUsuario) {
        this.runUsuario = runUsuario;
    }

    public String getIsbnLibro() {
        return isbnLibro;
    }

    public void setIsbnLibro(String isbnLibro) {
        this.isbnLibro = isbnLibro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public int getDiasPrestamo() {
        return diasPrestamo;
    }

    public void setDiasPrestamo(int diasPrestamo) {
        this.diasPrestamo = diasPrestamo;
    }

    // Métodos auxiliares
    public long calcularDiasRetraso() {
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(fechaDevolucion)) {
            return ChronoUnit.DAYS.between(fechaDevolucion, hoy);
        }
        return 0;
    }

    public int calcularMulta() {
        long diasRetraso = calcularDiasRetraso();
        return (int) (diasRetraso * 1000); // $1000 por día
    }

    public boolean estaAtrasado() {
        return LocalDate.now().isAfter(fechaDevolucion);
    }

    @Override
    public String toString() {
        return "Préstamo | RUN: " + runUsuario +
                " | ISBN: " + isbnLibro +
                " | Fecha préstamo: " + fechaPrestamo +
                " | Fecha devolución: " + fechaDevolucion +
                " | Días: " + diasPrestamo;
    }
}