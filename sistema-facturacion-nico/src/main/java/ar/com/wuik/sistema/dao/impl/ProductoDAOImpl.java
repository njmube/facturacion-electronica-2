package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.ProductoDAO;
import ar.com.wuik.sistema.entities.DetalleComprobante;
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
		String codigo = filter.getCodigo();
		Long idToExclude = filter.getIdToExclude();

		if (null != descripcion) {
			criteria.add(Restrictions.like("descripcion", descripcion,
					MatchMode.ANYWHERE));
		}
		if (null != descripcionCodigo) {
			criteria.add(Restrictions.or(Restrictions.like("descripcion",
					descripcionCodigo, MatchMode.ANYWHERE), Restrictions.like(
					"codigo", descripcionCodigo, MatchMode.ANYWHERE)));
		}
		if (null != codigo) {
			criteria.add(Restrictions.eq("codigo", codigo));
		}
		if (null != idToExclude) {
			criteria.add(Restrictions.ne("id", idToExclude));
		}

		return criteria;
	}

	@Override
	public boolean estaEnUso(Long id) throws DataAccessException {
		try {

			Criteria criteria = getSession().createCriteria(
					DetalleComprobante.class);
			criteria.setProjection(Projections.rowCount());
			criteria.add(Restrictions.eq("producto.id", id));
			Long cantidad = (Long) criteria.uniqueResult();

//			if (cantidad > 0) {
//				return Boolean.TRUE;
//			} else {
//				criteria = getSession()
//						.createCriteria(DetalleNotaCredito.class);
//				criteria.setProjection(Projections.rowCount());
//				criteria.add(Restrictions.eq("producto.id", id));
//				cantidad = (Long) criteria.uniqueResult();
//				if (cantidad > 0) {
//					return Boolean.TRUE;
//				} else {
//					criteria = getSession().createCriteria(
//							DetalleNotaDebito.class);
//					criteria.setProjection(Projections.rowCount());
//					criteria.add(Restrictions.eq("producto.id", id));
//					cantidad = (Long) criteria.uniqueResult();
//					if (cantidad > 0) {
//						return Boolean.TRUE;
//					} else {
//						criteria = getSession().createCriteria(
//								DetalleRemito.class);
//						criteria.setProjection(Projections.rowCount());
//						criteria.add(Restrictions.eq("producto.id", id));
//						cantidad = (Long) criteria.uniqueResult();
//						if (cantidad > 0) {
//							return Boolean.TRUE;
//						} else {
//							criteria = getSession().createCriteria(
//									StockProducto.class);
//							criteria.setProjection(Projections.rowCount());
//							criteria.add(Restrictions.eq("idProducto", id));
//							cantidad = (Long) criteria.uniqueResult();
//							if (cantidad > 0) {
//								return Boolean.TRUE;
//							}
//						}
//					}
//				}
//			}
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
		return Boolean.FALSE;
	}

}
