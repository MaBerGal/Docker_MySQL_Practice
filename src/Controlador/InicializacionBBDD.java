package Controlador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * La clase InicializacionBBDD se encarga de inicializar la base de datos, creando y poblando las tablas
 * necesarias para el funcionamiento de la aplicación.
 */
public class InicializacionBBDD {

    /**
     * Inicializa la base de datos ejecutando scripts SQL para crear y poblar las tablas 'cliente' y 'cuenta'.
     *
     * @param connection Objeto Connection que representa la conexión a la base de datos.
     */
    public void inicializarBBDD(Connection connection) {
        Statement statement = null;

        try {
            // Crear un objeto Statement
            statement = connection.createStatement();

            // ***************************************************************************
            // **** IMPORTANTE: "reinicia" las tablas en cada ejecución del programa. ****
            // ***************************************************************************
            statement.executeUpdate("DROP TABLE IF EXISTS cuenta");
            statement.executeUpdate("DROP TABLE IF EXISTS cliente");

            // Comprobar si la tabla cliente existe.
            ResultSet resultSet = statement.executeQuery("SHOW TABLES LIKE 'cliente'");
            if (!resultSet.next()) {
                // La tabla no existe, ejecutar script de creación y población.
                ejecutarScript("creacion_e_inserciones_cliente.sql", statement);
                System.out.println("Tabla cliente creada y poblada.");
            }

            // Comprobar si la tabla cuenta existe.
            resultSet = statement.executeQuery("SHOW TABLES LIKE 'cuenta'");
            if (!resultSet.next()) {
                // La tabla no existe, ejecutar script de creación y población.
                ejecutarScript("creacion_e_inserciones_cuenta.sql", statement);
                System.out.println("Tabla cuenta creada y poblada.");
            }

            // Realizar una consulta SELECT de ejemplo.
            System.out.println("\nConsulta de ejemplo:");
            resultSet = statement.executeQuery("SELECT * FROM cliente LIMIT 5");
            while (resultSet.next()) {
                int cliId = resultSet.getInt("cliId");
                String usuario = resultSet.getString("usuario");
                System.out.println("cliId: " + cliId + ", usuario: " + usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar recursos si estuviesen abiertos.
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ejecuta un script SQL desde un archivo y lo ejecuta en la base de datos.
     *
     * @param rutaScript Ruta del archivo que contiene el script SQL.
     * @param statement  Objeto Statement para ejecutar las consultas en la base de datos.
     */
    private void ejecutarScript(String rutaScript, Statement statement) {
        try {
            // Cargar el contenido del script desde el archivo en la raíz del proyecto.
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(rutaScript);

            if (inputStream == null) {
                System.err.println("Error: No se pudo encontrar el archivo " + rutaScript);
                return;
            }

            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            // Ejecutar cada declaración del script.
            String line;
            StringBuilder contenidoScript = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                contenidoScript.append(line).append(" ");
                if (line.trim().endsWith(";")) {
                    // Ejecutar la declaración actual.
                    if (contenidoScript.length() > 0) {
                        System.out.println("Ejecutando script: " + contenidoScript.toString());
                        statement.execute(contenidoScript.toString());
                    } else {
                        System.err.println("Advertencia: Línea vacía encontrada en el script.");
                    }
                    // Limpiar para la siguiente declaración.
                    contenidoScript = new StringBuilder();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
