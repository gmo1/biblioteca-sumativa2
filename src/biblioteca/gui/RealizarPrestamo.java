package biblioteca.gui;

import biblioteca.controlador.Biblioteca;
import biblioteca.modelo.*;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo para realizar préstamos de libros
 */
public class RealizarPrestamo extends JDialog {
    private Biblioteca biblioteca;
    private JTextField txtRun, txtIsbn;
    private JTextArea txtInfoUsuario, txtInfoLibro;

    public RealizarPrestamo(JFrame padre, Biblioteca biblioteca) {
        super(padre, "Realizar Préstamo", true);
        this.biblioteca = biblioteca;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setSize(700, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // Panel principal
        JPanel panelPrincipal = new JPanel(new GridLayout(3, 1, 10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel datos de entrada
        JPanel panelEntrada = crearPanelEntrada();
        panelPrincipal.add(panelEntrada);

        // Panel información usuario
        JPanel panelUsuario = crearPanelInfo("Información del Usuario", true);
        panelPrincipal.add(panelUsuario);

        // Panel información libro
        JPanel panelLibro = crearPanelInfo("Información del Libro", false);
        panelPrincipal.add(panelLibro);

        add(panelPrincipal, BorderLayout.CENTER);

        // Panel botones
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearPanelEntrada() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Préstamo"));

        panel.add(new JLabel("RUN Usuario: (Con puntos y guion)"));
        txtRun = new JTextField();
        panel.add(txtRun);

        JButton btnBuscarUsuario = new JButton("Buscar");
        btnBuscarUsuario.addActionListener(e -> buscarUsuario());
        panel.add(btnBuscarUsuario);

        panel.add(new JLabel("ISBN Libro:"));
        txtIsbn = new JTextField();
        panel.add(txtIsbn);

        JButton btnBuscarLibro = new JButton("Buscar");
        btnBuscarLibro.addActionListener(e -> buscarLibro());
        panel.add(btnBuscarLibro);

        return panel;
    }

    private JPanel crearPanelInfo(String titulo, boolean esUsuario) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(titulo));

        JTextArea txtArea = new JTextArea(5, 40);
        txtArea.setEditable(false);
        txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtArea.setBackground(new Color(245, 245, 245));

        if (esUsuario) {
            txtInfoUsuario = txtArea;
        } else {
            txtInfoLibro = txtArea;
        }

        JScrollPane scrollPane = new JScrollPane(txtArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnRealizarPrestamo = new JButton("Realizar Préstamo");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnVolver = new JButton("Volver");

        //boton de realizar prestamo
        btnRealizarPrestamo.setBackground(new Color(241, 196, 15));
        btnRealizarPrestamo.setForeground(Color.GRAY);
        btnRealizarPrestamo.setFont(new Font("Arial", Font.BOLD, 12));
        
        //boton de limpiar
        btnLimpiar.setBackground(new Color(241, 196, 15));
        btnLimpiar.setForeground(Color.GRAY);
        btnLimpiar.setFont(new Font("Arial", Font.BOLD, 12));
        
        //boton de volver
        btnVolver.setBackground(new Color(241, 196, 15));
        btnVolver.setForeground(Color.GRAY);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 12));

        btnRealizarPrestamo.addActionListener(e -> realizarPrestamo());
        btnLimpiar.addActionListener(e -> limpiar());
        btnVolver.addActionListener(e -> dispose());

        panel.add(btnRealizarPrestamo);
        panel.add(btnLimpiar);
        panel.add(btnVolver);
        
        
        
        final Color baseText = btnRealizarPrestamo.getForeground();
        final Color hoverText = Color.black;
        final Color pressText = new Color(230,230,230);
        
        btnRealizarPrestamo.addMouseListener(new java.awt.event.MouseAdapter() {
        	@Override public void mouseEntered(java.awt.event.MouseEvent e) { btnRealizarPrestamo.setForeground(hoverText); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { btnRealizarPrestamo.setForeground(baseText);  }
            @Override public void mousePressed(java.awt.event.MouseEvent e) { btnRealizarPrestamo.setForeground(pressText); }
            @Override public void mouseReleased(java.awt.event.MouseEvent e) {
            	if(btnRealizarPrestamo.getBounds().contains(e.getPoint())) btnRealizarPrestamo.setForeground(hoverText);
            	else btnRealizarPrestamo.setForeground(baseText);
            }
        });
        final Color base1Text = btnLimpiar.getForeground();
        final Color hover1Text = Color.black;
        final Color press1Text = new Color(230,230,230);
        
        btnLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
        	@Override public void mouseEntered(java.awt.event.MouseEvent e) { btnLimpiar.setForeground(hover1Text); }
        	@Override public void mouseExited (java.awt.event.MouseEvent e) { btnLimpiar.setForeground(base1Text);  }
        	@Override public void mousePressed(java.awt.event.MouseEvent e) { btnLimpiar.setForeground(press1Text); }
        	@Override public void mouseReleased(java.awt.event.MouseEvent e) {
        		if(btnLimpiar.getBounds().contains(e.getPoint())) btnLimpiar.setForeground(hover1Text);
        		else btnLimpiar.setForeground(base1Text);
        	}
        });
        final Color base2Text = btnVolver.getForeground();
        final Color hover2Text = Color.black;
        final Color press2Text = new Color(230,230,230);
        
