package Controlador;

import Modelo.Cuenta;
import Modelo.Cliente;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * La clase EditCliente se encarga de realizar operaciones relacionadas con la edición de clientes
 * utilizando un ResultSet y un Statement proporcionados.
 */
public class EditCliente {
    private Statement statement;
    private ResultSet resultSet;

    /**
     * Constructor de la clase EditCliente que recibe un Statement y un ResultSet como parámetros.
     * @param statement El Statement utilizado para realizar operaciones SQL.
     * @param resultSet El ResultSet que contiene los resultados de una consulta SQL.
     */
    public EditCliente(Statement statement, ResultSet resultSet) {
        this.statement = statement;
        this.resultSet = resultSet;
    }

    /**
     * Mueve el cursor del ResultSet al siguiente registro.
     * @return true si hay un siguiente registro, false si no lo hay o se produce una excepción.
     */
    public boolean intercambiarSiguiente() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza un objeto Cliente con los datos actuales del ResultSet.
     * @param connection La conexión a la base de datos para obtener información adicional si es necesario.
     * @return Un objeto Cliente actualizado con los datos del ResultSet actual.
     *         Devuelve null si ocurre una excepción durante el proceso.
     */
    public Cliente actualizarClienteActual(Connection connection) {
        try {
            int cliNumero = resultSet.getInt("cliId");
            String usuario = resultSet.getString("usuario");
            String contraseña = resultSet.getString("contraseña");
            String imagen = resultSet.getString("imagen");
            float saldoTotal = resultSet.getFloat("saldoTotal");
            List<Cuenta> c = Cliente.obtenerCuentasPorCliente(cliNumero, connection);

            return new Cliente(cliNumero, usuario, contraseña, imagen, saldoTotal, c);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
