
package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

import Controlador.*;
import Modelo.*;

/**
 * La clase GUI representa la interfaz gráfica principal de la aplicación.
 */
public class GUI extends JFrame {
    /**
     * Barra de menú que contiene los menús de la interfaz.
     */
    private JMenuBar menuBar;

    /**
     * Menú de validación en la barra de menú.
     */
    private JMenu menuValidar;

    /**
     * Menú de visualización en la barra de menú.
     */
    private JMenu menuVisualizar;

    /**
     * Menú "Acerca de" en la barra de menú.
     */
    private JMenu menuAcercaDe;

    /**
     * Elemento de menú para la acción "Entrar".
     */
    private JMenuItem itemEntrar;

    /**
     * Elemento de menú para la acción "Salir".
     */
    private JMenuItem itemSalir;

    /**
     * Elemento de menú para la acción "Detalle".
     */
    private JMenuItem itemDetalle;

    /**
     * Elemento de menú para la acción "Resumen".
     */
    private JMenuItem itemResumen;

    /**
     * Elemento de menú para la acción "Acerca de".
     */
    private JMenuItem itemAcercaDe;

    /**
     * Objeto Connection que representa la conexión a la base de datos.
     */
    private Connection conexion;

    /**
     * Variable para almacenar el cliente validado.
     */
    private Cliente clienteActual;

    /**
     * Variable para mantener la referencia al panel de detalle.
     */
    private DetallePanel detallePanel;

    /**
     * Variable para mantener la referencia al panel de resumen.
     */
    private ResumenPanel resumenPanel;

