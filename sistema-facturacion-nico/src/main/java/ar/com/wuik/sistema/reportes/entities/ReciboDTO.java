package ar.com.wuik.sistema.reportes.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.PagoReciboCheque;
import ar.com.wuik.sistema.entities.PagoReciboEfectivo;
import ar.com.wuik.swing.utils.WUtils;

public class ReciboDTO {

	private String compNro;
	private Date fechaEmision;
	private String clienteCuit;
	private String clienteDomicilio;
	private String clienteRazonSocial;
	private String clienteCondIVA;

	private BigDecimal total;
	private BigDecimal efectivo;
	private String totalLetras;

	private String nroCh1 = "-";
	private String nroCh2 = "-";
	private String nroCh3 = "-";
	private String nroCh4 = "-";

	private String bancoCh1 = "-";
	private String bancoCh2 = "-";
	private String bancoCh3 = "-";
	private String bancoCh4 = "-";

	private BigDecimal totalCh1;
	private BigDecimal totalCh2;
	private BigDecimal totalCh3;
	private BigDecimal totalCh4;

	private Date fechaComp1;
	private Date fechaComp2;
	private Date fechaComp3;
	private Date fechaComp4;
	private Date fechaComp5;
	private Date fechaComp6;

	private String nroComp1 = "-";
	private String nroComp2 = "-";
	private String nroComp3 = "-";
	private String nroComp4 = "-";
	private String nroComp5 = "-";
	private String nroComp6 = "-";

	private BigDecimal totalComp1;
	private BigDecimal totalComp2;
	private BigDecimal totalComp3;
	private BigDecimal totalComp4;
	private BigDecimal totalComp5;
	private BigDecimal totalComp6;
	private BigDecimal totalComp;

	public String getCompNro() {
		return compNro;
	}

