package ar.com.wuik.sistema.dao;

import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.entities.enums.TipoLetraComprobante;
import ar.com.wuik.sistema.exceptions.DataAccessException;

public interface ParametroDAO extends GenericCrudDAO<Parametro> {

	void incrementarNroRecibo() throws DataAccessException;

	void incrementarNroRemito() throws DataAccessException;

	void incrementarNroComprobante(TipoComprobante tipoComprobante, TipoLetraComprobante tipoLetraComprobante) throws DataAccessException;

	String obtenerNroComprobante(TipoComprobante tipoComprobante, TipoLetraComprobante tipoLetraComprobante) throws DataAccessException;

	Parametro getParametro() throws DataAccessException;
}
