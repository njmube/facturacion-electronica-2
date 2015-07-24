package ar.com.wuik.sistema.bo.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FEV1.dif.afip.gov.ar.entities.AlicuotaIVA;
import FEV1.dif.afip.gov.ar.entities.Comprobante;
import FEV1.dif.afip.gov.ar.entities.ComprobanteAsociado;
import FEV1.dif.afip.gov.ar.entities.Resultado;
import FEV1.dif.afip.gov.ar.entities.TipoComprobante;
import FEV1.dif.afip.gov.ar.entities.TipoConcepto;
import FEV1.dif.afip.gov.ar.entities.TipoDocumento;
import FEV1.dif.afip.gov.ar.entities.TipoIVA;
import FEV1.dif.afip.gov.ar.entities.TipoMoneda;
import FEV1.dif.afip.gov.ar.exceptions.ServiceException;
import FEV1.dif.afip.gov.ar.services.FacturacionService;
import FEV1.dif.afip.gov.ar.utils.AbstractFactory;
import ar.com.wuik.sistema.bo.NotaDebitoBO;
import ar.com.wuik.sistema.dao.ClienteDAO;
import ar.com.wuik.sistema.dao.NotaDebitoDAO;
import ar.com.wuik.sistema.dao.ParametroDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.DetalleNotaDebito;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.NotaDebito;
import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.NotaDebitoFilter;
import ar.com.wuik.sistema.reportes.entities.DetalleNotaDebitoDTO;
import ar.com.wuik.sistema.reportes.entities.NotaDebitoDTO;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.utils.WUtils;

