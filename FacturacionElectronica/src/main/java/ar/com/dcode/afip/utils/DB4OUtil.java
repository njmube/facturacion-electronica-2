package ar.com.dcode.afip.utils;

import java.io.File;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;


/**
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public class DB4OUtil {


	private static ObjectContainer db = Db4oEmbedded.openFile( Db4oEmbedded.newConfiguration(),
	    System.getProperty( "user.dir" ) + File.separator + "database.db" );
	private static Sequence sequence;

	static {
		ObjectSet<Sequence> seqs = db.query( Sequence.class );
		if ( seqs.size() == 0 ) {
			sequence = new Sequence();
			db.store( sequence );
		} else {
			sequence = seqs.get( 0 );
		}
	}

	public static long getSequence() {
		long seq = sequence.getSeq();
		sequence.setSeq( seq + 1 );
		db.store( sequence );
		return seq;
	}

	public static ObjectContainer getContainer() {
		return db;
	}

}

class Sequence {


	private long seq = 1;

	/**
	 * @return the seq
	 */
	public long getSeq() {
		return seq;
	}

	/**
	 * @param seq
	 *            the seq to set
	 */
	public void setSeq( long seq ) {
		this.seq = seq;
	}

}
