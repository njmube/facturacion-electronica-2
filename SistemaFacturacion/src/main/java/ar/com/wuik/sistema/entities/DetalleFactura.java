package ar.com.wuik.sistema.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "detalles_facturas")
public class DetalleFactura extends BaseEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PRODUCTO", nullable = true)
	private Producto producto;
	@Column(name = "CANTIDAD")
	private Integer cantidad;
	@Column(name = "PRECIO")
	private BigDecimal precio;
	@Column(name = "PORC_IVA")
	private BigDecimal iva;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_FACTURA", nullable = true)
	private Factura factura;

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

}
