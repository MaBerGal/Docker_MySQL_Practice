import Controlador.ConexionBBDD;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import Vista.*;
import Controlador.*;

public class Main {

    public static void main(String[] args) {
        Connection conexion = null;
        Statement statementDetalle = null;
        Statement statementResumen = null;

        try {
            // Intentamos establecer la conexión y obtener los Statements.
            conexion = ConexionBBDD.establecerConexion();
            statementDetalle = ConexionBBDD.obtenerStatementDetalle(conexion);
            statementResumen = ConexionBBDD.obtenerStatementResumen(conexion);

            // Si llegamos aquí, la conexión fue exitosa.
            System.out.println("Conexión exitosa a la base de datos.");

            new InicializacionBBDD().inicializarBBDD(conexion);

            final Connection conexionFinal = conexion;

            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new GUI(conexionFinal);
            });

        } catch (SQLException e) {
            // Manejo de errores de conexión.
            System.err.println("Error al establecer la conexión: " + e.getMessage());
        }
    }
}
