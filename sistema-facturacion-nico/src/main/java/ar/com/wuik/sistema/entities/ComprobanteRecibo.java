package ar.com.wuik.sistema.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "comprobantes_recibos")
public class ComprobanteRecibo extends BaseEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_RECIBO", nullable = true, insertable = false, updatable = false)
	private Recibo recibo;
	@Column(name = "ID_RECIBO")
	private Long idRecibo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_FACTURA", nullable = true, insertable = false, updatable = false)
	private Factura factura;
	@Column(name = "ID_FACTURA")
	private Long idFactura;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_NOTA_DEBITO", nullable = true, insertable = false, updatable = false)
	private NotaDebito notaDebito;
	@Column(name = "ID_NOTA_DEBITO")
	private Long idNotaDebito;

	public Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}

	public Long getIdRecibo() {
		return idRecibo;
	}

	public void setIdRecibo(Long idRecibo) {
		this.idRecibo = idRecibo;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public Long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Long idFactura) {
		this.idFactura = idFactura;
	}

	public NotaDebito getNotaDebito() {
		return notaDebito;
	}

	public void setNotaDebito(NotaDebito notaDebito) {
		this.notaDebito = notaDebito;
	}

	public Long getIdNotaDebito() {
		return idNotaDebito;
	}

	public void setIdNotaDebito(Long idNotaDebito) {
		this.idNotaDebito = idNotaDebito;
	}

}
