package FEV1.dif.afip.gov.ar.entities;

import java.util.Date;
import java.util.List;

public final class Resultado {

	public enum Estado {
		A, R, P
	}

	private String cae;
	private Date fecha;
	private Date fechaVtoCAE;
	private Estado estado;
	private long nroComprobante;
	private long ptoVta;
	private List<String> errores;

	public String getCae() {
		return cae;
	}

	public void setCae(String cae) {
		this.cae = cae;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public long getNroComprobante() {
		return nroComprobante;
	}

	public void setNroComprobante(long nroComprobante) {
		this.nroComprobante = nroComprobante;
	}

	public Date getFechaVtoCAE() {
		return fechaVtoCAE;
	}

	public void setFechaVtoCAE(Date fechaVtoCAE) {
		this.fechaVtoCAE = fechaVtoCAE;
	}

	public List<String> getErrores() {
		return errores;
	}

	public void setErrores(List<String> errores) {
		this.errores = errores;
	}

	public long getPtoVta() {
		return ptoVta;
	}

	public void setPtoVta(long ptoVta) {
		this.ptoVta = ptoVta;
	}
	
	public String getMensajeErrores(){
		StringBuilder str = new StringBuilder();
		for (String error : errores) {
			str.append("ERROR: " + error);
			str.append("\n");
		}
		return str.toString();
	}
	

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("CAE: " + cae);
		str.append("\n");
		str.append("FECHA: " + fecha);
		str.append("\n");
		str.append("ESTADO: " + estado);
		str.append("\n");
		str.append("NRO. COMP.: " + nroComprobante);
		str.append("\n");
		str.append("VTO. CAE: " + fechaVtoCAE);
		str.append("\n");
		str.append("PTO. VTA.: " + ptoVta);
		str.append("\n");
		for (String error : errores) {
			str.append("ERROR: " + error);
			str.append("\n");
		}
		return str.toString();
	}
}
