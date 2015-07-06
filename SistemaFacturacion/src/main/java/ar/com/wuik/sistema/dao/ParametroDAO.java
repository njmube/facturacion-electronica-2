package ar.com.wuik.sistema.dao;

import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.exceptions.DataAccessException;

public interface ParametroDAO extends GenericCrudDAO<Parametro> {
	
	void incrementarNroNotaCredito() throws DataAccessException;

	void incrementarNroNotaDebito() throws DataAccessException;

	void incrementarNroFactura() throws DataAccessException;

	void incrementarNroRecibo() throws DataAccessException;

	void incrementarNroMovInterno() throws DataAccessException;

	void incrementarNroRemito() throws DataAccessException;
}
