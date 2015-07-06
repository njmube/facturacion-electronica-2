package ar.com.wuik.classlife.dao.impl;

import ar.com.wuik.classlife.dao.UsuarioDAO;
import ar.com.wuik.classlife.entities.Usuario;


public class UsuarioDAOImpl extends GenericCrudHBDAOImpl<Usuario> implements UsuarioDAO {


	public UsuarioDAOImpl() {
		super( Usuario.class );
	}

}
