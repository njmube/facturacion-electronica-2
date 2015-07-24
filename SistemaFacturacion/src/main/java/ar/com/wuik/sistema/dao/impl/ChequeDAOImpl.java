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
		String aNombreDe = filter.getaNombreDe();
		Boolean enUso = filter.getEnUso();
		Long idCliente = filter.getIdCliente();
		List<Long> idsToExclude = filter.getIdsToExclude();
		List<Long> idsToInclude = filter.getIdsToInclude();

		if (WUtils.isNotEmpty(numero)) {
			criteria.add(Restrictions.eq("numero", numero));
		}

		if (WUtils.isNotEmpty(aNombreDe)) {
			criteria.add(Restrictions.like("aNombreDe", aNombreDe,
					MatchMode.ANYWHERE));
		}

		if (null != enUso) {
			criteria.add(Restrictions.eq("enUso", enUso));
		}

		if (null != idCliente) {
			criteria.add(Restrictions.eq("idCliente", idCliente));
		}

		if (WUtils.isNotEmpty(idsToInclude)) {
			criteria.add(Restrictions.in("id", idsToInclude));
		}

		if (WUtils.isNotEmpty(idsToExclude)) {
			criteria.add(Restrictions.not(Restrictions.in("id", idsToExclude)));
		}

		return criteria;
	}
}