    /**
     * Constructor de la interfaz gráfica.
     *
     * @param conexion Objeto Connection que representa la conexión a la base de datos.
     */
    public GUI(Connection conexion) {
        this.conexion = conexion;
        initGUI(); // Inicializa y configura la interfaz gráfica.

        // Establecer el icono de la ventana.
        ImageIcon icono = new ImageIcon("icono.png");
        setIconImage(icono.getImage());

        // Cerrar conexión al cerrar GUI.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                super.windowClosing(e);
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                    }
                } catch (SQLException fin) {
                    fin.printStackTrace();
                }
            }
        });
    }

    /**
     * Inicializa y configura la interfaz gráfica de usuario (GUI).
     * Configura la apariencia, la barra de menú, y maneja las acciones de los elementos de menú.
     */
    private void initGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configuración de la operación de cierre al hacer clic en la "X".
        setTitle("Sistema de Gestión de Clientes"); // Establece el título de la ventana.
        setSize(800, 600); // Establece el tamaño predeterminado de la ventana.

        // Crea un panel de fondo con la imagen home.png
        InicioPanel fondoPanel = new InicioPanel("home.png");

        // Configuración del menú
        menuBar = new JMenuBar();
        menuValidar = new JMenu("Validar");
        menuVisualizar = new JMenu("Visualizar");
        menuAcercaDe = new JMenu("Acerca de");

        // Items de menú
        itemEntrar = new JMenuItem("Entrar", new ImageIcon("entrar.png"));
        itemSalir = new JMenuItem("Salir",  new ImageIcon("salir.png"));
        itemDetalle = new JMenuItem("Detalle", new ImageIcon("detalle.png"));
        itemResumen = new JMenuItem("Resumen", new ImageIcon("resumen.png"));
        itemAcercaDe = new JMenuItem("Acerca de",  new ImageIcon("acercade.png"));

        // Acciones de los items de menú.
        // Al hacer click en "Entrar".
        itemEntrar.addActionListener(e -> {
            // Dialog para realizar la validación del usuario.
            String usuario = JOptionPane.showInputDialog("Introduce el nombre de usuario:");

            // Comprobar si el usuario ha clickado "Cancelar" o no ha escrito nada antes de pulsar enter.
            if (usuario == null || usuario.trim().isEmpty()) {
                return;  // Salir del dialog.
            }

            String pass = JOptionPane.showInputDialog("Introduce la contraseña:");

            if (pass == null || pass.trim().isEmpty()) {
                return;
            }

            AutentificacionCliente autentificacionCliente = new AutentificacionCliente(conexion);
            Cliente clienteValidado = autentificacionCliente.validarUsuario(usuario, pass);

            if (clienteValidado != null) {
                // Si la validación es exitosa, habilitar otros menús.
                menuVisualizar.setEnabled(true);
                itemSalir.setEnabled(true);  // Activar el item "Salir".
                itemEntrar.setEnabled(false);  // Desactivar el item "Entrar".
                clienteActual = clienteValidado; // Almacena el cliente validado en la variable clienteActual.
                fondoPanel.cambiarImagenFondo("home2.png");
                System.out.println(clienteActual);
                System.out.println(clienteValidado);
            } else {
                System.out.println(clienteActual);
                System.out.println(clienteValidado);
                // Mostrar un mensaje de error si la validación falla.
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error de validación", JOptionPane.ERROR_MESSAGE);

                // Si la validación falla, desactivar el menú visualizar.
                menuVisualizar.setEnabled(false);
            }
        });

        // Al hacer clic en "Salir".
        itemSalir.addActionListener(e -> {
            // Reactivar el item "Entrar" cuando se hace clic en "Salir".
            itemEntrar.setEnabled(true);
            itemSalir.setEnabled(false);  // Desactivar el item "Salir".

            // Desactivar la opción "Visualizar".
            menuVisualizar.setEnabled(false);

            // Si estuviesen visibles los otros paneles, esconderlos.
            if (detallePanel != null) {
                detallePanel.setVisible(false);
            }

            if (resumenPanel != null) {
                resumenPanel.setVisible(false);
            }

            // Cambiar la imagen de fondo.
            fondoPanel.cambiarImagenFondo("home.png");
        });

        // Al hacer click en "Detalle".
        itemDetalle.addActionListener(e -> {
            // Intentar mostrar panel de detalle.
            try {
                mostrarPanelDetalle();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Al hacer click en "Resumen".
        itemResumen.addActionListener(e -> {
            // Mostrar panel de resumen.
            mostrarPanelResumen();
        });

        // Al hacer click en "Acerca de"
        itemAcercaDe.addActionListener(e -> {
            // Se crea una instancia de un dialog modal personalizado y se hace visible.
            AcercaDeDialog acercaDeDialog = new AcercaDeDialog(GUI.this);
            acercaDeDialog.setVisible(true);
        });


        // Agrega elementos al menú.
        menuValidar.add(itemEntrar);
        menuValidar.add(itemSalir);
        menuVisualizar.add(itemDetalle);
        menuVisualizar.add(itemResumen);
        menuAcercaDe.add(itemAcercaDe);

        // Deshabilita menús hasta que se valide el usuario.
        menuVisualizar.setEnabled(false);
        itemSalir.setEnabled(false);

        // Agrega menús a la barra de menú
        menuBar.add(menuValidar);
        menuBar.add(menuVisualizar);
        menuBar.add(menuAcercaDe);

        setJMenuBar(menuBar);

        setContentPane(fondoPanel); // Establece el panel de fondo como contenido principal.
        setVisible(true); // Hace visible la ventana.
    }

    /**
     * Muestra el panel de detalle del cliente en la interfaz gráfica.
     * @throws SQLException Excepción lanzada en caso de problemas con la conexión a la base de datos.
     */
    private void mostrarPanelDetalle() throws SQLException {
        // Verifica si el clienteActual está validado.
        if (clienteActual != null) {
            // Crea un nuevo DetallePanel con la conexión y el clienteActual.
            detallePanel = new DetallePanel(conexion, clienteActual);

            // Establece el layout del JFrame como BorderLayout.
            getContentPane().setLayout(new BorderLayout());

            // Quita cualquier componente que esté en el área interior del JFrame.
            getContentPane().removeAll();

            // Agrega el DetallePanel al interior del JFrame.
            getContentPane().add(detallePanel, BorderLayout.CENTER);

            // Revalida y repinta el JFrame para reflejar los cambios.
            revalidate();
            repaint();
        } else {
            // Muestra un mensaje de error si el clienteActual no está validado (por si acaso).
            JOptionPane.showMessageDialog(this, "Por favor, valida un usuario primero", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra el panel de resumen del cliente en la interfaz gráfica.
     */
    private void mostrarPanelResumen() {
        // Verifica si el clienteActual está validado.
        if (clienteActual != null) {
            // Crea un nuevo ResumenPanel.
            resumenPanel = new ResumenPanel(conexion, clienteActual);

            // Establece el layout del JFrame como BorderLayout.
            getContentPane().setLayout(new BorderLayout());

            // Remueve cualquier componente que esté en el interior del JFrame.
            getContentPane().removeAll();

            // Agrega el ResumenPanel al interior del JFrame.
            getContentPane().add(resumenPanel, BorderLayout.CENTER);

            // Revalida y repinta el JFrame para reflejar los cambios.
            revalidate();
            repaint();
        } else {
            // Muestra un mensaje de error si el clienteActual no está validado (por si acaso).
            JOptionPane.showMessageDialog(this, "Por favor, valida un usuario primero", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
