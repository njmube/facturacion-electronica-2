package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FEV1.dif.afip.gov.ar.entities.Comprobante;
import FEV1.dif.afip.gov.ar.entities.TipoComprobante;
import FEV1.dif.afip.gov.ar.exceptions.ServiceException;
import FEV1.dif.afip.gov.ar.services.FacturacionService;
import ar.com.wuik.sistema.bo.AfipBO;
import ar.com.wuik.sistema.bo.ChequeBO;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;

public class AfipBOImpl implements AfipBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ChequeBO.class);
	private FacturacionService facturacionService;

	public AfipBOImpl() {
		facturacionService = AbstractFactory
				.getInstance(FacturacionService.class);
	}

	@Override
	public List<Comprobante> consultarComprobantes(long idTipoComprobante)
			throws BusinessException {
		try {
			List<Comprobante> comprobantes = facturacionService
					.getAllComprobantes(idTipoComprobante);
			return comprobantes;
		} catch (ServiceException sexc) {
			LOGGER.error(
					"consultarComprobantes() - Error al consultar Comprobantes",
					sexc);
			throw new BusinessException("Error al consultar Comprobantes");
		}
	}

	@Override
	public List<TipoComprobante> obtenerTiposComprobantes()
			throws BusinessException {
		try {
			List<TipoComprobante> tiposComprobantes = facturacionService
					.getAllTiposComprobantes();
			return tiposComprobantes;
		} catch (ServiceException sexc) {
			LOGGER.error(
					"obtenerTiposComprobantes() - Error al obtener Tipos de Comprobantes",
					sexc);
			throw new BusinessException(
					"Error al obtener Tipos de Comprobantes");
		}
	}

}
