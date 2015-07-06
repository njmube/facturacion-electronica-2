package ar.com.wuik.swing.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorCode;


public class WCustomLogger extends FileAppender {


	public WCustomLogger() {
	}

	public WCustomLogger(final Layout layout, final String filename, final boolean append, final boolean bufferedIO,
	                final int bufferSize) throws IOException {
		super( layout, filename, append, bufferedIO, bufferSize );
	}

	public WCustomLogger(final Layout layout, final String filename, final boolean append) throws IOException {
		super( layout, filename, append );
	}

	public WCustomLogger(final Layout layout, final String filename) throws IOException {
		super( layout, filename );
	}

	public void activateOptions() {
		if ( fileName != null ) {
			try {
				fileName = getNewLogFileName();
				setFile( fileName, fileAppend, bufferedIO, bufferSize );
			}
			catch( Exception e ) {
				errorHandler.error( "Error while activating log options", e, ErrorCode.FILE_OPEN_FAILURE );
			}
		}
	}

	private String getNewLogFileName() {
		if ( fileName != null ) {
			final String DOT = ".";
			final String HIPHEN = "-";
			final File logFile = new File( fileName );
			final String fileName = logFile.getName();
			String newFileName = "";

			final SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );

			final String date = format.format( new Date() );

			final int dotIndex = fileName.indexOf( DOT );
			if ( dotIndex != - 1 ) {
				// the file name has an extension. so, insert the time stamp
				// between the file name and the extension
				newFileName =
				    fileName.substring( 0, dotIndex ) + HIPHEN + date + DOT + fileName.substring( dotIndex + 1 );
			} else {
				// the file name has no extension. So, just append the timestamp
				// at the end.
				newFileName = fileName + HIPHEN + date;
			}
			return logFile.getParent() + File.separator + newFileName;
		}
		return null;
	}
}
