package ar.com.wuik.classlife.dao.impl;

import ar.com.wuik.classlife.dao.ArticuloDAO;
import ar.com.wuik.classlife.entities.Articulo;


public class ArticuloDAOImpl extends GenericCrudHBDAOImpl<Articulo> implements ArticuloDAO {


	public ArticuloDAOImpl() {
		super( Articulo.class );
	}

}
