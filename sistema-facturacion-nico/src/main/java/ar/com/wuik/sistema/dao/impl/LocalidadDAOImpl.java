package ar.com.wuik.sistema.dao.impl;

import ar.com.wuik.sistema.dao.LocalidadDAO;
import ar.com.wuik.sistema.entities.Localidad;

public class LocalidadDAOImpl extends GenericCrudHBDAOImpl<Localidad> implements
		LocalidadDAO {

	public LocalidadDAOImpl() {
		super(Localidad.class);
	}

}
