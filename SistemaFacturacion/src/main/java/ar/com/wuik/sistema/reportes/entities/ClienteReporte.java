/**
 * Autor : juan.vazquez@wuik.com.ar - Wuik-Working Innovation Creacion :
 * 02/03/2014 - 21:31:06
 */
package ar.com.wuik.sistema.reportes.entities;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public class ClienteReporte {


	private String cliente;
	private BigDecimal saldo;
	private Date ultEntrega;

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
	 * @return the saldo
	 */
	public BigDecimal getSaldo() {
		return saldo;
	}

	/**
	 * @param saldo
	 *            the saldo to set
	 */
	public void setSaldo( BigDecimal saldo ) {
		this.saldo = saldo;
	}

	/**
	 * @return the ultEntrega
	 */
	public Date getUltEntrega() {
		return ultEntrega;
	}

	/**
	 * @param ultEntrega
	 *            the ultEntrega to set
	 */
	public void setUltEntrega( Date ultEntrega ) {
		this.ultEntrega = ultEntrega;
	}

}
