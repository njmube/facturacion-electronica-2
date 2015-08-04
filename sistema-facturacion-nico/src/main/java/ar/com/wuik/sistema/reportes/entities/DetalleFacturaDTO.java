package ar.com.wuik.sistema.reportes.entities;

import java.math.BigDecimal;

public class DetalleFacturaDTO {

	private String codigo;
	private String producto;
	private int cantidad;
	private BigDecimal precioUnit;
	private BigDecimal subtotal;
	private BigDecimal alicuota;
	private BigDecimal subtotalConIVA;
	private String comentario;

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

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecioUnit() {
		return precioUnit;
	}

	public void setPrecioUnit(BigDecimal precioUnit) {
		this.precioUnit = precioUnit;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getAlicuota() {
		return alicuota;
	}

	public void setAlicuota(BigDecimal alicuota) {
		this.alicuota = alicuota;
	}

	public BigDecimal getSubtotalConIVA() {
		return subtotalConIVA;
	}

	public void setSubtotalConIVA(BigDecimal subtotalConIVA) {
		this.subtotalConIVA = subtotalConIVA;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

}
