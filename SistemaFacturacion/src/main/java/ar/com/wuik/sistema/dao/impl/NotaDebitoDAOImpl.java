package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.NotaDebitoDAO;
import ar.com.wuik.sistema.entities.NotaDebito;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.NotaDebitoFilter;

public class NotaDebitoDAOImpl extends GenericCrudHBDAOImpl<NotaDebito>
		implements NotaDebitoDAO {

	public NotaDebitoDAOImpl() {
		super(NotaDebito.class);
	}

	@Override
	public NotaDebito getById(Long id) throws DataAccessException {
		NotaDebito notaDebito = super.getById(id);
		Hibernate.initialize(notaDebito.getDetalles());
		Hibernate.initialize(notaDebito.getFacturas());
		return notaDebito;
	}

	@Override
	public List<NotaDebito> search(NotaDebitoFilter filter)
			throws DataAccessException {
		try {
			Criteria criteria = buildCriteria(filter);
			return criteria.list();
		} catch (HibernateException hbexc) {
			throw new DataAccessException(hbexc);
		}
	}

	private Criteria buildCriteria(NotaDebitoFilter filter) {

		Criteria criteria = getSession().createCriteria(NotaDebito.class);

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
