package ar.com.wuik.sistema.dao.impl;

import ar.com.wuik.sistema.dao.CondicionIVADAO;
import ar.com.wuik.sistema.entities.CondicionIVA;

public class CondicionIVADAOImpl extends GenericCrudHBDAOImpl<CondicionIVA>
		implements CondicionIVADAO {

	public CondicionIVADAOImpl() {
		super(CondicionIVA.class);
	}

}
