package FEV1.dif.afip.gov.ar.services.utils;

import java.util.ArrayList;
import java.util.List;

import FEV1.dif.afip.gov.ar.AlicIva;
import FEV1.dif.afip.gov.ar.CbteAsoc;
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
import FEV1.dif.afip.gov.ar.entities.AlicuotaIVA;
import FEV1.dif.afip.gov.ar.entities.Comprobante;
import FEV1.dif.afip.gov.ar.entities.ComprobanteAsociado;
import FEV1.dif.afip.gov.ar.entities.Resultado;
import FEV1.dif.afip.gov.ar.entities.TipoComprobante;
import FEV1.dif.afip.gov.ar.utils.ErrorsUtil;
import FEV1.dif.afip.gov.ar.utils.ParametrosUtil;
import FEV1.dif.afip.gov.ar.utils.Utils;

public class ServiceRequestParser {

	public static FECAERequest getFECAERequest(Comprobante comprobante) {

		FECAERequest request = new FECAERequest();

		// Cabecera
		FECAECabRequest feCabReq = new FECAECabRequest();
		feCabReq.setCantReg(1);
		feCabReq.setCbteTipo(comprobante.getTipoComprobante().getId());
		feCabReq.setPtoVta(Integer.valueOf(ParametrosUtil
				.getProperty("puntoventa")));
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
		detalle.setImpTotal(comprobante.getImporteTotal().doubleValue());
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

		// Alicuotas IVA.
		List<AlicuotaIVA> alicuotasComprobantes = comprobante.getAlicuotas();
		List<AlicIva> alicuotas = new ArrayList<AlicIva>();
		AlicIva alicuota = null;
		for (AlicuotaIVA alicuotaIVA : alicuotasComprobantes) {
			alicuota = new AlicIva();
			alicuota.setBaseImp(alicuotaIVA.getBaseImponible().doubleValue());
			alicuota.setImporte(alicuotaIVA.getTotalAlicuota().doubleValue());
			alicuota.setId(alicuotaIVA.getTipoIVA().getId());
			alicuotas.add(alicuota);
		}
		detalle.setIva(alicuotas.toArray(new AlicIva[0]));

		detalles[0] = detalle;
		request.setFeDetReq(detalles);
		return request;
	}

	public static FECompConsultaReq getFECompConsultaRequest(
			long nroComprobante, TipoComprobante tipoComprobante) {
		FECompConsultaReq feCompConsultaReq = new FECompConsultaReq();
		feCompConsultaReq.setCbteNro(nroComprobante);
		feCompConsultaReq.setCbteTipo(tipoComprobante.getId());
		feCompConsultaReq.setPtoVta(Integer.valueOf(ParametrosUtil
				.getProperty("puntoventa")));
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

}
