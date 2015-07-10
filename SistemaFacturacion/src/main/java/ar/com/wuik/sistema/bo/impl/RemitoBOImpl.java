package ar.com.wuik.sistema.bo.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FEV1.dif.afip.gov.ar.utils.AbstractFactory;
import ar.com.wuik.sistema.bo.RemitoBO;
import ar.com.wuik.sistema.dao.ParametroDAO;
import ar.com.wuik.sistema.dao.RemitoDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.DetalleRemito;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.RemitoFilter;
import ar.com.wuik.sistema.reportes.entities.DetalleFacturaDTO;
import ar.com.wuik.sistema.reportes.entities.RemitoDTO;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.utils.WUtils;

public class RemitoBOImpl implements RemitoBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RemitoBOImpl.class);
	private RemitoDAO remitoDAO;
	private ParametroDAO parametroDAO;

	public RemitoBOImpl() {
		remitoDAO = AbstractFactory.getInstance(RemitoDAO.class);
		parametroDAO = AbstractFactory.getInstance(ParametroDAO.class);
	}

	@Override
	public Remito obtener(Long id) throws BusinessException {
		try {
			return remitoDAO.getById(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtener() - Error al obtener Remito",daexc);
			throw new BusinessException(daexc, "Error al obtener Remito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Remito> buscar(RemitoFilter filter) throws BusinessException {
		try {
			return remitoDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar Remitos",daexc);
			throw new BusinessException(daexc, "Error al buscar Remitos");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(Remito remito) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			remitoDAO.saveOrUpdate(remito);
			parametroDAO.incrementarNroRemito();
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al guardar Remito",daexc);
			throw new BusinessException(daexc, "Error al guardar Remito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void actualizar(Remito remito) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			remitoDAO.saveOrUpdate(remito);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al actualizar Remito",daexc);
			throw new BusinessException(daexc, "Error al actualizar Remito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void eliminar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			remitoDAO.delete(id);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("eliminar() - Error al eliminar Remito",daexc);
			throw new BusinessException(daexc, "Error al eliminar Remito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public boolean estaEnUso(Long id) throws BusinessException {
		try {
			return remitoDAO.estaEnUso(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("estaEnUso() - Error al validar uso de Remito",daexc);
			throw new BusinessException(daexc, "Error al validar uso de Remito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Remito> obtenerTodos() throws BusinessException {
		try {
			return remitoDAO.getAll();
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerTodos() - Error al obtener todos los Remitos",daexc);
			throw new BusinessException(daexc,
					"Error al obtener todos los Remitos");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void cancelar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			Remito remito = remitoDAO.getById(id);
			remito.setActivo(Boolean.FALSE);
			remitoDAO.saveOrUpdate(remito);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("cancelar() - Error al cancelar Remito",daexc);
			throw new BusinessException(daexc, "Error al cancelar Remito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public RemitoDTO obtenerDTO(Long id) throws BusinessException {
		try {
			Remito remito = remitoDAO.getById(id);
			return convertToDTO(remito);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerDTO() - Error al obtener Remito",daexc);
			throw new BusinessException(daexc, "Error al obtener Remito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private RemitoDTO convertToDTO(Remito remito) {
		RemitoDTO remitoDTO = new RemitoDTO();

		// DATOS DEL CLIENTE.
		Cliente cliente = remito.getCliente();
		remitoDTO
				.setClienteCondIVA(cliente.getCondicionIVA().getDenominacion());
		remitoDTO.setClienteCuit(cliente.getCuit());
		remitoDTO.setClienteDomicilio(cliente.getDireccion());
		remitoDTO.setClienteRazonSocial(cliente.getRazonSocial());

		// DATOS PROPIOS. TODO: PONER EN PARAMETRICOS.
		remitoDTO.setRazonSocial("VAN DER BEKEN FRANCISCO NICOLAS");
		remitoDTO.setCondIVA("IVA Responsable Inscripto");
		remitoDTO.setCuit("20-04974118-1");
		remitoDTO.setDomicilio("Passo 50 - Rojas, Buenos Aires");
		remitoDTO.setIngBrutos("200049746181");
		remitoDTO.setInicioAct(WUtils.getDateFromString("01/01/1994"));

		List<DetalleRemito> detalles = remito.getDetalles();
		List<DetalleFacturaDTO> detallesDTO = new ArrayList<DetalleFacturaDTO>();
		DetalleFacturaDTO detalleFacturaDTO = null;
		for (DetalleRemito detalleRemito : detalles) {
			detalleFacturaDTO = new DetalleFacturaDTO();
			detalleFacturaDTO.setCantidad(detalleRemito.getCantidad());
			detalleFacturaDTO
					.setCodigo(detalleRemito.getProducto().getCodigo());
			detalleFacturaDTO.setProducto(detalleRemito.getProducto()
					.getDescripcion());
			detallesDTO.add(detalleFacturaDTO);
		}
		remitoDTO.setDetalles(detallesDTO);
		remitoDTO.setCompNro(remito.getNumero());
		remitoDTO.setFechaEmision(remito.getFecha());
		return remitoDTO;
	}
}
