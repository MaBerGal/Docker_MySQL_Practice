package Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Modelo.Cliente;

/**
 * La clase AutentificacionCliente proporciona métodos para autenticar a un cliente en la base de datos.
 */
public class AutentificacionCliente {

    private Connection conexion;

    /**
     * Constructor de la clase AutentificacionCliente que recibe una conexión a la base de datos como parámetro.
     *
     * @param conexion La conexión a la base de datos.
     */
    public AutentificacionCliente(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Valida la autenticación de un cliente en la base de datos.
     *
     * @param usuario El nombre de usuario del cliente.
     * @param pass    La contraseña del cliente.
     * @return Un objeto Cliente si la autenticación es exitosa, o null si no se encuentra un cliente válido.
     */
    public Cliente validarUsuario(String usuario, String pass) {
        String query = "SELECT * FROM cliente WHERE usuario = ? AND pass = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, usuario);
            statement.setString(2, pass);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Comprobar si la contraseña coincide.
                    String storedPassword = resultSet.getString("pass");
                    if (pass.equals(storedPassword)) {
                        // Utilizar el constructor apropiado con valores de la base de datos.
                        Cliente cliente = new Cliente(
                                resultSet.getInt("cliId"),
                                resultSet.getString("usuario"),
                                resultSet.getString("pass"),
                                resultSet.getString("imagen"),
                                resultSet.getFloat("saldoTotal"),
                                null
                        );
                        return cliente;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
