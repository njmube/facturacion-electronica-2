package FEV1.dif.afip.gov.ar;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import FEV1.dif.afip.gov.ar.entities.Comprobante;
import FEV1.dif.afip.gov.ar.entities.Resultado;
import FEV1.dif.afip.gov.ar.entities.TipoComprobanteEnum;
import FEV1.dif.afip.gov.ar.entities.TipoConceptoEnum;
import FEV1.dif.afip.gov.ar.entities.TipoDocumentoEnum;
import FEV1.dif.afip.gov.ar.entities.TipoMonedaEnum;
import FEV1.dif.afip.gov.ar.exceptions.ServiceException;
import FEV1.dif.afip.gov.ar.services.FacturacionService;
import FEV1.dif.afip.gov.ar.utils.AbstractFactory;

public class TestService {

	private FacturacionService facturacionService;

	@Before
	public void setUp() throws Exception {
		facturacionService = AbstractFactory
				.getInstance(FacturacionService.class);
	}

	@Test
	public void testService() {

		try {
			Resultado resultado = facturacionService
					.consultarUltimoComprobante(TipoComprobanteEnum.FACTURA_A);
			
//			Comprobante comprobante = createComprobante();
//			Resultado resultado = facturacionService.solicitarComprobante(comprobante);
			System.out.println(resultado);
		} catch (ServiceException sexc) {
			System.out.println(sexc.getMessage());
			sexc.printStackTrace();
		}
	}
	
	private Comprobante createComprobante(){
		Comprobante comprobante = new Comprobante();
		comprobante.setDocNro(33709399L);
		comprobante.setDocTipo(TipoDocumentoEnum.DNI);
		comprobante.setImporteIVA(BigDecimal.TEN);
		comprobante.setImporteSubtotal(BigDecimal.TEN);
		comprobante.setImporteTotal(BigDecimal.TEN);
		comprobante.setTipoMoneda(TipoMonedaEnum.PESOS_ARGENTINOS);
		comprobante.setTipoConcepto(TipoConceptoEnum.PRODUCTO);
		comprobante.setTipoComprobante(TipoComprobanteEnum.FACTURA_A);
		comprobante.setNroComprobante(1L);
		return comprobante;
	}
}