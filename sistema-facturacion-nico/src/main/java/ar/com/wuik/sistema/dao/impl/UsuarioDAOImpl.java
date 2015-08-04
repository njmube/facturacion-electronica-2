package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.UsuarioDAO;
import ar.com.wuik.sistema.entities.Usuario;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.UsuarioFilter;
import ar.com.wuik.swing.utils.WUtils;


public class UsuarioDAOImpl extends GenericCrudHBDAOImpl<Usuario> implements UsuarioDAO {


	public UsuarioDAOImpl() {
		super( Usuario.class );
	}

	@SuppressWarnings ( "unchecked" )
	@Override
	public List<Usuario> search( UsuarioFilter filter ) throws DataAccessException {
		try {
			Criteria criteria = buildCriteria( filter );
			return criteria.list();
		}
		catch( HibernateException hexc ) {
			throw new DataAccessException( hexc );
		}
	}

	/**
	 * @see ar.com.wuik.sistema.dao.UsuarioDAO#existeUsuario(java.lang.String,
	 *      java.lang.Long)
	 */
	@Override
	public boolean existeUsuario( String dni, Long id ) throws DataAccessException {

		try {
			UsuarioFilter filter = new UsuarioFilter();
			filter.setDni( dni );
			filter.setIdToExclude( id );
			Criteria criteria = buildCriteria( filter );
			criteria.setProjection( Projections.count( "id" ) );
			Long ocurrencias = (Long) criteria.uniqueResult();
			return ocurrencias > 0;
		}catch( HibernateException hexc ) {
			throw new DataAccessException( hexc );
		}
	}

	private Criteria buildCriteria( UsuarioFilter filter ) {

		Criteria criteria = getSession().createCriteria( Usuario.class );
		criteria.setResultTransformer( CriteriaSpecification.DISTINCT_ROOT_ENTITY );

		String dni = filter.getDni();
		String nombre = filter.getNombre();
		String password = filter.getPassword();
		Long idToExclude = filter.getIdToExclude();

		if ( WUtils.isNotEmpty( dni ) ) {
			criteria.add( Restrictions.eq( "dni", dni ) );
		}

		if ( WUtils.isNotEmpty( password ) ) {
			criteria.add( Restrictions.eq( "password", password ) );
		}

		if ( WUtils.isNotEmpty( nombre ) ) {
			criteria.add( Restrictions.like( "nombre", nombre, MatchMode.ANYWHERE ) );
		}

		if ( null != idToExclude ) {
			criteria.add( Restrictions.ne( "id", idToExclude ) );
		}

		return criteria;
	}
}
