package ar.com.wuik.swing.utils;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;


public class WEncrypterUtil {


	private static final String UNICODE_FORMAT = "UTF8";
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	private static KeySpec myKeySpec;
	private static SecretKeyFactory mySecretKeyFactory;
	private static Cipher cipher;
	private static byte[] keyAsBytes;
	private static String myEncryptionKey;
	private static String myEncryptionScheme;
	private static SecretKey key;

	static {
		try {
			myEncryptionKey = "ThisIsSecretEncryptionKey";
			myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
			keyAsBytes = myEncryptionKey.getBytes( UNICODE_FORMAT );
			myKeySpec = new DESedeKeySpec( keyAsBytes );
			mySecretKeyFactory = SecretKeyFactory.getInstance( myEncryptionScheme );
			cipher = Cipher.getInstance( myEncryptionScheme );
			key = mySecretKeyFactory.generateSecret( myKeySpec );
		}
		catch( Exception exc ) {
		}
	}

	/**
	 * Method To Encrypt The String
	 */
	public static String encrypt( String unencryptedString ) {
		if ( WUtils.isNotEmpty( unencryptedString ) ) {
			try {
				cipher.init( Cipher.ENCRYPT_MODE, key );
				final byte[] plainText = unencryptedString.getBytes( UNICODE_FORMAT );
				final byte[] encryptedText = cipher.doFinal( plainText );
				final Base64 base64encoder = new Base64();
				return base64encoder.encodeAsString( encryptedText );
			}
			catch( Exception e ) {
			}
		}
		return WUtils.EMPTY;
	}

	/**
	 * Method To Decrypt An Ecrypted String
	 */
	public static String decrypt( String encryptedString ) {
		if ( WUtils.isNotEmpty( encryptedString ) ) {
			try {
				cipher.init( Cipher.DECRYPT_MODE, key );
				final Base64 base64decoder = new Base64();
				final byte[] encryptedText = base64decoder.decode( encryptedString );
				final byte[] plainText = cipher.doFinal( encryptedText );
				return bytes2String( plainText );
			}
			catch( Exception e ) {
			}
		}
		return WUtils.EMPTY;
	}

	/**
	 * Returns String From An Array Of Bytes
	 */
	private static String bytes2String( byte[] bytes ) {
		final StringBuffer stringBuffer = new StringBuffer();
		for ( int i = 0; i < bytes.length; i++ ) {
			stringBuffer.append( (char) bytes[i] );
		}
		return stringBuffer.toString();
	}

	public static void main( String[] args ) {
		System.out.println( encrypt( "1" ) );
	}

}
