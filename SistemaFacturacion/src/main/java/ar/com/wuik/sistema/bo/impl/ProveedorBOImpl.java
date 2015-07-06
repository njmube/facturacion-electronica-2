package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import ar.com.wuik.sistema.bo.ProveedorBO;
import ar.com.wuik.sistema.dao.ProveedorDAO;
import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ProveedorFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;

public class ProveedorBOImpl implements ProveedorBO {

	private ProveedorDAO proveedorDAO;

	public ProveedorBOImpl() {
		proveedorDAO = AbstractFactory.getInstance(ProveedorDAO.class);
	}

	@Override
	public Proveedor obtener(Long id) throws BusinessException {
		try {
			return proveedorDAO.getById(id);
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
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
			throw new BusinessException(daexc);
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
			throw new BusinessException(daexc);
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
			throw new BusinessException(daexc);
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
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public boolean estaEnUso(Long id) throws BusinessException {
		throw new NotImplementedException();
	}

	@Override
	public List<Proveedor> obtenerTodos() throws BusinessException {
		try {
			return proveedorDAO.getAll();
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

}
