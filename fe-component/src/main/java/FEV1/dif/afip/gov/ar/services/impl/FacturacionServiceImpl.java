package FEV1.dif.afip.gov.ar.services.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import FEV1.dif.afip.gov.ar.CbteTipoResponse;
import FEV1.dif.afip.gov.ar.FEAuthRequest;
import FEV1.dif.afip.gov.ar.FECAERequest;
import FEV1.dif.afip.gov.ar.FECAEResponse;
import FEV1.dif.afip.gov.ar.FECompConsultaReq;
import FEV1.dif.afip.gov.ar.FECompConsultaResponse;
import FEV1.dif.afip.gov.ar.FERecuperaLastCbteResponse;
import FEV1.dif.afip.gov.ar.ServiceSoapProxy;
import FEV1.dif.afip.gov.ar.entities.Comprobante;
import FEV1.dif.afip.gov.ar.entities.Resultado;
import FEV1.dif.afip.gov.ar.entities.TipoComprobante;
import FEV1.dif.afip.gov.ar.entities.TipoComprobanteEnum;
import FEV1.dif.afip.gov.ar.exceptions.ServiceException;
import FEV1.dif.afip.gov.ar.services.FacturacionService;
import FEV1.dif.afip.gov.ar.services.utils.AuthorizationUtil;
import FEV1.dif.afip.gov.ar.services.utils.ServiceRequestParser;
import FEV1.dif.afip.gov.ar.utils.ParametrosUtil;

public class FacturacionServiceImpl implements FacturacionService {

	// Punto de Venta.
	private int ptoVta = Integer.valueOf(ParametrosUtil
			.getProperty("puntoventa"));

	public FacturacionServiceImpl() {
		ptoVta = Integer.valueOf(ParametrosUtil.getProperty("puntoventa"));
	}

	@Override
	public Resultado solicitarComprobante(Comprobante comprobante)
			throws ServiceException {

		// Autorizacion.
		FEAuthRequest authRequest = AuthorizationUtil.getAuthorization();

		try {
			ServiceSoapProxy service = new ServiceSoapProxy();

			// Request del Servicio.
			FECAERequest caeRequest = ServiceRequestParser.getFECAERequest(
					comprobante, ptoVta);

			// Response del Servicio.
			FECAEResponse response = service.FECAESolicitar(authRequest,
					caeRequest);

			// Parseo de respuesta.
			Resultado resultado = ServiceRequestParser
					.parseFECAEResponse(response);
			return resultado;
		} catch (RemoteException rexc) {
			throw new ServiceException(rexc,
					"Se ha producido un error al conectar a los servicios");
		}
	}

	@Override
	public Resultado consultarComprobante(long nroComprobante,
			TipoComprobanteEnum tipoComprobante) throws ServiceException {

		// Autorizacion.
		FEAuthRequest authRequest = AuthorizationUtil.getAuthorization();

		try {
			ServiceSoapProxy service = new ServiceSoapProxy();

			// Request del Servicio.
			FECompConsultaReq feCompConsReq = ServiceRequestParser
					.getFECompConsultaRequest(nroComprobante, tipoComprobante,
							ptoVta);

			// Response del Servicio.
			FECompConsultaResponse response = service.FECompConsultar(
					authRequest, feCompConsReq);

			// Parseo de respuesta.
			Resultado resultado = ServiceRequestParser
					.parseFECompConsResponse(response);
			return resultado;
		} catch (RemoteException rexc) {
			throw new ServiceException(rexc,
					"Se ha producido un error al conectar a los Servicios");
		}
	}

	@Override
	public Resultado consultarUltimoComprobante(
			TipoComprobanteEnum tipoComprobante) throws ServiceException {

		if (null == tipoComprobante) {
			return new Resultado();
		}

		// Autorizacion.
		FEAuthRequest authRequest = AuthorizationUtil.getAuthorization();

		try {
			ServiceSoapProxy service = new ServiceSoapProxy();

			// Response del Servicio.
			FERecuperaLastCbteResponse response = service
					.FECompUltimoAutorizado(authRequest, ptoVta,
							tipoComprobante.getId());

			// Parseo de respuesta.
			Resultado resultado = ServiceRequestParser
					.parseFERecuperaLastCbteResponse(response);
			return resultado;
		} catch (RemoteException rexc) {
			throw new ServiceException(rexc,
					"Se ha producido un error al conectar a los Servicios");
		}
	}

	@Override
	public List<TipoComprobante> getAllTiposComprobantes()
			throws ServiceException {

		// Autorizacion.
		FEAuthRequest authRequest = AuthorizationUtil.getAuthorization();

		try {
			ServiceSoapProxy service = new ServiceSoapProxy();

			// Response del Servicio.
			CbteTipoResponse response = service
					.FEParamGetTiposCbte(authRequest);

			// Parseo de respuesta.
			List<TipoComprobante> tiposComprobantes = ServiceRequestParser
					.parseFETiposCbteResponse(response);
			return tiposComprobantes;
		} catch (RemoteException rexc) {
			throw new ServiceException(rexc,
					"Se ha producido un error al conectar a los Servicios");
		}
	}

	@Override
	public List<Comprobante> getAllComprobantes(long idTipoComp)
			throws ServiceException {

		// Tipo de Comprobante.
		TipoComprobanteEnum tipoComprobante = TipoComprobanteEnum
				.fromValue((int) idTipoComp);

		// Autorizacion.
		FEAuthRequest authRequest = AuthorizationUtil.getAuthorization();

		System.out.println("TOKEN{" + authRequest.getToken() +"}");
		System.out.println("SIGN{" + authRequest.getSign() + "}");
		
		try {

			// Resultado de los Comprobantes.
			List<Comprobante> comprobantes = new ArrayList<Comprobante>();

			ServiceSoapProxy service = new ServiceSoapProxy();

			// Obtengo el Ult. Comprobante autorizado.
			Resultado resultadoUltComp = consultarUltimoComprobante(tipoComprobante);
			long nroComprobante = resultadoUltComp.getNroComprobante();

			// Response del Servicio, obtengo todos los comprobantes desde el 1
			// al último.
			for (int i = 1; i <= nroComprobante; i++) {
				FECompConsultaReq feCompConsReq = ServiceRequestParser
						.getFECompConsultaRequest(i, tipoComprobante, ptoVta);
				FECompConsultaResponse response = service.FECompConsultar(
						authRequest, feCompConsReq);
				comprobantes.add(ServiceRequestParser
						.parseFECompConsResponseComprobante(response));
			}

			return comprobantes;
		} catch (RemoteException rexc) {
			throw new ServiceException(rexc,
					"Se ha producido un error al conectar a los Servicios");
		}
	}

	@Override
	public List<Resultado> solicitarComprobantes(List<Comprobante> comprobantes)
			throws ServiceException {

		// Autorizacion.
		FEAuthRequest authRequest = AuthorizationUtil.getAuthorization();

		try {
			ServiceSoapProxy service = new ServiceSoapProxy();

			// Request del Servicio.
			FECAERequest caeRequest = ServiceRequestParser.getFECAERequest(
					comprobantes, ptoVta);

			// Response del Servicio.
			FECAEResponse response = service.FECAESolicitar(authRequest,
					caeRequest);

			// Parseo de respuesta.
			List<Resultado> resultados = ServiceRequestParser
					.parseFECAEResponseMasivos(response);
			return resultados;
		} catch (RemoteException rexc) {
			throw new ServiceException(rexc,
					"Se ha producido un error al conectar a los servicios");
		}

	}
}
