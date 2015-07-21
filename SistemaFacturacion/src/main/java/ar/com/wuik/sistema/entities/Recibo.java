package ar.com.wuik.sistema.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "recibos")
public class Recibo extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "ID_CLIENTE", nullable = false, insertable = false, updatable = false)
	private Cliente cliente;
	@Column(name = "ID_CLIENTE")
	private Long idCliente;
	@Column(name = "FECHA")
	private Date fecha;
	@Column(name = "NUMERO")
	private Long numero;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "recibos_facturas", joinColumns = { @JoinColumn(name = "ID_RECIBO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_FACTURA", nullable = false, updatable = false) })
	private Set<Factura> facturas;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "recibos_notas_debito", joinColumns = { @JoinColumn(name = "ID_RECIBO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_NOTA_DEBITO", nullable = false, updatable = false) })
	private Set<NotaDebito> notasDebito;
	
//	private boolean entrega;
//	private List<PagoReciboCheque> pagosCheque;
//	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
//	@JoinColumn(name = "ID_PAGO_EFECTIVO", nullable = false, insertable = false, updatable = false)
//	private PagoReciboEfectivo pagoEfectivo;
//	private List<PagoReciboNotaCredito> pagosNotasCredito;

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

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public Set<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(Set<Factura> facturas) {
		this.facturas = facturas;
	}

	public Set<NotaDebito> getNotasDebito() {
		return notasDebito;
	}

	public void setNotasDebito(Set<NotaDebito> notasDebito) {
		this.notasDebito = notasDebito;
	}

//	public List<PagoReciboCheque> getPagosCheque() {
//		return pagosCheque;
//	}
//
//	public void setPagosCheque(List<PagoReciboCheque> pagosCheque) {
//		this.pagosCheque = pagosCheque;
//	}
//
//	public List<PagoReciboEfectivo> getPagosEfectivo() {
//		return pagosEfectivo;
//	}
//
//	public void setPagosEfectivo(List<PagoReciboEfectivo> pagosEfectivo) {
//		this.pagosEfectivo = pagosEfectivo;
//	}
//
//	public List<PagoReciboNotaCredito> getPagosNotasCredito() {
//		return pagosNotasCredito;
//	}
//
//	public void setPagosNotasCredito(
//			List<PagoReciboNotaCredito> pagosNotasCredito) {
//		this.pagosNotasCredito = pagosNotasCredito;
//	}

}
