package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.ReciboDAO;
import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ReciboFilter;

public class ReciboDAOImpl extends GenericCrudHBDAOImpl<Recibo> implements
		ReciboDAO {

	public ReciboDAOImpl() {
		super(Recibo.class);
	}
	
	@Override
	public Recibo getById(Long id) throws DataAccessException {
		Recibo recibo = super.getById(id);
		if (null != recibo) {
			Hibernate.initialize(recibo.getNotasDebito());
			Hibernate.initialize(recibo.getFacturas());
			Hibernate.initialize(recibo.getPagosCheque());
			Hibernate.initialize(recibo.getPagosEfectivo());
		}
		return recibo;
	}

	@Override
	public List<Recibo> search(ReciboFilter filter) throws DataAccessException {
		try {
			Criteria criteria = buildCriteria(filter);
			return criteria.list();
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	private Criteria buildCriteria(ReciboFilter filter) {

		Criteria criteria = getSession().createCriteria(Recibo.class);

		Long idCliente = filter.getIdCliente();

		if (null != idCliente) {
			criteria.add(Restrictions.eq("idCliente", idCliente));
		}

		return criteria;
	}
}
