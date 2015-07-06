package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ClienteFilter;

public interface NotaCreditoBO {

	NotaCredito obtener(Long id) throws BusinessException;

	List<NotaCredito> buscar(ClienteFilter filter) throws BusinessException;

	void guardar(NotaCredito notaCredito) throws BusinessException;
	
	void guardarRegistrarAFIP(NotaCredito notaCredito) throws BusinessException;

	void actualizar(NotaCredito notaCredito) throws BusinessException;

	void eliminar(Long id) throws BusinessException;

	boolean estaEnUso(Long id) throws BusinessException;

	List<NotaCredito> obtenerTodos() throws BusinessException;

	void cancelar(Long id) throws BusinessException;
	

}
