package FEV1.dif.afip.gov.ar.services.utils;

import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import FEV1.dif.afip.gov.ar.AfipWsaaClient;
import FEV1.dif.afip.gov.ar.FEAuthRequest;
import FEV1.dif.afip.gov.ar.exceptions.ServiceException;
import FEV1.dif.afip.gov.ar.utils.ParametrosUtil;

public class AuthorizationUtil {

	public static FEAuthRequest getAuthorization() throws ServiceException {

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

		// Set proxy system vars
		// System.setProperty("http.proxyHost",
		// ParametrosUtil.getProperty("http_proxy"));
		// System.setProperty("http.proxyPort",
		// ParametrosUtil.getProperty("http_proxy_port"));
		// System.setProperty("http.proxyUser",
		// ParametrosUtil.getProperty("http_proxy_user"));
		// System.setProperty("http.proxyPassword",
		// ParametrosUtil.getProperty("http_proxy_password"));

		// Set the keystore used by SSL
		System.setProperty(
				"javax.net.ssl.trustStore",
				System.getProperty("user.dir") + "/"
						+ ParametrosUtil.getProperty("trustStore"));
		System.setProperty("javax.net.ssl.trustStorePassword",
				ParametrosUtil.getProperty("trustStore_password"));

		Long TicketTime = new Long(ParametrosUtil.getProperty("TicketTime"));

		// Create LoginTicketRequest_xml_cms
		byte[] LoginTicketRequest_xml_cms = AfipWsaaClient.create_cms(p12file,
				p12pass, signer, dstDN, service, TicketTime);

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
			return new FEAuthRequest(token, sign, Long.valueOf(cuit));
		} catch (DocumentException dexc) {
			throw new ServiceException(dexc,
					"Error al obtener datos de la Autorización");
		}
	}

	public static void main(String[] args) throws Exception {

		FEAuthRequest req = getAuthorization();

		System.out.println("<ar:Token>" + req.getToken() + "</ar:Token>");
		System.out.println("<ar:Sign>" + req.getSign() + "</ar:Sign>");
		System.out.println("<ar:Cuit>20276229185</ar:Cuit>");
	}

}
