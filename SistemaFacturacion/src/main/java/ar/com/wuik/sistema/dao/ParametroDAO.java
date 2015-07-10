package ar.com.wuik.sistema.dao;

import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.exceptions.DataAccessException;

public interface ParametroDAO extends GenericCrudDAO<Parametro> {

	void incrementarNroRecibo() throws DataAccessException;

	void incrementarNroRemito() throws DataAccessException;
}
