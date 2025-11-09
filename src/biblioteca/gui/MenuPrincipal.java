package biblioteca.gui;

import biblioteca.controlador.Biblioteca;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del sistema (Home)
 */
public class MenuPrincipal extends JFrame {
    private Biblioteca biblioteca;

    public MenuPrincipal() {
        biblioteca = new Biblioteca();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Sistema de Biblioteca UNAB - Antonio Varas");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior con título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(0, 51, 102));
        JLabel lblTitulo = new JLabel("SISTEMA DE BIBLIOTECA - UNAB");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel central con botones
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(5, 1, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Botones del menú
        JButton btnUsuarios = crearBoton("Gestión de Usuarios", new Color(52, 152, 219));
        JButton btnLibros = crearBoton("Gestión de Libros", new Color(46, 204, 113));
        JButton btnPrestamo = crearBoton("Realizar Préstamo", new Color(241, 196, 15));
        JButton btnDevolucion = crearBoton("Realizar Devolución", new Color(230, 126, 34));
        JButton btnSalir = crearBoton("Salir", new Color(231, 76, 60));

        panelCentral.add(btnUsuarios);
        panelCentral.add(btnLibros);
        panelCentral.add(btnPrestamo);
        panelCentral.add(btnDevolucion);
        panelCentral.add(btnSalir);

        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con información
        JPanel panelInferior = new JPanel();
        JLabel lblInfo = new JLabel("Universidad Nacional Andrés Bello - Sede Antonio Varas");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        panelInferior.add(lblInfo);
        add(panelInferior, BorderLayout.SOUTH);

        // Listeners
        btnUsuarios.addActionListener(e -> abrirGestionUsuarios());
        btnLibros.addActionListener(e -> abrirGestionLibros());
        btnPrestamo.addActionListener(e -> abrirRealizarPrestamo());
        btnDevolucion.addActionListener(e -> abrirRealizarDevolucion());
        btnSalir.addActionListener(e -> salir());
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createRaisedBevelBorder());
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private void abrirGestionUsuarios() {
        GestionUsuarios dialog = new GestionUsuarios(this, biblioteca);
        dialog.setVisible(true);
    }

    private void abrirGestionLibros() {
        GestionLibros dialog = new GestionLibros(this, biblioteca);
        dialog.setVisible(true);
    }

    private void abrirRealizarPrestamo() {
        RealizarPrestamo dialog = new RealizarPrestamo(this, biblioteca);
        dialog.setVisible(true);
    }

    private void abrirRealizarDevolucion() {
        RealizarDevolucion dialog = new RealizarDevolucion(this, biblioteca);
        dialog.setVisible(true);
    }

    private void salir() {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea salir del sistema?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}