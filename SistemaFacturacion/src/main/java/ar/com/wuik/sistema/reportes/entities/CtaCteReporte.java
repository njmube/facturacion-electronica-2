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
public class CtaCteReporte {


	private Date fecha;
	private String descripcion;
	private BigDecimal ctaCte;
	private BigDecimal entrega;

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
	 * @return the entrega
	 */
	public BigDecimal getEntrega() {
		return entrega;
	}

	/**
	 * @param entrega
	 *            the entrega to set
	 */
	public void setEntrega( BigDecimal entrega ) {
		this.entrega = entrega;
	}

}
