package Vista;

import Controlador.Resumen;
import Modelo.Cliente;
import Modelo.Cuenta;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * Panel que muestra un resumen de información para un cliente, incluyendo sus cuentas asociadas.
 */
public class ResumenPanel extends JPanel {

    /** Conexión a la base de datos. */
    private Connection conexion;

    /** Cliente actual para el cual se muestra el resumen. */
    private Cliente clienteActual;

    /** Tabla que muestra la información de las cuentas asociadas al cliente. */
    private JTable jTable;

    /** Botón para realizar cálculos o acciones relacionadas con el resumen. */
    private JButton btnCalcular;

    /** Etiqueta para mostrar el ID del cliente. */
    private JLabel lblClienteId;

    /** Etiqueta para mostrar el nombre de usuario del cliente. */
    private JLabel lblUsuario;

    /** Etiqueta para mostrar el saldo total del cliente. */
    private JLabel lblSaldoTotal;

    /** Valor del ID del cliente. */
    private JLabel lblClienteIdValor;

    /** Valor del nombre de usuario del cliente. */
    private JLabel lblUsuarioValor;

    /** Valor del saldo total del cliente. */
    private JLabel lblSaldoTotalValor;

    /** Imagen asociada al cliente. */
    private JLabel lblImagenCliente;

    /**
     * Constructor de la clase ResumenPanel.
     *
     * @param conexion       Conexión a la base de datos.
     * @param clienteActual  Cliente para el cual se muestra el resumen.
     */
    public ResumenPanel(Connection conexion, Cliente clienteActual) {
        this.conexion = conexion;
        this.clienteActual = clienteActual;

        // Inicializar la lista de cuentas asociadas al clienteActual.
        cargarCuentasDelCliente();

        initUI();
    }

