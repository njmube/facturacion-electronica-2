/**
 * Autor : juan.vazquez@wuik.com.ar - Wuik-Working Innovation Creacion :
 * 12/03/2014 - 10:48:40
 */
package ar.com.dcode.afip.utils;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Manifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ar.com.wuik.swing.utils.WUtils;


/**
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public class AppUtils {


	protected static final Logger LOGGER = LoggerFactory.getLogger( AppUtils.class );

	public static String getAppVersion() {
		try {
			URLClassLoader cl = (URLClassLoader) AppUtils.class.getClassLoader();
			URL url = cl.findResource( "META-INF/MANIFEST.MF" );
			Manifest manifest = new Manifest( url.openStream() );
			String version = manifest.getMainAttributes().getValue( "Implementation-Version" );
			if ( WUtils.isNotEmpty( version ) ) {
				return "v" + version;
			}
		}
		catch( Exception exc ) {
			LOGGER.error( "Error al obtener versión de Aplicación", exc );
		}
		return "<Version>";
	}

}
