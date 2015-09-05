package ar.com.wuik.sistema.bo.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FEV1.dif.afip.gov.ar.entities.AlicuotaIVA;
import FEV1.dif.afip.gov.ar.entities.ComprobanteAsociado;
import FEV1.dif.afip.gov.ar.entities.Resultado;
import FEV1.dif.afip.gov.ar.entities.TipoComprobanteEnum;
import FEV1.dif.afip.gov.ar.entities.TipoConceptoEnum;
import FEV1.dif.afip.gov.ar.entities.TipoDocumentoEnum;
import FEV1.dif.afip.gov.ar.entities.TipoIVAEnum;
import FEV1.dif.afip.gov.ar.entities.TipoMonedaEnum;
import FEV1.dif.afip.gov.ar.entities.TipoTributoEnum;
import FEV1.dif.afip.gov.ar.exceptions.ServiceException;
import FEV1.dif.afip.gov.ar.services.FacturacionService;
import FEV1.dif.afip.gov.ar.utils.AbstractFactory;
import ar.com.wuik.sistema.bo.ComprobanteBO;
import ar.com.wuik.sistema.dao.ClienteDAO;
import ar.com.wuik.sistema.dao.ComprobanteDAO;
import ar.com.wuik.sistema.dao.ParametroDAO;
import ar.com.wuik.sistema.dao.RemitoDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.DetalleComprobante;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.entities.TributoComprobante;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.entities.enums.TipoDocumento;
import ar.com.wuik.sistema.entities.enums.TipoLetraComprobante;
import ar.com.wuik.sistema.entities.enums.TipoTributo;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ComprobanteFilter;
import ar.com.wuik.sistema.reportes.entities.ComprobanteDTO;
import ar.com.wuik.sistema.reportes.entities.DetalleComprobanteDTO;
import ar.com.wuik.sistema.reportes.entities.TributoDTO;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.utils.WUtils;

