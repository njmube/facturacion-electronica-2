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
import ar.com.wuik.sistema.frames.afip.ConsultaComprobanteIFrame;
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
	private WMenuItemSecurity itemProveedor = null;
	private WMenuItemSecurity itemBackup = null;
	private WMenuItemSecurity itemSubdiarioIVA = null;
	private WMenuItemSecurity itemTipoProducto = null;
	private WMenuItemSecurity itemConsultaComprobantes = null;

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
			itemProducto = new WMenuItemSecurity("Productos", null,
					new ImageIcon(
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

	private WMenuItemSecurity getItemTipoProducto() {
		if (null == itemTipoProducto) {
			itemTipoProducto = new WMenuItemSecurity("Tipos de Producto", null,
					new ImageIcon(MainFrame.class
							.getResource("/icons32/tipos_productos.png")));
			itemTipoProducto.setVisible(Boolean.TRUE);
			itemTipoProducto.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addIFrame(new TipoProductoIFrame());
				}
			});
		}
		return itemTipoProducto;
	}

	private WMenuItemSecurity getItemCliente() {
		if (null == itemCliente) {
			itemCliente = new WMenuItemSecurity("Clientes", null,
					new ImageIcon(
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

	private WMenuItemSecurity getItemProveedor() {
		if (null == itemProveedor) {
			itemProveedor = new WMenuItemSecurity("Proveedores", null,
					new ImageIcon(
							MainFrame.class
									.getResource("/icons32/proveedores.png")));
			itemProveedor.setVisible(Boolean.TRUE);
			itemProveedor.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addIFrame(new ProveedorIFrame());
				}
			});
		}
		return itemProveedor;
	}

	private WMenuItemSecurity getItemBackup() {
		if (null == itemBackup) {
			itemBackup = new WMenuItemSecurity("Respaldo de Sistema", null,
					new ImageIcon(
							MainFrame.class
									.getResource("/icons32/backup.png")));
			itemBackup.setVisible(Boolean.TRUE);
			itemBackup.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addIFrame(new BackupIFrame());
				}
			});
		}
		return itemBackup;
	}

	private WMenuItemSecurity getItemParametro() {
		if (null == itemParametro) {
			itemParametro = new WMenuItemSecurity("Paramétricos", null,
					new ImageIcon(
							MainFrame.class
									.getResource("/icons32/parametricos.png")));
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

	private WMenuItemSecurity getItemConsultaComprobantes() {
		if (null == itemConsultaComprobantes) {
			itemConsultaComprobantes = new WMenuItemSecurity(
					"Consulta Comprobantes", null, new ImageIcon(
							MainFrame.class
									.getResource("/icons32/comprobantes.png")));
			itemConsultaComprobantes.setVisible(Boolean.TRUE);
			itemConsultaComprobantes.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addIFrame(new ConsultaComprobanteIFrame());
				}
			});
		}
		return itemConsultaComprobantes;
	}

	private WMenuItemSecurity getItemUsuario() {
		if (null == itemUsuario) {
			itemUsuario = new WMenuItemSecurity("Usuarios", null,
					new ImageIcon(
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
			itemCheque = new WMenuItemSecurity("Cheques", null, new ImageIcon(
					MainFrame.class.getResource("/icons32/cheques.png")));
			itemCheque.setVisible(Boolean.TRUE);
			itemCheque.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addIFrame(new ChequeIFrame());
				}
			});
		}
		return itemCheque;
	}

	protected void addMenuItems(JMenuBar menu, WMenuSecurity menuArchivo) {
		menuArchivo.setIcon(new ImageIcon(WCalendarIFrame.class
				.getResource("/icons32/home.png")));
		menuArchivo.add(getItemCerrarSesion(), menuArchivo.getComponentCount());
		menuArchivo.add(getItemEditarDatosPersonales(),
				menuArchivo.getComponentCount());
		menuArchivo.add(getItemBackup(), menuArchivo.getComponentCount());
		menu.add(getMenuAdministraciones());
		menu.add(getMenuAdmParametricas());
	}

	protected WMenuSecurity getMenuAdministraciones() {
		if (null == menuAdministraciones) {
			menuAdministraciones = new WMenuSecurity("Administraciones",
					new ImageIcon(WCalendarIFrame.class
							.getResource("/icons32/administraciones.png")),
					getItemCliente(), getItemProducto(), getItemTipoProducto(),
					getItemProveedor(), getItemCheque());
		}
		return menuAdministraciones;
	}

	protected WMenuSecurity getMenuAdmParametricas() {
		if (null == menuAdmParametricas) {
			menuAdmParametricas = new WMenuSecurity("Parámetros",
					new ImageIcon(WCalendarIFrame.class
							.getResource("/icons32/parametros.png")),
					getItemParametro(), getItemSubdiarioIVA(),
					getItemConsultaComprobantes(), getItemUsuario());
		}
		return menuAdmParametricas;
	}

	private WMenuItemSecurity getItemSubdiarioIVA() {
		if (null == itemSubdiarioIVA) {
			itemSubdiarioIVA = new WMenuItemSecurity("Subdiario de IVA", null,
					new ImageIcon(
							MainFrame.class
									.getResource("/icons32/subdiario.png")));
			itemSubdiarioIVA.setVisible(Boolean.TRUE);
			itemSubdiarioIVA.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					addIFrame(new SubdiarioIvaIFrame());
				}
			});
		}
		return itemSubdiarioIVA;
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
				// ((MainFrame) instance).setPermisos(permisos);
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
		setTitle(getApplicationName());
		super.showFrame();
	}

}
