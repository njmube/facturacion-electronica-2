package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.dao.ProductoDAO;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ProductoFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;

public class ProductoBOImpl implements ProductoBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProductoBOImpl.class);
	private ProductoDAO productoDAO;

	public ProductoBOImpl() {
		productoDAO = AbstractFactory.getInstance(ProductoDAO.class);
	}

	@Override
	public Producto obtener(Long id) throws BusinessException {
		try {
			return productoDAO.getById(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtner() - Error al obtener Producto", daexc);
			throw new BusinessException(daexc, "Error al obtener Producto");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Producto> buscar(ProductoFilter filter)
			throws BusinessException {
		try {
			return productoDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar Productos", daexc);
			throw new BusinessException(daexc, "Error al buscar Productos");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(Producto producto) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			productoDAO.saveOrUpdate(producto);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al guardar Producto", daexc);
			throw new BusinessException(daexc, "Error al guardar Producto");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void actualizar(Producto producto) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			productoDAO.saveOrUpdate(producto);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("actualizar() - Error al actualizar Producto", daexc);
			throw new BusinessException(daexc, "Error al actualizar Producto");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void eliminar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			productoDAO.delete(id);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("eliminar() - Error al eliminar Producto", daexc);
			throw new BusinessException(daexc, "Error al eliminar Producto");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Producto> obtenerTodos() throws BusinessException {
		try {
			return productoDAO.getAll();
		} catch (DataAccessException daexc) {
			LOGGER.error(
					"obtenerTodos() - Error al obtener todos los Productos",
					daexc);
			throw new BusinessException(daexc,
					"Error al obtener todos los Productos");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public boolean estaEnUso(Long id) throws BusinessException {
		try {
			return productoDAO.estaEnUso(id);
		} catch (DataAccessException daexc) {
			LOGGER.error(
					"estaEnUso() - Error al verificar el uso de Producto",
					daexc);
			throw new BusinessException(daexc,
					"Error al verificar el uso de Producto");
		} finally {
			HibernateUtil.closeSession();
		}
	}

}