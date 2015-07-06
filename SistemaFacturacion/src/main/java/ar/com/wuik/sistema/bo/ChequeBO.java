package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ChequeFilter;

public interface ChequeBO {

	Cheque obtener(Long id) throws BusinessException;

	List<Cheque> buscar(ChequeFilter filter) throws BusinessException;

	void guardar(Cheque cheque) throws BusinessException;

	void actualizar(Cheque cheque) throws BusinessException;

	void eliminar(Long id) throws BusinessException;

	boolean estaEnUso(Long id) throws BusinessException;

	List<Cheque> obtenerTodos() throws BusinessException;
}
