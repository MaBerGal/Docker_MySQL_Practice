package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * La clase ConexionBBDD proporciona métodos para establecer y cerrar conexiones con una base de datos MySQL.
 */
public class ConexionBBDD {
    private static final String URL = "jdbc:mysql://localhost:3306";
    private static final String USUARIO = "usuario";
    private static final String PASS = "python";

    /**
     * Establece una conexión a la base de datos y se selecciona.
     *
     * @return La conexión establecida.
     * @throws SQLException Si ocurre un error al establecer la conexión o al seleccionar la base de datos.
     */
    public static Connection establecerConexion() throws SQLException {
        Connection connection = null;
        try {
            // Establecer conexión.
            connection = DriverManager.getConnection(URL, USUARIO, PASS);

            // Seleccionar la base de datos .
            connection.createStatement().execute("USE bdpython");

            return connection;
        } catch (SQLException e) {
            // Manejo de errores
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Cierra la conexión proporcionada.
     *
     * @param conexion La conexión a cerrar.
     * @throws SQLException Si ocurre un error al cerrar la conexión.
     */
    public static void cerrarConexion(Connection conexion) throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
    }

    /**
     * Obtiene un objeto Statement para realizar consultas en modo DETALLE.
     *
     * @param conexion La conexión a la base de datos.
     * @return Un objeto Statement para consultas DETALLE.
     * @throws SQLException Si ocurre un error al obtener el Statement.
     */
    public static Statement obtenerStatementDetalle(Connection conexion) throws SQLException {
        return conexion.createStatement();
    }

    /**
     * Obtiene un objeto Statement para realizar consultas en modo RESUMEN.
     *
     * @param conexion La conexión a la base de datos.
     * @return Un objeto Statement para consultas RESUMEN.
     * @throws SQLException Si ocurre un error al obtener el Statement.
     */
    public static Statement obtenerStatementResumen(Connection conexion) throws SQLException {
        return conexion.createStatement();
    }
}
