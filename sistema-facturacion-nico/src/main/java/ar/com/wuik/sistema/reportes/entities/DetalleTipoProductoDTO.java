package ar.com.wuik.sistema.reportes.entities;

import java.math.BigDecimal;

public class DetalleTipoProductoDTO {

	private String tipoProducto;
	private BigDecimal neto;
	private BigDecimal iva;

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public BigDecimal getNeto() {
		return neto;
	}

	public void setNeto(BigDecimal neto) {
		this.neto = neto;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

}