public class NotaDebitoBOImpl implements NotaDebitoBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NotaDebitoBOImpl.class);
	private FacturacionService facturacionService;
	private NotaDebitoDAO notaDebitoDAO;
	private ClienteDAO clienteDAO;
	private ParametroDAO parametroDAO;

	public NotaDebitoBOImpl() {
		facturacionService = AbstractFactory
				.getInstance(FacturacionService.class);
		notaDebitoDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(NotaDebitoDAO.class);
		clienteDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ClienteDAO.class);
		parametroDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ParametroDAO.class);
	}

	@Override
	public NotaDebito obtener(Long id) throws BusinessException {
		try {
			return notaDebitoDAO.getById(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtener() - Error al obtener Nota de Débito", daexc);
			throw new BusinessException(daexc,
					"Error al obtener Nota de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<NotaDebito> buscar(NotaDebitoFilter filter)
			throws BusinessException {
		try {
			return notaDebitoDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar Notas de Débito", daexc);
			throw new BusinessException(daexc,
					"Error al buscar Notas de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(NotaDebito notaDebito) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			notaDebitoDAO.saveOrUpdate(notaDebito);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al guardar Nota de Débito", daexc);
			throw new BusinessException(daexc,
					"Error al guardar Nota de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardarRegistrarAFIP(NotaDebito notaDebito)
			throws BusinessException {
		try {
			HibernateUtil.startTransaction();

			boolean errorServicios = Boolean.FALSE;

			// Obtengo el Nro de Nota de Debito.
			String nroNotaDebito = parametroDAO.obtenerNroNotaDebito();
			notaDebito.setNroComprobante(nroNotaDebito);
			parametroDAO.incrementarNroNotaDebito();

			Resultado resultado = null;
			try {
				// Solicito Autorizacion a AFIP.
				Comprobante comprobante = crearComprobante(notaDebito);
				resultado = facturacionService
						.solicitarComprobante(comprobante);

			} catch (ServiceException sexc) {
				errorServicios = Boolean.TRUE;
			}

			// Si existen errores en el resultado los retorno como Exception.
			if (null != resultado && WUtils.isNotEmpty(resultado.getErrores())) {
				HibernateUtil.rollbackTransaction();
				HibernateUtil.closeSession();
				LOGGER.error("guardarRegistrarAFIP() - Error al registrar Nota de Debito");
				throw new BusinessException(resultado.getMensajeErrores());
			}

			// Si hubo errores en los Servicios, marco la Nota de Debito con
			// error.
			if (errorServicios) {
				notaDebito
						.setEstadoFacturacion(EstadoFacturacion.FACTURADO_ERROR);
			} else {
				// Datos de AFIP
				notaDebito.setCae(resultado.getCae());
				notaDebito.setFechaCAE(resultado.getFechaVtoCAE());
				notaDebito.setPtoVenta(StringUtils.leftPad(resultado.getPtoVta() + "", 4, "0"));
				notaDebito.setEstadoFacturacion(EstadoFacturacion.FACTURADO);
			}

			// Guardo la Nota de Debito con los datos de AFIP.
			notaDebitoDAO.saveOrUpdate(notaDebito);
			HibernateUtil.commitTransaction();

			if (errorServicios) {
				HibernateUtil.closeSession();
				LOGGER.error("guardarRegistrarAFIP() - Error en Servicios");
				throw new BusinessException(
						"Ha ocurrido un error al conectar a AFIP, la Nota de Débito se ha marcado con error");
			}

		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error(
					"guardarRegistrarAFIP() - Error al registrar Nota de Débito",
					daexc);
			throw new BusinessException(daexc,
					"Error al registrar Nota de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void registrarAFIP(NotaDebito notaDebito) throws BusinessException {
		try {
			HibernateUtil.startTransaction();

			String nroNotaDebito = notaDebito.getNroComprobante();
			Resultado resultado = null;
			boolean errorServicios = Boolean.FALSE;

			if (null == nroNotaDebito) {
				// Asigno el Nro. de Nota Debito y lo incremento.
				nroNotaDebito = parametroDAO.obtenerNroNotaDebito();
				notaDebito.setNroComprobante(nroNotaDebito);
				parametroDAO.incrementarNroNotaDebito();

				try {
					// Solicito Autorizacion a AFIP.
					Comprobante comprobante = crearComprobante(notaDebito);
					resultado = facturacionService
							.solicitarComprobante(comprobante);

				} catch (ServiceException sexc) {
					errorServicios = Boolean.TRUE;
				}
			} else {
				try {
					// Consulto si el comprobante con el Nro. de Nota de Debito
					// existe
					// en AFIP.
					resultado = facturacionService.consultarComprobante(
							Long.valueOf(nroNotaDebito), TipoComprobante.NOTA_CREDITO_A);

					// Si no existe lo envio a Autorizar a AFIP.
					if (null == resultado.getCae()) {
						Comprobante comprobante = crearComprobante(notaDebito);
						resultado = facturacionService
								.solicitarComprobante(comprobante);
					}
				} catch (ServiceException sexc) {
					errorServicios = Boolean.TRUE;
				}
			}

			// Si existen errores en el resultado los retorno como Exception.
			if (null != resultado && WUtils.isNotEmpty(resultado.getErrores())) {
				HibernateUtil.rollbackTransaction();
				LOGGER.error("registrarAFIP() - Error al registrar Nota de Debito");
				throw new BusinessException(resultado.getMensajeErrores());
			}

			// Si hubo errores en los Servicios, marco la Nota de Debito con
			// error.
			if (errorServicios) {
				notaDebito
						.setEstadoFacturacion(EstadoFacturacion.FACTURADO_ERROR);
			} else {
				// Datos de AFIP
				notaDebito.setCae(resultado.getCae());
				notaDebito.setFechaCAE(resultado.getFechaVtoCAE());
				notaDebito.setPtoVenta(StringUtils.leftPad(resultado.getPtoVta() + "", 4, "0"));
				notaDebito.setEstadoFacturacion(EstadoFacturacion.FACTURADO);
			}

			// Guardo la Nota de Debito con los datos de AFIP.
			notaDebitoDAO.saveOrUpdate(notaDebito);
			HibernateUtil.commitTransaction();

			if (errorServicios) {
				HibernateUtil.closeSession();
				LOGGER.error("registrarAFIP() - Error en Servicios");
				throw new BusinessException(
						"Ha ocurrido un error al conectar a AFIP, la Nota de Débito se ha marcado con error");
			}

		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("registrarAFIP() - Error al registrar Nota de Débito",
					daexc);
			throw new BusinessException(daexc,
					"Error al registrar Nota de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private Comprobante crearComprobante(NotaDebito notaDebito)
			throws DataAccessException {

		Cliente cliente = clienteDAO.getById(notaDebito.getIdCliente());
		long nroNotaCredito = Long.valueOf(notaDebito.getNroComprobante());

		String cuit = cliente.getCuit().replaceAll("-", "");
		Date fechaComprobante = notaDebito.getFechaVenta();
		BigDecimal subtotal = notaDebito.getSubtotal();
		BigDecimal iva = notaDebito.getIva();
		BigDecimal total = notaDebito.getTotal();

		Comprobante comprobante = new Comprobante();

		// DATOS DEL CLIENTE.
		comprobante.setDocNro(Long.valueOf(cuit));
		comprobante.setDocTipo(TipoDocumento.CUIT);

		// COTIZACION LA TOMA DEL TIPO DE MONEDA PORQUE ES EN PESOS.
		comprobante.setTipoMoneda(TipoMoneda.PESOS_ARGENTINOS);
		comprobante.setCotizacion(null);

		// COMPROBANTES ASOCIADOS.
		Set<Factura> facturas = notaDebito.getFacturas();
		if (WUtils.isNotEmpty(facturas)) {
			List<ComprobanteAsociado> comprobantesAsociados = new ArrayList<ComprobanteAsociado>();
			ComprobanteAsociado comprobanteAsociado = null;
			for (Factura factura : facturas) {
				comprobanteAsociado = new ComprobanteAsociado();
				comprobanteAsociado.setNumero(Long.valueOf(factura.getNroComprobante()));
				comprobanteAsociado.setPtoVta(Integer.valueOf(factura.getPtoVenta()));
				comprobanteAsociado
						.setTipoComprobante(TipoComprobante.FACTURA_A);
				comprobantesAsociados.add(comprobanteAsociado);
			}
			comprobante.setComprobantesAsociados(comprobantesAsociados);
		}

		// TOTALES.
		comprobante.setImporteIVA(iva);
		comprobante.setImporteSubtotal(subtotal);
		comprobante.setImporteTotal(total);

		// DATOS GENERALES DEL COMPROBANTE.
		comprobante.setFechaComprobante(fechaComprobante);
		comprobante.setTipoComprobante(TipoComprobante.NOTA_DEBITO_A);
		comprobante.setTipoConcepto(TipoConcepto.PRODUCTO);
		comprobante.setNroComprobante(nroNotaCredito);

		// DETALLES DE LA NOTA DE DEBITO.
		List<AlicuotaIVA> alicuotas = new ArrayList<AlicuotaIVA>();

		BigDecimal subtotal21 = BigDecimal.ZERO;
		BigDecimal totalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotal105 = BigDecimal.ZERO;
		BigDecimal totalIVA105 = BigDecimal.ZERO;
		List<DetalleNotaDebito> detalles = notaDebito.getDetalles();
		for (DetalleNotaDebito detalleNotaDebito : detalles) {
			if (detalleNotaDebito.getIva().doubleValue() == 21.00) {
				subtotal21 = subtotal21.add(detalleNotaDebito.getSubtotal());
				totalIVA21 = totalIVA21.add(detalleNotaDebito.getTotalIVA());
			} else if (detalleNotaDebito.getIva().doubleValue() == 10.50) {
				subtotal105 = subtotal105.add(detalleNotaDebito.getSubtotal());
				totalIVA105 = totalIVA105.add(detalleNotaDebito.getTotalIVA());
			}
		}

		// IVA DEL 21%
		if (subtotal21.doubleValue() > 0) {
			AlicuotaIVA alicuota21 = new AlicuotaIVA();
			alicuota21.setBaseImponible(subtotal21);
			alicuota21.setTipoIVA(TipoIVA.IVA_21);
			alicuota21.setTotalAlicuota(totalIVA21);
			alicuotas.add(alicuota21);
		}

		// IVA DEL 10.5%
		if (subtotal105.doubleValue() > 0) {
			AlicuotaIVA alicuota105 = new AlicuotaIVA();
			alicuota105.setBaseImponible(subtotal105);
			alicuota105.setTipoIVA(TipoIVA.IVA_10_5);
			alicuota105.setTotalAlicuota(totalIVA105);
			alicuotas.add(alicuota105);
		}

		comprobante.setAlicuotas(alicuotas);

		return comprobante;
	}

	@Override
	public void actualizar(NotaDebito notaDebito) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			notaDebitoDAO.saveOrUpdate(notaDebito);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("actualizar() - Error al actualizar Nota de Débito",
					daexc);
			throw new BusinessException(daexc,
					"Error al actualizar Nota de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public NotaDebitoDTO obtenerDTO(Long id) throws BusinessException {
		try {
			NotaDebito notaDebito = notaDebitoDAO.getById(id);
			return convertToDTO(notaDebito);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerDTO() - Error al obtener Nota de Débito",
					daexc);
			throw new BusinessException(daexc,
					"Error al obtener Nota de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private NotaDebitoDTO convertToDTO(NotaDebito notaDebito)
			throws DataAccessException {

		NotaDebitoDTO notaDebitoDTO = new NotaDebitoDTO();

		// DATOS DEL CLIENTE.
		Cliente cliente = notaDebito.getCliente();
		notaDebitoDTO.setClienteCondIVA(cliente.getCondicionIVA()
				.getDenominacion());
		notaDebitoDTO.setClienteCuit(cliente.getCuit());
		notaDebitoDTO.setClienteDomicilio(cliente.getDireccion());
		notaDebitoDTO.setClienteRazonSocial(cliente.getRazonSocial());

		// DATOS PROPIOS.
		Parametro parametro = parametroDAO.getParametro();
		notaDebitoDTO.setRazonSocial(parametro.getRazonSocial());
		notaDebitoDTO.setCondIVA(parametro.getCondIVA());
		notaDebitoDTO.setCuit(parametro.getCuit());
		notaDebitoDTO.setDomicilio(parametro.getDomicilio());
		notaDebitoDTO.setIngBrutos(parametro.getIngresosBrutos());
		notaDebitoDTO.setInicioAct(parametro.getInicioActividad());

		// DATOS DE LA NOTA DE DEBITO.
		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal subtotalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotalIVA105 = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;

		List<DetalleNotaDebito> detalles = notaDebito.getDetalles();
		List<DetalleNotaDebitoDTO> detallesDTO = new ArrayList<DetalleNotaDebitoDTO>();
		DetalleNotaDebitoDTO detalleNotaDebitoDTO = null;
		for (DetalleNotaDebito detalleNotaDebito : detalles) {
			detalleNotaDebitoDTO = new DetalleNotaDebitoDTO();
			detalleNotaDebitoDTO.setAlicuota(detalleNotaDebito.getIva());
			detalleNotaDebitoDTO.setCantidad(detalleNotaDebito.getCantidad());
			detalleNotaDebitoDTO.setCodigo((null != detalleNotaDebito
					.getProducto()) ? detalleNotaDebito.getProducto()
					.getCodigo() : "0");
			detalleNotaDebitoDTO.setPrecioUnit(detalleNotaDebito.getPrecio());
			detalleNotaDebitoDTO.setProducto((null != detalleNotaDebito
					.getProducto()) ? detalleNotaDebito.getProducto()
					.getDescripcion() : detalleNotaDebito.getDetalle());
			detalleNotaDebitoDTO.setSubtotal(detalleNotaDebito.getSubtotal());
			detalleNotaDebitoDTO
					.setSubtotalConIVA(detalleNotaDebito.getTotal());
			detalleNotaDebitoDTO.setComentario(detalleNotaDebito
					.getComentario());
			detallesDTO.add(detalleNotaDebitoDTO);

			if (detalleNotaDebito.getIva().doubleValue() == 21.00) {
				subtotalIVA21 = subtotalIVA21.add(detalleNotaDebito
						.getTotalIVA());
			} else if (detalleNotaDebito.getIva().doubleValue() == 10.50) {
				subtotalIVA105 = subtotalIVA105.add(detalleNotaDebito
						.getTotalIVA());
			}
			subtotal = subtotal.add(detalleNotaDebito.getSubtotal());
			total = total.add(detalleNotaDebito.getTotal());
		}
		notaDebitoDTO.setDetalles(detallesDTO);
		notaDebitoDTO.setCae(notaDebito.getCae());
		notaDebitoDTO.setVtoCAE(notaDebito.getFechaCAE());
		notaDebitoDTO.setCompNro(notaDebito.getNroComprobante().toString());
		notaDebitoDTO.setFechaEmision(notaDebito.getFechaVenta());
		notaDebitoDTO.setIva105(subtotalIVA105);
		notaDebitoDTO.setIva21(subtotalIVA21);
		notaDebitoDTO.setLetra("A");
		notaDebitoDTO.setPtoVta(notaDebito.getPtoVenta().toString());
		notaDebitoDTO.setSubtotal(subtotal);
		notaDebitoDTO.setTipo("NOTA DE DEBITO");
		notaDebitoDTO.setTotal(total);
		return notaDebitoDTO;
	}

	@Override
	public Long obtenerUltimoNroComprobante() throws BusinessException {
		try {
			Resultado resultado = facturacionService
					.consultarUltimoComprobante(TipoComprobante.NOTA_DEBITO_A);
			return resultado.getNroComprobante();
		} catch (ServiceException sexc) {
			LOGGER.error(
					"obtenerUltimoNroComprobante() - Error al obtener último Nro. de Nota de Débito",
					sexc);
			throw new BusinessException(sexc,
					"Error al obtener último Nro. de Nota de Débito");
		}
	}

}
