package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import ar.com.wuik.sistema.bo.FacturaBO;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ClienteFilter;

public class FacturaBOImpl implements FacturaBO{

	@Override
	public Factura obtener(Long id) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Factura> buscar(ClienteFilter filter) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void guardar(Factura factura) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardarRegistrarAFIP(Factura factura) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actualizar(Factura factura) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar(Long id) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean estaEnUso(Long id) throws BusinessException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Factura> obtenerTodos() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelar(Long id) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

}
