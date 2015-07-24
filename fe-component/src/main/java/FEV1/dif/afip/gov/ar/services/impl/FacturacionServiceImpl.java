package FEV1.dif.afip.gov.ar.services.impl;

import java.rmi.RemoteException;

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
			FECAERequest caeRequest = ServiceRequestParser
					.getFECAERequest(comprobante, ptoVta);

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
			TipoComprobante tipoComprobante) throws ServiceException {

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
	public Resultado consultarUltimoComprobante(TipoComprobante tipoComprobante)
			throws ServiceException {

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

}
