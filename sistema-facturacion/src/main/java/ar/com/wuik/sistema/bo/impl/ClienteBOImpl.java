package ar.com.wuik.sistema.bo.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.dao.ClienteDAO;
import ar.com.wuik.sistema.dao.ComprobanteDAO;
import ar.com.wuik.sistema.dao.ReciboDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ClienteFilter;
import ar.com.wuik.sistema.filters.ComprobanteFilter;
import ar.com.wuik.sistema.filters.ReciboFilter;
import ar.com.wuik.sistema.reportes.entities.DetalleResumenCuentaDTO;
import ar.com.wuik.sistema.reportes.entities.ResumenCuentaDTO;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.utils.WUtils;

public class ClienteBOImpl implements ClienteBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ClienteBOImpl.class);
	private ClienteDAO clienteDAO;
	private ComprobanteDAO comprobanteDAO;
	private ReciboDAO reciboDAO;

	public ClienteBOImpl() {
		clienteDAO = AbstractFactory.getInstance(ClienteDAO.class);
		comprobanteDAO = AbstractFactory.getInstance(ComprobanteDAO.class);
		reciboDAO = AbstractFactory.getInstance(ReciboDAO.class);
	}

	@Override
	public Cliente obtener(Long id) throws BusinessException {
		try {
			Cliente cliente = clienteDAO.getById(id);
			return cliente;
		} catch (DataAccessException daexc) {
			LOGGER.error("obtener() - Error al obtener Cliente", daexc);
			throw new BusinessException(daexc, "Error al obtener Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Cliente> buscar(ClienteFilter filter) throws BusinessException {
		try {
			return clienteDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar Clientes", daexc);
			throw new BusinessException(daexc, "Error al buscar Clientes");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(Cliente cliente) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			clienteDAO.saveOrUpdate(cliente);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al guardar Cliente", daexc);
			throw new BusinessException(daexc, "Error al guardar Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void actualizar(Cliente cliente) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			clienteDAO.saveOrUpdate(cliente);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("actualizar() - Error al actualizar Cliente", daexc);
			throw new BusinessException(daexc, "Error al actualizar Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void eliminar(Long idToDelete) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			clienteDAO.delete(idToDelete);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("eliminar() - Error al eliminar Cliente", daexc);
			throw new BusinessException(daexc, "Error al eliminar Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void activar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			Cliente cliente = clienteDAO.getById(id);
			cliente.setActivo(Boolean.TRUE);
			clienteDAO.saveOrUpdate(cliente);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("activar() - Error al activar Cliente", daexc);
			throw new BusinessException(daexc, "Error al activar Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void desactivar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			Cliente cliente = clienteDAO.getById(id);
			cliente.setActivo(Boolean.FALSE);
			clienteDAO.saveOrUpdate(cliente);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("deactivar() - Error al desactivar Cliente", daexc);
			throw new BusinessException(daexc, "Error al desactivar Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Cliente> obtenerTodos() throws BusinessException {
		try {
			return clienteDAO.getAll();
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerTodos() - Error al obtener todos los Cliente",
					daexc);
			throw new BusinessException(daexc,
					"Error al obtener todos los Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public boolean estaEnUso(Long id) throws BusinessException {
		try {
			return clienteDAO.estaEnUso(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("estaEnUso() - Error al validar uso del Cliente",
					daexc);
			throw new BusinessException(daexc,
					"Error al validar uso del Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public ResumenCuentaDTO obtenerResumenCuenta(Long id)
			throws BusinessException {

		try {

			ResumenCuentaDTO resumen = new ResumenCuentaDTO();
			Cliente cliente = clienteDAO.getById(id);
			resumen.setClienteCondIVA(cliente.getCondicionIVA()
					.getAbreviacion());
			resumen.setClienteCuit(cliente.getDocumento());
			resumen.setClienteDomicilio(cliente.getDireccion());
			resumen.setClienteRazonSocial(cliente.getRazonSocial());

			List<DetalleResumenCuentaDTO> detalles = new ArrayList<DetalleResumenCuentaDTO>();

			ComprobanteFilter filter = new ComprobanteFilter();
			filter.setIdCliente(id);
			filter.setActivo(Boolean.TRUE);
			List<Comprobante> comprobantes = comprobanteDAO.search(filter);

			if (WUtils.isNotEmpty(comprobantes)) {
				DetalleResumenCuentaDTO detalle = null;
				for (Comprobante comprobante : comprobantes) {
					detalle = new DetalleResumenCuentaDTO();
					detalle.setFecha(comprobante.getFechaVenta());
					detalle.setFechaVto(comprobante.getFechaCAE());

					String nroComprobante = "";
					if (WUtils.isNotEmpty(comprobante.getNroCompFormato())) {
						nroComprobante = comprobante.getNroCompFormato();
					} else {
						if (WUtils.isNotEmpty(comprobante.getNroComprobante())) {
							nroComprobante = comprobante.getNroComprobante();
						} else {
							nroComprobante = "0000-00000000";
						}
					}

					detalle.setNroComprobante(comprobante.getTipoComprobante()
							.getValue() + nroComprobante);
					switch (comprobante.getTipoComprobante()) {
					case FACTURA:
						detalle.setDebe(comprobante.getTotal());
						break;
					case NOTA_CREDITO:
						detalle.setHaber(comprobante.getTotal());
						break;
					case NOTA_DEBITO:
						detalle.setDebe(comprobante.getTotal());
						break;
					}
					detalles.add(detalle);
				}
			}

			ReciboFilter filterRecibo = new ReciboFilter();
			filterRecibo.setIdCliente(id);
			List<Recibo> recibos = reciboDAO.search(filterRecibo);
			if (WUtils.isNotEmpty(recibos)) {
				DetalleResumenCuentaDTO detalle = null;
				for (Recibo recibo : recibos) {
					detalle = new DetalleResumenCuentaDTO();
					detalle.setHaber(recibo.getTotal());
					detalle.setFecha(recibo.getFecha());
					detalle.setNroComprobante("RC" + recibo.getNumero());
					detalles.add(detalle);
				}
			}

			Collections.sort(detalles, new BeanComparator("fecha"));

			BigDecimal saldoInicial = cliente.getSaldoInicial();
			DetalleResumenCuentaDTO detalle = new DetalleResumenCuentaDTO();

			if (saldoInicial.doubleValue() < 0) {
				detalle.setDebe(saldoInicial.abs());
			} else if (saldoInicial.doubleValue() > 0) {
				detalle.setHaber(saldoInicial);
			} else {
				detalle.setDebe(saldoInicial);
				detalle.setHaber(saldoInicial);
			}
			detalle.setNroComprobante("SALDO INICIAL");
			detalles.add(0, detalle);

			popularSaldos(detalles);
			resumen.setDetalles(detalles);
			resumen.setSaldo(cliente.getSaldo());
			return resumen;
		} catch (DataAccessException daexc) {
			LOGGER.error(
					"obtenerResumenCuenta() - Error al obtener Resumen de Cuenta de Cliente",
					daexc);
			throw new BusinessException(daexc,
					"Error al obtener Resumen de Cuenta de Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private void popularSaldos(List<DetalleResumenCuentaDTO> detalles) {

		if (WUtils.isNotEmpty(detalles)) {
			BigDecimal saldo = BigDecimal.ZERO;
			for (DetalleResumenCuentaDTO detalle : detalles) {
				if (null != detalle.getDebe()) {
					saldo = saldo.subtract(detalle.getDebe());
				} else if (null != detalle.getHaber()) {
					saldo = saldo.add(detalle.getHaber());
				}
				detalle.setSaldo(saldo);
			}
		}

	}
}
