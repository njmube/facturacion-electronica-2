package ar.com.wuik.sistema.entities;

import java.util.ArrayList;
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

import org.hibernate.annotations.Formula;

@Entity
@Table(name = "remitos")
public class Remito extends BaseEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_COMPROBANTE", nullable = true)
	private Comprobante comprobante;
	@Column(name = "FECHA")
	private Date fecha;
	@Column(name = "NUMERO")
	private String numero;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE", nullable = false, insertable = false, updatable = false)
	private Cliente cliente;
	@Column(name = "ID_CLIENTE")
	private Long idCliente;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "remito", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DetalleRemito> detalles;
	@Column(name = "OBSERVACIONES")
	private String observaciones;
	@Formula(value = "(SELECT sum(dr.CANTIDAD) FROM detalles_remitos dr WHERE dr.ID_REMITO = ID)")
	private int cantidad;
	@Column(name = "ACTIVO")
	private boolean activo;

	public Remito() {
		this.detalles = new ArrayList<DetalleRemito>();
		this.activo = Boolean.TRUE;
	}

	public Comprobante getComprobante() {
		return comprobante;
	}

	public void setComprobante(Comprobante comprobante) {
		this.comprobante = comprobante;
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

	public List<DetalleRemito> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleRemito> detalles) {
		this.detalles = detalles;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

}
