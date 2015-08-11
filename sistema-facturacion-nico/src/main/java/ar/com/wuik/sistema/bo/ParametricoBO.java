package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Banco;
import ar.com.wuik.sistema.entities.Localidad;
import ar.com.wuik.sistema.exceptions.BusinessException;

public interface ParametricoBO {

	List<Banco> obtenerTodosBancos() throws BusinessException;

	List<Localidad> obtenerTodosLocalidades() throws BusinessException;

}
