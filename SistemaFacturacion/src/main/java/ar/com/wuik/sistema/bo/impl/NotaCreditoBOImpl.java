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
import ar.com.wuik.sistema.bo.NotaCreditoBO;
import ar.com.wuik.sistema.dao.ClienteDAO;
import ar.com.wuik.sistema.dao.NotaCreditoDAO;
import ar.com.wuik.sistema.dao.ParametroDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.DetalleNotaCredito;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.NotaCreditoFilter;
import ar.com.wuik.sistema.reportes.entities.DetalleNotaCreditoDTO;
import ar.com.wuik.sistema.reportes.entities.NotaCreditoDTO;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.utils.WUtils;

public class NotaCreditoBOImpl implements NotaCreditoBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NotaCreditoBOImpl.class);
	private FacturacionService facturacionService;
	private NotaCreditoDAO notaCreditoDAO;
	private ClienteDAO clienteDAO;
	private ParametroDAO parametroDAO;

	public NotaCreditoBOImpl() {
		facturacionService = AbstractFactory
				.getInstance(FacturacionService.class);
		notaCreditoDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(NotaCreditoDAO.class);
		clienteDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ClienteDAO.class);
		parametroDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ParametroDAO.class);
	}

	@Override
	public NotaCredito obtener(Long id) throws BusinessException {
		try {
			return notaCreditoDAO.getById(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtener() - Error al obtener Nota de Crédito", daexc);
			throw new BusinessException(daexc,
					"Error al obtener Nota de Crédito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<NotaCredito> buscar(NotaCreditoFilter filter)
			throws BusinessException {
		try {
			return notaCreditoDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar Notas de Crédito", daexc);
			throw new BusinessException(daexc,
					"Error al buscar Notas de Crédito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(NotaCredito notaCredito) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			notaCreditoDAO.saveOrUpdate(notaCredito);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al guardar Nota de Crédito", daexc);
			throw new BusinessException(daexc,
					"Error al guardar Nota de Crédito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardarRegistrarAFIP(NotaCredito notaCredito)
			throws BusinessException {
		
		try {
			HibernateUtil.startTransaction();

			boolean errorServicios = Boolean.FALSE;

			// Obtengo el Nro de Nota de Credito.
			String nroNotaCredito = parametroDAO.obtenerNroNotaCredito();
			notaCredito.setNroComprobante(nroNotaCredito);
			parametroDAO.incrementarNroNotaCredito();

			Resultado resultado = null;
			try {
				// Solicito Autorizacion a AFIP.
				Comprobante comprobante = crearComprobante(notaCredito);
				resultado = facturacionService
						.solicitarComprobante(comprobante);

			} catch (ServiceException sexc) {
				errorServicios = Boolean.TRUE;
			}

			// Si existen errores en el resultado los retorno como Exception.
			if (null != resultado && WUtils.isNotEmpty(resultado.getErrores())) {
				HibernateUtil.rollbackTransaction();
				HibernateUtil.closeSession();
				LOGGER.error("guardarRegistrarAFIP() - Error al registrar Nota de Credito");
				throw new BusinessException(resultado.getMensajeErrores());
			}

			// Si hubo errores en los Servicios, marco la Nota de Credito con error.
			if (errorServicios) {
				notaCredito.setEstadoFacturacion(EstadoFacturacion.FACTURADO_ERROR);
			} else {
				// Datos de AFIP
				notaCredito.setCae(resultado.getCae());
				notaCredito.setFechaCAE(resultado.getFechaVtoCAE());
				notaCredito.setPtoVenta(StringUtils.leftPad(resultado.getPtoVta() + "", 4, "0"));
				notaCredito.setEstadoFacturacion(EstadoFacturacion.FACTURADO);
			}

			// Guardo la Nota de Credito con los datos de AFIP.
			notaCreditoDAO.saveOrUpdate(notaCredito);
			HibernateUtil.commitTransaction();
			
			if (errorServicios) {
				HibernateUtil.closeSession();
				LOGGER.error("guardarRegistrarAFIP() - Error en Servicios");
				throw new BusinessException("Ha ocurrido un error al conectar a AFIP, la Nota de Crédito se ha marcado con error");
			}

		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardarRegistrarAFIP() - Error al registrar Nota de Crédito",
					daexc);
			throw new BusinessException(daexc, "Error al registrar Nota de Crédito");
		} finally {
			HibernateUtil.closeSession();
		}
		
	}

	@Override
	public void registrarAFIP(NotaCredito notaCredito) throws BusinessException {
		try {
			HibernateUtil.startTransaction();

			String nroNotaCredito = notaCredito.getNroComprobante();
			Resultado resultado = null;
			boolean errorServicios = Boolean.FALSE;

			if (null == nroNotaCredito) {
				// Asigno el Nro. de Nota Crédito y lo incremento.
				nroNotaCredito = parametroDAO.obtenerNroNotaCredito();
				notaCredito.setNroComprobante(nroNotaCredito);
				parametroDAO.incrementarNroNotaCredito();

				try {
					// Solicito Autorizacion a AFIP.
					Comprobante comprobante = crearComprobante(notaCredito);
					resultado = facturacionService
							.solicitarComprobante(comprobante);

				} catch (ServiceException sexc) {
					errorServicios = Boolean.TRUE;
				}
			} else {
				try {
					// Consulto si el comprobante con el Nro. de Nota de Crédito existe
					// en AFIP.
					resultado = facturacionService.consultarComprobante(
							Long.valueOf(nroNotaCredito), TipoComprobante.NOTA_CREDITO_A);

					// Si no existe lo envio a Autorizar a AFIP.
					if (null == resultado.getCae()) {
						Comprobante comprobante = crearComprobante(notaCredito);
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
				LOGGER.error("registrarAFIP() - Error al registrar Nota de Credito");
				throw new BusinessException(resultado.getMensajeErrores());
			}

			// Si hubo errores en los Servicios, marco la Nota de Credito con error.
			if (errorServicios) {
				notaCredito.setEstadoFacturacion(EstadoFacturacion.FACTURADO_ERROR);
			} else {
				// Datos de AFIP
				notaCredito.setCae(resultado.getCae());
				notaCredito.setFechaCAE(resultado.getFechaVtoCAE());
				notaCredito.setPtoVenta(StringUtils.leftPad(resultado.getPtoVta() + "", 4, "0"));
				notaCredito.setEstadoFacturacion(EstadoFacturacion.FACTURADO);
			}

			// Guardo la Nota de Credito con los datos de AFIP.
			notaCreditoDAO.saveOrUpdate(notaCredito);
			HibernateUtil.commitTransaction();

			if (errorServicios) {
				HibernateUtil.closeSession();
				LOGGER.error("registrarAFIP() - Error en Servicios");
				throw new BusinessException(
						"Ha ocurrido un error al conectar a AFIP, la Nota de Crédito se ha marcado con error");
			}

		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("registrarAFIP() - Error al registrar Nota de Crédito",
					daexc);
			throw new BusinessException(daexc, "Error al registrar Nota de Crédito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private Comprobante crearComprobante(NotaCredito notaCredito)
			throws DataAccessException {

		Cliente cliente = clienteDAO.getById(notaCredito.getIdCliente());
		long nroNotaCredito = Long.valueOf(notaCredito.getNroComprobante());

		String cuit = cliente.getCuit().replaceAll("-", "");
		Date fechaComprobante = notaCredito.getFechaVenta();
		BigDecimal subtotal = notaCredito.getSubtotal();
		BigDecimal iva = notaCredito.getIva();
		BigDecimal total = notaCredito.getTotal();

		Comprobante comprobante = new Comprobante();

		// DATOS DEL CLIENTE.
		comprobante.setDocNro(Long.valueOf(cuit));
		comprobante.setDocTipo(TipoDocumento.CUIT);

		// COTIZACION LA TOMA DEL TIPO DE MONEDA PORQUE ES EN PESOS.
		comprobante.setTipoMoneda(TipoMoneda.PESOS_ARGENTINOS);
		comprobante.setCotizacion(null);

		// COMPROBANTES ASOCIADOS.
		Set<Factura> facturas = notaCredito.getFacturas();
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
		comprobante.setTipoComprobante(TipoComprobante.NOTA_CREDITO_A);
		comprobante.setTipoConcepto(TipoConcepto.PRODUCTO);
		comprobante.setNroComprobante(nroNotaCredito);

		// DETALLES DE LA NOTA DE CREDITO.
		List<AlicuotaIVA> alicuotas = new ArrayList<AlicuotaIVA>();

		BigDecimal subtotal21 = BigDecimal.ZERO;
		BigDecimal totalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotal105 = BigDecimal.ZERO;
		BigDecimal totalIVA105 = BigDecimal.ZERO;
		List<DetalleNotaCredito> detalles = notaCredito.getDetalles();
		for (DetalleNotaCredito detalleNotaCredito : detalles) {
			if (detalleNotaCredito.getIva().doubleValue() == 21.00) {
				subtotal21 = subtotal21.add(detalleNotaCredito.getSubtotal());
				totalIVA21 = totalIVA21.add(detalleNotaCredito.getTotalIVA());
			} else if (detalleNotaCredito.getIva().doubleValue() == 10.50) {
				subtotal105 = subtotal105.add(detalleNotaCredito.getSubtotal());
				totalIVA105 = totalIVA105.add(detalleNotaCredito.getTotalIVA());
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
	public void actualizar(NotaCredito notaCredito) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			notaCreditoDAO.saveOrUpdate(notaCredito);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("actualizar() - Error al actualizar Nota de Crédito",
					daexc);
			throw new BusinessException(daexc,
					"Error al actualizar Nota de Crédito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public NotaCreditoDTO obtenerDTO(Long id) throws BusinessException {
		try {
			NotaCredito notaCredito = notaCreditoDAO.getById(id);
			return convertToDTO(notaCredito);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerDTO() - Error al obtener Nota de Crédito",
					daexc);
			throw new BusinessException(daexc,
					"Error al obtener Nota de Crédito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private NotaCreditoDTO convertToDTO(NotaCredito notaCredito)
			throws DataAccessException {
		NotaCreditoDTO notaCreditoDTO = new NotaCreditoDTO();

		// DATOS DEL CLIENTE.
		Cliente cliente = notaCredito.getCliente();
		notaCreditoDTO.setClienteCondIVA(cliente.getCondicionIVA()
				.getDenominacion());
		notaCreditoDTO.setClienteCuit(cliente.getCuit());
		notaCreditoDTO.setClienteDomicilio(cliente.getDireccion());
		notaCreditoDTO.setClienteRazonSocial(cliente.getRazonSocial());

		// DATOS PROPIOS.
		Parametro parametro = parametroDAO.getParametro();
		notaCreditoDTO.setRazonSocial(parametro.getRazonSocial());
		notaCreditoDTO.setCondIVA(parametro.getCondIVA());
		notaCreditoDTO.setCuit(parametro.getCuit());
		notaCreditoDTO.setDomicilio(parametro.getDomicilio());
		notaCreditoDTO.setIngBrutos(parametro.getIngresosBrutos());
		notaCreditoDTO.setInicioAct(parametro.getInicioActividad());

		// DATOS DE LA NOTA DE CREDITO.
		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal subtotalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotalIVA105 = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;

		List<DetalleNotaCredito> detalles = notaCredito.getDetalles();
		List<DetalleNotaCreditoDTO> detallesDTO = new ArrayList<DetalleNotaCreditoDTO>();
		DetalleNotaCreditoDTO detalleNotaCreditoDTO = null;
		for (DetalleNotaCredito detalleNotaCredito : detalles) {
			detalleNotaCreditoDTO = new DetalleNotaCreditoDTO();
			detalleNotaCreditoDTO.setAlicuota(detalleNotaCredito.getIva());
			detalleNotaCreditoDTO.setCantidad(detalleNotaCredito.getCantidad());
			detalleNotaCreditoDTO.setCodigo((null != detalleNotaCredito
					.getProducto()) ? detalleNotaCredito.getProducto()
					.getCodigo() : "0");
			detalleNotaCreditoDTO.setPrecioUnit(detalleNotaCredito.getPrecio());
			detalleNotaCreditoDTO.setProducto((null != detalleNotaCredito
					.getProducto()) ? detalleNotaCredito.getProducto()
					.getDescripcion() : detalleNotaCredito.getDetalle());
			detalleNotaCreditoDTO.setSubtotal(detalleNotaCredito.getSubtotal());
			detalleNotaCreditoDTO.setSubtotalConIVA(detalleNotaCredito
					.getTotal());
			detalleNotaCreditoDTO.setComentario(detalleNotaCredito
					.getComentario());
			detallesDTO.add(detalleNotaCreditoDTO);

			if (detalleNotaCredito.getIva().doubleValue() == 21.00) {
				subtotalIVA21 = subtotalIVA21.add(detalleNotaCredito
						.getTotalIVA());
			} else if (detalleNotaCredito.getIva().doubleValue() == 10.50) {
				subtotalIVA105 = subtotalIVA105.add(detalleNotaCredito
						.getTotalIVA());
			}
			subtotal = subtotal.add(detalleNotaCredito.getSubtotal());
			total = total.add(detalleNotaCredito.getTotal());
		}
		notaCreditoDTO.setDetalles(detallesDTO);
		notaCreditoDTO.setCae(notaCredito.getCae());
		notaCreditoDTO.setVtoCAE(notaCredito.getFechaCAE());
		notaCreditoDTO.setCompNro(notaCredito.getNroComprobante().toString());
		notaCreditoDTO.setFechaEmision(notaCredito.getFechaVenta());
		notaCreditoDTO.setIva105(subtotalIVA105);
		notaCreditoDTO.setIva21(subtotalIVA21);
		notaCreditoDTO.setLetra("A");
		notaCreditoDTO.setPtoVta(notaCredito.getPtoVenta().toString());
		notaCreditoDTO.setSubtotal(subtotal);
		notaCreditoDTO.setTipo("NOTA DE CREDITO");
		notaCreditoDTO.setTotal(total);
		return notaCreditoDTO;
	}

	@Override
	public Long obtenerUltimoNroComprobante() throws BusinessException {
		try {
			Resultado resultado = facturacionService
					.consultarUltimoComprobante(TipoComprobante.NOTA_CREDITO_A);
			return resultado.getNroComprobante();
		} catch (ServiceException sexc) {
			LOGGER.error(
					"obtenerUltimoNroComprobante() - Error al obtener último Nro. de Crédito",
					sexc);
			throw new BusinessException(sexc,
					"Error al obtener último Nro. de Crédito");
		}
	}
}