    /**
     * Carga las cuentas asociadas al cliente desde la base de datos y las asigna al cliente actual.
     */
    private void cargarCuentasDelCliente() {
        try {
            // Utiliza la clase Resumen para obtener las cuentas desde la base de datos.
            Resumen resumen = new Resumen(conexion, clienteActual.getCliId());
            List<Cuenta> cuentas = resumen.obtenerCuentas();
            clienteActual.setCuentas(cuentas);
            resumen.cerrar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicializa la interfaz de usuario del panel de resumen del cliente.
     */
    private void initUI() {
        setLayout(new BorderLayout());

        // Cargar imagen de fondo.
        ImageIcon imageIconResumen = new ImageIcon("bgvisualizar.png");
        Image imagenFondoResumen = imageIconResumen.getImage();

        // Crear un panel para mostrar la información del cliente actual con fondo.
        JPanel panelResumen = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibujar la imagen de fondo.
                g.drawImage(imagenFondoResumen, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Restricciones para colocar cada elemento en su sitio.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Crear etiquetas para mostrar la información del cliente.
        lblClienteId = new JLabel("ID de Cliente:");
        lblUsuario = new JLabel("Usuario:");
        lblSaldoTotal = new JLabel("Saldo Total:");

        // Establecer fuente y color de las etiquetas.
        lblClienteId.setFont(new Font("Arial", Font.BOLD, 16));
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 16));
        lblSaldoTotal.setFont(new Font("Arial", Font.BOLD, 16));

        lblClienteId.setForeground(Color.WHITE);
        lblUsuario.setForeground(Color.WHITE);
        lblSaldoTotal.setForeground(Color.WHITE);

        // Crear una etiqueta para mostrar la imagen del cliente.
        lblImagenCliente = new JLabel();
        lblImagenCliente.setPreferredSize(new Dimension(100, 100));
        lblImagenCliente.setIcon(cargarImagenCliente());
        lblImagenCliente.revalidate();
        lblImagenCliente.repaint();

        gbc.gridx = 2; // Colocar en la columna 2.
        gbc.gridy = 0; // Colocar en la fila 0.
        gbc.gridheight = 3; // Ocupar 3 filas.
        gbc.fill = GridBagConstraints.VERTICAL; // Permitir que la etiqueta ocupe el espacio vertical disponible.
        gbc.anchor = GridBagConstraints.EAST;
        panelResumen.add(lblImagenCliente, gbc);

        panelResumen.add(lblImagenCliente, gbc);

        // Crear etiquetas para mostrar los valores de la información del cliente.
        lblClienteIdValor = new JLabel(Integer.toString(clienteActual.getCliId()));
        lblUsuarioValor = new JLabel(clienteActual.getUsuario());
        lblSaldoTotalValor = new JLabel(Float.toString(clienteActual.getSaldoTotal()));

        // Establecer fuente y color de las etiquetas de valores.
        lblClienteIdValor.setFont(new Font("Arial", Font.BOLD, 16));
        lblUsuarioValor.setFont(new Font("Arial", Font.BOLD, 16));
        lblSaldoTotalValor.setFont(new Font("Arial", Font.BOLD, 16));

        lblClienteIdValor.setForeground(Color.BLACK);
        lblUsuarioValor.setForeground(Color.BLACK);
        lblSaldoTotalValor.setForeground(Color.BLACK);

        // Agregar etiquetas y campos de texto para mostrar la información del cliente al panel.
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
        panelResumen.add(lblClienteId, gbc);

        gbc.gridx = 1;
        panelResumen.add(lblClienteIdValor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelResumen.add(lblUsuario, gbc);

        gbc.gridx = 1;
        panelResumen.add(lblUsuarioValor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelResumen.add(lblSaldoTotal, gbc);

        gbc.gridx = 1;
        panelResumen.add(lblSaldoTotalValor, gbc);

        // Crear una JTable para mostrar los campos descriptivos de la segunda tabla (cuentas).
        jTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(jTable);

        // Usar restricciones separadas para JTable.
        GridBagConstraints tableConstraints = new GridBagConstraints();
        tableConstraints.gridx = 0;
        tableConstraints.gridy = 3;
        tableConstraints.gridwidth = 3;
        tableConstraints.fill = GridBagConstraints.BOTH;  // Permitir que la tabla llene el espacio disponible.
        tableConstraints.weightx = 1.0;  // Hacer que la tabla se estire horizontalmente.
        tableConstraints.weighty = 1.0;  // Hacer que la tabla se estire verticalmente.
        panelResumen.add(scrollPane, tableConstraints);

        // Crear un botón "Calcular".
        btnCalcular = new JButton("Calcular Saldo Total");
        btnCalcular.addActionListener(e -> {
            // Lógica para calcular y mostrar el resultado en una ventana emergente.
            calcularMostrarResultado();
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;  // Permitir que el botón llene el espacio horizontal y vertical disponibles.
        panelResumen.add(btnCalcular, gbc);

        // Agregar el panel de resumen al panel principal (this).
        add(panelResumen);

        // Cargar datos en la tabla.
        cargarDatosEnTabla();
    }


    private ImageIcon cargarImagenCliente() {
        // Verificar si la ruta de la imagen del cliente no es nula.
        String rutaImagen = clienteActual.getImagen();
        ImageIcon imagenCliente;

        if (rutaImagen != null && rutaImagen.length() > 0) {
            try {
                // Intentar cargar la imagen del cliente.
                imagenCliente = new ImageIcon(ImageIO.read(new File(rutaImagen)));
            } catch (IOException e) {
                e.printStackTrace();
                // Si hay un error al cargar la imagen, cargar la imagen predeterminada.
                imagenCliente = cargarImagenPredeterminada();
            }
        } else {
            // Si la ruta de la imagen es nula o tiene longitud cero, cargar la imagen predeterminada.
            imagenCliente = cargarImagenPredeterminada();
        }

        // Establecer la imagen en el JLabel.
        lblImagenCliente.setIcon(imagenCliente);
        return imagenCliente;
    }


    private ImageIcon cargarImagenPredeterminada() {
        try {
            // Cargar la imagen predeterminada.
            return new ImageIcon(ImageIO.read(new File("fotos/default.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar el error de carga de la imagen predeterminada.
            return null;
        }
    }

    /**
     * Carga los datos de las cuentas del cliente en la tabla.
     */
    private void cargarDatosEnTabla() {
        try {
            // Construir el modelo de tabla para las cuentas.
            Vector<String> columnNames = new Vector<>();
            columnNames.add("Número de Cuenta");
            columnNames.add("Saldo");
            Vector<Vector<Object>> data = new Vector<>();

            // Obtener la lista de cuentas asociadas al cliente actual.
            List<Cuenta> cuentas = clienteActual.getCuentas();

            // Agregar las cuentas al modelo de datos.
            for (Cuenta cuenta : cuentas) {
                Vector<Object> row = new Vector<>();
                row.add(cuenta.getCueId());
                row.add(cuenta.getSaldo());
                data.add(row);
            }

            JTableNoEditable model = new JTableNoEditable(data, columnNames);
            jTable.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calcula la suma de saldos de las cuentas asociadas al cliente actual, muestra el resultado en
     * una ventana emergente y actualiza los valores en el objeto clienteActual y la base de datos.
     */
    private void calcularMostrarResultado() {
        try {
            // Calcular la suma de saldos de las cuentas asociadas al cliente actual.
            float totalSaldos = 0;
            for (Cuenta cuenta : clienteActual.getCuentas()) {
                totalSaldos += cuenta.getSaldo();
            }

            // Mostrar el resultado en una ventana emergente.
            JOptionPane.showMessageDialog(this, "El resultado del cálculo es: " + totalSaldos,
                    "Resultado del cálculo", JOptionPane.INFORMATION_MESSAGE);

            // Actualizar el valor real del campo calculado en el cliente actual.
            clienteActual.setSaldoTotal(totalSaldos);

            // Actualizar el valor en la base de datos.
            actualizarSaldoTotalEnBD(totalSaldos);

            // Actualizar la etiqueta en el panel con el nuevo saldo total.
            lblSaldoTotalValor.setText(Float.toString(clienteActual.getSaldoTotal()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el saldo total del cliente en la base de datos.
     * @param nuevoSaldoTotal El nuevo saldo total del cliente.
     */
    private void actualizarSaldoTotalEnBD(float nuevoSaldoTotal) {
        clienteActual.setSaldoTotal(nuevoSaldoTotal);
        Resumen resumen = new Resumen(conexion, clienteActual.getCliId());
        resumen.actualizarSaldoTotal(nuevoSaldoTotal, clienteActual.getCliId());
    }
}
