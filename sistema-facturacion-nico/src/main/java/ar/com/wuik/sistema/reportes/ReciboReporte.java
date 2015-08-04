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
			parameters.put("RAZON_SOCIAL", reciboDTO.getRazonSocial());
			parameters.put("DOMICILIO", reciboDTO.getDomicilio());
			parameters.put("COND_IVA", reciboDTO.getCondIVA());
			parameters.put("COMP_NRO", reciboDTO.getCompNro());
			parameters.put("FECHA_EMISION", reciboDTO.getFechaEmision());
			parameters.put("CUIT", reciboDTO.getCuit());
			parameters.put("ING_BRUTOS", reciboDTO.getIngBrutos());
			parameters.put("INICIO_ATC", reciboDTO.getInicioAct());
			parameters.put("CLIENTE_CUIT", reciboDTO.getClienteCuit());
			parameters.put("CLIENTE_COND_IVA", reciboDTO.getClienteCondIVA());
			parameters.put("CLIENTE_RAZON", reciboDTO.getClienteRazonSocial());
			parameters.put("CLIENTE_DOM", reciboDTO.getClienteDomicilio());
			parameters.put("TOTAL", reciboDTO.getTotal());
			parameters.put("TOTAL_LETRAS", reciboDTO.getTotalLetras());
			parameters.put("BG_IMG", ReciboReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));
			parameters.put("NRO_CH_1", reciboDTO.getNroCh1());
			parameters.put("NRO_CH_2", reciboDTO.getNroCh2());
			parameters.put("NRO_CH_3", reciboDTO.getNroCh3());
			parameters.put("NRO_CH_4", reciboDTO.getNroCh4());
			parameters.put("BANCO_CH_1", reciboDTO.getBancoCh1());
			parameters.put("BANCO_CH_2", reciboDTO.getBancoCh2());
			parameters.put("BANCO_CH_3", reciboDTO.getBancoCh3());
			parameters.put("BANCO_CH_4", reciboDTO.getBancoCh4());
			parameters.put("TOTAL_CH_1", reciboDTO.getTotalCh1());
			parameters.put("TOTAL_CH_2", reciboDTO.getTotalCh2());
			parameters.put("TOTAL_CH_3", reciboDTO.getTotalCh3());
			parameters.put("TOTAL_CH_4", reciboDTO.getTotalCh4());
			parameters.put("FECHA_COMP_1", reciboDTO.getFechaComp1());
			parameters.put("FECHA_COMP_2", reciboDTO.getFechaComp2());
			parameters.put("FECHA_COMP_3", reciboDTO.getFechaComp3());
			parameters.put("FECHA_COMP_4", reciboDTO.getFechaComp4());
			parameters.put("FECHA_COMP_5", reciboDTO.getFechaComp5());
			parameters.put("FECHA_COMP_6", reciboDTO.getFechaComp6());
			parameters.put("NRO_COMP_1", reciboDTO.getNroComp1());
			parameters.put("NRO_COMP_2", reciboDTO.getNroComp2());
			parameters.put("NRO_COMP_3", reciboDTO.getNroComp3());
			parameters.put("NRO_COMP_4", reciboDTO.getNroComp4());
			parameters.put("NRO_COMP_5", reciboDTO.getNroComp5());
			parameters.put("NRO_COMP_6", reciboDTO.getNroComp6());
			parameters.put("TOTAL_COMP_1", reciboDTO.getTotalComp1());
			parameters.put("TOTAL_COMP_2", reciboDTO.getTotalComp2());
			parameters.put("TOTAL_COMP_3", reciboDTO.getTotalComp3());
			parameters.put("TOTAL_COMP_4", reciboDTO.getTotalComp4());
			parameters.put("TOTAL_COMP_5", reciboDTO.getTotalComp5());
			parameters.put("TOTAL_COMP_6", reciboDTO.getTotalComp6());
			parameters.put("TOTAL_COMP", reciboDTO.getTotalComp());
			parameters.put("EFECTIVO", reciboDTO.getEfectivo());

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(ReciboReporte.class
							.getResourceAsStream("/reportes/recibo.jasper"));

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, new JREmptyDataSource());

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
