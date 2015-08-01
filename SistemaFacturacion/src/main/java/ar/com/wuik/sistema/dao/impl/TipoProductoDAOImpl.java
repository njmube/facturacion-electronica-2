
package ar.com.wuik.sistema.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.TipoProductoDAO;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.entities.TipoProducto;
import ar.com.wuik.sistema.exceptions.DataAccessException;

public class TipoProductoDAOImpl extends GenericCrudHBDAOImpl<TipoProducto>
		implements TipoProductoDAO {

	public TipoProductoDAOImpl() {
		super(TipoProducto.class);
	}
	
	@Override
	public boolean estaEnUso(Long id) throws DataAccessException {
		try {
			
			Criteria criteria = getSession()
					.createCriteria(Producto.class);
			criteria.setProjection(Projections.rowCount());
			criteria.add(Restrictions.eq("idTipo", id));
			long cantidad = (Long) criteria.uniqueResult();
			return cantidad > 0;
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

}
