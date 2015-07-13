package ar.com.wuik.sistema.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "detalles_notas_creditos")
public class DetalleNotaCredito extends BaseEntity {

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
	@JoinColumn(name = "ID_NOTA_CREDITO", nullable = true)
	private NotaCredito notaCredito;
	@Transient
	private Long temporalId;
	@Column(name = "COMENTARIO")
	private String comentario;
	@Column(name = "DETALLE")
	private String detalle;


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

	public NotaCredito getNotaCredito() {
		return notaCredito;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}

	public Long getTemporalId() {
		return temporalId;
	}

	public void setTemporalId(Long temporalId) {
		this.temporalId = temporalId;
	}

	public BigDecimal getSubtotal() {
		return getPrecio().multiply(new BigDecimal(getCantidad()));
	}

	public BigDecimal getTotal() {
		BigDecimal iva = getIva().add(new BigDecimal(100));
		return getSubtotal().multiply(iva).divide(new BigDecimal(100));
	}

	public BigDecimal getTotalIVA() {
		return getTotal().subtract(getSubtotal());
	}

	public Long getCoalesceId() {
		return (null != getId()) ? getId() : temporalId;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	
}