        btnVolver.addMouseListener(new java.awt.event.MouseAdapter() {
        	@Override public void mouseEntered(java.awt.event.MouseEvent e) { btnVolver.setForeground(hover2Text); }
        	@Override public void mouseExited (java.awt.event.MouseEvent e) { btnVolver.setForeground(base2Text);  }
        	@Override public void mousePressed(java.awt.event.MouseEvent e) { btnVolver.setForeground(press2Text); }
        	@Override public void mouseReleased(java.awt.event.MouseEvent e) {
        		if(btnVolver.getBounds().contains(e.getPoint())) btnVolver.setForeground(hover1Text);
        		else btnVolver.setForeground(base1Text);
        	}
        });

        return panel;
    }

    private void buscarUsuario() {
        String run = txtRun.getText().trim();
        if (run.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un RUN");
            return;
        }

        Usuario usuario = biblioteca.buscarUsuario(run);
        if (usuario == null) {
            txtInfoUsuario.setText("Usuario no encontrado");
        } else {
            StringBuilder info = new StringBuilder();
            info.append("Nombre: ").append(usuario.getNombreCompleto()).append("\n");
            info.append("RUN: ").append(usuario.getRun()).append("\n");
            info.append("Tipo: ").append(usuario.getTipo()).append("\n");
            info.append("Género: ").append(usuario.getGenero()).append("\n");
            info.append(usuario.getInfoAdicional()).append("\n");
            info.append("Préstamo activo: ").append(usuario.tienePrestamo() ? "SÍ" : "NO").append("\n");

            if (usuario instanceof Estudiante) {
                info.append("Días de préstamo permitidos: ").append(((Estudiante) usuario).getDiasPrestamo());
            } else if (usuario instanceof Docente) {
                info.append("Días de préstamo permitidos: ").append(((Docente) usuario).getDiasPrestamo());
            }

            txtInfoUsuario.setText(info.toString());
        }
    }

    private void buscarLibro() {
        String isbn = txtIsbn.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ISBN");
            return;
        }

        Libro libro = biblioteca.buscarLibro(isbn);
        if (libro == null) {
            txtInfoLibro.setText("Libro no encontrado");
        } else {
            StringBuilder info = new StringBuilder();
            info.append("ISBN: ").append(libro.getIsbn()).append("\n");
            info.append("Título: ").append(libro.getTitulo()).append("\n");
            info.append("Autor: ").append(libro.getAutor()).append("\n");
            info.append("Cantidad total: ").append(libro.getCantidadBiblioteca()).append("\n");
            info.append("Cantidad disponible: ").append(libro.getCantidadDisponible()).append("\n");
            info.append("Estado: ").append(libro.tieneDisponibilidad() ? "DISPONIBLE" : "NO DISPONIBLE");

            txtInfoLibro.setText(info.toString());
        }
    }

    private void realizarPrestamo() {
        String run = txtRun.getText().trim();
        String isbn = txtIsbn.getText().trim();

        if (run.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }

        String resultado = biblioteca.realizarPrestamo(run, isbn);

        if (resultado.contains("exitosamente")) {
            // Mostrar tarjeta de préstamo
            String tarjeta = biblioteca.generarTarjetaPrestamo(run, isbn);

            JTextArea txtTarjeta = new JTextArea(tarjeta);
            txtTarjeta.setEditable(false);
            txtTarjeta.setFont(new Font("Monospaced", Font.PLAIN, 11));

            JScrollPane scrollPane = new JScrollPane(txtTarjeta);
            scrollPane.setPreferredSize(new Dimension(450, 400));

            JOptionPane.showMessageDialog(
                    this,
                    scrollPane,
                    "Tarjeta de Préstamo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiar();
        } else {
            JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtRun.setText("");
        txtIsbn.setText("");
        txtInfoUsuario.setText("");
        txtInfoLibro.setText("");
    }
}