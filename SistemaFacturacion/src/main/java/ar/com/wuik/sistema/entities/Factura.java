package ar.com.wuik.sistema.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.com.wuik.swing.utils.WUtils;

@Entity
@Table(name = "facturas")
public class Factura extends BaseEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CLIENTE", nullable = true, insertable = false, updatable = false)
	private Cliente cliente;
	@Column(name = "ID_CLIENTE")
	private Long idCliente;
	@Column(name = "FECHA_VENTA")
	private Date fechaVenta;
	@Column(name = "FECHA_CAE")
	private Date fechaCAE;
	@Column(name = "CAE")
	private String cae;
	@Column(name = "ACTIVO")
	private boolean activo = Boolean.TRUE;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "factura", cascade = CascadeType.ALL)
	private List<Remito> remitos;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "factura", cascade = CascadeType.ALL)
	private List<DetalleFactura> detalles;
	@Column(name = "SUBTOTAL")
	private BigDecimal subtotal;
	@Column(name = "IVA")
	private BigDecimal iva;
	@Column(name = "TOTAL")
	private BigDecimal total;
	@Column(name = "PTO_VENTA")
	private Long ptoVenta;
	@Column(name = "NUMERO_COMPROBANTE")
	private Long nroComprobante;
	@Column(name = "OBSERVACIONES")
	private String observaciones;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_NOTA_CREDITO", nullable = true)
	private NotaCredito notaCredito;

	public Factura() {
		this.detalles = new ArrayList<DetalleFactura>();
		this.remitos = new ArrayList<Remito>();
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Date getFechaVenta() {
		return fechaVenta;
	}

	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
	}

	public Date getFechaCAE() {
		return fechaCAE;
	}

	public void setFechaCAE(Date fechaCAE) {
		this.fechaCAE = fechaCAE;
	}

	public String getCae() {
		return cae;
	}

	public void setCae(String cae) {
		this.cae = cae;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public List<Remito> getRemitos() {
		return remitos;
	}

	public void setRemitos(List<Remito> remitos) {
		this.remitos = remitos;
	}

	public List<DetalleFactura> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleFactura> detalles) {
		this.detalles = detalles;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Long getPtoVenta() {
		return ptoVenta;
	}

	public void setPtoVenta(Long ptoVenta) {
		this.ptoVenta = ptoVenta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Long getNroComprobante() {
		return nroComprobante;
	}

	public void setNroComprobante(Long nroComprobante) {
		this.nroComprobante = nroComprobante;
	}

	public String getEstadoPago() {
		return "IMPAGA";
	}

	public boolean isFacturada() {
		return WUtils.isNotEmpty(this.cae);
	}

	public NotaCredito getNotaCredito() {
		return notaCredito;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}

}