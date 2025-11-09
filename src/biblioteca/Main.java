package biblioteca;

import biblioteca.gui.MenuPrincipal;
import javax.swing.*;

/**
 * clase principal del Sistema de Biblioteca
 * aqui ejecutamos la app
 */
public class Main {
    public static void main(String[] args) {
        // Configurar Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
        }

        // Ejecutar la interfaz gráfica en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MenuPrincipal ventanaPrincipal = new MenuPrincipal();
            ventanaPrincipal.setVisible(true);
        });
    }
}
