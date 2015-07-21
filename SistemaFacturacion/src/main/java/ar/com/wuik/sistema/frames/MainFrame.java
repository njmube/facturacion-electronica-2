package ar.com.wuik.sistema.frames;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SaharaSkin;

import ar.com.wuik.sistema.entities.Permiso;
import ar.com.wuik.sistema.entities.Usuario;
import ar.com.wuik.sistema.frames.parametricos.ParametroIFrame;
import ar.com.wuik.sistema.frames.parametricos.UsuarioIFrame;
import ar.com.wuik.sistema.frames.parametricos.UsuarioVerIFrame;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.sistema.utils.ParametrosUtil;
import ar.com.wuik.swing.components.WSessionMonitor;
import ar.com.wuik.swing.components.security.WMenuItemSecurity;
import ar.com.wuik.swing.components.security.WMenuSecurity;
import ar.com.wuik.swing.frames.WAbstractIFrame;
import ar.com.wuik.swing.frames.WApplication;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WUtils;

public class MainFrame extends WApplication {

	public MainFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				MainFrame.class.getResource("/icons32/icono.png")));
	}

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 4667090445204195673L;

	// Menu Administraciones ------------
	private WMenuItemSecurity itemProducto = null;
	private WMenuItemSecurity itemCliente = null;
	private WMenuItemSecurity itemUsuario = null;
	private WMenuItemSecurity itemParametro = null;
	private WMenuItemSecurity itemCheque = null;

	// Menu Archivo -------------
	private WMenuItemSecurity itemEditarDatosPersonales = null;
	private WMenuItemSecurity itemCerrarSesion = null;

	// Menu Herramientas

	private WMenuSecurity menuAdministraciones = null;
	private WMenuSecurity menuAdmParametricas = null;

	public static Usuario usuarioLogin;

	@Override
	protected void setLookAndFeel() {

		try {
			SubstanceLookAndFeel.setSkin(new SaharaSkin());
		} catch (Exception e) {
			System.out.println("Substance Skin failed to initialize");
		}
	}

	private WMenuItemSecurity getItemProducto() {
		if (null == itemProducto) {
			itemProducto = new WMenuItemSecurity("Productos",
					null, new ImageIcon(
							MainFrame.class
									.getResource("/icons32/productos.png")));
			itemProducto.setVisible(Boolean.TRUE);
			itemProducto.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					 addIFrame(new ProductoIFrame());
				}
			});
		}
		return itemProducto;
	}

	private WMenuItemSecurity getItemCliente() {
		if (null == itemCliente) {
			itemCliente = new WMenuItemSecurity("Clientes",
					null, new ImageIcon(
							MainFrame.class
									.getResource("/icons32/clientes.png")));
			itemCliente.setVisible(Boolean.TRUE);
			itemCliente.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addIFrame(new ClienteIFrame());
				}
			});
		}
		return itemCliente;
	}

	private WMenuItemSecurity getItemParametro() {
		if (null == itemParametro) {
			itemParametro = new WMenuItemSecurity("Parámetros",
					null, new ImageIcon(
							MainFrame.class
									.getResource("/icons32/parametros.png")));
			itemParametro.setVisible(Boolean.TRUE);
			itemParametro.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addIFrame(new ParametroIFrame());
				}
			});
		}
		return itemParametro;
	}

	private WMenuItemSecurity getItemUsuario() {
		if (null == itemUsuario) {
			itemUsuario = new WMenuItemSecurity("Usuarios",
					null, new ImageIcon(
							MainFrame.class
									.getResource("/icons32/usuarios.png")));
			itemUsuario.setVisible(Boolean.TRUE);
			itemUsuario.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addIFrame(new UsuarioIFrame());
				}
			});
		}
		return itemUsuario;
	}

	private WMenuItemSecurity getItemEditarDatosPersonales() {
		if (null == itemEditarDatosPersonales) {
			itemEditarDatosPersonales = new WMenuItemSecurity(
					"Editar Datos Usuario", null, new ImageIcon(
							MainFrame.class
									.getResource("/icons32/usuarios.png")));
			itemEditarDatosPersonales.setVisible(Boolean.TRUE);
			itemEditarDatosPersonales.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addIFrame(new UsuarioVerIFrame(usuarioLogin.getId(), null));
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
	
	

	private WMenuItemSecurity getItemCheque() {
		if (null == itemCheque) {
			itemCheque = new WMenuItemSecurity("Cheques", null,
					new ImageIcon(
							MainFrame.class.getResource("/icons32/cheque.png")));
			itemCheque.setVisible(Boolean.TRUE);
			itemCheque.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new ChequeIFrame());
				}
			});
		}
		return itemCheque;
	}
	

	protected void addMenuItems(JMenuBar menu, WMenuSecurity menuArchivo) {
		menuArchivo.add(getItemCerrarSesion(), menuArchivo.getComponentCount());
		menuArchivo.add(getItemEditarDatosPersonales(),
				menuArchivo.getComponentCount());
		menu.add(getMenuAdministraciones());
		menu.add(getMenuAdmParametricas());
	}

	protected WMenuSecurity getMenuAdministraciones() {
		if (null == menuAdministraciones) {
			menuAdministraciones = new WMenuSecurity("Administraciones",
					new ImageIcon(WCalendarIFrame.class
							.getResource("/icons32/administraciones.png")),
					getItemCliente(),  getItemProducto(), getItemCheque());
		}
		return menuAdministraciones;
	}

	protected WMenuSecurity getMenuAdmParametricas() {
		if (null == menuAdmParametricas) {
			menuAdmParametricas = new WMenuSecurity("Paramétricas",
					new ImageIcon(WCalendarIFrame.class
							.getResource("/icons32/parametricas.png")),
					getItemParametro(), getItemUsuario());
		}
		return menuAdmParametricas;
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

		Set<Permiso> permisosUsuario = usuarioLogin.getPermisos();
		if (WUtils.isNotEmpty(permisosUsuario)) {
			for (Permiso permiso : permisosUsuario) {
				permisos.add(permiso.getCodigo());
			}
		}

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
//				((MainFrame) instance).setPermisos(permisos);
				((MainFrame) instance).showFrame();
			}
		});
	}

	/**
	 * @see ar.com.wuik.swing.frames.WApplication#getApplicationName()
	 */
	@Override
	protected String getApplicationName() {
		return "Administración " + AppUtils.getAppVersion() + " :: "
				+ usuarioLogin.getNombre();
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
		dispose();
		new Login().showFrame();
	}

	/**
	 * @see ar.com.wuik.swing.frames.WApplication#showFrame()
	 */
	@Override
	protected void showFrame() {
		// applySecurity();
		super.showFrame();
	}

}
