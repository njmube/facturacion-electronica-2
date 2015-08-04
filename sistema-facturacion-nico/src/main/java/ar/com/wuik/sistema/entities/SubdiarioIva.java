package ar.com.wuik.sistema.entities;

import java.math.BigDecimal;
import java.util.Date;

public class SubdiarioIva {

	private String tipoComprobante;
	private String numero;
	private String cliente;
	private Date fecha;
	private BigDecimal totalIva;
	private boolean suma = Boolean.FALSE;

	public SubdiarioIva(Factura factura) {
		this.suma = Boolean.TRUE;
		this.tipoComprobante = "FACTURA";
		this.numero = factura.getNroCompFormato();
		this.cliente = factura.getCliente().getRazonSocial();
		this.fecha = factura.getFechaVenta();
		this.totalIva = factura.getIva();
	}

	public SubdiarioIva(NotaCredito notaCredito) {
		this.suma = Boolean.FALSE;
		this.tipoComprobante = "NOTA CREDITO";
		this.numero = notaCredito.getNroCompFormato();
		this.cliente = notaCredito.getCliente().getRazonSocial();
		this.fecha = notaCredito.getFechaVenta();
		this.totalIva = notaCredito.getIva();
	}

	public SubdiarioIva(NotaDebito notaDebito) {
		this.suma = Boolean.TRUE;
		this.tipoComprobante = "NOTA DEBITO";
		this.numero = notaDebito.getNroCompFormato();
		this.cliente = notaDebito.getCliente().getRazonSocial();
		this.fecha = notaDebito.getFechaVenta();
		this.totalIva = notaDebito.getIva();
	}

	public String getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getTotalIva() {
		return totalIva;
	}

	public void setTotalIva(BigDecimal totalIva) {
		this.totalIva = totalIva;
	}

	public boolean isSuma() {
		return suma;
	}

	public void setSuma(boolean suma) {
		this.suma = suma;
	}

}
