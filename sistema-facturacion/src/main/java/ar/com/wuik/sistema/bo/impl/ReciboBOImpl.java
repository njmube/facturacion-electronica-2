package ar.com.wuik.sistema.bo.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ParametroBO;
import ar.com.wuik.sistema.bo.ReciboBO;
import ar.com.wuik.sistema.dao.ParametroDAO;
import ar.com.wuik.sistema.dao.ReciboDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.PagoReciboCheque;
import ar.com.wuik.sistema.entities.PagoReciboEfectivo;
import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ReciboFilter;
import ar.com.wuik.sistema.reportes.entities.DetalleReciboDTO;
import ar.com.wuik.sistema.reportes.entities.ReciboDTO;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.sistema.utils.MonedaUtils;
import ar.com.wuik.swing.utils.WUtils;

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
		parametroBO = ar.com.wuik.sistema.utils.AbstractFactory
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

		List<PagoReciboEfectivo> pagosEfectivo = recibo.getPagosEfectivo();
		if (WUtils.isNotEmpty(pagosEfectivo)) {
			BigDecimal totalEfectivo = BigDecimal.ZERO;
			for (PagoReciboEfectivo pagoReciboEfectivo : pagosEfectivo) {
				totalEfectivo = totalEfectivo.add(pagoReciboEfectivo.getTotal());
			}
			reciboDTO.setTotalEfectivo(totalEfectivo);
		}
		List<PagoReciboCheque> pagosCheque = recibo.getPagosCheque();
		if (WUtils.isNotEmpty(pagosCheque)) {
			BigDecimal totalCheque = BigDecimal.ZERO;
			for (PagoReciboCheque pagoReciboCheque : pagosCheque) {
				totalCheque = totalCheque.add(pagoReciboCheque.getCheque().getImporte());
			}
			reciboDTO.setTotalCheque(totalCheque);
		}
		List<DetalleReciboDTO> detalles = convertirDetalles(pagosEfectivo,
				pagosCheque);

		reciboDTO.setDetalles(detalles);
		reciboDTO.setCompNro(recibo.getNumero());
		reciboDTO.setFechaEmision(recibo.getFecha());

		reciboDTO.setTotal(recibo.getTotal());
		reciboDTO.setTotalLetras(MonedaUtils.enLetras(recibo.getTotal()).toUpperCase() + " --------------------------------");

		return reciboDTO;
	}


	private List<DetalleReciboDTO> convertirDetalles(
			List<PagoReciboEfectivo> pagosEfectivo,
			List<PagoReciboCheque> pagosCheque) {

		List<DetalleReciboDTO> detalles = new ArrayList<DetalleReciboDTO>();

		if (WUtils.isNotEmpty(pagosEfectivo)) {
			DetalleReciboDTO detalle = null;
			for (PagoReciboEfectivo pagoReciboEfectivo : pagosEfectivo) {
				detalle = new DetalleReciboDTO();
				detalle.setTipo("EFECTIVO");
				detalle.setTotal(pagoReciboEfectivo.getTotal());
				detalles.add(detalle);
			}
		}

		if (WUtils.isNotEmpty(pagosCheque)) {
			DetalleReciboDTO detalle = null;
			for (PagoReciboCheque pagoReciboCheque : pagosCheque) {
				detalle = new DetalleReciboDTO();
				detalle.setTipo("CHEQUE");
				detalle.setBanco(pagoReciboCheque.getCheque().getBanco().getNombre());
				detalle.setNroCheque(pagoReciboCheque.getCheque().getNumero());
				detalle.setTotal(pagoReciboCheque.getCheque().getImporte());
				detalles.add(detalle);
			}
		}
		return detalles;
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
