package biblioteca.util;

import biblioteca.modelo.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Clase para gestionar persistencia en archivos de texto plano
 */
public class GestorArchivos {
    private static final String RUTA_USUARIOS = "datos/usuarios.txt";
    private static final String RUTA_LIBROS = "datos/libros.txt";
    private static final String RUTA_PRESTAMOS = "datos/prestamos.txt";
    private static final String SEPARADOR = "|";

    /**
     * Guarda la lista de usuarios en archivo
     */
    public static void guardarUsuarios(ArrayList<Usuario> usuarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_USUARIOS))) {
            for (Usuario u : usuarios) {
                String linea;
                if (u instanceof Estudiante) {
                    Estudiante e = (Estudiante) u;
                    linea = "ESTUDIANTE" + SEPARADOR +
                            e.getNombreCompleto() + SEPARADOR +
                            e.getRun() + SEPARADOR +
                            e.getGenero() + SEPARADOR +
                            (e.getPrestamo() != null ? e.getPrestamo() : "") + SEPARADOR +
                            e.getCarrera();
                } else if (u instanceof Docente) {
                    Docente d = (Docente) u;
                    linea = "DOCENTE" + SEPARADOR +
                            d.getNombreCompleto() + SEPARADOR +
                            d.getRun() + SEPARADOR +
                            d.getGenero() + SEPARADOR +
                            (d.getPrestamo() != null ? d.getPrestamo() : "") + SEPARADOR +
                            d.getProfesion() + SEPARADOR +
                            d.getGrados();
                } else {
                    continue;
                }
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de usuarios desde archivo
     */
    public static ArrayList<Usuario> cargarUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(RUTA_USUARIOS);

        if (!archivo.exists()) {
            return usuarios;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split("\\" + SEPARADOR);
                if (datos.length < 5) continue;

                String tipo = datos[0];
                String nombre = datos[1];
                String run = datos[2];
                String genero = datos[3];
                String prestamo = datos[4].isEmpty() ? null : datos[4];

                Usuario usuario = null;
                if (tipo.equals("ESTUDIANTE") && datos.length >= 6) {
                    String carrera = datos[5];
                    usuario = new Estudiante(nombre, run, genero, carrera);
                } else if (tipo.equals("DOCENTE") && datos.length >= 7) {
                    String profesion = datos[5];
                    String grados = datos[6];
                    usuario = new Docente(nombre, run, genero, profesion, grados);
                }

                if (usuario != null) {
                    usuario.setPrestamo(prestamo);
                    usuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    /**
     * Guarda la lista de libros en archivo
     */
    public static void guardarLibros(ArrayList<Libro> libros) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_LIBROS))) {
            for (Libro l : libros) {
                String linea = l.getIsbn() + SEPARADOR +
                        l.getTitulo() + SEPARADOR +
                        l.getAutor() + SEPARADOR +
                        l.getCantidadBiblioteca() + SEPARADOR +
                        l.getCantidadDisponible() + SEPARADOR +
                        (l.getImagen() != null ? l.getImagen() : "");
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar libros: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de libros desde archivo
     */
    public static ArrayList<Libro> cargarLibros() {
        ArrayList<Libro> libros = new ArrayList<>();
        File archivo = new File(RUTA_LIBROS);

        if (!archivo.exists()) {
            return libros;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split("\\" + SEPARADOR);
                if (datos.length >= 5) {
                    String isbn = datos[0];
                    String titulo = datos[1];
                    String autor = datos[2];
                    int cantBiblioteca = Integer.parseInt(datos[3]);
                    int cantDisponible = Integer.parseInt(datos[4]);
                    String imagen = datos.length > 5 ? datos[5] : "";

                    Libro libro = new Libro(isbn, titulo, autor, cantBiblioteca, cantDisponible, imagen);
                    libros.add(libro);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al cargar libros: " + e.getMessage());
        }

        return libros;
    }

    /**
     * Guarda la lista de préstamos en archivo
     */
    public static void guardarPrestamos(ArrayList<Prestamo> prestamos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_PRESTAMOS))) {
            for (Prestamo p : prestamos) {
                String linea = p.getRunUsuario() + SEPARADOR +
                        p.getIsbnLibro() + SEPARADOR +
                        p.getFechaPrestamo().toString() + SEPARADOR +
                        p.getFechaDevolucion().toString() + SEPARADOR +
                        p.getDiasPrestamo();
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar préstamos: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de préstamos desde archivo
     */
    public static ArrayList<Prestamo> cargarPrestamos() {
        ArrayList<Prestamo> prestamos = new ArrayList<>();
        File archivo = new File(RUTA_PRESTAMOS);

        if (!archivo.exists()) {
            return prestamos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split("\\" + SEPARADOR);
                if (datos.length >= 5) {
                    String run = datos[0];
                    String isbn = datos[1];
                    LocalDate fechaPrestamo = LocalDate.parse(datos[2]);
                    LocalDate fechaDevolucion = LocalDate.parse(datos[3]);
                    int dias = Integer.parseInt(datos[4]);

                    Prestamo prestamo = new Prestamo(run, isbn, fechaPrestamo, fechaDevolucion, dias);
                    prestamos.add(prestamo);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al cargar préstamos: " + e.getMessage());
        }

        return prestamos;
    }

    /**
     * Crea los directorios necesarios si no existen
     */
    public static void inicializarDirectorios() {
        File dirDatos = new File("datos");
        if (!dirDatos.exists()) {
            dirDatos.mkdir();
        }
    }
}