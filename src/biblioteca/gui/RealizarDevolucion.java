package biblioteca.gui;

import biblioteca.controlador.Biblioteca;
import biblioteca.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Diálogo para realizar devoluciones de libros
 */
public class RealizarDevolucion extends JDialog {
    private Biblioteca biblioteca;
    private JTextField txtRun, txtIsbn;
    private JTextArea txtInfoPrestamo;

    public RealizarDevolucion(JFrame padre, Biblioteca biblioteca) {
        super(padre, "Realizar Devolución", true);
        this.biblioteca = biblioteca;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setSize(700, 450);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel datos de entrada
        JPanel panelEntrada = crearPanelEntrada();
        panelPrincipal.add(panelEntrada, BorderLayout.NORTH);

        // Panel información préstamo
        JPanel panelInfo = crearPanelInfo();
        panelPrincipal.add(panelInfo, BorderLayout.CENTER);

        add(panelPrincipal, BorderLayout.CENTER);

        // Panel botones
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearPanelEntrada() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Datos de la Devolución"));

        panel.add(new JLabel("RUN Usuario: (Con puntos y guion)"));
        txtRun = new JTextField();
        panel.add(txtRun);

        panel.add(new JLabel("ISBN Libro:"));
        txtIsbn = new JTextField();
        panel.add(txtIsbn);

        return panel;
    }

    private JPanel crearPanelInfo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Información del Préstamo"));

        txtInfoPrestamo = new JTextArea(12, 50);
        txtInfoPrestamo.setEditable(false);
        txtInfoPrestamo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtInfoPrestamo.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(txtInfoPrestamo);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnBuscar = new JButton("Buscar Préstamo");
        JButton btnDevolver = new JButton("Realizar Devolución");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnVolver = new JButton("Volver");

        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        btnDevolver.setBackground(new Color(230, 126, 34));
        btnDevolver.setForeground(Color.WHITE);
        btnDevolver.setFont(new Font("Arial", Font.BOLD, 12));

        btnBuscar.addActionListener(e -> buscarPrestamo());
        btnDevolver.addActionListener(e -> realizarDevolucion());
        btnLimpiar.addActionListener(e -> limpiar());
        btnVolver.addActionListener(e -> dispose());

        panel.add(btnBuscar);
        panel.add(btnDevolver);
        panel.add(btnLimpiar);
        panel.add(btnVolver);

        return panel;
    }

    private void buscarPrestamo() {
        String run = txtRun.getText().trim();
        String isbn = txtIsbn.getText().trim();

        if (run.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }

        Usuario usuario = biblioteca.buscarUsuario(run);
        Libro libro = biblioteca.buscarLibro(isbn);
        Prestamo prestamo = biblioteca.buscarPrestamo(run, isbn);

        if (usuario == null) {
            txtInfoPrestamo.setText("Usuario no encontrado");
            return;
        }

        if (libro == null) {
            txtInfoPrestamo.setText("Libro no encontrado");
            return;
        }

        if (prestamo == null) {
            txtInfoPrestamo.setText("No existe un préstamo con esos datos\n\n");
            txtInfoPrestamo.append("Usuario: " + usuario.getNombreCompleto() + "\n");
            txtInfoPrestamo.append("Libro prestado actual: " +
                    (usuario.getPrestamo() != null ? usuario.getPrestamo() : "Ninguno"));
            return;
        }

        // Mostrar información del préstamo
        StringBuilder info = new StringBuilder();
        info.append("═══════════════════════════════════════\n");
        info.append("          INFORMACIÓN DEL PRÉSTAMO\n");
        info.append("═══════════════════════════════════════\n\n");

        info.append("USUARIO:\n");
        info.append("  Nombre: ").append(usuario.getNombreCompleto()).append("\n");
        info.append("  RUN: ").append(usuario.getRun()).append("\n");
        info.append("  Tipo: ").append(usuario.getTipo()).append("\n\n");

        info.append("LIBRO:\n");
        info.append("  Título: ").append(libro.getTitulo()).append("\n");
        info.append("  Autor: ").append(libro.getAutor()).append("\n");
        info.append("  ISBN: ").append(libro.getIsbn()).append("\n\n");

        info.append("PRÉSTAMO:\n");
        info.append("  Fecha préstamo: ").append(prestamo.getFechaPrestamo()).append("\n");
        info.append("  Fecha devolución: ").append(prestamo.getFechaDevolucion()).append("\n");
        info.append("  Días de préstamo: ").append(prestamo.getDiasPrestamo()).append("\n");
        info.append("  Fecha actual: ").append(LocalDate.now()).append("\n\n");

        long diasRetraso = prestamo.calcularDiasRetraso();
        if (diasRetraso > 0) {
            info.append("⚠ ATENCIÓN: DEVOLUCIÓN ATRASADA ⚠\n");
            info.append("  Días de retraso: ").append(diasRetraso).append("\n");
            info.append("  Multa a pagar: $").append(prestamo.calcularMulta()).append("\n");
        } else {
            info.append("✓ Devolución a tiempo\n");
        }

        info.append("\n═══════════════════════════════════════");

        txtInfoPrestamo.setText(info.toString());
    }

    private void realizarDevolucion() {
        String run = txtRun.getText().trim();
        String isbn = txtIsbn.getText().trim();

        if (run.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }

        // Buscar préstamo para verificar si hay multa
        Prestamo prestamo = biblioteca.buscarPrestamo(run, isbn);
        if (prestamo != null && prestamo.calcularMulta() > 0) {
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "El usuario tiene una multa de $" + prestamo.calcularMulta() +
                            "\n¿Desea continuar con la devolución?",
                    "Multa por retraso",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }
        }

        String resultado = biblioteca.realizarDevolucion(run, isbn);

        if (resultado.contains("exitosamente")) {
            JOptionPane.showMessageDialog(this, resultado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtRun.setText("");
        txtIsbn.setText("");
        txtInfoPrestamo.setText("");
    }
}