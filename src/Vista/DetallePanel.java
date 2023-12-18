package Vista;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import Controlador.Detalle;
import Modelo.Cliente;
import Modelo.Cuenta;

/**
 * Panel de detalles que muestra información detallada de las cuentas asociadas a un cliente.
 * Permite la navegación entre las cuentas, la modificación de datos y el guardado en la base de datos.
 */
public class DetallePanel extends JPanel {
    /** Conexión con la base de datos. */
    private Connection conexion;
    /** El cliente validado en el sistema. */
    private Cliente clienteActual;
    /** Instancia del controlador diseñado para esta clase. */
    private Detalle detalle;
    /** Textos para los distintos campos de la tabla de cuentas. */
    private JLabel lblCuentaId, lblSaldo, lblFechaCreacion, lblCuentaIdValor, lblNoCuentas, lblInfoCuenta;
    /** Para modificar el saldo. */
    private JTextField txtSaldo;
    /** Botones para la navegación y guardado de cambios realizados. */
    private JButton btnAnterior, btnSiguiente, btnPrimero, btnUltimo, btnGuardar;
    /** Elemento DatePicker para seleccionar fechas. */
    private JDatePickerImpl dpFechaCreacion;
    /** Para guardar las cuentas asociadas a cada cliente. */
    private List<Cuenta> cuentasCliente;
    /** Guarda la posicion actual en la lista. */
    private int posicionActual;
    /** Para comparar con el saldo modificado. */
    private float saldoOriginal;
    /** Para comparar con la fecha de creación modificada. */
    private Calendar fechaCreacionOriginal;

    /**
     * Constructor que inicializa el panel de detalles.
     *
     * @param conexion      Conexión a la base de datos.
     * @param clienteActual Cliente cuyas cuentas se mostrarán en el panel.
     * @throws SQLException Excepción que puede ocurrir al interactuar con la base de datos.
     */
    public DetallePanel(Connection conexion, Cliente clienteActual) throws SQLException {
        this.conexion = conexion;
        this.clienteActual = clienteActual;
        this.detalle = new Detalle(conexion);
        this.cuentasCliente = detalle.obtenerCuentasPorCliente(clienteActual.getCliId());
        this.posicionActual = 0;

        initUI();
        cargaInicialDetalleCuentaActual();
    }

