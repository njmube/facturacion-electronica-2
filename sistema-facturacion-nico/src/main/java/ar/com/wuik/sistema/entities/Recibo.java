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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "recibos")
public class Recibo extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE", nullable = false, insertable = false, updatable = false)
	private Cliente cliente;
	@Column(name = "ID_CLIENTE")
	private Long idCliente;
	@Column(name = "FECHA")
	private Date fecha;
	@Column(name = "NUMERO")
	private String numero;
	@Column(name = "OBSERVACIONES")
	private String observaciones;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "recibos_comprobantes", joinColumns = { @JoinColumn(name = "ID_RECIBO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_COMPROBANTE", nullable = false, updatable = false) })
	private Set<Comprobante> comprobantes;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "recibo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PagoReciboEfectivo> pagosEfectivo;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "recibo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PagoReciboCheque> pagosCheque;
	@Column(name = "TOTAL")
	private BigDecimal total;

	public Recibo() {
		this.pagosCheque = new ArrayList<PagoReciboCheque>();
		this.comprobantes = new HashSet<Comprobante>();
		this.pagosEfectivo = new ArrayList<PagoReciboEfectivo>();
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public List<PagoReciboEfectivo> getPagosEfectivo() {
		return pagosEfectivo;
	}

	public void setPagosEfectivo(List<PagoReciboEfectivo> pagosEfectivo) {
		this.pagosEfectivo = pagosEfectivo;
	}

	public List<PagoReciboCheque> getPagosCheque() {
		return pagosCheque;
	}

	public void setPagosCheque(List<PagoReciboCheque> pagosCheque) {
		this.pagosCheque = pagosCheque;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Set<Comprobante> getComprobantes() {
		return comprobantes;
	}

	public void setComprobantes(Set<Comprobante> comprobantes) {
		this.comprobantes = comprobantes;
	}

}
