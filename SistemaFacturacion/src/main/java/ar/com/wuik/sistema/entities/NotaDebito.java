package ar.com.wuik.sistema.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;

@Entity
@Table(name = "notas_debitos")
public class NotaDebito extends BaseEntity {

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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "notaDebito", cascade = CascadeType.ALL)
	private List<DetalleNotaDebito> detalles;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "notas_debitos_facturas", joinColumns = { @JoinColumn(name = "ID_NOTA_DEBITO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_FACTURA", nullable = false, updatable = false) })
	private Set<Factura> facturas;
	@Column(name = "SUBTOTAL")
	private BigDecimal subtotal;
	@Column(name = "IVA")
	private BigDecimal iva;
	@Column(name = "TOTAL")
	private BigDecimal total;
	@Column(name = "PTO_VENTA")
	private String ptoVenta;
	@Column(name = "NUMERO_COMPROBANTE")
	private String nroComprobante;
	@Column(name = "OBSERVACIONES")
	private String observaciones;
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ESTADO_FACTURACION")
	private EstadoFacturacion estadoFacturacion;
	@Formula(value = "(select IF(count(*) > 1, 1, 0) from recibos_notas_debito rnd where rnd.ID_NOTA_DEBITO = ID)")
	private boolean paga;

	public NotaDebito() {
		this.detalles = new ArrayList<DetalleNotaDebito>();
		this.facturas = new HashSet<Factura>();
		this.estadoFacturacion = EstadoFacturacion.SIN_FACTURAR;
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

	public List<DetalleNotaDebito> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleNotaDebito> detalles) {
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

	public String getPtoVenta() {
		return ptoVenta;
	}

	public void setPtoVenta(String ptoVenta) {
		this.ptoVenta = ptoVenta;
	}

	public String getNroComprobante() {
		return nroComprobante;
	}

	public void setNroComprobante(String nroComprobante) {
		this.nroComprobante = nroComprobante;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Set<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(Set<Factura> facturas) {
		this.facturas = facturas;
	}

	public EstadoFacturacion getEstadoFacturacion() {
		return estadoFacturacion;
	}

	public void setEstadoFacturacion(EstadoFacturacion estadoFacturacion) {
		this.estadoFacturacion = estadoFacturacion;
	}

	public String getEstado() {
		if (activo) {
			return "ACTIVA - " + getEstadoFacturacion().getDenominacion();
		} else {
			return "ANULADA - " + getEstadoFacturacion().getDenominacion();
		}
	}

	public boolean isPaga() {
		return paga;
	}

	public void setPaga(boolean paga) {
		this.paga = paga;
	}

}
