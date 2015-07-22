package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ProveedorBO;
import ar.com.wuik.sistema.dao.ProveedorDAO;
import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ProveedorFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;

public class ProveedorBOImpl implements ProveedorBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProveedorBOImpl.class);
	private ProveedorDAO proveedorDAO;

	public ProveedorBOImpl() {
		proveedorDAO = AbstractFactory.getInstance(ProveedorDAO.class);
	}

	@Override
	public Proveedor obtener(Long id) throws BusinessException {
		try {
			return proveedorDAO.getById(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("guardar() - Error al obtener Proveedor", daexc);
			throw new BusinessException(daexc, "Error al obtener Proveedor");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Proveedor> buscar(ProveedorFilter filter)
			throws BusinessException {
		try {
			return proveedorDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("guardar() - Error al buscar Proveedores", daexc);
			throw new BusinessException(daexc, "Error al buscar Proveedores");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(Proveedor proveedor) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			proveedorDAO.saveOrUpdate(proveedor);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al guardar Proveedor", daexc);
			throw new BusinessException(daexc, "Error al guardar Proveedor");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void actualizar(Proveedor proveedor) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			proveedorDAO.saveOrUpdate(proveedor);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("actualizar() - Error al actualizar Proveedor", daexc);
			throw new BusinessException(daexc, "Error al actualizar Proveedor");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void eliminar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			proveedorDAO.delete(id);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("eliminar() - Error al eliminar Proveedor", daexc);
			throw new BusinessException(daexc, "Error al eliminar Proveedor");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public boolean estaEnUso(Long id) throws BusinessException {
		try {
			return proveedorDAO.estaEnUso(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("estaEnUso() - Error al validar uso del Proveedor",
					daexc);
			throw new BusinessException(daexc,
					"Error al validar uso del Proveedor");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Proveedor> obtenerTodosActivos() throws BusinessException {
		try {
			
			ProveedorFilter filter = new ProveedorFilter();
			filter.setActivo(Boolean.TRUE);
			return proveedorDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error(
					"obtenerTodosActivos() - Error al obtener todos los Proveedores activos",
					daexc);
			throw new BusinessException(daexc,
					"Error al obtener todos los Proveedores activos");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void activar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			Proveedor proveedor = proveedorDAO.getById(id);
			proveedor.setActivo(Boolean.TRUE);
			proveedorDAO.saveOrUpdate(proveedor);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("activar() - Error al activar Proveedor", daexc);
			throw new BusinessException(daexc, "Error al activar Proveedor");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void desactivar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			Proveedor proveedor = proveedorDAO.getById(id);
			proveedor.setActivo(Boolean.FALSE);
			proveedorDAO.saveOrUpdate(proveedor);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("desactivar() - Error al desactivar Proveedor", daexc);
			throw new BusinessException(daexc, "Error al desactivar Proveedor");
		} finally {
			HibernateUtil.closeSession();
		}
	}

}
