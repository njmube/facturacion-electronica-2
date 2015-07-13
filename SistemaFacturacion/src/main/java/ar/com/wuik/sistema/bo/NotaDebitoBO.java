package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.NotaDebito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.NotaDebitoFilter;
import ar.com.wuik.sistema.reportes.entities.NotaDebitoDTO;

public interface NotaDebitoBO {

	NotaDebito obtener(Long id) throws BusinessException;

	List<NotaDebito> buscar(NotaDebitoFilter filter) throws BusinessException;

	void guardar(NotaDebito notaDebito) throws BusinessException;

	void guardarRegistrarAFIP(NotaDebito notaDebito) throws BusinessException;

	void actualizar(NotaDebito notaDebito) throws BusinessException;

	NotaDebitoDTO obtenerDTO(Long id) throws BusinessException;

}

