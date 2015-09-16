package ar.com.wuik.sistema.reportes.entities;

import java.math.BigDecimal;


public class DetalleRemitoDTO {

	private String codigo;
	private String producto;
	private BigDecimal cantidad;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

}
