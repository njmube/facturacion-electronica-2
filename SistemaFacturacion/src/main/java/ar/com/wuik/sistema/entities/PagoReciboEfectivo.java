package ar.com.wuik.sistema.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pagos_recibos_efectivo")
public class PagoReciboEfectivo extends BaseEntity {

	@Column(name = "TOTAL")
	private BigDecimal total;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_RECIBO", nullable = false)
	private Recibo recibo;

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}

}