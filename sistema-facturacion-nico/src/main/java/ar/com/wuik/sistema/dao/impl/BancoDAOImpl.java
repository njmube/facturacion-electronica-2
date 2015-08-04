package ar.com.wuik.sistema.dao.impl;

import ar.com.wuik.sistema.dao.BancoDAO;
import ar.com.wuik.sistema.entities.Banco;

public class BancoDAOImpl extends GenericCrudHBDAOImpl<Banco> implements
		BancoDAO {

	public BancoDAOImpl() {
		super(Banco.class);
	}
	

}
