package ar.com.wuik.sistema.bo;

import java.util.List;

import FEV1.dif.afip.gov.ar.entities.Comprobante;
import FEV1.dif.afip.gov.ar.entities.TipoComprobante;
import ar.com.wuik.sistema.exceptions.BusinessException;

public interface AfipBO {

	List<Comprobante> consultarComprobantes(long idTipoComprobante) throws BusinessException;

	List<TipoComprobante> obtenerTiposComprobantes() throws BusinessException;

}
