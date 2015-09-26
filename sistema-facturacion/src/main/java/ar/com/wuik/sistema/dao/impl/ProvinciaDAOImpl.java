package ar.com.wuik.sistema.dao.impl;

import ar.com.wuik.sistema.dao.ProvinciaDAO;
import ar.com.wuik.sistema.entities.Provincia;

public class ProvinciaDAOImpl extends GenericCrudHBDAOImpl<Provincia> implements
		ProvinciaDAO {

	public ProvinciaDAOImpl() {
		super(Provincia.class);
	}

}
