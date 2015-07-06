package ar.com.wuik.sistema.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pagos_recibos_cheques")
public class PagoReciboCheque extends BaseEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_NOTA_CREDITO", nullable = false, insertable = false, updatable = false)
	private NotaCredito notaCredito;
	@Column(name = "ID_NOTA_CREDITO")
	private Long idNotaCredito;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_RECIBO", nullable = false)
	private Recibo recibo;

	public NotaCredito getNotaCredito() {
		return notaCredito;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}

	public Long getIdNotaCredito() {
		return idNotaCredito;
	}

	public void setIdNotaCredito(Long idNotaCredito) {
		this.idNotaCredito = idNotaCredito;
	}

	public Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}

}