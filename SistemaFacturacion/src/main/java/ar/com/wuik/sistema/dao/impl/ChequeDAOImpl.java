package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.ChequeDAO;
import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ChequeFilter;
import ar.com.wuik.swing.utils.WUtils;

public class ChequeDAOImpl extends GenericCrudHBDAOImpl<Cheque> implements
		ChequeDAO {

	public ChequeDAOImpl() {
		super(Cheque.class);
	}

	public List<Cheque> search(ChequeFilter filter) throws DataAccessException {
		try {
			Criteria criteria = createCriteria(filter);
			return criteria.list();
		} catch (HibernateException hbexc) {
			throw new DataAccessException(hbexc);
		}
	}

	private Criteria createCriteria(ChequeFilter filter) {
		Criteria criteria = getSession().createCriteria(Cheque.class);

		String numero = filter.getNumero();
		String recibidoDe = filter.getRecibidoDe();

		if (WUtils.isNotEmpty(numero)) {
			criteria.add(Restrictions.eq("numero", numero));
		}

		if (WUtils.isNotEmpty(recibidoDe)) {
			criteria.add(Restrictions.like("recibidoDe", recibidoDe, MatchMode.ANYWHERE));
		}
		return criteria;
	}
	
	@Override
	public boolean estaEnUso(Long id) throws DataAccessException {
		// TODO Auto-generated method stub
		return false;
	}
}