	public void setCompNro(String compNro) {
		this.compNro = compNro;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getClienteCuit() {
		return clienteCuit;
	}

	public void setClienteCuit(String clienteCuit) {
		this.clienteCuit = clienteCuit;
	}

	public String getClienteDomicilio() {
		return clienteDomicilio;
	}

	public void setClienteDomicilio(String clienteDomicilio) {
		this.clienteDomicilio = clienteDomicilio;
	}

	public String getClienteRazonSocial() {
		return clienteRazonSocial;
	}

	public void setClienteRazonSocial(String clienteRazonSocial) {
		this.clienteRazonSocial = clienteRazonSocial;
	}

	public String getClienteCondIVA() {
		return clienteCondIVA;
	}

	public void setClienteCondIVA(String clienteCondIVA) {
		this.clienteCondIVA = clienteCondIVA;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getNroCh1() {
		return nroCh1;
	}

	public void setNroCh1(String nroCh1) {
		this.nroCh1 = nroCh1;
	}

	public String getNroCh2() {
		return nroCh2;
	}

	public void setNroCh2(String nroCh2) {
		this.nroCh2 = nroCh2;
	}

	public String getNroCh3() {
		return nroCh3;
	}

	public void setNroCh3(String nroCh3) {
		this.nroCh3 = nroCh3;
	}

	public String getNroCh4() {
		return nroCh4;
	}

	public void setNroCh4(String nroCh4) {
		this.nroCh4 = nroCh4;
	}

	public String getBancoCh1() {
		return bancoCh1;
	}

	public void setBancoCh1(String bancoCh1) {
		this.bancoCh1 = bancoCh1;
	}

	public String getBancoCh2() {
		return bancoCh2;
	}

	public void setBancoCh2(String bancoCh2) {
		this.bancoCh2 = bancoCh2;
	}

	public String getBancoCh3() {
		return bancoCh3;
	}

	public void setBancoCh3(String bancoCh3) {
		this.bancoCh3 = bancoCh3;
	}

	public String getBancoCh4() {
		return bancoCh4;
	}

	public void setBancoCh4(String bancoCh4) {
		this.bancoCh4 = bancoCh4;
	}

	public BigDecimal getTotalCh1() {
		return totalCh1;
	}

	public void setTotalCh1(BigDecimal totalCh1) {
		this.totalCh1 = totalCh1;
	}

	public BigDecimal getTotalCh2() {
		return totalCh2;
	}

	public void setTotalCh2(BigDecimal totalCh2) {
		this.totalCh2 = totalCh2;
	}

	public BigDecimal getTotalCh3() {
		return totalCh3;
	}

	public void setTotalCh3(BigDecimal totalCh3) {
		this.totalCh3 = totalCh3;
	}

	public BigDecimal getTotalCh4() {
		return totalCh4;
	}

	public void setTotalCh4(BigDecimal totalCh4) {
		this.totalCh4 = totalCh4;
	}

	public Date getFechaComp1() {
		return fechaComp1;
	}

	public void setFechaComp1(Date fechaComp1) {
		this.fechaComp1 = fechaComp1;
	}

	public Date getFechaComp2() {
		return fechaComp2;
	}

	public void setFechaComp2(Date fechaComp2) {
		this.fechaComp2 = fechaComp2;
	}

	public Date getFechaComp3() {
		return fechaComp3;
	}

	public void setFechaComp3(Date fechaComp3) {
		this.fechaComp3 = fechaComp3;
	}

	public Date getFechaComp4() {
		return fechaComp4;
	}

	public void setFechaComp4(Date fechaComp4) {
		this.fechaComp4 = fechaComp4;
	}

	public Date getFechaComp5() {
		return fechaComp5;
	}

	public void setFechaComp5(Date fechaComp5) {
		this.fechaComp5 = fechaComp5;
	}

	public Date getFechaComp6() {
		return fechaComp6;
	}

	public void setFechaComp6(Date fechaComp6) {
		this.fechaComp6 = fechaComp6;
	}

	public String getNroComp1() {
		return nroComp1;
	}

	public void setNroComp1(String nroComp1) {
		this.nroComp1 = nroComp1;
	}

	public String getNroComp2() {
		return nroComp2;
	}

	public void setNroComp2(String nroComp2) {
		this.nroComp2 = nroComp2;
	}

	public String getNroComp3() {
		return nroComp3;
	}

	public void setNroComp3(String nroComp3) {
		this.nroComp3 = nroComp3;
	}

	public String getNroComp4() {
		return nroComp4;
	}

	public void setNroComp4(String nroComp4) {
		this.nroComp4 = nroComp4;
	}

	public String getNroComp5() {
		return nroComp5;
	}

	public void setNroComp5(String nroComp5) {
		this.nroComp5 = nroComp5;
	}

	public String getNroComp6() {
		return nroComp6;
	}

	public void setNroComp6(String nroComp6) {
		this.nroComp6 = nroComp6;
	}

	public BigDecimal getTotalComp1() {
		return totalComp1;
	}

	public void setTotalComp1(BigDecimal totalComp1) {
		this.totalComp1 = totalComp1;
	}

	public BigDecimal getTotalComp2() {
		return totalComp2;
	}

	public void setTotalComp2(BigDecimal totalComp2) {
		this.totalComp2 = totalComp2;
	}

	public BigDecimal getTotalComp3() {
		return totalComp3;
	}

	public void setTotalComp3(BigDecimal totalComp3) {
		this.totalComp3 = totalComp3;
	}

	public BigDecimal getTotalComp4() {
		return totalComp4;
	}

	public void setTotalComp4(BigDecimal totalComp4) {
		this.totalComp4 = totalComp4;
	}

	public BigDecimal getTotalComp5() {
		return totalComp5;
	}

	public void setTotalComp5(BigDecimal totalComp5) {
		this.totalComp5 = totalComp5;
	}

	public BigDecimal getTotalComp6() {
		return totalComp6;
	}

	public void setTotalComp6(BigDecimal totalComp6) {
		this.totalComp6 = totalComp6;
	}

	public BigDecimal getTotalComp() {
		return totalComp;
	}

	public void setTotalComp(BigDecimal totalComp) {
		this.totalComp = totalComp;
	}

	public String getTotalLetras() {
		return totalLetras;
	}

	public void setTotalLetras(String totalLetras) {
		this.totalLetras = totalLetras;
	}

	public void setCheques(List<PagoReciboCheque> pagosCheque) {

		if (WUtils.isNotEmpty(pagosCheque)) {
			if (pagosCheque.size() >= 1) {
				PagoReciboCheque pagoReciboCheque = pagosCheque.get(0);
				setBancoCh1(pagoReciboCheque.getCheque().getBanco().getNombre());
				setNroCh1(pagoReciboCheque.getCheque().getNumero());
				setTotalCh1(pagoReciboCheque.getCheque().getImporte());
			}

			if (pagosCheque.size() >= 2) {
				PagoReciboCheque pagoReciboCheque = pagosCheque.get(1);
				setBancoCh2(pagoReciboCheque.getCheque().getBanco().getNombre());
				setNroCh2(pagoReciboCheque.getCheque().getNumero());
				setTotalCh2(pagoReciboCheque.getCheque().getImporte());
			}

			if (pagosCheque.size() >= 3) {
				PagoReciboCheque pagoReciboCheque = pagosCheque.get(2);
				setBancoCh3(pagoReciboCheque.getCheque().getBanco().getNombre());
				setNroCh3(pagoReciboCheque.getCheque().getNumero());
				setTotalCh3(pagoReciboCheque.getCheque().getImporte());
			}

			if (pagosCheque.size() >= 4) {
				PagoReciboCheque pagoReciboCheque = pagosCheque.get(3);
				setBancoCh4(pagoReciboCheque.getCheque().getBanco().getNombre());
				setNroCh4(pagoReciboCheque.getCheque().getNumero());
				setTotalCh4(pagoReciboCheque.getCheque().getImporte());
			}
		}
	}

	public void setEfectivo(List<PagoReciboEfectivo> pagosEfectivo) {

		if (WUtils.isNotEmpty(pagosEfectivo)) {
			PagoReciboEfectivo pagoReciboEfectivo = pagosEfectivo.get(0);
			setEfectivo(pagoReciboEfectivo.getTotal());
		}
	}

	public BigDecimal getEfectivo() {
		return efectivo;
	}

	public void setEfectivo(BigDecimal efectivo) {
		this.efectivo = efectivo;
	}

	public void setComprobantes(List<Comprobante> comprobantes) {


		BigDecimal totalComprobantes = BigDecimal.ZERO;

		if (WUtils.isNotEmpty(comprobantes)) {
			for (Comprobante comprobante : comprobantes) {
				totalComprobantes = totalComprobantes.add(comprobante.getTotal());
			}
		}

		if (comprobantes.size() >= 1) {
			Comprobante comp = comprobantes.get(0);
			setNroComp1(comp.getNroCompFormato());
			setFechaComp1(comp.getFechaVenta());
			setTotalComp1(comp.getTotal());
		}

		if (comprobantes.size() >= 2) {
			Comprobante comp = comprobantes.get(1);
			setNroComp2(comp.getNroCompFormato());
			setFechaComp2(comp.getFechaVenta());
			setTotalComp2(comp.getTotal());
		}

		if (comprobantes.size() >= 3) {
			Comprobante comp = comprobantes.get(2);
			setNroComp3(comp.getNroCompFormato());
			setFechaComp3(comp.getFechaVenta());
			setTotalComp3(comp.getTotal());
		}

		if (comprobantes.size() >= 4) {
			Comprobante comp = comprobantes.get(3);
			setNroComp4(comp.getNroCompFormato());
			setFechaComp4(comp.getFechaVenta());
			setTotalComp4(comp.getTotal());
		}

		if (comprobantes.size() >= 5) {
			Comprobante comp = comprobantes.get(4);
			setNroComp5(comp.getNroCompFormato());
			setFechaComp5(comp.getFechaVenta());
			setTotalComp5(comp.getTotal());
		}

		if (comprobantes.size() >= 6) {
			Comprobante comp = comprobantes.get(5);
			setNroComp6(comp.getNroCompFormato());
			setFechaComp6(comp.getFechaVenta());
			setTotalComp6(comp.getTotal());
		}

		setTotalComp(totalComprobantes);

	}
}

