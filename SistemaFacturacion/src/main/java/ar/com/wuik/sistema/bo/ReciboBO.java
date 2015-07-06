package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ClienteFilter;

public interface ReciboBO {

	Recibo obtener(Long id) throws BusinessException;

	List<Recibo> buscar(ClienteFilter filter) throws BusinessException;

	void guardar(Recibo recibo) throws BusinessException;

	void actualizar(Recibo recibo) throws BusinessException;

	void eliminar(Long id) throws BusinessException;

	boolean estaEnUso(Long id) throws BusinessException;

	List<Recibo> obtenerTodos() throws BusinessException;

	void cancelar(Long id) throws BusinessException;
	

}

