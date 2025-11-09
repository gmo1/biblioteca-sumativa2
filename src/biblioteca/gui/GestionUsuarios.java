package biblioteca.gui;

import biblioteca.controlador.Biblioteca;
import biblioteca.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Diálogo para gestionar usuarios (CRUD)
 */
public class GestionUsuarios extends JDialog {
    private Biblioteca biblioteca;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    private JTextField txtNombre, txtRun, txtCarrera, txtProfesion, txtGrados;
    private JComboBox<String> cmbGenero, cmbTipo;

    public GestionUsuarios(JFrame padre, Biblioteca biblioteca) {
        super(padre, "Gestión de Usuarios UNAB", true);
        this.biblioteca = biblioteca;
        inicializarComponentes();
        cargarUsuarios();
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
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));

        // Tipo de usuario
        panel.add(new JLabel("Tipo:"));
        cmbTipo = new JComboBox<>(new String[]{"Estudiante", "Docente"});
        cmbTipo.addActionListener(e -> actualizarCampos());
        panel.add(cmbTipo);

        // Nombre
        panel.add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        // RUN
        panel.add(new JLabel("RUN: (Con puntos y guion)"));
        txtRun = new JTextField();
        panel.add(txtRun);

        // Género
        panel.add(new JLabel("Género: (Masculino/Femenino)"));
        cmbGenero = new JComboBox<>(new String[]{"M", "F"});
        panel.add(cmbGenero);

        // Carrera (Estudiante)
        panel.add(new JLabel("Carrera:"));
        txtCarrera = new JTextField();
        panel.add(txtCarrera);

        // Profesión (Docente)
        panel.add(new JLabel("Profesión:"));
        txtProfesion = new JTextField();
        txtProfesion.setEnabled(false);
        panel.add(txtProfesion);

        // Grados (Docente)
        panel.add(new JLabel("Grados: (Magister/Doctorado/No)"));
        txtGrados = new JTextField();
        txtGrados.setEnabled(false);
        panel.add(txtGrados);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Usuarios Registrados"));

        String[] columnas = {"Tipo", "Nombre", "RUN", "Género", "Info Adicional", "Préstamo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnVolver = new JButton("Volver");

        btnAgregar.addActionListener(e -> agregarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnVolver.addActionListener(e -> dispose());

        panel.add(btnAgregar);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnVolver);

        return panel;
    }

    private void actualizarCampos() {
        boolean esEstudiante = cmbTipo.getSelectedItem().equals("Estudiante");
        txtCarrera.setEnabled(esEstudiante);
        txtProfesion.setEnabled(!esEstudiante);
        txtGrados.setEnabled(!esEstudiante);
    }

    private void agregarUsuario() {
        try {
            String nombre = txtNombre.getText().trim();
            String run = txtRun.getText().trim();
            String genero = (String) cmbGenero.getSelectedItem();

            if (nombre.isEmpty() || run.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios");
                return;
            }

            Usuario usuario;
            if (cmbTipo.getSelectedItem().equals("Estudiante")) {
                String carrera = txtCarrera.getText().trim();
                if (carrera.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ingrese la carrera");
                    return;
                }
                usuario = new Estudiante(nombre, run, genero, carrera);
            } else {
                String profesion = txtProfesion.getText().trim();
                String grados = txtGrados.getText().trim();
                if (profesion.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ingrese la profesión");
                    return;
                }
                usuario = new Docente(nombre, run, genero, profesion, grados);
            }

            String resultado = biblioteca.agregarUsuario(usuario);
            JOptionPane.showMessageDialog(this, resultado);

            if (resultado.contains("exitosamente")) {
                cargarUsuarios();
                limpiarCampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void editarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla");
            return;
        }

        String runOriginal = (String) modeloTabla.getValueAt(filaSeleccionada, 2);

        try {
            String nombre = txtNombre.getText().trim();
            String run = txtRun.getText().trim();
            String genero = (String) cmbGenero.getSelectedItem();

            if (nombre.isEmpty() || run.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios");
                return;
            }

            Usuario usuario;
            if (cmbTipo.getSelectedItem().equals("Estudiante")) {
                String carrera = txtCarrera.getText().trim();
                usuario = new Estudiante(nombre, run, genero, carrera);
            } else {
                String profesion = txtProfesion.getText().trim();
                String grados = txtGrados.getText().trim();
                usuario = new Docente(nombre, run, genero, profesion, grados);
            }

            String resultado = biblioteca.editarUsuario(runOriginal, usuario);
            JOptionPane.showMessageDialog(this, resultado);

            if (resultado.contains("exitosamente")) {
                cargarUsuarios();
                limpiarCampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla");
            return;
        }

        String run = (String) modeloTabla.getValueAt(filaSeleccionada, 2);

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar este usuario?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            String resultado = biblioteca.eliminarUsuario(run);
            JOptionPane.showMessageDialog(this, resultado);

            if (resultado.contains("exitosamente")) {
                cargarUsuarios();
                limpiarCampos();
            }
        }
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        for (Usuario u : biblioteca.getUsuarios()) {
            Object[] fila = {
                    u.getTipo(),
                    u.getNombreCompleto(),
                    u.getRun(),
                    u.getGenero(),
                    u.getInfoAdicional(),
                    u.tienePrestamo() ? u.getPrestamo() : "No"
            };
            modeloTabla.addRow(fila);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtRun.setText("");
        txtCarrera.setText("");
        txtProfesion.setText("");
        txtGrados.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbGenero.setSelectedIndex(0);
        actualizarCampos();
    }
}