package ar.com.wuik.sistema.frames;

import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SaharaSkin;

import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.frames.WLoader;
import ar.com.wuik.swing.utils.WFrameUtils;

public class Loader extends WLoader {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -4090881654692862764L;

	@Override
	protected void setLookAndFeel() {
		try {
			SubstanceLookAndFeel.setSkin(new SaharaSkin());
		} catch (Exception e) {
			System.out.println("Substance Skin failed to initialize");
		}
	}
	
	

    /**
     * 
     */
    public Loader() {
	    setIconImage( Toolkit.getDefaultToolkit().getImage( Loader.class.getResource( "/icons32/loading.png" ) ) );
    }

	@Override
	protected void onLoadFinished() {
		// Aca muestro la siguiente pantalla
		Login.main(null);
	}

	@Override
	protected void loadConfiguration() {
		// Aca inicializo Hibernate.
		HibernateUtil.getSessionFactory();
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				WFrameUtils.initLayout();
				JFrame.setDefaultLookAndFeelDecorated(false);
				JDialog.setDefaultLookAndFeelDecorated(false);
				Loader loader = new Loader();
				loader.showFrame();
			}
		});
	}
}
