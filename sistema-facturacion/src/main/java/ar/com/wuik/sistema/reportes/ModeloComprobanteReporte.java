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
import ar.com.wuik.sistema.exceptions.ReportException;

public class ModeloComprobanteReporte {

	public static void generarFactura(Long idFactura) throws ReportException {

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("BG_IMG", ModeloComprobanteReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(ModeloComprobanteReporte.class
							.getResourceAsStream("/reportes/comprobante_template.jasper"));

			// ORIGINAL
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, new JREmptyDataSource());

			JasperViewer.viewReport(jasperPrint, Boolean.FALSE);

		} catch (JRException jrexc) {
			throw new ReportException(jrexc, "Error al generar Modelo de Comprobante");
		}
	}

	public static void main(String[] args) throws Exception {
		generarFactura(94L);
		// generarRecibo(94L);
		// generarRemito(94L);
	}
}
