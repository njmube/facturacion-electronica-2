package ar.com.wuik.sistema.reportes;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import ar.com.wuik.sistema.bo.ReciboBO;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.reportes.entities.ReciboDTO;
import ar.com.wuik.sistema.utils.AbstractFactory;

public class ReciboReporte {

	public static void generarRecibo(Long idRecibo) throws ReportException {

		try {
			ReciboBO reciboBO = AbstractFactory.getInstance(ReciboBO.class);
			ReciboDTO reciboDTO = reciboBO.obtenerDTO(idRecibo);

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("COMP_NRO", reciboDTO.getCompNro());
			parameters.put("FECHA_EMISION", reciboDTO.getFechaEmision());
			parameters.put("CLIENTE_CUIT", reciboDTO.getClienteCuit());
			parameters.put("CLIENTE_COND_IVA", reciboDTO.getClienteCondIVA());
			parameters.put("CLIENTE_RAZON", reciboDTO.getClienteRazonSocial());
			parameters.put("CLIENTE_DOM", reciboDTO.getClienteDomicilio());
			parameters.put("EFECTIVO", reciboDTO.getTotalEfectivo());
			parameters.put("CHEQUE", reciboDTO.getTotalCheque());
			parameters.put("TOTAL", reciboDTO.getTotal());
			parameters.put("TOTAL_LETRAS", reciboDTO.getTotalLetras());
			parameters.put("BG_IMG", ReciboReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(ReciboReporte.class
							.getResourceAsStream("/reportes/recibo.jasper"));

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(reciboDTO.getDetalles()));

			JasperViewer.viewReport(jasperPrint, Boolean.FALSE);
		} catch (BusinessException bexc) {
			throw new ReportException(bexc, "Error al obtener Recibo");
		} catch (JRException jrexc) {
			throw new ReportException(jrexc, "Error al generar Recibo");
		}
	}

	public static void main(String[] args) throws Exception {
		generarRecibo(1L);
	}
}
