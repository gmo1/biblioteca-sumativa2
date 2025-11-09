package biblioteca.gui;

import biblioteca.controlador.Biblioteca;
import biblioteca.modelo.Docente;
import biblioteca.modelo.Estudiante;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Usuario;

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

        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.GRAY);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 12));
        
        
        btnLimpiar.setBackground(new Color(46, 204, 113));
        btnLimpiar.setForeground(Color.GRAY);
        btnLimpiar.setFont(new Font("Arial", Font.BOLD, 12));
        
        
        btnVolver.setBackground(new Color(46, 204, 113));
        btnVolver.setForeground(Color.GRAY);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 12));
        
        
        btnEliminar.setBackground(new Color(46, 204, 113));
        btnEliminar.setForeground(Color.GRAY);
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 12));
        
        
        
        
        
        btnAgregar.addActionListener(e -> agregarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnVolver.addActionListener(e -> dispose());

        panel.add(btnAgregar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnVolver);
        
        
        
        final Color baseText = btnAgregar.getForeground();
        final Color hoverText = Color.black;
        final Color pressText = new Color(230,230,230);
        
        btnAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
        	@Override public void mouseEntered(java.awt.event.MouseEvent e) { btnAgregar.setForeground(hoverText); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { btnAgregar.setForeground(baseText);  }
            @Override public void mousePressed(java.awt.event.MouseEvent e) { btnAgregar.setForeground(pressText); }
            @Override public void mouseReleased(java.awt.event.MouseEvent e) {
            	if(btnAgregar.getBounds().contains(e.getPoint())) btnAgregar.setForeground(hoverText);
            	else btnAgregar.setForeground(baseText);
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
        		if(btnVolver.getBounds().contains(e.getPoint())) btnVolver.setForeground(hover2Text);
        		else btnVolver.setForeground(base2Text);
        	}
        });
        
        
        final Color base3Text = btnEliminar.getForeground();
        final Color hover3Text = Color.black;
        final Color press3Text = new Color(230,230,230);
        
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
        	@Override public void mouseEntered(java.awt.event.MouseEvent e) { btnEliminar.setForeground(hover3Text); }
        	@Override public void mouseExited (java.awt.event.MouseEvent e) { btnEliminar.setForeground(base3Text);  }
        	@Override public void mousePressed(java.awt.event.MouseEvent e) { btnEliminar.setForeground(press3Text); }
        	@Override public void mouseReleased(java.awt.event.MouseEvent e) {
        		if(btnEliminar.getBounds().contains(e.getPoint())) btnEliminar.setForeground(hover3Text);
        		else btnEliminar.setForeground(base3Text);
        	}
        });
        
        
        

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