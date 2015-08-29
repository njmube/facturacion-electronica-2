package ar.com.wuik.sistema.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import ar.com.wuik.sistema.dao.ComprobanteDAO;
import ar.com.wuik.sistema.dao.ComprobanteProveedorDAO;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ComprobanteFilter;
import ar.com.wuik.sistema.filters.ComprobanteProveedorFilter;
import ar.com.wuik.swing.utils.WUtils;

public class ComprobanteProveedorDAOImpl extends GenericCrudHBDAOImpl<Comprobante> implements
		ComprobanteProveedorDAO {

	public ComprobanteProveedorDAOImpl() {
		super(Comprobante.class);
	}

	@Override
	public Comprobante getById(Long id) throws DataAccessException {
		Comprobante comprobante = super.getById(id);
		Hibernate.initialize(comprobante.getDetalles());
		Hibernate.initialize(comprobante.getRemitos());
		Hibernate.initialize(comprobante.getComprobantesAsociados());
		Hibernate.initialize(comprobante.getTributos());
		return comprobante;
	}

	@Override
	public List<Comprobante> search (ComprobanteProveedorFilter filter)
			throws DataAccessException {
		try {
			Criteria criteria = buildCriteria(filter);
			List<Comprobante> comprobantes = criteria.list();
			if (WUtils.isNotEmpty(comprobantes)) {
				Boolean inicializarDetalles = filter.getInicializarDetalles();
				if (null != inicializarDetalles && inicializarDetalles) {
					for (Comprobante comprobante : comprobantes) {
						Hibernate.initialize(comprobante.getDetalles());
					}
				}
			}
			return comprobantes;
		} catch (HibernateException hbexc) {
			throw new DataAccessException(hbexc);
		}
	}

	private Criteria buildCriteria(ComprobanteProveedorFilter filter) {

		Criteria criteria = getSession().createCriteria(Comprobante.class);

		Long idProveedor = filter.getIdProveedor();
		Boolean asignado = filter.getAsignado();
		Boolean activo = filter.getActivo();
		List<Long> idsToExclude = filter.getIdsToExclude();
		List<Long> idsToInclude = filter.getIdsToInclude();
		EstadoFacturacion estadoFacturacion = filter.getEstadoFacturacion();
		Boolean paga = filter.getPaga();
		Date desde = filter.getDesde();
		Date hasta = filter.getHasta();
		TipoComprobante tipo = filter.getTipoComprobante();

		if (null != idProveedor) {
			criteria.add(Restrictions.eq("idProveedor", idProveedor));
		}
		
		if (null != tipo) {
			criteria.add(Restrictions.eq("tipoComprobante", tipo));
		}

		if (null != asignado) {
			criteria.createAlias("notasCredito", "notasCredito",
					JoinType.LEFT_OUTER_JOIN);
			if (asignado) {
				criteria.add(Restrictions.sizeGt("notasCredito", 0));
			} else {
				criteria.add(Restrictions.sizeEq("notasCredito", 0));
			}
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
			criteria.add(Restrictions.eq("pago", paga));
		}

		if (null != desde && null != hasta) {
			criteria.add(Restrictions.between("fechaVenta", desde, hasta));
		}

		return criteria;
	}

	@Override
	public boolean isUtilizada(Long id) throws DataAccessException {
		// TODO Auto-generated method stub
		return false;
	}

	



}
