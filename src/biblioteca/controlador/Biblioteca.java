package biblioteca.controlador;

import biblioteca.modelo.*;
import biblioteca.util.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Controlador central del sistema de biblioteca
 * Gestiona todas las operaciones CRUD y lógica de negocio
 */
public class Biblioteca {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Libro> libros;
    private ArrayList<Prestamo> prestamos;

    public Biblioteca() {
        GestorArchivos.inicializarDirectorios();
        this.usuarios = GestorArchivos.cargarUsuarios();
        this.libros = GestorArchivos.cargarLibros();
        this.prestamos = GestorArchivos.cargarPrestamos();
    }

    // ==================== GESTIÓN DE USUARIOS ====================

    /**
     * Agrega un nuevo usuario al sistema
     */
    public String agregarUsuario(Usuario usuario) {
        // Validar RUN
        if (!Validador.validarRUN(usuario.getRun())) {
            return "Error: RUN inválido";
        }

        // Validar que RUN no se repita
        if (buscarUsuario(usuario.getRun()) != null) {
            return "Error: Ya existe un usuario con ese RUN";
        }

        // Validar género
        if (!Validador.validarGenero(usuario.getGenero())) {
            return "Error: Género debe ser M o F";
        }

        usuarios.add(usuario);
        GestorArchivos.guardarUsuarios(usuarios);
        return "Usuario agregado exitosamente";
    }

    /**
     * Edita un usuario existente
     */
    public String editarUsuario(String runOriginal, Usuario usuarioNuevo) {
        Usuario usuario = buscarUsuario(runOriginal);
        if (usuario == null) {
            return "Error: Usuario no encontrado";
        }

        // Si cambió el RUN, validar que no exista otro con ese RUN
        if (!runOriginal.equals(usuarioNuevo.getRun())) {
            if (!Validador.validarRUN(usuarioNuevo.getRun())) {
                return "Error: RUN inválido";
            }
            if (buscarUsuario(usuarioNuevo.getRun()) != null) {
                return "Error: Ya existe un usuario con ese RUN";
            }
        }

        // Validar género
        if (!Validador.validarGenero(usuarioNuevo.getGenero())) {
            return "Error: Género debe ser M o F";
        }

        // Actualizar datos
        usuario.setNombreCompleto(usuarioNuevo.getNombreCompleto());
        usuario.setRun(usuarioNuevo.getRun());
        usuario.setGenero(usuarioNuevo.getGenero());

        if (usuario instanceof Estudiante && usuarioNuevo instanceof Estudiante) {
            ((Estudiante) usuario).setCarrera(((Estudiante) usuarioNuevo).getCarrera());
        } else if (usuario instanceof Docente && usuarioNuevo instanceof Docente) {
            ((Docente) usuario).setProfesion(((Docente) usuarioNuevo).getProfesion());
            ((Docente) usuario).setGrados(((Docente) usuarioNuevo).getGrados());
        }

        GestorArchivos.guardarUsuarios(usuarios);
        return "Usuario editado exitosamente";
    }

    /**
     * Elimina un usuario del sistema
     */
    public String eliminarUsuario(String run) {
        Usuario usuario = buscarUsuario(run);
        if (usuario == null) {
            return "Error: Usuario no encontrado";
        }

        // Validar que no tenga préstamos activos
        if (usuario.tienePrestamo()) {
            return "Error: El usuario tiene un préstamo activo";
        }

        usuarios.remove(usuario);
        GestorArchivos.guardarUsuarios(usuarios);
        return "Usuario eliminado exitosamente";
    }

