package ar.com.wuik.classlife.bo.impl;

import java.util.List;

import ar.com.dcode.afip.utils.AbstractFactory;
import ar.com.wuik.classlife.bo.UsuarioBO;
import ar.com.wuik.classlife.dao.UsuarioDAO;
import ar.com.wuik.classlife.entities.Usuario;
import ar.com.wuik.classlife.exceptions.BusinessException;
import ar.com.wuik.classlife.exceptions.DataAccessException;


public class UsuarioBOImpl implements UsuarioBO {


	private UsuarioDAO usuarioDAO;

	public UsuarioBOImpl() {
		usuarioDAO = AbstractFactory.getInstance( UsuarioDAO.class );
	}

	@Override
	public Usuario getById( Long id ) throws BusinessException {
		try {
			return usuarioDAO.getById( id );
		}
		catch( DataAccessException daexc ) {
			throw new BusinessException( daexc );
		}
		finally {
		}
	}

	@Override
	public void saveOrUpdate( Usuario usuario ) throws BusinessException {
		try {
			usuarioDAO.saveOrUpdate( usuario );
		}
		catch( DataAccessException daexc ) {
			throw new BusinessException( daexc );
		}
		finally {
		}
	}

	@Override
	public void deleteByIDs( List<Long> idsToDelete ) throws BusinessException {
		try {
			for ( Long id: idsToDelete ) {
				usuarioDAO.delete( id );
			}
		}
		catch( DataAccessException daexc ) {
			throw new BusinessException( daexc );
		}
		finally {
		}
	}

}
