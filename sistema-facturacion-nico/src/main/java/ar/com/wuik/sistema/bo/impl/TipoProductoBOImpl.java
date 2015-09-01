package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.TipoProductoBO;
import ar.com.wuik.sistema.dao.TipoProductoDAO;
import ar.com.wuik.sistema.entities.TipoProducto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;

public class TipoProductoBOImpl implements TipoProductoBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TipoProductoBOImpl.class);
	private TipoProductoDAO tipoProductoDAO;

	public TipoProductoBOImpl() {
		tipoProductoDAO = AbstractFactory.getInstance(TipoProductoDAO.class);
	}

	@Override
	public TipoProducto obtener(Long id) throws BusinessException {
		try {
			TipoProducto tipoProducto = tipoProductoDAO.getById(id);
			return tipoProducto;
		} catch (DataAccessException daexc) {
			LOGGER.error("obtener() - Error al obtener Tipo de Producto",daexc);
			throw new BusinessException(daexc, "Error al obtener Tipo de Producto");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(TipoProducto tipoProducto) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			tipoProductoDAO.saveOrUpdate(tipoProducto);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al guardar Tipo de Producto",daexc);
			throw new BusinessException(daexc, "Error al guardar Tipo de Producto");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void actualizar(TipoProducto tipoProducto) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			tipoProductoDAO.saveOrUpdate(tipoProducto);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("actualizar() - Error al actualizar Tipo de Producto",daexc); 
			throw new BusinessException(daexc, "Error al actualizar Tipo de Producto");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<TipoProducto> obtenerTodos() throws BusinessException {
		try {
			return tipoProductoDAO.getAll();
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerTodos() - Error al obtener todos los Tipos de Producto",daexc);
			throw new BusinessException(daexc, "Error al obtener todos los Tipos de Producto");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void eliminar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			tipoProductoDAO.delete(id);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("eliminar() - Error al eliminar Tipo de Producto",daexc); 
			throw new BusinessException(daexc, "Error al eliminar Tipo de Producto");
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	@Override
	public boolean estaEnUso(Long selectedItem) throws BusinessException {
		try {
			return tipoProductoDAO.estaEnUso(selectedItem);
		} catch (DataAccessException daexc) {
			LOGGER.error("estaEnUso() - Error al validar uso de Tipo de Producto",daexc);
			throw new BusinessException(daexc, "Error al validar uso de Tipo de Producto");
		} finally {
			HibernateUtil.closeSession();
		}
	}

}
