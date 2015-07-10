package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.FacturaDAO;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.FacturaFilter;

public class FacturaDAOImpl extends GenericCrudHBDAOImpl<Factura> implements
		FacturaDAO {

	public FacturaDAOImpl() {
		super(Factura.class);
	}
	
	@Override
	public Factura getById(Long id) throws DataAccessException {
		Factura factura = super.getById(id);
		Hibernate.initialize(factura.getDetalles());
		Hibernate.initialize(factura.getRemitos());
		return factura;
	}

	@Override
	public List<Factura> search(FacturaFilter filter)
			throws DataAccessException {
		try {
			Criteria criteria = buildCriteria(filter);
			return criteria.list();
		} catch (HibernateException hbexc) {
			throw new DataAccessException(hbexc);
		}
	}

	private Criteria buildCriteria(FacturaFilter filter) {

		Criteria criteria = getSession().createCriteria(Factura.class);

		Long idCliente = filter.getIdCliente();

		if (null != idCliente) {
			criteria.add(Restrictions.eq("idCliente", idCliente));
		}

		return criteria;
	}

	@Override
	public boolean isUtilizada(Long id) throws DataAccessException {
		// TODO Auto-generated method stub
		return false;
	}

}
