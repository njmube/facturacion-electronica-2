package ar.com.wuik.sistema.dao;

import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.exceptions.DataAccessException;

public interface ParametroDAO extends GenericCrudDAO<Parametro> {

	void incrementarNroRecibo() throws DataAccessException;

	void incrementarNroRemito() throws DataAccessException;

	void incrementarNroFactura() throws DataAccessException;

	void incrementarNroNotaCredito() throws DataAccessException;

	void incrementarNroNotaDebito() throws DataAccessException;

	String obtenerNroFactura() throws DataAccessException;

	String obtenerNroNotaCredito() throws DataAccessException;

	String obtenerNroNotaDebito() throws DataAccessException;
	
	String obtenerNroFacturaFormato() throws DataAccessException;

	String obtenerNroNotaCreditoFormato() throws DataAccessException;

	String obtenerNroNotaDebitoFormato() throws DataAccessException;

	String obtenerPtoVenta() throws DataAccessException;

	Parametro getParametro() throws DataAccessException;
}
