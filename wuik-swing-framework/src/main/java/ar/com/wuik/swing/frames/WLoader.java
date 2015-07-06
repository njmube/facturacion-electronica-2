package ar.com.wuik.swing.frames;

import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase base para Aplicaciones.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public class WLoader extends WAbstractFrame {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1929389865189791143L;
	private static final Logger LOGGER = LoggerFactory.getLogger(WLoader.class);
	private JPanel jContentPane = null;
	private JProgressBar statusBar = null;
	private JLabel lblCargando;

	/**
	 * This method initializes statusBar
	 * 
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getStatusBar() {
		if (statusBar == null) {
			statusBar = new JProgressBar();
			statusBar.setBounds(new Rectangle(10, 33, 200, 25));
			statusBar.setIndeterminate(Boolean.TRUE);
		}
		return statusBar;
	}

	/**
	 * This is the default constructor
	 */
	public WLoader() {
		setLookAndFeel();
		initialize();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	/**
	 * @see ar.com.wuik.swing.frames.WAbstractFrame#showFrame()
	 */
	@Override
	public void showFrame() {
		super.showFrame();
		loadSystemConfiguration();
	}

	private void loadSystemConfiguration() {
		Thread tr = new Thread(new Runnable() {

			@Override
			public void run() {
				loadConfiguration();
				setVisible(Boolean.FALSE);
				dispose();
				System.setProperty("user.timezone", "America/Buenos_Aires");
				LOGGER.info("Configuration - [LOADED]");
				onLoadFinished();

			}
		});
		tr.start();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(220, 80);
		this.setContentPane(getJContentPane());
		this.setUndecorated(Boolean.TRUE);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			jContentPane.setLayout(null);
			jContentPane.add(getStatusBar(), null);
			jContentPane.add(getLblCargando());
		}
		return jContentPane;
	}

	/**
	 * Establece un Custom Look & Feel.
	 */
	protected void setLookAndFeel() {
	}

	protected void loadConfiguration() {
	}

	protected void onLoadFinished() {
	}
	protected JLabel getLblCargando() {
		if (lblCargando == null) {
			lblCargando = new JLabel("Cargando Configuración");
			lblCargando.setHorizontalAlignment(SwingConstants.CENTER);
			lblCargando.setBounds(10, 8, 200, 25);
		}
		return lblCargando;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
