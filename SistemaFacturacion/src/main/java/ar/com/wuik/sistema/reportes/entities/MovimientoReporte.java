/**
 * Autor : juan.vazquez@wuik.com.ar - Wuik-Working Innovation Creacion :
 * 27/02/2014 - 15:20:44
 */
package ar.com.wuik.sistema.reportes.entities;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public class MovimientoReporte {


	private Date fecha;
	private String descripcion;
	private String cliente;
	private BigDecimal efectivo;
	private BigDecimal tarjeta;
	private BigDecimal ctaCte;
	private BigDecimal entrada;
	private BigDecimal salida;

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 *            the fecha to set
	 */
	public void setFecha( Date fecha ) {
		this.fecha = fecha;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            the descripcion to set
	 */
	public void setDescripcion( String descripcion ) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the cliente
	 */
	public String getCliente() {
		return cliente;
	}

	/**
	 * @param cliente
	 *            the cliente to set
	 */
	public void setCliente( String cliente ) {
		this.cliente = cliente;
	}

	/**
	 * @return the efectivo
	 */
	public BigDecimal getEfectivo() {
		return efectivo;
	}

	/**
	 * @param efectivo
	 *            the efectivo to set
	 */
	public void setEfectivo( BigDecimal efectivo ) {
		this.efectivo = efectivo;
	}

	/**
	 * @return the tarjeta
	 */
	public BigDecimal getTarjeta() {
		return tarjeta;
	}

	/**
	 * @param tarjeta
	 *            the tarjeta to set
	 */
	public void setTarjeta( BigDecimal tarjeta ) {
		this.tarjeta = tarjeta;
	}

	/**
	 * @return the ctaCte
	 */
	public BigDecimal getCtaCte() {
		return ctaCte;
	}

	/**
	 * @param ctaCte
	 *            the ctaCte to set
	 */
	public void setCtaCte( BigDecimal ctaCte ) {
		this.ctaCte = ctaCte;
	}

	/**
	 * @return the entrada
	 */
	public BigDecimal getEntrada() {
		return entrada;
	}

	/**
	 * @param entrada
	 *            the entrada to set
	 */
	public void setEntrada( BigDecimal entrada ) {
		this.entrada = entrada;
	}

	/**
	 * @return the salida
	 */
	public BigDecimal getSalida() {
		return salida;
	}

	/**
	 * @param salida
	 *            the salida to set
	 */
	public void setSalida( BigDecimal salida ) {
		this.salida = salida;
	}

}
