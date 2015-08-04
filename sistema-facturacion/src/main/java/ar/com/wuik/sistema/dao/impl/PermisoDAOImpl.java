package ar.com.wuik.sistema.dao.impl;

import ar.com.wuik.sistema.dao.PermisoDAO;
import ar.com.wuik.sistema.entities.Permiso;

public class PermisoDAOImpl extends GenericCrudHBDAOImpl<Permiso> implements PermisoDAO {

	public PermisoDAOImpl() {
		super(Permiso.class);
	}

}
