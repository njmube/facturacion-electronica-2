package FEV1.dif.afip.gov.ar.services.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import FEV1.dif.afip.gov.ar.AlicIva;
import FEV1.dif.afip.gov.ar.CbteAsoc;
import FEV1.dif.afip.gov.ar.CbteTipo;
import FEV1.dif.afip.gov.ar.CbteTipoResponse;
import FEV1.dif.afip.gov.ar.Err;
import FEV1.dif.afip.gov.ar.FECAECabRequest;
import FEV1.dif.afip.gov.ar.FECAECabResponse;
import FEV1.dif.afip.gov.ar.FECAEDetRequest;
import FEV1.dif.afip.gov.ar.FECAEDetResponse;
import FEV1.dif.afip.gov.ar.FECAERequest;
import FEV1.dif.afip.gov.ar.FECAEResponse;
import FEV1.dif.afip.gov.ar.FECompConsResponse;
import FEV1.dif.afip.gov.ar.FECompConsultaReq;
import FEV1.dif.afip.gov.ar.FECompConsultaResponse;
import FEV1.dif.afip.gov.ar.FERecuperaLastCbteResponse;
import FEV1.dif.afip.gov.ar.Obs;
import FEV1.dif.afip.gov.ar.Tributo;
import FEV1.dif.afip.gov.ar.entities.AlicuotaIVA;
import FEV1.dif.afip.gov.ar.entities.Comprobante;
import FEV1.dif.afip.gov.ar.entities.ComprobanteAsociado;
import FEV1.dif.afip.gov.ar.entities.Resultado;
import FEV1.dif.afip.gov.ar.entities.TipoComprobante;
import FEV1.dif.afip.gov.ar.entities.TipoComprobanteEnum;
import FEV1.dif.afip.gov.ar.entities.TipoConceptoEnum;
import FEV1.dif.afip.gov.ar.entities.TipoDocumentoEnum;
import FEV1.dif.afip.gov.ar.entities.TipoTributoEnum;
import FEV1.dif.afip.gov.ar.entities.TributoComprobante;
import FEV1.dif.afip.gov.ar.utils.ErrorsUtil;
import FEV1.dif.afip.gov.ar.utils.ParametrosUtil;
import FEV1.dif.afip.gov.ar.utils.Utils;

public class ServiceRequestParser {

	public static FECAERequest getFECAERequest(Comprobante comprobante,
			int ptoVta) {

		FECAERequest request = new FECAERequest();

		// Cabecera
		FECAECabRequest feCabReq = new FECAECabRequest();
		feCabReq.setCantReg(1);
		feCabReq.setCbteTipo(comprobante.getTipoComprobante().getId());
		feCabReq.setPtoVta(ptoVta);
		request.setFeCabReq(feCabReq);

		// Detalle
		FECAEDetRequest[] detalles = new FECAEDetRequest[1];
		FECAEDetRequest detalle = new FECAEDetRequest();
		detalle.setCbteDesde(comprobante.getNroComprobante());
		detalle.setCbteHasta(comprobante.getNroComprobante());
		detalle.setCbteFch(Utils.getStringFromDate(
				comprobante.getFechaComprobante(), "yyyyMMdd"));
		detalle.setConcepto(comprobante.getTipoConcepto().getId());
		detalle.setDocNro(comprobante.getDocNro());
		detalle.setDocTipo(comprobante.getDocTipo().getId());
		detalle.setImpIVA(comprobante.getImporteIVA().doubleValue());
		detalle.setImpNeto(comprobante.getImporteSubtotal().doubleValue());
		
		BigDecimal total = comprobante.getImporteTotal();
		if (null != comprobante.getImporteTributos()) {
			detalle.setImpTrib(comprobante.getImporteTributos().doubleValue());
			total = total.add(comprobante.getImporteTributos());	
		}
		detalle.setImpTotal(total.doubleValue());
		detalle.setMonId(comprobante.getTipoMoneda().getId());
		detalle.setMonCotiz((null != comprobante.getCotizacion()) ? comprobante
				.getCotizacion().doubleValue() : comprobante.getTipoMoneda()
				.getCotizacion().doubleValue());

		// Comprobantes Asociados.
		List<ComprobanteAsociado> comprobantesAsoc = comprobante
				.getComprobantesAsociados();
		if (null != comprobantesAsoc && !comprobantesAsoc.isEmpty()) {
			List<CbteAsoc> cbtesAsoc = new ArrayList<CbteAsoc>();
			for (ComprobanteAsociado comprobanteAsociado : comprobantesAsoc) {
				CbteAsoc cbteAsoc = new CbteAsoc();
				cbteAsoc.setNro(comprobanteAsociado.getNumero());
				cbteAsoc.setPtoVta(comprobanteAsociado.getPtoVta());
				cbteAsoc.setTipo(comprobanteAsociado.getTipoComprobante()
						.getId());
				cbtesAsoc.add(cbteAsoc);
			}
			detalle.setCbtesAsoc(cbtesAsoc.toArray(new CbteAsoc[0]));
		}

		// Otros Tributos
		List<TributoComprobante> tributosComprobante = comprobante
				.getTributos();
		if (null != tributosComprobante && !tributosComprobante.isEmpty()) {
			List<Tributo> tributos = new ArrayList<Tributo>();
			Tributo tributo = null;
			for (TributoComprobante tributoComprobante : tributosComprobante) {
				tributo = new Tributo();
				tributo.setAlic(tributoComprobante.getAlicuota().doubleValue());
				tributo.setBaseImp(tributoComprobante.getBaseImporte()
						.doubleValue());
				tributo.setDesc(tributoComprobante.getDetalle());
				tributo.setId((short) TipoTributoEnum.OTRO.getId());
				tributo.setImporte(tributoComprobante.getImporte()
						.doubleValue());
				tributos.add(tributo);
			}
			detalle.setTributos(tributos.toArray(new Tributo[0]));
		}

		// Alicuotas IVA.
		List<AlicuotaIVA> alicuotasComprobantes = comprobante.getAlicuotas();
		if (null != alicuotasComprobantes && !alicuotasComprobantes.isEmpty()) {
			List<AlicIva> alicuotas = new ArrayList<AlicIva>();
			AlicIva alicuota = null;
			for (AlicuotaIVA alicuotaIVA : alicuotasComprobantes) {
				alicuota = new AlicIva();
				alicuota.setBaseImp(alicuotaIVA.getBaseImponible()
						.doubleValue());
				alicuota.setImporte(alicuotaIVA.getTotalAlicuota()
						.doubleValue());
				alicuota.setId(alicuotaIVA.getTipoIVA().getId());
				alicuotas.add(alicuota);
			}
			detalle.setIva(alicuotas.toArray(new AlicIva[0]));
		}

		detalles[0] = detalle;
		request.setFeDetReq(detalles);
		return request;
	}

