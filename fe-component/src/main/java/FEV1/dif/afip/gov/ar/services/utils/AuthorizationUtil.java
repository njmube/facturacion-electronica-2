package FEV1.dif.afip.gov.ar.services.utils;

import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import FEV1.dif.afip.gov.ar.AfipWsaaClient;
import FEV1.dif.afip.gov.ar.FEAuthRequest;
import FEV1.dif.afip.gov.ar.entities.Resultado;
import FEV1.dif.afip.gov.ar.entities.TipoComprobanteEnum;
import FEV1.dif.afip.gov.ar.exceptions.ServiceException;
import FEV1.dif.afip.gov.ar.services.FacturacionService;
import FEV1.dif.afip.gov.ar.utils.AbstractFactory;
import FEV1.dif.afip.gov.ar.utils.ParametrosUtil;

public class AuthorizationUtil {

	private static Date token_expired;
	private static FEAuthRequest authRequest;

	public static FEAuthRequest getAuthorization() throws ServiceException {

		if (null == token_expired || new Date().compareTo(token_expired) > 0
				|| null == authRequest) {

			String LoginTicketResponse = null;

			System.setProperty("http.proxyHost", "");
			System.setProperty("http.proxyPort", "80");

			String endpoint = ParametrosUtil.getProperty("endpoint");
			String service = ParametrosUtil.getProperty("service");
			String dstDN = ParametrosUtil.getProperty("dstdn");

			String p12file = System.getProperty("user.dir") + "/"
					+ ParametrosUtil.getProperty("keystore");
			String signer = ParametrosUtil.getProperty("keystore-signer");
			String p12pass = ParametrosUtil.getProperty("keystore-password");

			// Set the keystore used by SSL
			System.setProperty(
					"javax.net.ssl.trustStore",
					System.getProperty("user.dir") + "/"
							+ ParametrosUtil.getProperty("trustStore"));
			System.setProperty("javax.net.ssl.trustStorePassword",
					ParametrosUtil.getProperty("trustStore_password"));

			Long TicketTime = new Long(ParametrosUtil.getProperty("TicketTime"));

			// Create LoginTicketRequest_xml_cms
			byte[] LoginTicketRequest_xml_cms = AfipWsaaClient.create_cms(
					p12file, p12pass, signer, dstDN, service, TicketTime);

			// Invoke AFIP wsaa and get LoginTicketResponse
			try {
				LoginTicketResponse = AfipWsaaClient.invoke_wsaa(
						LoginTicketRequest_xml_cms, endpoint);
			} catch (RemoteException exc) {
				throw new ServiceException(exc,
						"Error al conectar a Servicio de Autorización");
			} catch (MalformedURLException murlexc) {
				throw new ServiceException(murlexc,
						"Error en la URL de Servicio de Autorización");
			} catch (javax.xml.rpc.ServiceException sexc) {
				throw new ServiceException(sexc,
						"Error al crear comunicación para Servicio de Autorización");
			}
			// Get token & sign from LoginTicketResponse
			Reader tokenReader = new StringReader(LoginTicketResponse);
			try {
				Document tokenDoc = new SAXReader(false).read(tokenReader);
				String token = tokenDoc
						.valueOf("/loginTicketResponse/credentials/token");
				String sign = tokenDoc
						.valueOf("/loginTicketResponse/credentials/sign");
				String cuit = ParametrosUtil.getProperty("cuit");
				String expiration = tokenDoc
						.valueOf("/loginTicketResponse/header/expirationTime");
				authRequest = new FEAuthRequest(token, sign, Long.valueOf(cuit));

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");
				try {
					token_expired = sdf.parse(expiration.substring(0, 19));
				} catch (ParseException e) {
				}

			} catch (DocumentException dexc) {
				throw new ServiceException(dexc,
						"Error al obtener datos de la Autorización");
			}
		}
		return authRequest;
	}

	public static void main(String[] args) throws Exception {
		
		
//		FacturacionService facturacionService = AbstractFactory
//				.getInstance(FacturacionService.class);
//
//		try {
//			Resultado resultado = facturacionService
//					.consultarUltimoComprobante(TipoComprobanteEnum.FACTURA_A);
//			
////			Comprobante comprobante = createComprobante();
////			Resultado resultado = facturacionService.solicitarComprobante(comprobante);
//			System.out.println(resultado);
//		} catch (ServiceException sexc) {
//			System.out.println(sexc.getMessage());
//			sexc.printStackTrace();
//		}

		FEAuthRequest req = getAuthorization();
		System.out.println("<ar:Token>" + req.getToken() + "</ar:Token>");
		System.out.println("<ar:Sign>" + req.getSign() + "</ar:Sign>");
		System.out.println("<ar:Cuit>20049746181</ar:Cuit>");
		
	}

}
