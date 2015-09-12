package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.model.DetalleResumenCuentaModel;
import ar.com.wuik.sistema.reportes.ResumenReporte;
import ar.com.wuik.sistema.reportes.entities.DetalleResumenCuentaDTO;
import ar.com.wuik.sistema.reportes.entities.ResumenCuentaDTO;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;

public class ResumenCuentaIFrame extends WAbstractModelIFrame implements
		WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<DetalleResumenCuentaDTO> tablePanel;
	private JButton btnCerrar;
	private Long idCliente;
	private static final String CAMPO_TIPO_COMP = "tipoComprobante";

	/**
	 * Create the frame.
	 */
	public ResumenCuentaIFrame(Long idCliente) {
		this.idCliente = idCliente;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Comprobantes");
		setFrameIcon(new ImageIcon(
				ResumenCuentaIFrame.class
						.getResource("/icons/resumen_cuenta.png")));
		setBounds(0, 0, 1000, 519);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getTablePanel());
		loadResumen();
	}

	/**
	 * @see ar.com.wuik.swing.components.security.WSecure#applySecurity(java.util.List)
	 */
	@Override
	public void applySecurity(List<String> permisos) {
	}

	@Override
	protected boolean validateModel(WModel model) {
		return false;
	}

	private WTablePanel<DetalleResumenCuentaDTO> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(DetalleResumenCuentaModel.class,
					Boolean.FALSE);
			tablePanel.setBounds(10, 11, 978, 429);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();
		WToolbarButton buttonImprimir = new WToolbarButton("Imprimir Resumen",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/imprimir.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							ResumenReporte.generarImpresion(idCliente);
						} catch (ReportException rexc) {
							showGlobalErrorMsg(rexc.getMessage());
						}
					}
				}, "Imprimir Resumen", null);

		toolbarButtons.add(buttonImprimir);
		return toolbarButtons;
	}

	private JButton getBtnCerrar() {
		if (btnCerrar == null) {
			btnCerrar = new JButton("Cerrar");
			btnCerrar.setFocusable(false);
			btnCerrar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCerrar.setIcon(new ImageIcon(ResumenCuentaIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(885, 451, 103, 30);
		}
		return btnCerrar;
	}

	public void loadResumen() {
		try {
			ClienteBO clienteBO = AbstractFactory.getInstance(ClienteBO.class);
			ResumenCuentaDTO resumen = clienteBO
					.obtenerResumenCuenta(idCliente);
			getTablePanel().addData(resumen.getDetalles());
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	@Override
	protected JComponent getFocusComponent() {
		return null;
	}
}
