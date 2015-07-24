package ar.com.wuik.swing.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Clase de Utilidad para Reportes (JasperReports)
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public final class WJasperUtils {

	/**
	 * Concatena varios reportes.
	 * 
	 * @param jasperPrinters
	 *            List<JasperPrint> - La lista de reportes.
	 * @return El reporte con todos los reportes concatenados.
	 */
	public static JasperPrint concatReports(List<JasperPrint> jasperPrinters) {
		JasperPrint jp = null;
		if (WUtils.isNotEmpty(jasperPrinters)) {
			jp = jasperPrinters.get(0);
			int jasperSize = jasperPrinters.size();
			for (int i = 1; i < jasperSize; i++) {
				List<JRPrintPage> pages = new ArrayList<JRPrintPage>();
				pages.addAll(jasperPrinters.get(i).getPages());
				for (int j = 0; j < pages.size(); j++) {
					jp.addPage((JRPrintPage) pages.get(j));
				}
			}
		}
		return jp;
	}

	public static JasperPrint concatReports(JasperPrint... jasperPrinters) {
		JasperPrint jp = null;
		if (WUtils.isNotEmpty(jasperPrinters)) {
			jp = jasperPrinters[0];
			int jasperSize = jasperPrinters.length;
			for (int i = 1; i < jasperSize; i++) {
				List<JRPrintPage> pages = new ArrayList<JRPrintPage>();
				pages.addAll(jasperPrinters[i].getPages());
				for (int j = 0; j < pages.size(); j++) {
					jp.addPage((JRPrintPage) pages.get(j));
				}
			}
		}
		return jp;
	}

}
