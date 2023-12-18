package Controlador;

import Modelo.Cuenta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * La clase Resumen se encarga de obtener información resumida de las cuentas asociadas a un cliente.
 */
public class Resumen {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    /**
     * Constructor de la clase Resumen.
     *
     * @param connection Objeto Connection que representa la conexión a la base de datos.
     * @param cliId      Identificador del cliente para el cual se obtendrá el resumen.
     */
    public Resumen(Connection connection, int cliId) {
        this.connection = connection;
        try {
            // Modifica la consulta para incluir una cláusula WHERE para el ID del cliente.
            preparedStatement = connection.prepareStatement("SELECT * FROM cuenta WHERE cliId = ?");
            preparedStatement.setInt(1, cliId);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene una lista de objetos Cuenta que representan las cuentas asociadas al cliente.
     *
     * @return Lista de objetos Cuenta.
     */
    public List<Cuenta> obtenerCuentas() {
        List<Cuenta> cuentas = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int cueId = resultSet.getInt("cueId");
                int cliId = resultSet.getInt("cliId");
                float saldo = resultSet.getFloat("saldo");
                Date date = resultSet.getDate("fechaCreacion");
                GregorianCalendar gre = new GregorianCalendar();
                gre.setTime(date);
                Cuenta cuenta = new Cuenta(cueId, cliId, saldo, gre);
                cuentas.add(cuenta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cuentas;
    }

    /**
     * Actualiza el saldo total del cliente y en la base de datos.
     *
     * @param nuevoSaldoTotal El nuevo saldo total del cliente.
     */
    public void actualizarSaldoTotal(float nuevoSaldoTotal, int cliId) {

        // Actualizar el saldo total del cliente en la base de datos.
        try {
            String query = "UPDATE cliente SET saldoTotal = ? WHERE cliId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setFloat(1, nuevoSaldoTotal);
                preparedStatement.setInt(2, cliId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Cierra los recursos asociados a la consulta y conexión.
     */
    public void cerrar() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
