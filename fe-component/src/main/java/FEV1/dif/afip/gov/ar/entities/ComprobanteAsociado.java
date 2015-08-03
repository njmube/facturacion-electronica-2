package FEV1.dif.afip.gov.ar.entities;

public class ComprobanteAsociado {

	private TipoComprobanteEnum tipoComprobante;
	private int ptoVta;
	private long numero;

	public TipoComprobanteEnum getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(TipoComprobanteEnum tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public int getPtoVta() {
		return ptoVta;
	}

	public void setPtoVta(int ptoVta) {
		this.ptoVta = ptoVta;
	}

	public long getNumero() {
		return numero;
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}

}