    /**
     * Inicializa y configura la interfaz de usuario del panel de detalles de cuentas,
     * incluyendo la disposición de componentes, manejo de eventos y estilos visuales.
     */
    private void initUI() {
        setLayout(new BorderLayout());

        // Cargar imagen de fondo.
        ImageIcon imageIcon = new ImageIcon("bgvisualizar.png");
        Image imagenFondo = imageIcon.getImage();

        // Crear panel superior y pintar imagen de fondo en él.
        JPanel panelSuperior = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibuja la imagen de fondo
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Restricciones de posición para colocar cada elemento en su sitio.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        // Padding.
        gbc.insets = new Insets(5, 5, 5, 5);

        // JLabel para mostrar información de cuenta.
        lblInfoCuenta = new JLabel(getInfoCuentaLabel());
        lblInfoCuenta.setFont(new Font("Arial", Font.BOLD, 16));
        lblInfoCuenta.setForeground(new Color(255, 255, 255)); // Blanco.
        panelSuperior.add(lblInfoCuenta, gbc);

        // Columnas de la tabla cuenta.
        lblCuentaId = crearLabelPersonalizada("Cuenta ID:");
        lblCuentaIdValor = crearLabelPersonalizada("");
        lblSaldo = crearLabelPersonalizada("Saldo:");
        lblFechaCreacion = crearLabelPersonalizada("Fecha Creación:");

        lblCuentaId.setForeground(new Color(30, 30, 30)); // Casi negro
        lblCuentaIdValor.setForeground(new Color(50, 50, 50)); // Gris oscuro
        lblSaldo.setForeground(new Color(30, 30, 30)); // Casi negro
        lblFechaCreacion.setForeground(new Color(30, 30, 30)); // Casi negro

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1;
        panelSuperior.add(lblCuentaId, gbc);
        gbc.gridx = 1;
        panelSuperior.add(lblCuentaIdValor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelSuperior.add(lblSaldo, gbc);
        gbc.gridx = 1;
        txtSaldo = new JTextField();
        panelSuperior.add(txtSaldo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelSuperior.add(lblFechaCreacion, gbc);
        gbc.gridx = 1;

        // Crear y añadir Date Picker.
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        dpFechaCreacion = new JDatePickerImpl(datePanel, new JFormattedTextField.AbstractFormatter() {
            @Override
            public Object stringToValue(String text) {
                return null;
            }

            @Override
            public String valueToString(Object value) {
                if (value != null) {
                    Calendar calendar = (Calendar) value;
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    return format.format(calendar.getTime());
                }
                return "";
            }
        });
        panelSuperior.add(dpFechaCreacion, gbc);

        // Agregar un JLabel para mostrar el mensaje cuando no hay cuentas asociadas.
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Para que ocupe dos columnas.
        gbc.anchor = GridBagConstraints.CENTER;
        lblNoCuentas = new JLabel("No tiene cuentas asociadas.");
        lblNoCuentas.setForeground(Color.RED);
        panelSuperior.add(lblNoCuentas, gbc);

        // Panel de botones.
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnPrimero = new JButton("Primero");
        btnAnterior = new JButton("Anterior");
        btnSiguiente = new JButton("Siguiente");
        btnUltimo = new JButton("Último");
        btnGuardar = new JButton("Guardar");

        // Color para los botones.
        Color colorPrimero = new Color(148, 179, 192);  // Azul
        Color colorAnterior = new Color(183, 146, 131);  // Rojo
        Color colorSiguiente = new Color(144, 194, 144);  // Verde
        Color colorUltimo = new Color(166, 157, 117);  // Amarillo
        Color colorGuardar = new Color(143, 106, 143);  // Púrpura
        btnPrimero.setBackground(colorPrimero);
        btnAnterior.setBackground(colorAnterior);
        btnSiguiente.setBackground(colorSiguiente);
        btnUltimo.setBackground(colorUltimo);
        btnGuardar.setBackground(colorGuardar);

        // Comportamiento de los botones (definidos en métodos específicos para cada uno).
        btnPrimero.addActionListener(e -> mostrarPrimeraCuenta());
        btnAnterior.addActionListener(e -> mostrarCuentaAnterior());
        btnSiguiente.addActionListener(e -> mostrarCuentaSiguiente());
        btnUltimo.addActionListener(e -> mostrarUltimaCuenta());
        btnGuardar.addActionListener(e -> guardarCuenta());

        // Agregar listeners para los cambios en tiempo real en txtSaldo y dpFechaCreacion.
        txtSaldo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                habilitarDesabilitarBotonGuardar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                habilitarDesabilitarBotonGuardar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                habilitarDesabilitarBotonGuardar();
            }
        });

        dpFechaCreacion.addActionListener(e -> habilitarDesabilitarBotonGuardar());

        // Añadir botones al panel de botones.
        panelBotones.add(btnPrimero);
        panelBotones.add(btnAnterior);
        panelBotones.add(btnSiguiente);
        panelBotones.add(btnUltimo);
        panelBotones.add(btnGuardar);

        // Agregar ambos paneles al panel principal.
        add(panelSuperior, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }


