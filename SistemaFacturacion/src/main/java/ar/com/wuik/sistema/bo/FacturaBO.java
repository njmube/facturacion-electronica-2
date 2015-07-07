package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.FacturaFilter;
import ar.com.wuik.sistema.reportes.entities.FacturaDTO;

public interface FacturaBO {

	Factura obtener(Long id) throws BusinessException;

	List<Factura> buscar(FacturaFilter filter) throws BusinessException;

	void guardar(Factura factura) throws BusinessException;
	
	void guardarRegistrarAFIP(Factura factura) throws BusinessException;

	void actualizar(Factura factura) throws BusinessException;

	void eliminar(Long id) throws BusinessException;

	boolean estaEnUso(Long id) throws BusinessException;

	List<Factura> obtenerTodos() throws BusinessException;

	void cancelar(Long id) throws BusinessException;
	
	FacturaDTO obtenerDTO(Long id) throws BusinessException;
	
}
