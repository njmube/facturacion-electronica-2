package ar.com.wuik.sistema.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.wuik.sistema.entities.enums.TipoIVAEnum;

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
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ID_TIPO_IVA")
	private TipoIVAEnum tipoIVA;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_FACTURA", nullable = true)
	private Factura factura;
	@Column(name = "DETALLE")
	private String detalle;
	@Transient
	private Long temporalId;

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

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public BigDecimal getSubtotal() {
		return getPrecio().multiply(new BigDecimal(getCantidad()));
	}

	public BigDecimal getTotal() {
		BigDecimal iva = getTipoIVA().getImporte().add(new BigDecimal(100));
		return getSubtotal().multiply(iva).divide(new BigDecimal(100));
	}

	public BigDecimal getTotalIVA() {
		return getTotal().subtract(getSubtotal());
	}

	public Long getCoalesceId() {
		return (null != getId()) ? getId() : temporalId;
	}

	public Long getTemporalId() {
		return temporalId;
	}

	public void setTemporalId(Long temporalId) {
		this.temporalId = temporalId;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public TipoIVAEnum getTipoIVA() {
		return tipoIVA;
	}

	public void setTipoIVA(TipoIVAEnum tipoIVA) {
		this.tipoIVA = tipoIVA;
	}

}
