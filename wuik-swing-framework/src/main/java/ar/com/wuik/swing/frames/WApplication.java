package ar.com.wuik.swing.frames;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.swing.components.WSessionMonitor;
import ar.com.wuik.swing.components.security.WMenuItemSecurity;
import ar.com.wuik.swing.components.security.WMenuSecurity;
import ar.com.wuik.swing.components.security.WSecure;

/**
 * Clase base para Aplicaciones.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public class WApplication extends JFrame implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 3911830151744870992L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WApplication.class);
	public static final String application = "";
	private JPanel contentPane = null;
	private JMenuBar menu = null;
	private WMenuSecurity menuArchivo = null;
	private WMenuItemSecurity itemSalir;
	private JDesktopPane desktopPane = null;
	public static WApplication instance;
	private List<String> permisos;

	public WApplication() {
		instance = this;
		setLookAndFeel();
		initialize();
		initializeMenu();
		initializeKeyListeners();
		initializeTrackSystemEvents();
		LOGGER.info("Application - [STARTED]");
	}

	private void initializeKeyListeners() {
		final Map<Integer, ActionListener> keyMapEvent = getKeyMapEvent();

		if (LOGGER.isDebugEnabled()) {
			if (null == keyMapEvent) {
				LOGGER.debug("Not Key Map Event Added");
			}
		}

		KeyboardFocusManager kb = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		kb.addKeyEventPostProcessor(new KeyEventPostProcessor() {

			@Override
			public boolean postProcessKeyEvent(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE
						&& e.getID() == KeyEvent.KEY_PRESSED) {
					if (getDesktopPane().getComponentCount() > 0) {
						JInternalFrame[] iframes = getDesktopPane()
								.getAllFrames();

						JInternalFrame iframeActive = null;
						Integer index = 0;
						for (JInternalFrame iframe : iframes) {
							if (iframe.getLayer() >= index) {
								index = iframe.getLayer();
								iframeActive = iframe;
							}
						}

						if (null != iframeActive) {
							if (iframeActive instanceof WAbstractIFrame) {
								try {
									((WAbstractIFrame) iframeActive).setClosed(true);
								} catch (PropertyVetoException e1) {
								}
							}
						}
					} else {
						if (isVisible()) {
							lastEscapeKeyPressed();
						}
					}
				}

				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (null != keyMapEvent) {
						ActionListener actionListener = keyMapEvent.get(e
								.getKeyCode());
						if (null != actionListener) {
							actionListener.actionPerformed(null);
						}
					}
				}

				return Boolean.TRUE;
			}
		});
	}

	public static <T> WApplication getInstance() {
		return instance;
	}

	private void initializeMenu() {
		JMenuBar menu = getMenu();
		menu.add(getMenuArchivo());
		addMenuItems(menu, getMenuArchivo());
	}

	protected void addMenuItems(JMenuBar menu, WMenuSecurity menuArchivo) {
	}

	private JMenuBar getMenu() {
		if (menu == null) {
			menu = new JMenuBar();
		}
		return menu;
	}

	protected WMenuSecurity getMenuArchivo() {
		if (null == menuArchivo) {
			menuArchivo = new WMenuSecurity("Archivo", new ImageIcon(
					WCalendarIFrame.class.getResource("/icons/archivo.png")),
					getItemSalir());
		}
		return menuArchivo;
	}

	private WMenuItemSecurity getItemSalir() {
		if (null == itemSalir) {
			itemSalir = new WMenuItemSecurity("Salir", null, new ImageIcon(
					WCalendarIFrame.class.getResource("/icons/salir.png")));
			itemSalir.setVisible(Boolean.TRUE);
			itemSalir.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return itemSalir;
	}

	public JDesktopPane getDesktopPane() {
		if (desktopPane == null) {
			desktopPane = new JDesktopPane();
			desktopPane.setBackground(Color.WHITE);
		}
		return desktopPane;
	}

	private final Integer[] layerValues = { JLayeredPane.DEFAULT_LAYER,
			JLayeredPane.PALETTE_LAYER, JLayeredPane.MODAL_LAYER,
			JLayeredPane.POPUP_LAYER, JLayeredPane.DRAG_LAYER };
	private JPanel panel;

	public void addModalIFrame(WAbstractIFrame iFrameToAdd) {

		JInternalFrame[] iFrames = getDesktopPane().getAllFrames();
		for (JInternalFrame iFrame : iFrames) {
			iFrame.getGlassPane().setVisible(true);
			iFrame.setFocusable(false);
			iFrame.setClosable(false);
		}

		getDesktopPane().add(iFrameToAdd, layerValues[iFrames.length]);
		if (iFrameToAdd instanceof WSecure) {
			((WSecure) iFrameToAdd).applySecurity(permisos);
		}
		iFrameToAdd.showFrame();
	}

	public void addIFrame(WAbstractIFrame iFrameToAdd) {
		closeAllIFrame();
		getDesktopPane().add(iFrameToAdd, JLayeredPane.DEFAULT_LAYER);
		if (iFrameToAdd instanceof WSecure) {
			((WSecure) iFrameToAdd).applySecurity(permisos);
		}
		iFrameToAdd.showFrame();
	}

	public void closeAllIFrame() {
		getDesktopPane().removeAll();
		getDesktopPane().updateUI();
	}

	private void initialize() {
		this.setMinimumSize(new Dimension(700, 300));
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(
				getClass().getResource("/icons/application.png")));
		this.setJMenuBar(getMenu());
		this.setContentPane(getJContentPane());
		this.setLocationRelativeTo(null);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setResizable(Boolean.TRUE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(getApplicationName() + " - " + getVersion());
	}

	private String getVersion() {
		return getClass().getPackage().getImplementationVersion();
	}

	protected String getApplicationName() {
		return "No App Name";
	}

	protected JPanel getJContentPane() {
		if (contentPane == null) {
			contentPane = new JPanel();
			contentPane.setLayout(new BorderLayout(0, 0));
			contentPane.add(getDesktopPane(), BorderLayout.CENTER);
			if (isAddTopPanel()) {
				contentPane.add(getPanel(), BorderLayout.SOUTH);
			}
		}
		return contentPane;
	}

	/**
	 * @see ar.com.wuik.swing.components.security.WSecure#applySecurity(java.util.List)
	 */
	@Override
	public void applySecurity(List<String> permisos) {
		applySecurity();
	}

	protected void applySecurity() {
		JMenuBar menu = getMenu();
		int menuCount = menu.getMenuCount();
		for (int i = 0; i < menuCount; i++) {
			WMenuSecurity menuSecurity = (WMenuSecurity) menu.getMenu(i);
			if (null != menuSecurity) {
				menuSecurity.evaluateMenuItems(permisos);
			}
		}
		this.setVisible(Boolean.TRUE);
		this.setTitle(getApplicationName());

		WSessionMonitor.getInstance().setLastTimeEvent(
				System.currentTimeMillis());
	}

	protected void close() {
		closeAllIFrame();
		int menuCount = menu.getMenuCount();
		for (int i = 0; i < menuCount; i++) {
			WMenuSecurity menuSecurity = (WMenuSecurity) menu.getMenu(i);
			if (null != menuSecurity) {
				menuSecurity.restore();
			}
		}
		this.setVisible(Boolean.FALSE);
	}

	public void closeIFrame(WAbstractIFrame iFrameToRemove) {
		getDesktopPane().remove(iFrameToRemove);
		getDesktopPane().updateUI();
		JInternalFrame[] iFrames = getDesktopPane().getAllFrames();
		if (null != iFrames && iFrames.length > 0) {
			JInternalFrame iFrame = iFrames[0];
			iFrame.getGlassPane().setVisible(false);
			iFrame.setFocusable(true);
			iFrame.setClosable(true);
			try {
				iFrame.setSelected(true);
			} catch (PropertyVetoException e) {
			}
		}
	}

	private void initializeTrackSystemEvents() {
		WSessionMonitor.getInstance().setLastTimeEvent(
				System.currentTimeMillis());

		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

			public void eventDispatched(AWTEvent event) {
				if (isVisible()) {
					String eventText = event.toString();
					if (eventText.indexOf("PRESSED") != -1
							|| eventText.indexOf("RELEASED") != -1) {

						long currentTime = System.currentTimeMillis();

						if (WSessionMonitor.getInstance().hasExpiredSession(
								currentTime)) {
							sessionExpiredEvent();
						} else {
							WSessionMonitor.getInstance().setLastTimeEvent(
									currentTime);
						}
					}
				}
			}
		}, AWTEvent.MOUSE_EVENT_MASK + AWTEvent.KEY_EVENT_MASK);
	}

	protected void showFrame() {
		setVisible(Boolean.TRUE);
	}

	/**
	 * Retorna el mapa de Teclas y Listeners, es utilizado por el Framework para
	 * ejecutar el listener según la tecla especificada, por ej.
	 * put(KeyEvent.VK_ENTER, new ActionListener())
	 * 
	 * @return El mapa de Teclas y Listeners.
	 */
	protected Map<Integer, ActionListener> getKeyMapEvent() {
		return null;
	}

	/**
	 * Establece un Custom Look & Feel.
	 */
	protected void setLookAndFeel() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Set Look & Feel");
		}
	}

	/**
	 * Callback para cuando el Tiempo de Sesión há expirado.
	 */
	protected void sessionExpiredEvent() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Session Expired Event");
		}
	}

	/**
	 * Callback para cuando ya no hay mas ventanas para cerrar con la Tecla ESC.
	 */
	protected void lastEscapeKeyPressed() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Last Escape Key Pressed");
		}
	}

	protected boolean isAddTopPanel() {
		return Boolean.TRUE;
	}

	protected JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
		}
		return panel;
	}

	protected void setPermisos(List<String> permisos) {
		this.permisos = permisos;
	}

	public List<String> getPermisos() {
		return permisos;
	}
}
