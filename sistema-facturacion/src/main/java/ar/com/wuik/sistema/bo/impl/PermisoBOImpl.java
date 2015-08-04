package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import ar.com.wuik.sistema.bo.PermisoBO;
import ar.com.wuik.sistema.dao.PermisoDAO;
import ar.com.wuik.sistema.entities.Permiso;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;

public class PermisoBOImpl implements PermisoBO{

	private PermisoDAO permisoDAO;

	public PermisoBOImpl() {
		permisoDAO = AbstractFactory.getInstance( PermisoDAO.class );
	}
	
	@Override
	public List<Permiso> getAll() throws BusinessException {
		try {
			return permisoDAO.getAll();
		}
		catch( DataAccessException daexc ) {
			throw new BusinessException( daexc );
		}
		finally {
			HibernateUtil.closeSession();
		}
	}
	
	/**
	 * @see ar.com.wuik.sistema.bo.PermisoBO#getById(java.lang.Long)
	 */
	@Override
	public Permiso getById( Long id ) throws BusinessException {
		try {
			return permisoDAO.getById( id );
		}
		catch( DataAccessException daexc ) {
			throw new BusinessException( daexc );
		}
		finally {
			HibernateUtil.closeSession();
		}
	}

}
