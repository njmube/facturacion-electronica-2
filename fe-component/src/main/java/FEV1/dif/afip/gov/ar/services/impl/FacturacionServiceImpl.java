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

	@Override
	public Resultado solicitarComprobante(Comprobante comprobante)
			throws ServiceException {

		// Autorizacion.
		FEAuthRequest authRequest = AuthorizationUtil.getAuthorization();

		try {
			ServiceSoapProxy service = new ServiceSoapProxy();
			Resultado resultadoUltComp = consultarUltimoComprobante(
					comprobante.getTipoComprobante(), authRequest);
			// Evaluo los nros de comprobantes
			long nroComprobante = resultadoUltComp.getNroComprobante();
			long proxNroComprobante = nroComprobante + 1;

			// Request del Servicio.
			FECAERequest caeRequest = ServiceRequestParser.getFECAERequest(
					comprobante, proxNroComprobante);

			// Response del Servicio.
			FECAEResponse response = service.FECAESolicitar(authRequest,
					caeRequest);

			// Parseo de respuesta.
			Resultado resultado = ServiceRequestParser
					.parseFECAEResponse(response);
			if (null != resultado.getErrores()
					&& !resultado.getErrores().isEmpty()) {
				throw new ServiceException("Error al solicitar comprobante",
						resultado.getErrores());
			}
			return resultado;
		} catch (RemoteException rexc) {
			throw new ServiceException(rexc,
					"Se ha producido un error al conectar a los servicios");
		} finally {

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
					.getFECompConsultaRequest(nroComprobante, tipoComprobante);

			// Response del Servicio.
			FECompConsultaResponse response = service.FECompConsultar(
					authRequest, feCompConsReq);

			// Parseo de respuesta.
			Resultado resultado = ServiceRequestParser
					.parseFECompConsResponse(response);
			if (null != resultado.getErrores()
					&& !resultado.getErrores().isEmpty()) {
				throw new ServiceException("Error al consultar comprobante",
						resultado.getErrores());
			}
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

		// Consulta de Comprobante
		return consultarUltimoComprobante(tipoComprobante, authRequest);
	}

	private Resultado consultarUltimoComprobante(
			TipoComprobante tipoComprobante, FEAuthRequest authRequest)
			throws ServiceException {

		// Punto de Venta.
		int ptoVta = Integer.valueOf(ParametrosUtil.getProperty("puntoventa"));

		try {
			ServiceSoapProxy service = new ServiceSoapProxy();

			// Response del Servicio.
			FERecuperaLastCbteResponse response = service
					.FECompUltimoAutorizado(authRequest, ptoVta,
							tipoComprobante.getId());

			// Parseo de respuesta.
			Resultado resultado = ServiceRequestParser
					.parseFERecuperaLastCbteResponse(response);
			if (null != resultado.getErrores()
					&& !resultado.getErrores().isEmpty()) {
				throw new ServiceException(
						"Error al consultar �ltimo comprobante",
						resultado.getErrores());
			}
			return resultado;
		} catch (RemoteException rexc) {
			throw new ServiceException(rexc,
					"Se ha producido un error al conectar a los Servicios");
		}
	}

}
