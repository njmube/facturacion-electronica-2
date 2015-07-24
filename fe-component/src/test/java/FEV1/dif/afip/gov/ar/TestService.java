package FEV1.dif.afip.gov.ar;

import org.junit.Before;
import org.junit.Test;

import FEV1.dif.afip.gov.ar.entities.Resultado;
import FEV1.dif.afip.gov.ar.entities.TipoComprobante;
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
					.consultarUltimoComprobante(TipoComprobante.FACTURA_A);
			System.out.println(resultado);
		} catch (ServiceException sexc) {
			System.out.println(sexc.getMessage());
			sexc.printStackTrace();
		}
	}
}