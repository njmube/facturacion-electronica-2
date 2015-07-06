package ar.com.wuik.sistema.entities;

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
	@Column(name = "FECHA_VTO")
	private Date fechaVto;
	@Column(name = "FECHA_CAE")
	private Date fechaCAE;
	@Column(name = "NUMERO")
	private String numero;
	@Column(name = "CAE")
	private String cae;
	@Column(name = "ACTIVO")
	private boolean activo;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "notaDebito", cascade = CascadeType.ALL)
	private List<DetalleNotaDebito> detalles;

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

	public Date getFechaVto() {
		return fechaVto;
	}

	public void setFechaVto(Date fechaVto) {
		this.fechaVto = fechaVto;
	}

	public Date getFechaCAE() {
		return fechaCAE;
	}

	public void setFechaCAE(Date fechaCAE) {
		this.fechaCAE = fechaCAE;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
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

}
