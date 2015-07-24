package ar.com.wuik.sistema.reportes;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import ar.com.wuik.sistema.bo.FacturaBO;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.reportes.entities.FacturaDTO;
import ar.com.wuik.sistema.utils.AbstractFactory;

public class ModeloComprobanteReporte {

	public static void generarFactura(Long idFactura) throws ReportException {

		try {
			FacturaBO facturaBO = AbstractFactory.getInstance(FacturaBO.class);
			FacturaDTO facturaDTO = facturaBO.obtenerDTO(idFactura);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("RAZON_SOCIAL", facturaDTO.getRazonSocial());
			parameters.put("DOMICILIO", facturaDTO.getDomicilio());
			parameters.put("COND_IVA", facturaDTO.getCondIVA());
			parameters.put("CUIT", facturaDTO.getCuit());
			parameters.put("ING_BRUTOS", facturaDTO.getIngBrutos());
			parameters.put("INICIO_ATC", facturaDTO.getInicioAct());
			parameters.put("BG_IMG", ModeloComprobanteReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(ModeloComprobanteReporte.class
							.getResourceAsStream("/reportes/comprobante.jasper"));

			// ORIGINAL
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, new JREmptyDataSource());

			JasperViewer.viewReport(jasperPrint, Boolean.FALSE);
		} catch (BusinessException bexc) {
			throw new ReportException(bexc, "Error al obtener Factura");
		} catch (JRException jrexc) {
			throw new ReportException(jrexc, "Error al generar Factura");
		}
	}

	public static void main(String[] args) throws Exception {
		generarFactura(94L);
	}
}
