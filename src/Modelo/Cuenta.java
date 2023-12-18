package Modelo;

import java.util.GregorianCalendar;

/**
 * La clase Cuenta representa una cuenta asociada a un cliente en la base de datos.
 */
public class Cuenta {
    /** Identificador único de la cuenta (clave primaria). */
    private int cueId;

    /** Identificador único del cliente al que pertenece la cuenta (clave foránea). */
    private int cliId;

    /** Saldo actual de la cuenta. */
    private float saldo;

    /** Fecha de creación de la cuenta. */
    private GregorianCalendar fechaCreacion;

    /**
     * Constructor para crear un objeto Cuenta con información específica.
     *
     * @param cueId          Identificador único de la cuenta.
     * @param cliId          Identificador único del cliente al que pertenece la cuenta.
     * @param saldo          Saldo actual de la cuenta.
     * @param fechaCreacion  Fecha de creación de la cuenta.
     */
    public Cuenta(int cueId, int cliId, float saldo, GregorianCalendar fechaCreacion) {
        this.cueId = cueId;
        this.cliId = cliId;
        this.saldo = saldo;
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene el identificador único de la cuenta.
     *
     * @return Identificador único de la cuenta.
     */
    public int getCueId() {
        return cueId;
    }

    /**
     * Establece el identificador único de la cuenta.
     *
     * @param cueId Nuevo identificador único de la cuenta.
     */
    public void setCueId(int cueId) {
        this.cueId = cueId;
    }

    /**
     * Obtiene el identificador único del cliente al que pertenece la cuenta.
     *
     * @return Identificador único del cliente.
     */
    public int getCliId() {
        return cliId;
    }

    /**
     * Establece el identificador único del cliente al que pertenece la cuenta.
     *
     * @param cliId Nuevo identificador único del cliente.
     */
    public void setCliId(int cliId) {
        this.cliId = cliId;
    }

    /**
     * Obtiene el saldo actual de la cuenta.
     *
     * @return Saldo actual de la cuenta.
     */
    public float getSaldo() {
        return saldo;
    }

    /**
     * Establece el saldo actual de la cuenta.
     *
     * @param saldo Nuevo saldo de la cuenta.
     */
    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    /**
     * Obtiene la fecha de creación de la cuenta.
     *
     * @return Fecha de creación de la cuenta.
     */
    public GregorianCalendar getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha de creación de la cuenta.
     *
     * @param fechaCreacion Nueva fecha de creación de la cuenta.
     */
    public void setFechaCreacion(GregorianCalendar fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