	public static FECompConsultaReq getFECompConsultaRequest(
			long nroComprobante, TipoComprobanteEnum tipoComprobante, int ptoVta) {
		FECompConsultaReq feCompConsultaReq = new FECompConsultaReq();
		feCompConsultaReq.setCbteNro(nroComprobante);
		feCompConsultaReq.setCbteTipo(tipoComprobante.getId());
		feCompConsultaReq.setPtoVta(ptoVta);
		return feCompConsultaReq;
	}

	public static Resultado parseFECompConsResponse(
			FECompConsultaResponse response) {

		Resultado resultado = new Resultado();

		// Datos del comprobante
		FECompConsResponse datosComprobante = response.getResultGet();

		if (null != datosComprobante) {
			resultado.setCae(datosComprobante.getCodAutorizacion());
			resultado.setPtoVta(datosComprobante.getPtoVta());
			resultado.setPtoVtaFormato(Utils
					.generarFormatoPtoVta(datosComprobante.getPtoVta()));
			resultado.setEstado(Resultado.Estado.valueOf(datosComprobante
					.getResultado()));
			resultado.setNroComprobanteFormato(Utils.generarFormatoComprobante(
					datosComprobante.getPtoVta(),
					datosComprobante.getCbteDesde()));
			resultado.setFechaVtoCAE(Utils.getDateFromString(
					datosComprobante.getFchVto(), "yyyyMMdd"));
			resultado.setFecha(Utils.getDateFromString(
					datosComprobante.getCbteFch(), "yyyyMMdd"));
			resultado.setNroComprobante(datosComprobante.getCbteDesde());
			resultado.setCodigoBarras(Utils.generarCodigoBarras(
					ParametrosUtil.getProperty("cuit"),
					datosComprobante.getCbteTipo(),
					datosComprobante.getPtoVta(),
					datosComprobante.getCodAutorizacion(),
					datosComprobante.getFchVto()));
		}

		List<String> errores = new ArrayList<String>();
		// Errores
		Err[] errors = response.getErrors();
		if (null != errors) {
			String msg = "";
			for (Err err : errors) {
				msg = ErrorsUtil.getProperty("" + err.getCode(), err.getMsg());
				errores.add("[" + err.getCode() + "] " + msg);
			}
		}
		resultado.setErrores(errores);

		return resultado;
	}

