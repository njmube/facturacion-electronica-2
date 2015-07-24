package ar.com.wuik.sistema.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "pagos_recibos_cheques")
public class PagoReciboCheque extends BaseEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CHEQUE", nullable = false, insertable = false, updatable = false)
	private Cheque cheque;
	@Column(name = "ID_CHEQUE")
	private Long idCheque;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_RECIBO", nullable = false)
	private Recibo recibo;
	@Transient
	private Long temporalId;

	public Cheque getCheque() {
		return cheque;
	}

	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}

	public Long getIdCheque() {
		return idCheque;
	}

	public void setIdCheque(Long idCheque) {
		this.idCheque = idCheque;
	}

	public Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
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

}