package ar.com.wuik.sistema.entities;

import java.math.BigDecimal;
import java.util.Date;

public class Liquidacion {

	private NotaDebito notaDebito;
	private Factura factura;

	public Liquidacion(Factura factura) {
		this.factura = factura;
	}

	public Liquidacion(NotaDebito notaDebito) {
		this.notaDebito = notaDebito;
	}

	public Long getId() {
		if (null != notaDebito) {
			return notaDebito.getId().longValue() * -1;
		}
		if (null != factura) {
			return factura.getId();
		}
		return null;
	}

	public Date getFecha() {
		if (null != notaDebito) {
			return notaDebito.getFechaVenta();
		}
		if (null != factura) {
			return factura.getFechaVenta();
		}
		return null;
	}

	public String getComprobante() {
		if (null != notaDebito) {
			return notaDebito.getNroCompFormato();
		}
		if (null != factura) {
			return factura.getNroCompFormato();
		}
		return "";
	}

	public String getTipo() {
		if (null != notaDebito) {
			return "Nota de Débito";
		}
		if (null != factura) {
			return "Factura";
		}
		return "";
	}

	public BigDecimal getTotal() {
		if (null != notaDebito) {
			return notaDebito.getTotal();
		}
		if (null != factura) {
			return factura.getTotal();
		}
		return null;
	}

	public Long getIdNotaDebito() {
		if (null != notaDebito) {
			return notaDebito.getId();
		}
		return null;
	}

	public Long getIdFactura() {
		if (null != factura) {
			return factura.getId();
		}
		return null;
	}

	public NotaDebito getNotaDebito() {
		return notaDebito;
	}

	public Factura getFactura() {
		return factura;
	}

}
