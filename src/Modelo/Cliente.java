package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Representa a un cliente en la base de datos.
 */
public class Cliente {

    /** Identificador único del cliente (clave primaria). */
    private int cliId;

    /** Nombre de usuario del cliente. */
    private String usuario;

    /** Contraseña del cliente. */
    private String pass;

    /** Imagen asociada al cliente. */
    private String imagen;

    /** Saldo total del cliente. */
    private float saldoTotal;

    /** Lista de cuentas asociadas al cliente. */
    private List<Cuenta> cuentas;

    /**
     * Constructor de la clase Cliente.
     *
     * @param cliId     Identificador único del cliente.
     * @param usuario   Nombre de usuario del cliente.
     * @param contraseña Contraseña del cliente.
     * @param imagen    Imagen asociada al cliente.
     * @param saldoTotal Saldo total del cliente.
     * @param cuentas   Lista de cuentas asociadas al cliente.
     */
    public Cliente(int cliId, String usuario, String contraseña, String imagen, float saldoTotal, List<Cuenta> cuentas) {
        this.cliId = cliId;
        this.usuario = usuario;
        this.pass = contraseña;
        this.imagen = imagen;
        this.saldoTotal = saldoTotal;

        // Verifica si la lista de cuentas es nula antes de asignarla.
        if (cuentas != null) {
            this.cuentas = cuentas;
        } else {
            this.cuentas = new ArrayList<>();
        }
    }

    // Getters y setters

    /**
     * Obtiene el identificador único del cliente.
     *
     * @return Identificador único del cliente.
     */
    public int getCliId() {
        return cliId;
    }

    /**
     * Establece el identificador único del cliente.
     *
     * @param cliId Identificador único del cliente.
     */
    public void setCliId(int cliId) {
        this.cliId = cliId;
    }

    /**
     * Obtiene el nombre de usuario del cliente.
     *
     * @return Nombre de usuario del cliente.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Establece el nombre de usuario del cliente.
     *
     * @param usuario Nombre de usuario del cliente.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene la contraseña del cliente.
     *
     * @return Contraseña del cliente.
     */
    public String getPass() {
        return pass;
    }

    /**
     * Establece la contraseña del cliente.
     *
     * @param pass Contraseña del cliente.
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Obtiene la imagen asociada al cliente.
     *
     * @return Imagen asociada al cliente.
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen asociada al cliente.
     *
     * @param imagen Imagen asociada al cliente.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Obtiene el saldo total del cliente.
     *
     * @return Saldo total del cliente.
     */
    public float getSaldoTotal() {
        return saldoTotal;
    }

    /**
     * Establece el saldo total del cliente.
     *
     * @param saldoTotal Saldo total del cliente.
     */
    public void setSaldoTotal(float saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    /**
     * Obtiene la lista de cuentas asociadas al cliente.
     *
     * @return Lista de cuentas asociadas al cliente.
     */
    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    /**
     * Establece la lista de cuentas asociadas al cliente.
     *
     * @param cuentas Lista de cuentas asociadas al cliente.
     */
    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    /**
     * Obtiene una lista de cuentas asociadas a un cliente específico.
     *
     * @param cliNumero  Número de cliente.
     * @param connection Conexión a la base de datos.
     * @return Lista de cuentas asociadas al cliente.
     * @throws SQLException Si ocurre un error de SQL.
     */
    public static List<Cuenta> obtenerCuentasPorCliente(int cliNumero, Connection connection) throws SQLException {
        // Inicializa una lista para almacenar las cuentas asociadas al cliente.
        List<Cuenta> cuentas = new ArrayList<>();

        // Consulta SQL para seleccionar cuentas de la base de datos.
        String sql = "SELECT cuentaId, saldo, fechaCreacion FROM cuenta WHERE cliId = ?";

        // Utiliza try-with-resources para asegurar la correcta liberación de recursos JDBC.
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Establece el parámetro en la consulta SQL con el número de cliente proporcionado.
            preparedStatement.setInt(1, cliNumero);

            // Ejecuta la consulta y obtiene un conjunto de resultados.
            try (ResultSet cuentaResultSet = preparedStatement.executeQuery()) {
                // Itera sobre las filas del conjunto de resultados.
                while (cuentaResultSet.next()) {
                    // Extrae información de la fila actual del conjunto de resultados.
                    int cuentaId = cuentaResultSet.getInt("cuentaId");
                    float saldoCuenta = cuentaResultSet.getFloat("saldo");
                    GregorianCalendar fechaCreacion = obtenerFechaCreacion(cuentaResultSet);

                    // Crea un objeto Cuenta con la información de la fila actual.
                    Cuenta cuenta = new Cuenta(cuentaId, cliNumero, saldoCuenta, fechaCreacion);

                    // Agrega la cuenta a la lista de cuentas asociadas al cliente.
                    cuentas.add(cuenta);
                }
            }
        }

        // Devuelve la lista de cuentas asociadas al cliente.
        return cuentas;
    }

    /**
     * Obtiene la fecha de creación de una cuenta desde un ResultSet.
     *
     * @param resultSet ResultSet que contiene la fecha de creación.
     * @return Fecha de creación de la cuenta.
     * @throws SQLException Si ocurre un error de SQL.
     */
    public static GregorianCalendar obtenerFechaCreacion(ResultSet resultSet) throws SQLException {
        java.sql.Date fechaSql = resultSet.getDate("fechaCreacion");
        if (fechaSql != null) {
            // Conversión de sql.Date a GregorianCalendar.
            return new GregorianCalendar(fechaSql.toLocalDate().getYear(), fechaSql.toLocalDate().getMonthValue() - 1, fechaSql.toLocalDate().getDayOfMonth());
        } else {
            return null;
        }
    }
}

