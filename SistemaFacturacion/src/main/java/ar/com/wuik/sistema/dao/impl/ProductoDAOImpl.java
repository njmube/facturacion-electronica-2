package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.ProductoDAO;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ProductoFilter;

public class ProductoDAOImpl extends GenericCrudHBDAOImpl<Producto> implements
		ProductoDAO {

	public ProductoDAOImpl() {
		super(Producto.class);
	}

	@Override
	public List<Producto> search(ProductoFilter filter)
			throws DataAccessException {
		Criteria criteria = buildCriteria(filter);
		try {
			return criteria.list();
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	private Criteria buildCriteria(ProductoFilter filter) {
		Criteria criteria = getSession().createCriteria(Producto.class);

		String descripcion = filter.getDescripcion();
		String descripcionCodigo = filter.getDescripcionCodigo();
		
		if (null != descripcion) {
			criteria.add(Restrictions.like("descripcion", descripcion,
					MatchMode.ANYWHERE));
		}
		if (null != descripcionCodigo) {
			criteria.add(Restrictions.or(Restrictions.like("descripcion", descripcionCodigo,
					MatchMode.ANYWHERE), Restrictions.like("codigo", descripcionCodigo,
					MatchMode.ANYWHERE)));
		}
		
		return criteria;
	}

}
