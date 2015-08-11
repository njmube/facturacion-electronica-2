package ar.com.wuik.sistema.bo.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ParametroBO;
import ar.com.wuik.sistema.bo.ReciboBO;
import ar.com.wuik.sistema.dao.ParametroDAO;
import ar.com.wuik.sistema.dao.ReciboDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ReciboFilter;
import ar.com.wuik.sistema.reportes.entities.ReciboDTO;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.sistema.utils.MonedaUtils;

public class ReciboBOImpl implements ReciboBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReciboBOImpl.class);
	private ReciboDAO reciboDAO;
	private ParametroDAO parametroDAO;
	private ParametroBO parametroBO;

	public ReciboBOImpl() {
		reciboDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ReciboDAO.class);
		parametroDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ParametroDAO.class);
		parametroBO= ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ParametroBO.class);
	}

	@Override
	public Recibo obtener(Long id) throws BusinessException {
		try {
			return reciboDAO.getById(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtener() - Error al obtener Recibo", daexc);
			throw new BusinessException(daexc, "Error al obtener Recibo");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Recibo> buscar(ReciboFilter filter) throws BusinessException {
		try {
			return reciboDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar Recibos", daexc);
			throw new BusinessException(daexc, "Error al buscar Recibos");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(Recibo recibo) throws BusinessException {
		try {
			String nroRecibo = parametroBO.getNroRecibo();
			recibo.setNumero(nroRecibo);
			HibernateUtil.startTransaction();
			reciboDAO.saveOrUpdate(recibo);
			parametroDAO.incrementarNroRecibo();
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al guardar Recibo", daexc);
			throw new BusinessException(daexc, "Error al guardar Recibo");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void actualizar(Recibo recibo) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			reciboDAO.saveOrUpdate(recibo);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("actualizar() - Error al actualizar Recibo", daexc);
			throw new BusinessException(daexc, "Error al actualizar Recibo");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public ReciboDTO obtenerDTO(Long id) throws BusinessException {
		try {
			Recibo recibo = reciboDAO.getById(id);
			return convertToDTO(recibo);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerDTO() - Error al obtener Recibo", daexc);
			throw new BusinessException(daexc, "Error al obtener Recibo");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private ReciboDTO convertToDTO(Recibo recibo) throws DataAccessException {
		ReciboDTO reciboDTO = new ReciboDTO();
		
		// DATOS DEL CLIENTE.
		Cliente cliente = recibo.getCliente();
		reciboDTO
				.setClienteCondIVA(cliente.getCondicionIVA().getDenominacion());
		reciboDTO.setClienteCuit(cliente.getDocumento());
		reciboDTO.setClienteDomicilio(cliente.getDireccion());
		reciboDTO.setClienteRazonSocial(cliente.getRazonSocial());

		reciboDTO.setCheques(recibo.getPagosCheque());
		reciboDTO.setEfectivo(recibo.getPagosEfectivo());
		List<Comprobante> comprobantes = new ArrayList<Comprobante>(recibo.getComprobantes());
		reciboDTO.setComprobantes(comprobantes);
		reciboDTO.setCompNro(recibo.getNumero());
		reciboDTO.setFechaEmision(recibo.getFecha());
				
		reciboDTO.setTotal(recibo.getTotal());
		reciboDTO.setTotalLetras(MonedaUtils.enLetras(recibo.getTotal()));
		
		return reciboDTO;
	}

	@Override
	public void eliminar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			reciboDAO.delete(id);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("eliminar() - Error al eliminar Recibo", daexc);
			throw new BusinessException(daexc, "Error al eliminar Recibo");
		} finally {
			HibernateUtil.closeSession();
		}
	}
}
