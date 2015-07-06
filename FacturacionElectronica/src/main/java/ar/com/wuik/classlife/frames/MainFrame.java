package ar.com.wuik.classlife.frames;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.RavenSkin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dcode.afip.utils.AppUtils;
import ar.com.dcode.afip.utils.ParametrosUtil;
import ar.com.wuik.classlife.entities.Usuario;
import ar.com.wuik.swing.components.WSessionMonitor;
import ar.com.wuik.swing.components.security.WMenuItemSecurity;
import ar.com.wuik.swing.components.security.WMenuSecurity;
import ar.com.wuik.swing.events.CalendarSelectedEvent;
import ar.com.wuik.swing.frames.WAbstractIFrame;
import ar.com.wuik.swing.frames.WApplication;
import ar.com.wuik.swing.frames.WCalendarIFrame;

public class MainFrame extends WApplication {

	public MainFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				MainFrame.class.getResource("/icons32/icono.png")));
	}

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 4667090445204195673L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MainFrame.class);

	// Menu Administraciones ------------
	private WMenuItemSecurity itemCalendar = null;
	private WMenuItemSecurity itemArticulo = null;
	private WMenuItemSecurity itemCliente = null;
	private WMenuItemSecurity itemVenta = null;
	private WMenuItemSecurity itemMovimiento = null;
	private WMenuItemSecurity itemUsuario = null;

	// Menu Reportes ------------
	private WMenuItemSecurity itemReporteStock = null;
	private WMenuItemSecurity itemReporteCtaCte = null;
	private WMenuItemSecurity itemReporteCajaDiaria = null;
	private WMenuItemSecurity itemReporteMovimientos = null;

	// Menu Archivo -------------
	private WMenuItemSecurity itemEditarDatosPersonales = null;
	private WMenuItemSecurity itemCerrarSesion = null;

	// Menu Herramientas
	private WMenuItemSecurity itemCargaArticulo = null;

	private WMenuSecurity menuAdministraciones = null;
	private WMenuSecurity menuReportes = null;
	private WMenuSecurity menuHerramientas = null;

	public static Usuario usuarioLogin;

	@Override
	protected void setLookAndFeel() {

		try {
			SubstanceLookAndFeel.setSkin(new RavenSkin());
		} catch (Exception e) {
			System.out.println("Substance Skin failed to initialize");
		}
	}

	private WMenuItemSecurity getItemCalendar() {
		if (null == itemCalendar) {
			itemCalendar = new WMenuItemSecurity("Calendario", null, null);
			itemCalendar.setVisible(Boolean.TRUE);
			itemCalendar.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addIFrame(new WCalendarIFrame(new Date(),
							new CalendarSelectedEvent() {

								@Override
								public void execute(Date selectedFecha) {
									LOGGER.info(selectedFecha.toString());

								}
							}));
				}
			});
		}
		return itemCalendar;
	}

	private WMenuItemSecurity getItemArticulo() {
		if (null == itemArticulo) {
			itemArticulo = new WMenuItemSecurity("Articulos", null,
					new ImageIcon(
							MainFrame.class
									.getResource("/icons32/articulo.png")));
			itemArticulo.setVisible(Boolean.TRUE);
			itemArticulo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addIFrame(new ArticuloIFrame());
				}
			});
		}
		return itemArticulo;
	}

	private WMenuItemSecurity getItemEditarDatosPersonales() {
		if (null == itemEditarDatosPersonales) {
			itemEditarDatosPersonales = new WMenuItemSecurity(
					"Editar Datos Usuario", null,
					new ImageIcon(MainFrame.class
							.getResource("/icons32/usuario.png")));
			itemEditarDatosPersonales.setVisible(Boolean.TRUE);
			itemEditarDatosPersonales.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// addIFrame( new UsuarioVerIFrame( usuarioLogin.getId(),
					// null ) );
				}
			});
		}
		return itemEditarDatosPersonales;
	}

	private WMenuItemSecurity getItemCerrarSesion() {
		if (null == itemCerrarSesion) {
			itemCerrarSesion = new WMenuItemSecurity("Cerrar Sesión", null,
					new ImageIcon(
							MainFrame.class.getResource("/icons32/login.png")));
			itemCerrarSesion.setVisible(Boolean.TRUE);
			itemCerrarSesion.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					logout(Boolean.FALSE);
				}
			});
		}
		return itemCerrarSesion;
	}

	@Override
	protected void addMenuItems(JMenuBar menu, WMenuSecurity menuArchivo) {
		// menuArchivo.add( getItemCalendar() );
		menuArchivo.add(getItemCerrarSesion(), menuArchivo.getComponentCount());
		menuArchivo.add(getItemEditarDatosPersonales(),
				menuArchivo.getComponentCount());
		menu.add(getMenuAdministraciones());
	}

	protected WMenuSecurity getMenuAdministraciones() {
		if (null == menuAdministraciones) {
			menuAdministraciones = new WMenuSecurity("Administraciones",
					new ImageIcon(WCalendarIFrame.class
							.getResource("/icons32/administraciones.png")),
					getItemArticulo());
		}
		return menuAdministraciones;
	}


	@Override
	protected Map<Integer, ActionListener> getKeyMapEvent() {
		Map<Integer, ActionListener> mapEvent = new HashMap<Integer, ActionListener>();
		mapEvent.put(KeyEvent.VK_ENTER, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JInternalFrame[] iframes = getDesktopPane().getAllFrames();

				JInternalFrame iframeActive = null;
				Integer index = 0;
				for (JInternalFrame iframe : iframes) {
					if (iframe.getLayer() >= index) {
						index = iframe.getLayer();
						iframeActive = iframe;
					}
				}

				if (iframeActive instanceof WAbstractIFrame) {
					((WAbstractIFrame) iframeActive).enterPressed();
				}
			}
		});

		return mapEvent;
	}

	@Override
	protected boolean isAddTopPanel() {
		return Boolean.FALSE;
	}

	public static void main(Usuario usuario) {
		usuarioLogin = usuario;

		final List<String> permisos = new ArrayList<String>();

		WSessionMonitor.getInstance().setLastTimeEvent(
				System.currentTimeMillis());

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				JFrame.setDefaultLookAndFeelDecorated(true);
				JDialog.setDefaultLookAndFeelDecorated(true);
				if (null == instance) {
					instance = new MainFrame();
					WSessionMonitor.getInstance().setTimeoutMinutes(
							Long.valueOf(ParametrosUtil
									.getProperty(ParametrosUtil.IDLE_TIME)));
				}
				((MainFrame) instance).setPermisos(permisos);
				((MainFrame) instance).showFrame();
			}
		});
	}

	/**
	 * @see ar.com.wuik.swing.frames.WApplication#getApplicationName()
	 */
	@Override
	protected String getApplicationName() {
		return "SISTEMA CLASS-LIFE " + AppUtils.getAppVersion() + " :: "
				+ "USUARIO";
	}

	public void logout(boolean confirmacion) {
		if (confirmacion) {
			int seleccion = JOptionPane.showConfirmDialog(MainFrame.this,
					"¿Desea cerrar la Sesión actual?", "Cerrar Sesión",
					JOptionPane.OK_CANCEL_OPTION);
			if (seleccion == JOptionPane.OK_OPTION) {
				close();
			}
		} else {
			close();

		}
	}

	/**
	 * @see ar.com.wuik.swing.frames.WApplication#sessionExpiredEvent()
	 */
	@Override
	protected void sessionExpiredEvent() {
		logout(Boolean.FALSE);
	}

	/**
	 * @see ar.com.wuik.swing.frames.WApplication#lastEscapeKeyPressed()
	 */
	@Override
	protected void lastEscapeKeyPressed() {
		logout(Boolean.TRUE);
	}

	/**
	 * @see ar.com.wuik.swing.frames.WApplication#close()
	 */
	@Override
	protected void close() {
		super.close();
		new Login().showFrame();
	}

	/**
	 * @see ar.com.wuik.swing.frames.WApplication#showFrame()
	 */
	@Override
	protected void showFrame() {
		applySecurity();
		super.showFrame();
	}

}
