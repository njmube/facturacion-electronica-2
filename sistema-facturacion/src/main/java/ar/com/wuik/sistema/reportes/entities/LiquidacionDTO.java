package ar.com.wuik.sistema.reportes.entities;

import java.math.BigDecimal;
import java.util.Date;

public class LiquidacionDTO {

	private Date fecha;
	private String nro;
	private BigDecimal total;

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getNro() {
		return nro;
	}

	public void setNro(String nro) {
		this.nro = nro;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
