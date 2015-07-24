package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.NotaDebitoDAO;
import ar.com.wuik.sistema.entities.NotaDebito;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.NotaDebitoFilter;
import ar.com.wuik.swing.utils.WUtils;

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
		Boolean activo = filter.getActivo();
		List<Long> idsToExclude = filter.getIdsToExclude();
		List<Long> idsToInclude = filter.getIdsToInclude();
		EstadoFacturacion estadoFacturacion = filter.getEstadoFacturacion();
		Boolean paga = filter.getPaga();

		if (null != idCliente) {
			criteria.add(Restrictions.eq("idCliente", idCliente));
		}

		if (null != estadoFacturacion) {
			criteria.add(Restrictions
					.eq("estadoFacturacion", estadoFacturacion));
		}

		if (null != activo) {
			criteria.add(Restrictions.eq("activo", activo));
		}

		if (WUtils.isNotEmpty(idsToInclude)) {
			criteria.add(Restrictions.in("id", idsToInclude));
		}

		if (WUtils.isNotEmpty(idsToExclude)) {
			criteria.add(Restrictions.not(Restrictions.in("id", idsToExclude)));
		}

		if (null != paga) {
			criteria.add(Restrictions.eq("paga", paga));
		}

		return criteria;
	}

	@Override
	public boolean isUtilizada(Long id) throws DataAccessException {
		// TODO Auto-generated method stub
		return false;
	}

}
