package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.dao.ProductoDAO;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ProductoFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;

public class ProductoBOImpl implements ProductoBO {

	private ProductoDAO productoDAO;

	public ProductoBOImpl() {
		productoDAO = AbstractFactory.getInstance(ProductoDAO.class);
	}

	@Override
	public Producto obtener(Long id) throws BusinessException {
		try {
			return productoDAO.getById(id);
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
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
			throw new BusinessException(daexc);
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
			throw new BusinessException(daexc);
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
			throw new BusinessException(daexc);
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
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Producto> obtenerTodos() throws BusinessException {
		try {
			return productoDAO.getAll();
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public boolean estaEnUso(Long id) throws BusinessException {
		throw new NotImplementedException();
	}
}
