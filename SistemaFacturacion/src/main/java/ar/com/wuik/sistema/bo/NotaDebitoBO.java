package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.NotaDebito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ClienteFilter;

public interface NotaDebitoBO {

	NotaDebito obtener(Long id) throws BusinessException;

	List<NotaDebito> buscar(ClienteFilter filter) throws BusinessException;

	void guardar(NotaDebito notaDebito) throws BusinessException;
	
	void guardarRegistrarAFIP(NotaDebito notaDebito) throws BusinessException;

	void actualizar(NotaDebito notaDebito) throws BusinessException;

	void eliminar(Long id) throws BusinessException;

	boolean estaEnUso(Long id) throws BusinessException;

	List<NotaDebito> obtenerTodos() throws BusinessException;

	void cancelar(Long id) throws BusinessException;
	

}