    /**
     * Busca un usuario por RUN
     */
    public Usuario buscarUsuario(String run) {
        for (Usuario u : usuarios) {
            if (u.getRun().equals(run)) {
                return u;
            }
        }
        return null;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    // ==================== GESTIÓN DE LIBROS ====================

    /**
     * Agrega un nuevo libro al sistema
     */
    public String agregarLibro(Libro libro) {
        // Validar ISBN
        if (!Validador.validarISBN(libro.getIsbn())) {
            return "Error: ISBN no puede estar vacío";
        }

        // Validar que ISBN sea único
        if (buscarLibro(libro.getIsbn()) != null) {
            return "Error: Ya existe un libro con ese ISBN";
        }

        // Validar cantidades
        if (!Validador.validarCantidadPositiva(libro.getCantidadBiblioteca())) {
            return "Error: Cantidad en biblioteca debe ser mayor a cero";
        }

        if (!Validador.validarCantidadDisponible(libro.getCantidadDisponible(),
                libro.getCantidadBiblioteca())) {
            return "Error: Cantidad disponible debe ser entre 0 y cantidad total";
        }

        libros.add(libro);
        GestorArchivos.guardarLibros(libros);
        return "Libro agregado exitosamente";
    }

    /**
     * Elimina un libro del sistema
     */
    public String eliminarLibro(String isbn) {
        Libro libro = buscarLibro(isbn);
        if (libro == null) {
            return "Error: Libro no encontrado";
        }

        // Verificar que no esté prestado
        if (libro.getCantidadDisponible() < libro.getCantidadBiblioteca()) {
            return "Error: El libro tiene ejemplares prestados";
        }

        libros.remove(libro);
        GestorArchivos.guardarLibros(libros);
        return "Libro eliminado exitosamente";
    }

    /**
     * Busca un libro por ISBN
     */
    public Libro buscarLibro(String isbn) {
        for (Libro l : libros) {
            if (l.getIsbn().equals(isbn)) {
                return l;
            }
        }
        return null;
    }

    public ArrayList<Libro> getLibros() {
        return libros;
    }

    // ==================== GESTIÓN DE PRÉSTAMOS ====================

    /**
     * Realiza un préstamo de libro
     */
    public String realizarPrestamo(String run, String isbn) {
        // Validar que el libro exista
        Libro libro = buscarLibro(isbn);
        if (libro == null) {
            return "Error: Libro no encontrado";
        }

        // Validar que haya disponibilidad
        if (!libro.tieneDisponibilidad()) {
            return "Error: No hay ejemplares disponibles";
        }

        // Validar que el usuario exista
        Usuario usuario = buscarUsuario(run);
        if (usuario == null) {
            return "Error: Usuario no encontrado";
        }

        // Validar que el usuario esté habilitado
        if (usuario.tienePrestamo()) {
            return "Error: El usuario ya tiene un préstamo activo";
        }

        // Calcular fechas según tipo de usuario
        LocalDate fechaPrestamo = LocalDate.now();
        int diasPrestamo;

        if (usuario instanceof Estudiante) {
            diasPrestamo = ((Estudiante) usuario).getDiasPrestamo();
        } else if (usuario instanceof Docente) {
            diasPrestamo = ((Docente) usuario).getDiasPrestamo();
        } else {
            return "Error: Tipo de usuario no válido";
        }

        LocalDate fechaDevolucion = fechaPrestamo.plusDays(diasPrestamo);

        // Crear el préstamo
        Prestamo prestamo = new Prestamo(run, isbn, fechaPrestamo, fechaDevolucion, diasPrestamo);
        prestamos.add(prestamo);

        // Actualizar usuario y libro
        usuario.setPrestamo(isbn);
        libro.prestar();

        // Guardar cambios
        GestorArchivos.guardarUsuarios(usuarios);
        GestorArchivos.guardarLibros(libros);
        GestorArchivos.guardarPrestamos(prestamos);

        return "Préstamo realizado exitosamente";
    }

    /**
     * Realiza la devolución de un libro
     */
    public String realizarDevolucion(String run, String isbn) {
        // Validar que el libro exista
        Libro libro = buscarLibro(isbn);
        if (libro == null) {
            return "Error: Libro no encontrado";
        }

        // Validar que el usuario exista
        Usuario usuario = buscarUsuario(run);
        if (usuario == null) {
            return "Error: Usuario no encontrado";
        }

        // Validar que el préstamo coincida
        if (!isbn.equals(usuario.getPrestamo())) {
            return "Error: El usuario no tiene prestado ese libro";
        }

        // Buscar el préstamo correspondiente
        Prestamo prestamo = buscarPrestamo(run, isbn);
        if (prestamo == null) {
            return "Error: No se encontró el registro del préstamo";
        }

        // Calcular multa si hay retraso
        int multa = prestamo.calcularMulta();
        String mensaje = "Devolución realizada exitosamente";
        if (multa > 0) {
            mensaje += "\nMulta por retraso: $" + multa;
        }

        // Actualizar usuario y libro
        usuario.setPrestamo(null);
        libro.devolver();

        // Eliminar préstamo
        prestamos.remove(prestamo);

        // Guardar cambios
        GestorArchivos.guardarUsuarios(usuarios);
        GestorArchivos.guardarLibros(libros);
        GestorArchivos.guardarPrestamos(prestamos);

        return mensaje;
    }

    /**
     * Busca un préstamo específico
     */
    public Prestamo buscarPrestamo(String run, String isbn) {
        for (Prestamo p : prestamos) {
            if (p.getRunUsuario().equals(run) && p.getIsbnLibro().equals(isbn)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Genera el texto para la tarjeta de préstamo
     */
    public String generarTarjetaPrestamo(String run, String isbn) {
        Usuario usuario = buscarUsuario(run);
        Libro libro = buscarLibro(isbn);
        Prestamo prestamo = buscarPrestamo(run, isbn);

        if (usuario == null || libro == null || prestamo == null) {
            return "Error: No se pudo generar la tarjeta";
        }

        StringBuilder tarjeta = new StringBuilder();
        tarjeta.append("═══════════════════════════════════════\n");
        tarjeta.append("       TARJETA DE PRÉSTAMO BIBLIOTECA\n");
        tarjeta.append("       Universidad Nacional Andrés Bello\n");
        tarjeta.append("═══════════════════════════════════════\n\n");

        tarjeta.append("LIBRO:\n");
        tarjeta.append("  Título: ").append(libro.getTitulo()).append("\n");
        tarjeta.append("  Autor: ").append(libro.getAutor()).append("\n");
        tarjeta.append("  ISBN: ").append(libro.getIsbn()).append("\n\n");

        tarjeta.append("USUARIO:\n");
        tarjeta.append("  Nombre: ").append(usuario.getNombreCompleto()).append("\n");
        tarjeta.append("  RUN: ").append(usuario.getRun()).append("\n");
        tarjeta.append("  Tipo: ").append(usuario.getTipo()).append("\n\n");

        tarjeta.append("PRÉSTAMO:\n");
        tarjeta.append("  Fecha préstamo: ").append(prestamo.getFechaPrestamo()).append("\n");
        tarjeta.append("  Fecha devolución: ").append(prestamo.getFechaDevolucion()).append("\n");
        tarjeta.append("  Días de préstamo: ").append(prestamo.getDiasPrestamo()).append("\n\n");

        tarjeta.append("═══════════════════════════════════════\n");
        tarjeta.append("Recuerde devolver el libro a tiempo\n");
        tarjeta.append("Multa por retraso: $1.000 por día\n");
        tarjeta.append("═══════════════════════════════════════\n");

        return tarjeta.toString();
    }

    public ArrayList<Prestamo> getPrestamos() {
        return prestamos;
    }
}