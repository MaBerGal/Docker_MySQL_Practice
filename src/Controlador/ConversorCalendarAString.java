package Controlador;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * La clase ConversorCalendarAString proporciona un método estático para convertir objetos GregorianCalendar a cadenas de texto.
 */
public class ConversorCalendarAString {

    /**
     * Convierte un objeto GregorianCalendar a una cadena de texto en el formato "yyyy-MM-dd".
     *
     * @param calendar El objeto GregorianCalendar a convertir.
     * @return Una cadena de texto en formato "yyyy-MM-dd" que representa la fecha del calendario.
     *         Devuelve null si el calendario proporcionado es nulo.
     */
    public static String convertirCalendarAString(GregorianCalendar calendar) {
        if (calendar != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(calendar.getTime());
        }
        return null;  // Devuelve null si el calendario es nulo.
    }
}
