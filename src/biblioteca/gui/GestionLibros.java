package biblioteca.gui;

import biblioteca.controlador.Biblioteca;
import biblioteca.modelo.Libro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Diálogo para gestionar libros (CRUD)
 */
public class GestionLibros extends JDialog {
    private Biblioteca biblioteca;
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    private JTextField txtIsbn, txtTitulo, txtAutor, txtCantBiblioteca, txtCantDisponible, txtImagen;

    public GestionLibros(JFrame padre, Biblioteca biblioteca) {
        super(padre, "Gestión de Libros", true);
        this.biblioteca = biblioteca;
        inicializarComponentes();
        cargarLibros();
    }

    private void inicializarComponentes() {
        setSize(900, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // Panel superior - Formulario
        JPanel panelFormulario = crearPanelFormulario();
        add(panelFormulario, BorderLayout.NORTH);

        // Panel central - Tabla
        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.CENTER);

        // Panel inferior - Botones
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Libro"));

        // ISBN
        panel.add(new JLabel("ISBN:"));
        txtIsbn = new JTextField();
        panel.add(txtIsbn);

        // Título
        panel.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        panel.add(txtTitulo);

        // Autor
        panel.add(new JLabel("Autor:"));
        txtAutor = new JTextField();
        panel.add(txtAutor);

        // Cantidad en biblioteca
        panel.add(new JLabel("Cantidad Total:"));
        txtCantBiblioteca = new JTextField();
        panel.add(txtCantBiblioteca);

        // Cantidad disponible
        panel.add(new JLabel("Cantidad Disponible:"));
        txtCantDisponible = new JTextField();
        panel.add(txtCantDisponible);

        // Imagen
        panel.add(new JLabel("Imagen (nombre):"));
        txtImagen = new JTextField();
        panel.add(txtImagen);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Libros Registrados"));

        String[] columnas = {"ISBN", "Título", "Autor", "Cant. Total", "Cant. Disponible", "Imagen"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnVolver = new JButton("Volver");

        btnAgregar.addActionListener(e -> agregarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnVolver.addActionListener(e -> dispose());

        panel.add(btnAgregar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnVolver);

        return panel;
    }

    private void agregarLibro() {
        try {
            String isbn = txtIsbn.getText().trim();
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String imagen = txtImagen.getText().trim();

            if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios");
                return;
            }

            int cantBiblioteca;
            int cantDisponible;

            try {
                cantBiblioteca = Integer.parseInt(txtCantBiblioteca.getText().trim());
                cantDisponible = Integer.parseInt(txtCantDisponible.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Las cantidades deben ser números");
                return;
            }

            Libro libro = new Libro(isbn, titulo, autor, cantBiblioteca, cantDisponible, imagen);
            String resultado = biblioteca.agregarLibro(libro);
            JOptionPane.showMessageDialog(this, resultado);

            if (resultado.contains("exitosamente")) {
                cargarLibros();
                limpiarCampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void eliminarLibro() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro de la tabla");
            return;
        }

        String isbn = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este libro?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            String resultado = biblioteca.eliminarLibro(isbn);
            JOptionPane.showMessageDialog(this, resultado);

            if (resultado.contains("exitosamente")) {
                cargarLibros();
                limpiarCampos();
            }
        }
    }

    private void cargarLibros() {
        modeloTabla.setRowCount(0);
        for (Libro l : biblioteca.getLibros()) {
            Object[] fila = {
                    l.getIsbn(),
                    l.getTitulo(),
                    l.getAutor(),
                    l.getCantidadBiblioteca(),
                    l.getCantidadDisponible(),
                    l.getImagen()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void limpiarCampos() {
        txtIsbn.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtCantBiblioteca.setText("");
        txtCantDisponible.setText("");
        txtImagen.setText("");
    }
}