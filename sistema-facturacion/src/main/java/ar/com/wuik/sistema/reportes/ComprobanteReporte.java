package ar.com.wuik.sistema.reportes;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ComprobanteBO;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.reportes.entities.ComprobanteDTO;
import ar.com.wuik.sistema.reportes.entities.DetalleComprobanteDTO;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.MailUtil;
import ar.com.wuik.swing.utils.WJasperUtils;

public class ComprobanteReporte {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ComprobanteReporte.class);

	public static void generarImpresion(Long idComprobante)
			throws ReportException {

		try {
			ComprobanteBO comprobanteBO = AbstractFactory
					.getInstance(ComprobanteBO.class);
			ComprobanteDTO comprobanteDTO = comprobanteBO
					.obtenerDTO(idComprobante);

			List<DetalleComprobanteDTO> detalles = comprobanteDTO.getDetalles();
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("COMP_TIPO", comprobanteDTO.getTipo());
			parameters.put("COMP_NRO", comprobanteDTO.getCompNro());
			parameters.put("FECHA_EMISION", comprobanteDTO.getFechaEmision());
			parameters.put("CLIENTE_CUIT", comprobanteDTO.getClienteCuit());
			parameters.put("CLIENTE_COND_IVA",
					comprobanteDTO.getClienteCondIVA());
			parameters.put("CLIENTE_RAZON",
					comprobanteDTO.getClienteRazonSocial());
			parameters.put("CLIENTE_DOM", comprobanteDTO.getClienteDomicilio());
			parameters.put("REMITOS", comprobanteDTO.getRemitos());
			parameters.put("SUBTOTAL", comprobanteDTO.getSubtotal());
			parameters.put("IVA_21", comprobanteDTO.getIva21());
			parameters.put("IVA_105", comprobanteDTO.getIva105());
			parameters.put("OTROS_TRIBUTOS", comprobanteDTO.getOtrosTributos());
			parameters.put("SUBTOTAL_CON_IVA",
					comprobanteDTO.getSubtotalConIVA());
			parameters.put("TOTAL", comprobanteDTO.getTotal());
			parameters.put("CAE", comprobanteDTO.getCae());
			parameters.put("VTO_CAE", comprobanteDTO.getVtoCAE());
			parameters.put("COD_BARRAS", comprobanteDTO.getCodigoBarras());
			parameters.put("OBSERVACIONES", comprobanteDTO.getObservaciones());
			parameters
					.put("TRIBUTOS",
							new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(
									comprobanteDTO.getTributos()));
			parameters
					.put("SUBREPORT",
							JRLoader.loadObject(ComprobanteReporte.class
									.getResourceAsStream("/reportes/subreport_tributos.jasper")));
			parameters.put("COPIA", "ORIGINAL");
			parameters.put("BG_IMG", ComprobanteReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));

			String reporte = comprobanteDTO.getLetra().equals("A") ? "comprobante_a.jasper"
					: "comprobante_b.jasper";

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(ComprobanteReporte.class
							.getResourceAsStream("/reportes/" + reporte));

			// ORIGINAL
			JasperPrint jasperPrintOriginal = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(
							detalles));

			// DUPLICADO
			parameters.put("COPIA", "DUPLICADO");
			parameters
					.put("TRIBUTOS",
							new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(
									comprobanteDTO.getTributos()));
			parameters.put("BG_IMG", ComprobanteReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));
			JasperPrint jasperPrintDuplicado = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(
							detalles));

			JasperPrint jasperPrint = WJasperUtils.concatReports(
					jasperPrintOriginal, jasperPrintDuplicado);

			JasperViewer.viewReport(jasperPrint, Boolean.FALSE);
		} catch (BusinessException bexc) {
			throw new ReportException(bexc, "Error al obtener Comprobante");
		} catch (JRException jrexc) {
			LOGGER.error("Error al generar Comprobante", jrexc);
			throw new ReportException(jrexc, "Error al generar Comprobante");
		}
	}

	public static void generarImpresionEnviarMail(Long idComprobante,
			String mail) throws ReportException {

		try {
			ComprobanteBO comprobanteBO = AbstractFactory
					.getInstance(ComprobanteBO.class);
			ComprobanteDTO comprobanteDTO = comprobanteBO
					.obtenerDTO(idComprobante);

			List<DetalleComprobanteDTO> detalles = comprobanteDTO.getDetalles();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("COMP_LETRA", comprobanteDTO.getLetra());
			parameters.put("COMP_TIPO", comprobanteDTO.getTipo());
			parameters.put("COMP_NRO", comprobanteDTO.getCompNro());
			parameters.put("FECHA_EMISION", comprobanteDTO.getFechaEmision());
			parameters.put("CLIENTE_CUIT", comprobanteDTO.getClienteCuit());
			parameters.put("CLIENTE_COND_IVA",
					comprobanteDTO.getClienteCondIVA());
			parameters.put("CLIENTE_RAZON",
					comprobanteDTO.getClienteRazonSocial());
			parameters.put("CLIENTE_DOM", comprobanteDTO.getClienteDomicilio());
			parameters.put("REMITOS", comprobanteDTO.getRemitos());
			parameters.put("SUBTOTAL", comprobanteDTO.getSubtotal());
			parameters.put("IVA_21", comprobanteDTO.getIva21());
			parameters.put("IVA_105", comprobanteDTO.getIva105());
			parameters.put("SHOW_ALL", Boolean.TRUE);
			parameters.put("OTROS_TRIBUTOS", comprobanteDTO.getOtrosTributos());
			parameters.put("SUBTOTAL_CON_IVA",
					comprobanteDTO.getSubtotalConIVA());
			parameters.put("TOTAL", comprobanteDTO.getTotal());
			parameters.put("CAE", comprobanteDTO.getCae());
			parameters.put("VTO_CAE", comprobanteDTO.getVtoCAE());
			parameters.put("COD_BARRAS", comprobanteDTO.getCodigoBarras());
			parameters.put("OBSERVACIONES", comprobanteDTO.getObservaciones());
			parameters
					.put("TRIBUTOS",
							new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(
									comprobanteDTO.getTributos()));
			parameters
					.put("SUBREPORT_OBJECT",
							JRLoader.loadObject(ComprobanteReporte.class
									.getResourceAsStream("/reportes/subreport_tributos.jasper")));
			parameters.put("COPIA", "ORIGINAL");
			parameters.put("BG_IMG", ComprobanteReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));

			String reporte = comprobanteDTO.getLetra().equals("A") ? "comprobante_a.jasper"
					: "comprobante_b.jasper";

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(ComprobanteReporte.class
							.getResourceAsStream("/reportes/" + reporte));

			// ORIGINAL
			JasperPrint jasperPrintOriginal = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(
							detalles));

			String fileName = System.getProperty("java.io.tmpdir")
					+ "/factura-" + System.currentTimeMillis() + ".pdf";
			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT,
					jasperPrintOriginal);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
					new FileOutputStream(fileName));
			exporter.exportReport();

			MailUtil.sendFacturaMail(fileName, mail);

		} catch (BusinessException bexc) {
			throw new ReportException(bexc, "Error al obtener Comprobante");
		} catch (JRException jrexc) {
			LOGGER.error("Error al generar Comprobante", jrexc);
			throw new ReportException(jrexc, "Error al generar Comprobante");
		} catch (FileNotFoundException fnfexc) {
			LOGGER.error("Error al generar Comprobante", fnfexc);
			throw new ReportException(fnfexc, "Error al generar Comprobante");
		} catch (Exception exc) {
			LOGGER.error("Error al generar Comprobante", exc);
			throw new ReportException(exc, "Error al generar Comprobante");
		}
	}

	public static void main(String[] args) throws Exception {
		generarImpresion(4L);
	}
}