public class ComprobanteBOImpl implements ComprobanteBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ComprobanteBOImpl.class);
	private FacturacionService facturacionService;
	private ComprobanteDAO comprobanteDAO;
	private ClienteDAO clienteDAO;
	private ParametroDAO parametroDAO;
	private RemitoDAO remitoDAO;

	public ComprobanteBOImpl() {
		facturacionService = AbstractFactory
				.getInstance(FacturacionService.class);
		comprobanteDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ComprobanteDAO.class);
		clienteDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ClienteDAO.class);
		parametroDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ParametroDAO.class);
		remitoDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(RemitoDAO.class);
	}

	@Override
	public Comprobante obtener(Long id) throws BusinessException {
		try {
			return comprobanteDAO.getById(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtener() - Error al obtener comprobanteAfip", daexc);
			throw new BusinessException(daexc,
					"Error al obtener comprobanteAfip");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Comprobante> buscar(ComprobanteFilter filter)
			throws BusinessException {
		try {
			return comprobanteDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar comprobantes", daexc);
			throw new BusinessException(daexc, "Error al buscar comprobantes");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(Comprobante comprobanteAfip) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			comprobanteDAO.saveOrUpdate(comprobanteAfip);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al guardar comprobanteAfip", daexc);
			throw new BusinessException(daexc,
					"Error al guardar comprobanteAfip");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardarRegistrarAFIP(Comprobante comprobante)
			throws BusinessException {

		try {
			HibernateUtil.startTransaction();

			boolean errorServicios = Boolean.FALSE;

			// Obtengo el Nro de Comprobante.
			String nrocomprobante = parametroDAO.obtenerNroComprobante(
					comprobante.getTipoComprobante(),
					comprobante.getTipoLetraComprobante());
			comprobante.setNroComprobante(nrocomprobante);
			parametroDAO.incrementarNroComprobante(
					comprobante.getTipoComprobante(),
					comprobante.getTipoLetraComprobante());

			Resultado resultado = null;
			try {
				// Solicito Autorizacion a AFIP.
				FEV1.dif.afip.gov.ar.entities.Comprobante comprobanteAfip = crearComprobante(comprobante);
				resultado = facturacionService
						.solicitarComprobante(comprobanteAfip);

			} catch (ServiceException sexc) {
				errorServicios = Boolean.TRUE;
			}

			// Si existen errores en el resultado los retorno como Exception.
			if (null != resultado && WUtils.isNotEmpty(resultado.getErrores())) {
				HibernateUtil.rollbackTransaction();
				HibernateUtil.closeSession();
				LOGGER.error("guardarRegistrarAFIP() - Error al registrar comprobanteAfip");
				throw new BusinessException(resultado.getMensajeErrores());
			}

			// Si hubo errores en los Servicios, marco el Comprobante con
			// error.
			if (errorServicios) {
				comprobante
						.setEstadoFacturacion(EstadoFacturacion.FACTURADO_ERROR);
			} else {
				// Datos de AFIP
				comprobante.setCae(resultado.getCae());
				comprobante.setFechaCAE(resultado.getFechaVtoCAE());
				comprobante.setPtoVenta(resultado.getPtoVtaFormato());
				comprobante.setEstadoFacturacion(EstadoFacturacion.FACTURADO);
				comprobante.setNroCompFormato(resultado
						.getNroComprobanteFormato());
				comprobante.setCodBarras(resultado.getCodigoBarras());
			}

			// Guardo el Comprobante con los datos de AFIP.
			comprobanteDAO.saveOrUpdate(comprobante);
			HibernateUtil.commitTransaction();

			if (errorServicios) {
				HibernateUtil.closeSession();
				LOGGER.error("guardarRegistrarAFIP() - Error en Servicios");
				throw new BusinessException(
						"Ha ocurrido un error al conectar a AFIP, la comprobanteAfip se ha marcado con error");
			}

		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error(
					"guardarRegistrarAFIP() - Error al registrar comprobanteAfip",
					daexc);
			throw new BusinessException(daexc,
					"Error al registrar comprobanteAfip");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void registrarAFIP(Comprobante comprobante) throws BusinessException {
		try {
			HibernateUtil.startTransaction();

			String nrocomprobante = comprobante.getNroComprobante();
			Resultado resultado = null;
			boolean errorServicios = Boolean.FALSE;

			if (null == nrocomprobante) {
				// Asigno el Nro. de Comprobante y lo incremento.
				nrocomprobante = parametroDAO.obtenerNroComprobante(
						comprobante.getTipoComprobante(),
						comprobante.getTipoLetraComprobante());
				comprobante.setNroComprobante(nrocomprobante);
				parametroDAO.incrementarNroComprobante(
						comprobante.getTipoComprobante(),
						comprobante.getTipoLetraComprobante());

				try {
					// Solicito Autorizacion a AFIP.
					FEV1.dif.afip.gov.ar.entities.Comprobante comprobanteAfip = crearComprobante(comprobante);
					resultado = facturacionService
							.solicitarComprobante(comprobanteAfip);

				} catch (ServiceException sexc) {
					errorServicios = Boolean.TRUE;
				}
			} else {
				try {
					// Consulto si el Comprobante con el Nro. asociado existe en
					// AFIP.
					resultado = facturacionService.consultarComprobante(
							Long.valueOf(nrocomprobante),
							getTipoComprobante(comprobante));

					// Si no existe lo envio a Autorizar a AFIP.
					if (null == resultado.getCae()) {
						FEV1.dif.afip.gov.ar.entities.Comprobante comprobanteAfip = crearComprobante(comprobante);
						resultado = facturacionService
								.solicitarComprobante(comprobanteAfip);
					}
				} catch (ServiceException sexc) {
					errorServicios = Boolean.TRUE;
				}
			}

			// Si existen errores en el resultado los retorno como Exception.
			if (null != resultado && WUtils.isNotEmpty(resultado.getErrores())) {
				HibernateUtil.rollbackTransaction();
				LOGGER.error("registrarAFIP() - Error al registrar comprobanteAfip");
				throw new BusinessException(resultado.getMensajeErrores());
			}

			// Si hubo errores en los Servicios, marco el Comprobante con
			if (errorServicios) {
				comprobante
						.setEstadoFacturacion(EstadoFacturacion.FACTURADO_ERROR);
			} else {
				// Datos de AFIP
				comprobante.setCae(resultado.getCae());
				comprobante.setFechaCAE(resultado.getFechaVtoCAE());
				comprobante.setPtoVenta(resultado.getPtoVtaFormato());
				comprobante.setEstadoFacturacion(EstadoFacturacion.FACTURADO);
				comprobante.setNroCompFormato(resultado
						.getNroComprobanteFormato());
				comprobante.setCodBarras(resultado.getCodigoBarras());
			}

			// Guardo el Comprobante con los datos de AFIP.
			comprobanteDAO.saveOrUpdate(comprobante);
			HibernateUtil.commitTransaction();

			if (errorServicios) {
				HibernateUtil.closeSession();
				LOGGER.error("registrarAFIP() - Error en Servicios");
				throw new BusinessException(
						"Ha ocurrido un error al conectar a AFIP, la comprobanteAfip se ha marcado con error");
			}

		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error(
					"registrarAFIP() - Error al registrar comprobanteAfip",
					daexc);
			throw new BusinessException(daexc,
					"Error al registrar comprobanteAfip");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private FEV1.dif.afip.gov.ar.entities.Comprobante crearComprobante(
			Comprobante comprobante) throws DataAccessException {

		Cliente cliente = clienteDAO.getById(comprobante.getIdCliente());
		long nrocomprobante = Long.valueOf(comprobante.getNroComprobante());

		String documento = cliente.getDocumento().replaceAll("-", "");
		Date fechaComprobante = comprobante.getFechaVenta();
		BigDecimal subtotal = comprobante.getSubtotal();
		BigDecimal iva = comprobante.getIva();
		BigDecimal total = comprobante.getTotal();
		BigDecimal totalTributos = comprobante.getTotalTributos();

		FEV1.dif.afip.gov.ar.entities.Comprobante comprobanteAfip = new FEV1.dif.afip.gov.ar.entities.Comprobante();

		// DATOS DEL CLIENTE.
		comprobanteAfip.setDocNro(Long.valueOf(documento));
		comprobanteAfip.setDocTipo(getTipoDocumento(cliente.getTipoDocumento()));

		// COTIZACION LA TOMA DEL TIPO DE MONEDA PORQUE ES EN PESOS.
		comprobanteAfip.setTipoMoneda(TipoMonedaEnum.PESOS_ARGENTINOS);
		comprobanteAfip.setCotizacion(null);

		// TOTALES.
		comprobanteAfip.setImporteIVA(iva);
		comprobanteAfip.setImporteSubtotal(subtotal);
		comprobanteAfip.setImporteTotal(total);
		comprobanteAfip.setImporteTributos(totalTributos);

		// DATOS GENERALES DEL COMPROBANTE.
		comprobanteAfip.setFechaComprobante(fechaComprobante);
		comprobanteAfip.setTipoComprobante(getTipoComprobante(comprobante));
		comprobanteAfip.setTipoConcepto(TipoConceptoEnum.PRODUCTO);
		comprobanteAfip.setComprobantesAsociados(null);
		comprobanteAfip.setNroComprobante(nrocomprobante);

		// COMPROBANTES ASOCIADOS.
		Set<Comprobante> comprobantes = comprobante.getComprobantesAsociados();
		if (WUtils.isNotEmpty(comprobantes)) {
			List<ComprobanteAsociado> comprobantesAsociados = new ArrayList<ComprobanteAsociado>();
			ComprobanteAsociado comprobanteAsociado = null;
			for (Comprobante comprobanteAsoc : comprobantes) {
				comprobanteAsociado = new ComprobanteAsociado();
				comprobanteAsociado.setNumero(Long.valueOf(comprobanteAsoc
						.getNroComprobante()));
				comprobanteAsociado.setPtoVta(Integer.valueOf(comprobanteAsoc
						.getPtoVenta()));
				comprobanteAsociado
						.setTipoComprobante(getTipoComprobante(comprobanteAsoc));
				comprobantesAsociados.add(comprobanteAsociado);
			}
			comprobanteAfip.setComprobantesAsociados(comprobantesAsociados);
		}

		// TRIBUTOS
		List<TributoComprobante> tributos = comprobante.getTributos();
		if (WUtils.isNotEmpty(tributos)) {
			List<FEV1.dif.afip.gov.ar.entities.TributoComprobante> tributosComprobantes = new ArrayList<FEV1.dif.afip.gov.ar.entities.TributoComprobante>();
			FEV1.dif.afip.gov.ar.entities.TributoComprobante tributoComprobante = null;
			for (TributoComprobante tributo : tributos) {
				tributoComprobante = new FEV1.dif.afip.gov.ar.entities.TributoComprobante();
				tributoComprobante.setAlicuota(tributo.getAlicuota());
				tributoComprobante.setBaseImporte(tributo.getBaseImporte());
				tributoComprobante.setDetalle(tributo.getDetalle());
				tributoComprobante.setImporte(tributo.getImporte());
				tributoComprobante.setTipoTributoEnum(getTipoTributo(tributo
						.getTributo()));
				tributosComprobantes.add(tributoComprobante);
			}
			comprobanteAfip.setTributos(tributosComprobantes);
		}

		// DETALLES DEl COMPROBANTE.
		List<AlicuotaIVA> alicuotas = new ArrayList<AlicuotaIVA>();

		BigDecimal subtotal21 = BigDecimal.ZERO;
		BigDecimal totalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotal105 = BigDecimal.ZERO;
		BigDecimal totalIVA105 = BigDecimal.ZERO;
		List<DetalleComprobante> detalles = comprobante.getDetalles();
		for (DetalleComprobante detalleComprobante : detalles) {
			if (detalleComprobante.getTipoIVA().equals(
					ar.com.wuik.sistema.entities.enums.TipoIVAEnum.IVA_21)) {
				subtotal21 = subtotal21.add(detalleComprobante.getSubtotal());
				totalIVA21 = totalIVA21.add(detalleComprobante.getTotalIVA());
			} else if (detalleComprobante.getTipoIVA().equals(
					ar.com.wuik.sistema.entities.enums.TipoIVAEnum.IVA_105)) {
				subtotal105 = subtotal105.add(detalleComprobante.getSubtotal());
				totalIVA105 = totalIVA105.add(detalleComprobante.getTotalIVA());
			}
		}

		// IVA DEL 21%
		if (subtotal21.doubleValue() > 0) {
			AlicuotaIVA alicuota21 = new AlicuotaIVA();
			alicuota21.setBaseImponible(subtotal21);
			alicuota21.setTipoIVA(TipoIVAEnum.IVA_21);
			alicuota21.setTotalAlicuota(totalIVA21);
			alicuotas.add(alicuota21);
		}

		// IVA DEL 10.5%
		if (subtotal105.doubleValue() > 0) {
			AlicuotaIVA alicuota105 = new AlicuotaIVA();
			alicuota105.setBaseImponible(subtotal105);
			alicuota105.setTipoIVA(TipoIVAEnum.IVA_10_5);
			alicuota105.setTotalAlicuota(totalIVA105);
			alicuotas.add(alicuota105);
		}

		comprobanteAfip.setAlicuotas(alicuotas);

		return comprobanteAfip;
	}

	private TipoDocumentoEnum getTipoDocumento(TipoDocumento tipoDocumento) {
		switch (tipoDocumento) {
		case CUIL:
			return TipoDocumentoEnum.CUIL;
		case CUIT:
			return TipoDocumentoEnum.CUIT;
		case DNI:
			return TipoDocumentoEnum.DNI;
		case OTROS:
			return TipoDocumentoEnum.OTROS;
		}
		return null;
	}

	private TipoTributoEnum getTipoTributo(TipoTributo tributo) {
		switch (tributo) {
		case IMP_INTERNOS:
			return TipoTributoEnum.IMP_INTERNO;
		case IMP_MUNIC:
			return TipoTributoEnum.IMP_MUNICIPAL;
		case PER_RET_IMP_GANANCIAS:
			return TipoTributoEnum.OTRO;
		case PER_RET_ING_BRUTOS:
			return TipoTributoEnum.OTRO;
		case PER_RET_IVA:
			return TipoTributoEnum.OTRO;
		case OTROS:
			return TipoTributoEnum.OTRO;
		}
		return null;
	}

	private TipoComprobanteEnum getTipoComprobante(Comprobante comprobante) {

		TipoComprobante tipoComprobante = comprobante.getTipoComprobante();
		TipoLetraComprobante tipoLetraComprobante = comprobante
				.getTipoLetraComprobante();
		switch (tipoComprobante) {
		case FACTURA:

			if (tipoLetraComprobante.equals(TipoLetraComprobante.A)) {
				return TipoComprobanteEnum.FACTURA_A;
			} else if (tipoLetraComprobante.equals(TipoLetraComprobante.B)) {
				return TipoComprobanteEnum.FACTURA_B;
			}

			break;
		case NOTA_CREDITO:

			if (tipoLetraComprobante.equals(TipoLetraComprobante.A)) {
				return TipoComprobanteEnum.NOTA_CREDITO_A;
			} else if (tipoLetraComprobante.equals(TipoLetraComprobante.B)) {
				return TipoComprobanteEnum.NOTA_CREDITO_B;
			}

			break;
		case NOTA_DEBITO:

			if (tipoLetraComprobante.equals(TipoLetraComprobante.A)) {
				return TipoComprobanteEnum.NOTA_DEBITO_A;
			} else if (tipoLetraComprobante.equals(TipoLetraComprobante.B)) {
				return TipoComprobanteEnum.NOTA_DEBITO_B;
			}

			break;
		}

		return null;
	}

	@Override
	public void actualizar(Comprobante comprobanteAfip)
			throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			comprobanteDAO.saveOrUpdate(comprobanteAfip);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("actualizar() - Error al actualizar comprobanteAfip",
					daexc);
			throw new BusinessException(daexc,
					"Error al actualizar comprobanteAfip");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void cancelar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			Comprobante comprobanteAfip = comprobanteDAO.getById(id);
			comprobanteAfip.setActivo(Boolean.FALSE);
			
			List<Remito> remitos = comprobanteAfip.getRemitos();
			if (null != remitos) {
				for (Remito remito : remitos) {
					remito.setComprobante(null);
					remitoDAO.saveOrUpdate(remito);
				}
			}
			
			comprobanteDAO.saveOrUpdate(comprobanteAfip);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("cancelar() - Error al cancelar comprobanteAfip",
					daexc);
			throw new BusinessException(daexc,
					"Error al cancelar comprobanteAfip");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public ComprobanteDTO obtenerDTO(Long id) throws BusinessException {
		try {
			Comprobante comprobanteAfip = comprobanteDAO.getById(id);
			return convertToDTO(comprobanteAfip);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerDTO() - Error al obtener comprobanteAfip",
					daexc);
			throw new BusinessException(daexc,
					"Error al obtener comprobanteAfip");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private ComprobanteDTO convertToDTO(Comprobante comprobanteAfip)
			throws DataAccessException {
		ComprobanteDTO comprobanteDTO = new ComprobanteDTO();

		// DATOS DEL CLIENTE.
		Cliente cliente = comprobanteAfip.getCliente();
		comprobanteDTO.setClienteCondIVA(cliente.getCondicionIVA()
				.getDenominacion());
		comprobanteDTO.setClienteCuit(cliente.getDocumento());
		comprobanteDTO.setClienteDomicilio(cliente.getDireccion());
		comprobanteDTO.setClienteRazonSocial(cliente.getRazonSocial());

		// DATOS REMITOS.
		List<Remito> remitos = comprobanteAfip.getRemitos();
		if (WUtils.isNotEmpty(remitos)) {
			String detalleRemitos = "";
			for (Remito remito : remitos) {
				detalleRemitos += remito.getNumero() + " - ";
			}
			comprobanteDTO.setRemitos(detalleRemitos.substring(0,
					detalleRemitos.length() - 3));
		}

		// TRIBUTOS
		List<TributoComprobante> tributos = comprobanteAfip.getTributos();
		if (WUtils.isNotEmpty(tributos)) {
			List<TributoDTO> tributosDTO = new ArrayList<TributoDTO>();
			TributoDTO tributoDTO = null;
			for (TributoComprobante tributo : tributos) {
				tributoDTO = new TributoDTO();
				tributoDTO.setAlicuota(tributo.getAlicuota());
				tributoDTO.setDetalle(tributo.getDetalle());
				tributoDTO.setImporte(tributo.getImporte());
				tributoDTO.setTributo(tributo.getTributo().getDescripcion());
				tributosDTO.add(tributoDTO);
			}
			comprobanteDTO.setTributos(tributosDTO);
		}

		// DATOS DE LA comprobanteAfip.
		BigDecimal subtotalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotalIVA105 = BigDecimal.ZERO;

		List<DetalleComprobante> detalles = comprobanteAfip.getDetalles();
		List<DetalleComprobanteDTO> detallesDTO = new ArrayList<DetalleComprobanteDTO>();
		DetalleComprobanteDTO detalleComprobanteDTO = null;
		for (DetalleComprobante detallecomprobante : detalles) {
			detalleComprobanteDTO = new DetalleComprobanteDTO();
			detalleComprobanteDTO.setAlicuota(detallecomprobante.getTipoIVA()
					.getImporte());
			detalleComprobanteDTO.setCantidad(detallecomprobante.getCantidad());
			detalleComprobanteDTO.setCodigo((null != detallecomprobante
					.getProducto()) ? detallecomprobante.getProducto()
					.getCodigo() : "0");
			detalleComprobanteDTO.setPrecioUnit(detallecomprobante.getPrecio());
			detalleComprobanteDTO.setPrecioUnitConIVA(detallecomprobante
					.getPrecioConIVA());
			detalleComprobanteDTO.setProducto((null != detallecomprobante
					.getProducto()) ? detallecomprobante.getProducto()
					.getDescripcion() : detallecomprobante.getDetalle());
			detalleComprobanteDTO.setSubtotal(detallecomprobante.getSubtotal());
			detalleComprobanteDTO.setSubtotalConIVA(detallecomprobante
					.getSubtotalConIVA());
			detallesDTO.add(detalleComprobanteDTO);

			if (detallecomprobante.getTipoIVA().equals(
					ar.com.wuik.sistema.entities.enums.TipoIVAEnum.IVA_21)) {
				subtotalIVA21 = subtotalIVA21.add(detallecomprobante
						.getTotalIVA());
			} else if (detallecomprobante.getTipoIVA().equals(
					ar.com.wuik.sistema.entities.enums.TipoIVAEnum.IVA_105)) {
				subtotalIVA105 = subtotalIVA105.add(detallecomprobante
						.getTotalIVA());
			}
		}
		comprobanteDTO.setDetalles(detallesDTO);
		comprobanteDTO.setCae(comprobanteAfip.getCae());
		comprobanteDTO.setVtoCAE(comprobanteAfip.getFechaCAE());
		comprobanteDTO.setCompNro(comprobanteAfip.getNroComprobante());
		comprobanteDTO.setFechaEmision(comprobanteAfip.getFechaVenta());
		comprobanteDTO.setIva105(subtotalIVA105);
		comprobanteDTO.setIva21(subtotalIVA21);
		comprobanteDTO.setLetra(comprobanteAfip.getTipoLetraComprobante()
				.name());
		comprobanteDTO.setPtoVta(comprobanteAfip.getPtoVenta().toString());
		comprobanteDTO.setSubtotal(comprobanteAfip.getSubtotal());
		comprobanteDTO.setSubtotalConIVA(comprobanteAfip.getTotal());
		comprobanteDTO.setTipo(comprobanteAfip.getTipoComprobante()
				.getDescripcion().toUpperCase());

		BigDecimal total = comprobanteAfip.getTotal();
		if (null != comprobanteAfip.getTotalTributos()) {
			total = total.add(comprobanteAfip.getTotalTributos());
		}

		comprobanteDTO.setTotal(total);
		comprobanteDTO.setOtrosTributos(comprobanteAfip.getTotalTributos());
		comprobanteDTO.setCodigoBarras(comprobanteAfip.getCodBarras());
		return comprobanteDTO;
	}

	@Override
	public Long obtenerUltimoNroComprobante(TipoComprobante tipoComprobante,
			TipoLetraComprobante tipoLetraComprobante) throws BusinessException {
		try {
			TipoComprobanteEnum tipoComprobanteEnum = null;
			switch (tipoComprobante) {
			case FACTURA:

				if (tipoLetraComprobante.equals(TipoLetraComprobante.A)) {
					tipoComprobanteEnum = TipoComprobanteEnum.FACTURA_A;
				} else if (tipoLetraComprobante.equals(TipoLetraComprobante.B)) {
					tipoComprobanteEnum = TipoComprobanteEnum.FACTURA_B;
				}

				break;
			case NOTA_CREDITO:

				if (tipoLetraComprobante.equals(TipoLetraComprobante.A)) {
					tipoComprobanteEnum = TipoComprobanteEnum.NOTA_CREDITO_A;
				} else if (tipoLetraComprobante.equals(TipoLetraComprobante.B)) {
					tipoComprobanteEnum = TipoComprobanteEnum.NOTA_CREDITO_B;
				}

				break;
			case NOTA_DEBITO:

				if (tipoLetraComprobante.equals(TipoLetraComprobante.A)) {
					tipoComprobanteEnum = TipoComprobanteEnum.NOTA_DEBITO_A;
				} else if (tipoLetraComprobante.equals(TipoLetraComprobante.B)) {
					tipoComprobanteEnum = TipoComprobanteEnum.NOTA_DEBITO_B;
				}

				break;
			}

			Resultado resultado = facturacionService
					.consultarUltimoComprobante(tipoComprobanteEnum);
			return resultado.getNroComprobante();
		} catch (ServiceException sexc) {
			LOGGER.error(
					"obtenerUltimoNroComprobante() - Error al obtener último Nro. de Comprobante",
					sexc);
			throw new BusinessException(sexc,
					"Error al obtener último Nro. de Comprobante");
		}
	}
}
