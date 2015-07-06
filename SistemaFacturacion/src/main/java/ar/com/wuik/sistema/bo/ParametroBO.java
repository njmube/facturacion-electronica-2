package ar.com.wuik.sistema.bo;

import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.exceptions.BusinessException;

public interface ParametroBO {

	Parametro obtener(Long id) throws BusinessException;

	void actualizar(Parametro parametro) throws BusinessException;

	String getNroRecibo() throws BusinessException;

	String getNroRemito() throws BusinessException;
}
