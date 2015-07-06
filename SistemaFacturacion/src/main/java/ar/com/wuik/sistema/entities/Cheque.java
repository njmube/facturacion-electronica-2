package ar.com.wuik.sistema.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cheques")
public class Cheque extends BaseEntity {

	@Column(name = "NUMERO")
	private String numero;
	@Column(name = "FECHA_EMISION")
	private Date fechaEmision;
	@Column(name = "FECHA_PAGO")
	private Date fechaPago;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_BANCO", nullable = true, insertable = false, updatable = false)
	private Banco banco;
	@Column(name = "ID_BANCO")
	private Long idBanco;
	@Column(name = "IMPORTE")
	private BigDecimal importe;
	@Column(name = "RECIBIDO_DE")
	private String recibidoDe;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public String getRecibidoDe() {
		return recibidoDe;
	}

	public void setRecibidoDe(String recibidoDe) {
		this.recibidoDe = recibidoDe;
	}

}
