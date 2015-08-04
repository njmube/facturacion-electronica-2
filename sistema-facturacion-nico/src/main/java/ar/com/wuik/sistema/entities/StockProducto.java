package ar.com.wuik.sistema.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "stocks_productos")
public class StockProducto extends BaseEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PRODUCTO", nullable = true, insertable = false, updatable = false)
	private Producto producto;
	@Column(name = "ID_PRODUCTO")
	private Long idProducto;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PROVEEDOR", nullable = true, insertable = false, updatable = false)
	private Proveedor proveedor;
	@Column(name = "ID_PROVEEDOR")
	private Long idProveedor;
	@Column(name = "CANTIDAD")
	private Integer cantidad;
	@Column(name = "FACTURA")
	private String factura;
	@Column(name = "FECHA")
	private Date fecha;

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getFactura() {
		return factura;
	}

	public void setFactura(String factura) {
		this.factura = factura;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

}
