package Vista;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * La clase JTableNoEditable extiende DefaultTableModel y proporciona una implementación
 * que hace que todas las celdas de la tabla no sean editables.
 */
public class JTableNoEditable extends DefaultTableModel {

    /**
     * Constructor de la clase JTableNoEditable.
     *
     * @param data        Vectores que representan los datos de la tabla.
     * @param columnNames Vector que representa los nombres de las columnas de la tabla.
     */
    public JTableNoEditable(Vector<?> data, Vector<?> columnNames) {
        super((Vector<? extends Vector>) data, columnNames);
    }

    /**
     * Sobrescribe el método isCellEditable para hacer que todas las celdas de la tabla no sean editables.
     *
     * @param row    Índice de la fila de la celda.
     * @param column Índice de la columna de la celda.
     * @return false, indicando que la celda no es editable.
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        // Hacer que todas las celdas no sean editables.
        return false;
    }
}

