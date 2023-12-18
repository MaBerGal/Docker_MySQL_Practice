package Controlador;

import Modelo.Cliente;
import Modelo.Cuenta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * La clase Detalle proporciona métodos para navegar y manipular registros de clientes y cuentas en la base de datos.
 */
public class Detalle {
    private Connection connection;
    private ResultSet resultSet;

    /**
     * Constructor de la clase Detalle que recibe una conexión a la base de datos como parámetro.
     * @param connection La conexión a la base de datos.
     */
    public Detalle(Connection connection) {
        this.connection = connection;
    }

    /**
     * Avanza al siguiente registro en el ResultSet.
     * @return true si hay un siguiente registro, false si no lo hay o se produce una excepción.
     * @throws SQLException Si ocurre un error al acceder al siguiente registro.
     */
    public boolean siguiente() throws SQLException {
        return resultSet.next();
    }

    /**
     * Retrocede al registro anterior en el ResultSet.
     * @return true si hay un registro anterior, false si no lo hay o se produce una excepción.
     * @throws SQLException Si ocurre un error al acceder al registro anterior.
     */
    public boolean anterior() throws SQLException {
        return resultSet.previous();
    }

    /**
     * Mueve el cursor al primer registro en el ResultSet.
     * @return true si hay un primer registro, false si no lo hay o se produce una excepción.
     */
    public boolean primero() {
        try {
            if (resultSet != null) {
                return resultSet.first();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Mueve el cursor al último registro en el ResultSet.
     * @return true si hay un último registro, false si no lo hay o se produce una excepción.
     * @throws SQLException Si ocurre un error al acceder al último registro.
     */
    public boolean ultimo() throws SQLException {
        return resultSet.last();
    }

    /**
     * Obtiene un objeto Cliente basado en el registro actual del ResultSet.
     * @return Un objeto Cliente con los datos del registro actual.
     * @throws SQLException Si ocurre un error al obtener los datos del cliente.
     */
    public Cliente obtenerClienteActual() throws SQLException {
        int cliId = resultSet.getInt("cliId");
        String usuario = resultSet.getString("usuario");
        String contraseña = resultSet.getString("contraseña");
        String imagen = resultSet.getString("imagen");
        float saldoTotal = resultSet.getFloat("saldoTotal");

        // Obtener las cuentas asociadas al cliente.
        List<Cuenta> cuentas = obtenerCuentasPorCliente(cliId);

        return new Cliente(cliId, usuario, contraseña, imagen, saldoTotal, cuentas);
    }

    /**
     * Guarda o modifica un registro de cliente en la base de datos.
     * @param cliente El objeto Cliente a guardar o modificar.
     * @throws SQLException Si ocurre un error al ejecutar la operación en la base de datos.
     */
    public void guardarModificarCliente(Cliente cliente) throws SQLException {
        // SQL para insertar o actualizar un cliente.
        String sqlCliente = "INSERT INTO cliente (cliId, nombre, usuario, contraseña, imagen, saldoTotal) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), usuario = VALUES(usuario), " +
                "contraseña = VALUES(contraseña), imagen = VALUES(imagen), saldoTotal = VALUES(saldoTotal)";

        try (PreparedStatement preparedStatementCliente = connection.prepareStatement(sqlCliente)) {
            preparedStatementCliente.setInt(1, cliente.getCliId());
            preparedStatementCliente.setString(3, cliente.getUsuario());
            preparedStatementCliente.setString(4, cliente.getPass());
            preparedStatementCliente.setString(5, cliente.getImagen());
            preparedStatementCliente.setFloat(6, cliente.getSaldoTotal());

            preparedStatementCliente.executeUpdate();
        }
    }

    /**
     * Guarda o modifica un registro de cuenta en la base de datos.
     * @param cuenta El objeto Cuenta a guardar o modificar.
     * @throws SQLException Si ocurre un error al ejecutar la operación en la base de datos.
     */
    public void guardarModificarCuenta(Cuenta cuenta) throws SQLException {
        // SQL para insertar o actualizar una cuenta
        String sqlCuenta = "INSERT INTO cuenta (cueId, cliId, saldo, fechaCreacion) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE saldo = VALUES(saldo), fechaCreacion = VALUES(fechaCreacion)";

        try (PreparedStatement preparedStatementCuenta = connection.prepareStatement(sqlCuenta)) {
            preparedStatementCuenta.setInt(1, cuenta.getCueId());
            preparedStatementCuenta.setInt(2, cuenta.getCliId());
            preparedStatementCuenta.setFloat(3, cuenta.getSaldo());
            preparedStatementCuenta.setDate(4, new java.sql.Date(cuenta.getFechaCreacion().getTimeInMillis()));

            preparedStatementCuenta.executeUpdate();
        }
    }

    /**
     * Obtiene una lista de cuentas asociadas a un cliente.
     * @param cliId El ID del cliente.
     * @return Una lista de objetos Cuenta asociados al cliente.
     * @throws SQLException Si ocurre un error al obtener las cuentas desde la base de datos.
     */
    public List<Cuenta> obtenerCuentasPorCliente(int cliId) throws SQLException {
        List<Cuenta> cuentas = new ArrayList<>();

        String sqlCuentas = "SELECT * FROM cuenta WHERE cliId = ?";
        try (PreparedStatement preparedStatementCuentas = connection.prepareStatement(sqlCuentas)) {
            preparedStatementCuentas.setInt(1, cliId);

            try (ResultSet resultSetCuentas = preparedStatementCuentas.executeQuery()) {
                while (resultSetCuentas.next()) {
                    int cueId = resultSetCuentas.getInt("cueId");
                    float saldo = resultSetCuentas.getFloat("saldo");
                    java.sql.Date fechaCreacion = resultSetCuentas.getDate("fechaCreacion");

                    // Convertir java.sql.Date a java.util.Date y luego a GregorianCalendar.
                    java.util.Date utilDate = new java.util.Date(fechaCreacion.getTime());
                    java.util.GregorianCalendar gregorianCalendar = new java.util.GregorianCalendar();
                    gregorianCalendar.setTime(utilDate);

                    cuentas.add(new Cuenta(cueId, cliId, saldo, gregorianCalendar));
                }
            }
        }

        return cuentas;
    }

    /**
     * Muestra el detalle de las cuentas de un cliente en un ResultSet.
     * @param cliId El ID del cliente.
     * @return Un ResultSet que contiene el detalle de las cuentas del cliente.
     * @throws SQLException Si ocurre un error al ejecutar la consulta en la base de datos.
     */
    public ResultSet mostrarDetalleCuentasPorCliente(int cliId) throws SQLException {
        String sql = "SELECT * FROM cuenta WHERE cliId = ?";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, cliId);
            resultSet = preparedStatement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // No se cierra el resultSet para permitir seguir trabajando sobre él tras la llamada a este método.
            // Lo mismo para el preparedStatement.
            if (preparedStatement != null) {
                // preparedStatement.close();
            }
        }
    }

    /**
     * Obtiene un objeto Cuenta basado en el registro actual del ResultSet.
     * @return Un objeto Cuenta con los datos del registro actual.
     * @throws SQLException Si ocurre un error al obtener los datos de la cuenta.
     */
    public Cuenta obtenerCuentaActual() throws SQLException {
        int cueId = resultSet.getInt("cueId");
        int cliId = resultSet.getInt("cliId");
        float saldo = resultSet.getFloat("saldo");
        java.sql.Date fechaCreacion = resultSet.getDate("fechaCreacion");

        // Convertir java.sql.Date a java.util.Date y luego a GregorianCalendar
        java.util.Date utilDate = new java.util.Date(fechaCreacion.getTime());
        java.util.GregorianCalendar gregorianCalendar = new java.util.GregorianCalendar();
        gregorianCalendar.setTime(utilDate);

        return new Cuenta(cueId, cliId, saldo, gregorianCalendar);
    }
}
