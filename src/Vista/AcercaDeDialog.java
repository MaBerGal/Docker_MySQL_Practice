package Vista;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;

/**
 * La clase AcercaDeDialog representa un cuadro de diálogo modal que muestra información sobre la aplicación.
 */
public class AcercaDeDialog extends JDialog {

    /**
     * Constructor de la clase AcercaDeDialog.
     *
     * @param parent Ventana principal a la que está asociado el cuadro de diálogo.
     */
    public AcercaDeDialog(JFrame parent) {
        super(parent, "Acerca de", true); // Establece el título y modalidad del cuadro de diálogo.
        initComponents(); // Inicializa y configura los componentes del cuadro de diálogo.
    }

    /**
     * Inicializa y configura los componentes del cuadro de diálogo.
     */
    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Agrega un borde interno al panel.
        panel.setBackground(new Color(0, 0, 0)); // Establece el color de fondo del panel.

        JTextArea textArea = new JTextArea(
                "Autores: Manuel Bernal y Carlos Gowing\n" +
                        "Versión: 1.0.0\n" +
                        "Año:        2023\n"
        );
        textArea.setEditable(false);
        textArea.setBackground(panel.getBackground());
        textArea.setFont(new Font("Arial", Font.BOLD, 14)); // Configura la fuente y estilo del texto.
        textArea.setForeground(new Color(250, 250, 250)); // Establece el color del texto.

        JLabel iconLabel = new JLabel(new ImageIcon("acercadeicon.png")); // Crea un JLabel con un icono.
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centra el icono horizontalmente.

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(e -> dispose()); // Cierra el cuadro de diálogo al hacer clic en "Aceptar".

        // Agrega los componentes al panel en las distintas regiones.
        panel.add(iconLabel, BorderLayout.NORTH);
        panel.add(textArea, BorderLayout.CENTER);
        panel.add(btnAceptar, BorderLayout.SOUTH);

        // Configura el cuadro de diálogo.
        add(panel);
        pack(); // Ajusta automáticamente el tamaño del cuadro de diálogo según su contenido.
        setLocationRelativeTo(null); // Centra el cuadro de diálogo en la pantalla.
        setResizable(false); // Evita que el usuario redimensione el cuadro de diálogo.
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // Cierra el cuadro de diálogo al hacer clic en la "X".
    }
}
