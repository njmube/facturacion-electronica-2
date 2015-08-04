package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.StockProductoDAO;
import ar.com.wuik.sistema.entities.StockProducto;
import ar.com.wuik.sistema.exceptions.DataAccessException;

public class StockProductoDAOImpl extends GenericCrudHBDAOImpl<StockProducto>
		implements StockProductoDAO {

	public StockProductoDAOImpl() {
		super(StockProducto.class);
	}

	@Override
	public List<StockProducto> search(Long idProducto)
			throws DataAccessException {
		try {
			Criteria criteria = getSession()
					.createCriteria(StockProducto.class);
			criteria.add(Restrictions.eq("idProducto", idProducto));
			return criteria.list();
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}
}