    /**
     * Crea un JLabel con formato personalizado en HTML, utilizando una fuente Arial de tamaño 14 y negrita.
     *
     * @param text El texto a ser mostrado en el JLabel.
     * @return El JLabel con el formato especificado.
     */
    private JLabel crearLabelPersonalizada(String text) {
        JLabel label = new JLabel("<html><b>" + text + "</b></html>");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    /**
     * Obtiene el texto dinámico para el JLabel lblInfoCuenta, que muestra información de la cuenta actual del cliente.
     *
     * @return El texto dinámico que incluye el número de cuenta y el nombre de usuario del cliente.
     */
    private String getInfoCuentaLabel() {
        if (clienteActual != null && !cuentasCliente.isEmpty()) {
            int numeroCuenta = posicionActual + 1; // Sumamos 1 para mostrar una numeración basada en 1
            return "Cuenta #" + numeroCuenta + " (" + clienteActual.getUsuario() + ")";
        } else {
            return "";
        }
    }

    /**
     * Muestra el detalle de la cuenta actual del cliente en el panel, actualizando la visibilidad de los componentes
     * según la existencia de cuentas asociadas y estableciendo los valores originales de la cuenta.
     */
    private void cargaInicialDetalleCuentaActual() {
        if (clienteActual != null) {
            try {
                // Obtener las cuentas asociadas al cliente.
                cuentasCliente = detalle.obtenerCuentasPorCliente(clienteActual.getCliId());

                if (!cuentasCliente.isEmpty()) {
                    // Si hay cuentas asociadas, mostrar los componentes correspondientes.
                    lblCuentaId.setVisible(true);
                    lblCuentaIdValor.setVisible(true);
                    lblSaldo.setVisible(true);
                    lblFechaCreacion.setVisible(true);
                    txtSaldo.setVisible(true);
                    dpFechaCreacion.setVisible(true);
                    btnAnterior.setVisible(true);
                    btnSiguiente.setVisible(true);
                    btnGuardar.setVisible(true);

                    // Habilitar/deshabilitar botones de navegación según la posición en el conjunto de resultados.
                    actualizarEstadoBotonesNavegacion();

                    // Ocultar el JLabel "No tiene cuentas asociadas".
                    lblNoCuentas.setVisible(false);

                    // Actualizar lblInfoCuenta con el número actual y total de cuentas.
                    int numeroCuenta = posicionActual + 1;
                    lblInfoCuenta.setText("Cuenta #" + numeroCuenta + " de " + cuentasCliente.size() + " (" + clienteActual.getUsuario() + ")");

                    // Mostrar la cuenta actual.
                    mostrarCuentaActual();

                    // Al cargar una cuenta, establecer los valores originales.
                    Cuenta cuenta = cuentasCliente.get(posicionActual);
                    saldoOriginal = cuenta.getSaldo();
                    fechaCreacionOriginal = cuenta.getFechaCreacion() != null ? (GregorianCalendar) cuenta.getFechaCreacion().clone() : null;

                    // Habilitar o deshabilitar el botón de guardar según los cambios en saldo o fecha de creación.
                    habilitarDesabilitarBotonGuardar();
                } else {
                    // Si no hay cuentas asociadas, ocultar los componentes y mostrar el JLabel correspondiente.
                    lblCuentaId.setVisible(false);
                    lblCuentaIdValor.setVisible(false);
                    lblSaldo.setVisible(false);
                    lblFechaCreacion.setVisible(false);
                    txtSaldo.setVisible(false);
                    dpFechaCreacion.setVisible(false);
                    btnAnterior.setVisible(false);
                    btnSiguiente.setVisible(false);
                    btnGuardar.setVisible(false);

                    // Mostrar el JLabel "No tiene cuentas asociadas".
                    lblNoCuentas.setVisible(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Muestra la información de la cuenta actual en los componentes del panel, actualizando los campos
     * con los valores de la cuenta y habilitando o deshabilitando el botón de guardar según los cambios realizados.
     */
    private void mostrarCuentaActual() {
        // Obtener la cuenta actual.
        Cuenta cuenta = cuentasCliente.get(posicionActual);

        // Mostrar la información de la cuenta en los componentes correspondientes.
        lblCuentaIdValor.setText(String.valueOf(cuenta.getCueId()));
        txtSaldo.setText(String.valueOf(cuenta.getSaldo()));

        // Actualizar lblInfoCuenta con el número actual y total de cuentas.
        int numeroCuenta = posicionActual + 1;
        lblInfoCuenta.setText("Cuenta #" + numeroCuenta + " de " + cuentasCliente.size() + " (" + clienteActual.getUsuario() + ")");

        // Establecer la fecha de creación seleccionada en el date picker según la fecha de creación de la cuenta.
        GregorianCalendar fechaCreacion = cuenta.getFechaCreacion();
        if (fechaCreacion != null) {
            dpFechaCreacion.getModel().setDate(
                    fechaCreacion.get(Calendar.YEAR),
                    fechaCreacion.get(Calendar.MONTH),
                    fechaCreacion.get(Calendar.DAY_OF_MONTH)
            );
            dpFechaCreacion.getModel().setSelected(true);
        } else {
            dpFechaCreacion.getModel().setSelected(false);
        }

        // Establecer los valores originales para comparar cambios.
        saldoOriginal = cuenta.getSaldo();
        fechaCreacionOriginal = cuenta.getFechaCreacion() != null ? (GregorianCalendar) cuenta.getFechaCreacion().clone() : null;

        // Habilitar o deshabilitar el botón de guardar según los cambios en saldo o fecha de creación.
        habilitarDesabilitarBotonGuardar();
    }

    /**
     * Actualiza el estado de los botones de navegación según la posición actual en la lista de cuentas.
     * Habilita o deshabilita los botones según la posición actual en la lista.
     */
    private void actualizarEstadoBotonesNavegacion() {
        btnPrimero.setEnabled(posicionActual > 0);
        btnAnterior.setEnabled(posicionActual > 0);
        btnSiguiente.setEnabled(posicionActual < cuentasCliente.size() - 1);
        btnUltimo.setEnabled(posicionActual < cuentasCliente.size() - 1);
    }

    /**
     * Habilita o deshabilita el botón de guardar según los cambios detectados en los campos de saldo y fecha de creación.
     * Si el campo de saldo está vacío o si no hay cambios, el botón se deshabilita. De lo contrario, se habilita.
     */
    private void habilitarDesabilitarBotonGuardar() {
        // Obtener el texto actual del campo de saldo.
        String saldoActualStr = txtSaldo.getText().trim();

        // Verificar si el campo de saldo está vacío.
        if (saldoActualStr.isEmpty()) {
            // Si está vacío, deshabilitar el botón Guardar.
            btnGuardar.setEnabled(false);
            return;
        }

        try {
            // Intentar convertir el texto del saldo a float.
            float saldoActual = Float.parseFloat(saldoActualStr);
            // Obtener la fecha de creación actual del DatePicker.
            GregorianCalendar fechaCreacionActual = obtenerFechaCreacionActual();

            // Comparar el saldo actual con el saldo original para detectar cambios en el saldo.
            boolean cambiosEnSaldo = saldoActual != saldoOriginal;

            // Comparar la fecha de creación actual con la fecha de creación original para detectar cambios en la fecha.
            boolean cambiosEnFecha = fechaCreacionActual != null &&
                    (fechaCreacionActual.get(Calendar.YEAR) != fechaCreacionOriginal.get(Calendar.YEAR) ||
                            fechaCreacionActual.get(Calendar.MONTH) != fechaCreacionOriginal.get(Calendar.MONTH) ||
                            fechaCreacionActual.get(Calendar.DAY_OF_MONTH) != fechaCreacionOriginal.get(Calendar.DAY_OF_MONTH));

            // Habilitar o deshabilitar el botón Guardar según los cambios en saldo o fecha de creación.
            btnGuardar.setEnabled(cambiosEnSaldo || cambiosEnFecha);
        } catch (NumberFormatException e) {
            // Manejar la excepción si el texto del saldo no es un número válido.
            btnGuardar.setEnabled(false);
            System.out.println("Error de formato en el saldo: " + e.getMessage());
        }
    }

    /**
     * Obtiene la fecha de creación actual del date picker y la devuelve como un objeto GregorianCalendar.
     * Si no se ha seleccionado ninguna fecha, devuelve null.
     *
     * @return La fecha de creación actual o null si no se ha seleccionado ninguna fecha.
     */
    private GregorianCalendar obtenerFechaCreacionActual() {
        Object value = dpFechaCreacion.getModel().getValue();
        if (value != null) {
            // Convertir el valor del DatePicker a un objeto GregorianCalendar.
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime((Date) value);
            return calendar;
        }
        return null;
    }


    /**
     * Muestra la cuenta anterior en la lista de cuentas del cliente. Si no hay cuentas anteriores,
     * no realiza ninguna acción. Después de mostrar la cuenta actual, actualiza el estado de los botones de navegación.
     */
    private void mostrarCuentaAnterior() {
        if (posicionActual > 0) {
            // Decrementar la posición actual y mostrar la cuenta actualizada.
            posicionActual--;
            mostrarCuentaActual();
        }
        // Actualizar el estado de los botones de navegación.
        actualizarEstadoBotonesNavegacion();
    }

    /**
     * Muestra la cuenta siguiente en la lista de cuentas del cliente. Si no hay cuentas siguientes,
     * no realiza ninguna acción. Después de mostrar la cuenta actual, actualiza el estado de los botones de navegación.
     */
    private void mostrarCuentaSiguiente() {
        if (posicionActual < cuentasCliente.size() - 1) {
            // Incrementar la posición actual y mostrar la cuenta actualizada.
            posicionActual++;
            mostrarCuentaActual();
        }
        // Actualizar el estado de los botones de navegación.
        actualizarEstadoBotonesNavegacion();
    }

    /**
     * Muestra la primera cuenta en la lista de cuentas del cliente. Si no hay cuentas,
     * no realiza ninguna acción. Después de mostrar la cuenta actual, actualiza el estado de los botones de navegación.
     */
    private void mostrarPrimeraCuenta() {
        if (!cuentasCliente.isEmpty()) {
            // Establecer la posición actual en la primera cuenta y mostrarla.
            posicionActual = 0;
            mostrarCuentaActual();
            // Actualizar el estado de los botones de navegación.
            actualizarEstadoBotonesNavegacion();
        }
    }

    /**
     * Muestra la última cuenta en la lista de cuentas del cliente. Si no hay cuentas,
     * no realiza ninguna acción. Después de mostrar la cuenta actual, actualiza el estado de los botones de navegación.
     */
    private void mostrarUltimaCuenta() {
        if (!cuentasCliente.isEmpty()) {
            // Establecer la posición actual en la última cuenta y mostrarla.
            posicionActual = cuentasCliente.size() - 1;
            mostrarCuentaActual();
            // Actualizar el estado de los botones de navegación.
            actualizarEstadoBotonesNavegacion();
        }
    }


    /**
     * Guarda los cambios realizados en la cuenta actual. Compara los valores modificados
     * con los valores originales antes de guardar. Si hay cambios en el saldo o la fecha de creación,
     * actualiza la cuenta en la base de datos y en la lista 'cuentasCliente'. Después de guardar, actualiza
     * los valores originales y desactiva el botón Guardar. Maneja excepciones SQL y de formato de número.
     */
    private void guardarCuenta() {
        try {
            // Obtener la cuenta original y los nuevos valores de saldo y fecha de creación.
            Cuenta cuentaOriginal = cuentasCliente.get(posicionActual);
            float nuevoSaldo = Float.parseFloat(txtSaldo.getText());
            GregorianCalendar nuevaFechaCreacion = obtenerFechaCreacionActual();

            // Comparar con los valores originales antes de guardar.
            boolean cambiosEnSaldo = nuevoSaldo != saldoOriginal;
            boolean cambiosEnFecha = !Objects.equals(nuevaFechaCreacion, fechaCreacionOriginal);

            // Si hay cambios en saldo o fecha, realizar la actualización.
            if (cambiosEnSaldo || cambiosEnFecha) {
                // Crear una nueva instancia de cuenta con los nuevos valores.
                Cuenta cuenta = new Cuenta(cuentaOriginal.getCueId(), clienteActual.getCliId(), nuevoSaldo, new GregorianCalendar());
                cuenta.setFechaCreacion(nuevaFechaCreacion);

                // Guardar o modificar la cuenta en la base de datos.
                detalle.guardarModificarCuenta(cuenta);

                // Actualizar la lista 'cuentasCliente' después de guardar la cuenta.
                cuentasCliente.set(posicionActual, cuenta);

                // Actualizar los valores originales después de guardar.
                saldoOriginal = nuevoSaldo;
                fechaCreacionOriginal = nuevaFechaCreacion;

                // Desactivar el botón Guardar después de guardar la cuenta.
                btnGuardar.setEnabled(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción si ha habido un error accediendo a la base de datos.
            JOptionPane.showMessageDialog(this, "Error al acceder a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Manejar la excepción si el texto no es un número válido.
            JOptionPane.showMessageDialog(this, "Formato de saldo incorrecto. Ingrese un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);

        }

    }

}
