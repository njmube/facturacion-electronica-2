package FEV1.dif.afip.gov.ar.services;

import java.util.List;

import FEV1.dif.afip.gov.ar.entities.Comprobante;
import FEV1.dif.afip.gov.ar.entities.Resultado;
import FEV1.dif.afip.gov.ar.entities.TipoComprobante;
import FEV1.dif.afip.gov.ar.entities.TipoComprobanteEnum;
import FEV1.dif.afip.gov.ar.exceptions.ServiceException;

public interface FacturacionService {

	Resultado solicitarComprobante(Comprobante comprobante)
			throws ServiceException;

	Resultado consultarComprobante(long nroComprobante,
			TipoComprobanteEnum tipoComprobante) throws ServiceException;

	Resultado consultarUltimoComprobante(TipoComprobanteEnum tipoComprobante)
			throws ServiceException;

	List<TipoComprobante> getAllTiposComprobantes() throws ServiceException;

	List<Comprobante> getAllComprobantes(long idTipoComp) throws ServiceException;
}
