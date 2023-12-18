package Vista;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * La clase InicioPanel extiende JPanel y proporciona funcionalidad para mostrar una imagen de fondo en un panel.
 */
public class InicioPanel extends JPanel {

    /**
     * Imagen de fondo del panel.
     */
    private Image imagenFondo;

    /**
     * Constructor de la clase InicioPanel.
     *
     * @param rutaImagen Ruta de la imagen de fondo.
     */
    public InicioPanel(String rutaImagen) {
        cargarImagenFondo(rutaImagen);
    }

    /**
     * Carga la imagen de fondo desde la ruta proporcionada.
     *
     * @param rutaImagen Ruta de la imagen de fondo.
     */
    private void cargarImagenFondo(String rutaImagen) {
        // Cargar la imagen de fondo desde la ruta proporcionada.
        ImageIcon imageIcon = new ImageIcon(rutaImagen);
        imagenFondo = imageIcon.getImage();
    }

    /**
     * Cambia la imagen de fondo del panel.
     *
     * @param nuevaRutaImagen Nueva ruta de la imagen de fondo.
     */
    public void cambiarImagenFondo(String nuevaRutaImagen) {
        // Cambiar la imagen de fondo.
        cargarImagenFondo(nuevaRutaImagen);
        repaint();  // Repintar el panel para reflejar el cambio.
    }

    /**
     * Sobrescribe el método paintComponent para dibujar la imagen de fondo en el panel.
     *
     * @param g Objeto Graphics utilizado para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar la imagen de fondo.
        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
    }
}
