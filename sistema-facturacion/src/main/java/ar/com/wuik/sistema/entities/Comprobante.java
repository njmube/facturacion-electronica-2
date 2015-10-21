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
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.entities.enums.TipoLetraComprobante;

@Entity
@Table(name = "comprobantes")
public class Comprobante extends BaseEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CLIENTE", nullable = false, insertable = false, updatable = false)
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
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "comprobante", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<Remito> remitos;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "comprobante", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DetalleComprobante> detalles;
	@Column(name = "SUBTOTAL")
	private BigDecimal subtotal;
	@Column(name = "IVA")
	private BigDecimal iva;
	@Column(name = "TRIBUTOS")
	private BigDecimal totalTributos;
	@Column(name = "TOTAL")
	private BigDecimal total;
	@Column(name = "PTO_VENTA")
	private String ptoVenta;
	@Column(name = "NUMERO_COMPROBANTE")
	private String nroComprobante;
	@Column(name = "OBSERVACIONES")
	private String observaciones;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "comprobantes_asociados", joinColumns = { @JoinColumn(name = "ID_COMPROBANTE_ASOC", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_COMPROBANTE", nullable = false, updatable = false) })
	private Set<Comprobante> comprobantesAsociados;
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ESTADO_FACTURACION")
	private EstadoFacturacion estadoFacturacion;
	@Formula(value = "(select IF(count(*) >= 1, 1, 0) from recibos_comprobantes rc where rc.ID_COMPROBANTE = ID)")
	private boolean pago;
	@Column(name = "NUMERO_COMP_FORMATO")
	private String nroCompFormato;
	@Column(name = "COD_BARRAS")
	private String codBarras;
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_LETRA")
	private TipoLetraComprobante tipoLetraComprobante;
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO")
	private TipoComprobante tipoComprobante;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "comprobante", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TributoComprobante> tributos;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PROVEEDOR", nullable = false, insertable = false, updatable = false)
	private Proveedor proveedor;
	@Column(name = "ID_PROVEEDOR")
	private Long idProveedor;

	public Comprobante() {
		this.detalles = new ArrayList<DetalleComprobante>();
		this.estadoFacturacion = EstadoFacturacion.SIN_FACTURAR;
		this.comprobantesAsociados = new HashSet<Comprobante>();
		this.remitos = new ArrayList<Remito>();
		this.tributos = new ArrayList<TributoComprobante>();
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

	public List<DetalleComprobante> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleComprobante> detalles) {
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

	public Set<Comprobante> getComprobantesAsociados() {
		return comprobantesAsociados;
	}

	public void setComprobantesAsociados(Set<Comprobante> comprobantesAsociados) {
		this.comprobantesAsociados = comprobantesAsociados;
	}

	public EstadoFacturacion getEstadoFacturacion() {
		return estadoFacturacion;
	}

	public void setEstadoFacturacion(EstadoFacturacion estadoFacturacion) {
		this.estadoFacturacion = estadoFacturacion;
	}

	public boolean isPago() {
		return pago;
	}

	public void setPago(boolean pago) {
		this.pago = pago;
	}

	public String getNroCompFormato() {
		return nroCompFormato;
	}

	public void setNroCompFormato(String nroCompFormato) {
		this.nroCompFormato = nroCompFormato;
	}

	public String getCodBarras() {
		return codBarras;
	}

	public void setCodBarras(String codBarras) {
		this.codBarras = codBarras;
	}

	public String getEstado() {
		if (activo) {
			return "ACTIVA - " + getEstadoFacturacion().getDenominacion();
		} else {
			return "ANULADA - " + getEstadoFacturacion().getDenominacion();
		}
	}

	public TipoLetraComprobante getTipoLetraComprobante() {
		return tipoLetraComprobante;
	}

	public void setTipoLetraComprobante(
			TipoLetraComprobante tipoLetraComprobante) {
		this.tipoLetraComprobante = tipoLetraComprobante;
	}

	public TipoComprobante getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(TipoComprobante tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public List<TributoComprobante> getTributos() {
		return tributos;
	}

	public void setTributos(List<TributoComprobante> tributos) {
		this.tributos = tributos;
	}

	public BigDecimal getTotalTributos() {
		return totalTributos;
	}

	public void setTotalTributos(BigDecimal totalTributos) {
		this.totalTributos = totalTributos;
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

}
