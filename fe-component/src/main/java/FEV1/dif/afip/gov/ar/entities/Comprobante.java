package FEV1.dif.afip.gov.ar.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public final class Comprobante {

	private TipoComprobanteEnum tipoComprobante;
	private Date fechaComprobante;
	private Long docNro;
	private long nroComprobante;
	private TipoDocumentoEnum docTipo;
	private TipoConceptoEnum tipoConcepto;
	private BigDecimal importeTotal;
	private BigDecimal importeSubtotal;
	private BigDecimal importeIVA;
	private BigDecimal importeTributos;
	private TipoMonedaEnum tipoMoneda;
	private BigDecimal cotizacion;
	private List<AlicuotaIVA> alicuotas;
	private List<TributoComprobante> tributos;
	private List<ComprobanteAsociado> comprobantesAsociados;
	private List<Item> items;

	// Datos para vista.
	private String ptoVentaFormato;
	private String nroComprobanteFormato;
	private String estado;
	private Date fechaVtoCAE;
	private Date fecha;

	// Datos para comprobantes asociados.
	private int ptoVenta;
	private String cae;

	public TipoComprobanteEnum getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(TipoComprobanteEnum tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public Date getFechaComprobante() {
		return fechaComprobante;
	}

	public void setFechaComprobante(Date fechaComprobante) {
		this.fechaComprobante = fechaComprobante;
	}

	public Long getDocNro() {
		return docNro;
	}

	public void setDocNro(Long docNro) {
		this.docNro = docNro;
	}

	public TipoDocumentoEnum getDocTipo() {
		return docTipo;
	}

	public void setDocTipo(TipoDocumentoEnum docTipo) {
		this.docTipo = docTipo;
	}

	public TipoConceptoEnum getTipoConcepto() {
		return tipoConcepto;
	}

	public void setTipoConcepto(TipoConceptoEnum tipoConcepto) {
		this.tipoConcepto = tipoConcepto;
	}

	public BigDecimal getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(BigDecimal importeTotal) {
		this.importeTotal = importeTotal;
	}

	public BigDecimal getImporteSubtotal() {
		return importeSubtotal;
	}

	public void setImporteSubtotal(BigDecimal importeSubtotal) {
		this.importeSubtotal = importeSubtotal;
	}

	public BigDecimal getImporteIVA() {
		return importeIVA;
	}

	public void setImporteIVA(BigDecimal importeIVA) {
		this.importeIVA = importeIVA;
	}

	public List<AlicuotaIVA> getAlicuotas() {
		return alicuotas;
	}

	public void setAlicuotas(List<AlicuotaIVA> alicuotas) {
		this.alicuotas = alicuotas;
	}

	public List<ComprobanteAsociado> getComprobantesAsociados() {
		return comprobantesAsociados;
	}

	public void setComprobantesAsociados(
			List<ComprobanteAsociado> comprobantesAsociados) {
		this.comprobantesAsociados = comprobantesAsociados;
	}

	public int getPtoVenta() {
		return ptoVenta;
	}

	public void setPtoVenta(int ptoVenta) {
		this.ptoVenta = ptoVenta;
	}

	public String getCae() {
		return cae;
	}

	public void setCae(String cae) {
		this.cae = cae;
	}

	public TipoMonedaEnum getTipoMoneda() {
		return tipoMoneda;
	}

	public void setTipoMoneda(TipoMonedaEnum tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	public BigDecimal getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(BigDecimal cotizacion) {
		this.cotizacion = cotizacion;
	}

	public long getNroComprobante() {
		return nroComprobante;
	}

	public void setNroComprobante(long nroComprobante) {
		this.nroComprobante = nroComprobante;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getPtoVentaFormato() {
		return ptoVentaFormato;
	}

	public void setPtoVentaFormato(String ptoVentaFormato) {
		this.ptoVentaFormato = ptoVentaFormato;
	}

	public String getNroComprobanteFormato() {
		return nroComprobanteFormato;
	}

	public void setNroComprobanteFormato(String nroComprobanteFormato) {
		this.nroComprobanteFormato = nroComprobanteFormato;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFechaVtoCAE() {
		return fechaVtoCAE;
	}

	public void setFechaVtoCAE(Date fechaVtoCAE) {
		this.fechaVtoCAE = fechaVtoCAE;
	}

	public List<TributoComprobante> getTributos() {
		return tributos;
	}

	public void setTributos(List<TributoComprobante> tributos) {
		this.tributos = tributos;
	}

	public BigDecimal getImporteTributos() {
		return importeTributos;
	}

	public void setImporteTributos(BigDecimal importeTributos) {
		this.importeTributos = importeTributos;
	}

}