	public static Comprobante parseFECompConsResponseComprobante(
			FECompConsultaResponse response) {

		Comprobante comprobante = new Comprobante();

		// Datos del comprobante
		FECompConsResponse datosComprobante = response.getResultGet();

		if (null != datosComprobante) {
			comprobante.setCae(datosComprobante.getCodAutorizacion());
			comprobante.setPtoVenta(datosComprobante.getPtoVta());
			comprobante.setPtoVentaFormato(Utils
					.generarFormatoPtoVta(datosComprobante.getPtoVta()));
			comprobante.setEstado(datosComprobante.getResultado());
			comprobante.setNroComprobanteFormato(Utils
					.generarFormatoComprobante(datosComprobante.getPtoVta(),
							datosComprobante.getCbteDesde()));
			comprobante.setFechaVtoCAE(Utils.getDateFromString(
					datosComprobante.getFchVto(), "yyyyMMdd"));
			comprobante.setFecha(Utils.getDateFromString(
					datosComprobante.getCbteFch(), "yyyyMMdd"));
			comprobante.setNroComprobante(datosComprobante.getCbteDesde());
			comprobante.setDocTipo(TipoDocumentoEnum.fromValue(datosComprobante
					.getDocTipo()));
			comprobante.setDocNro(datosComprobante.getDocNro());
			comprobante.setImporteIVA(BigDecimal.valueOf(datosComprobante
					.getImpIVA()));
			comprobante.setTipoConcepto(TipoConceptoEnum
					.fromValue(datosComprobante.getConcepto()));
			comprobante.setImporteSubtotal(BigDecimal.valueOf(
					datosComprobante.getImpNeto()).subtract(
					BigDecimal.valueOf(datosComprobante.getImpIVA())));
			comprobante.setImporteTotal(BigDecimal.valueOf(datosComprobante
					.getImpNeto()));
		}
		return comprobante;
	}

	public static Resultado parseFECAEResponse(FECAEResponse response) {

		String estado = response.getFeCabResp().getResultado();
		FECAEDetResponse[] detalles = response.getFeDetResp();
		FECAECabResponse cabecera = response.getFeCabResp();

		FECAEDetResponse detalle = detalles[0];

		Resultado resultado = new Resultado();
		resultado.setCae(detalle.getCAE());
		resultado.setNroComprobante(detalle.getCbteDesde());
		resultado.setNroComprobanteFormato(Utils.generarFormatoComprobante(
				cabecera.getPtoVta(), detalle.getCbteDesde()));
		resultado.setPtoVta(cabecera.getPtoVta());
		resultado.setFechaVtoCAE(Utils.getDateFromString(
				detalle.getCAEFchVto(), "yyyyMMdd"));
		resultado.setPtoVtaFormato(Utils.generarFormatoPtoVta(cabecera
				.getPtoVta()));
		resultado.setFecha(Utils.getDateFromString(detalle.getCbteFch(),
				"yyyyMMdd"));
		resultado.setEstado(Resultado.Estado.valueOf(estado));
		resultado
				.setCodigoBarras(Utils.generarCodigoBarras(
						ParametrosUtil.getProperty("cuit"),
						cabecera.getCbteTipo(), cabecera.getPtoVta(),
						detalle.getCAE(), detalle.getCAEFchVto()));

		List<String> errores = new ArrayList<String>();

		// / Observaciones - Validaciones
		Obs[] obs = response.getFeDetResp()[0].getObservaciones();
		if (null != obs) {
			for (Obs obs2 : obs) {
				errores.add("[" + obs2.getCode() + "] " + obs2.getMsg());
			}
		}
		// Errores
		Err[] errors = response.getErrors();
		if (null != errors) {
			String msg = "";
			for (Err err : errors) {
				msg = ErrorsUtil.getProperty("" + err.getCode(), err.getMsg());
				errores.add("[" + err.getCode() + "] " + msg);
			}
		}
		resultado.setErrores(errores);

		return resultado;
	}

	public static Resultado parseFERecuperaLastCbteResponse(
			FERecuperaLastCbteResponse response) {

		Resultado resultado = new Resultado();
		resultado.setNroComprobante(response.getCbteNro());
		resultado.setNroComprobanteFormato(Utils.generarFormatoComprobante(
				response.getPtoVta(), response.getCbteNro()));
		resultado.setPtoVta(response.getPtoVta());
		resultado.setPtoVtaFormato(Utils.generarFormatoPtoVta(response
				.getPtoVta()));

		// Errores
		Err[] errors = response.getErrors();
		if (null != errors) {
			String msg = "";
			for (Err err : errors) {
				msg = ErrorsUtil.getProperty("" + err.getCode(), err.getMsg());
				resultado.getErrores().add("[" + err.getCode() + "] " + msg);
			}
		}
		return resultado;
	}

	public static List<TipoComprobante> parseFETiposCbteResponse(
			CbteTipoResponse response) {

		List<TipoComprobante> tiposComprobantes = null;
		CbteTipo[] cbtesTipo = response.getResultGet();
		if (null != cbtesTipo) {
			tiposComprobantes = new ArrayList<TipoComprobante>();
			for (CbteTipo cbteTipo : cbtesTipo) {
				tiposComprobantes.add(new TipoComprobante(cbteTipo.getId(),
						cbteTipo.getDesc()));
			}
		}
		return tiposComprobantes;
	}

}
