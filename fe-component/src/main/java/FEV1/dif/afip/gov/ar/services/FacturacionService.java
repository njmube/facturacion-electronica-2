package FEV1.dif.afip.gov.ar.services;

import FEV1.dif.afip.gov.ar.entities.Comprobante;
import FEV1.dif.afip.gov.ar.entities.Resultado;
import FEV1.dif.afip.gov.ar.entities.TipoComprobante;
import FEV1.dif.afip.gov.ar.exceptions.ServiceException;

public interface FacturacionService {

	Resultado solicitarComprobante(Comprobante comprobante)
			throws ServiceException;

	Resultado consultarComprobante(long nroComprobante,
			TipoComprobante tipoComprobante) throws ServiceException;

	Resultado consultarUltimoComprobante(TipoComprobante tipoComprobante)
			throws ServiceException;
}
