package ar.com.wuik.sistema.frames.afip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import FEV1.dif.afip.gov.ar.entities.Comprobante;
import FEV1.dif.afip.gov.ar.entities.TipoComprobante;
import ar.com.wuik.sistema.bo.AfipBO;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.model.ComprobanteConsultaModel;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WUtils;

public class ConsultaComprobanteIFrame extends WAbstractModelIFrame implements
		WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<Comprobante> tablePanel;
	private JButton btnCerrar;
	private JPanel panel;
	private JLabel lblTipoComp;
	private JComboBox cmbTipoComp;

	/**
	 * Create the frame.
	 */
	public ConsultaComprobanteIFrame() {
		setBorder(new LineBorder(null, 1, true));
		setTitle("Ventas");
		setFrameIcon(new ImageIcon(
				ConsultaComprobanteIFrame.class
						.getResource("/icons/comprobantes.png")));
		setBounds(0, 0, 1000, 519);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getTablePanel());
		getContentPane().add(getPanel());
		loadTiposComprobantes();
		search();
	}

	private void loadTiposComprobantes() {

		AfipBO afipBO = AbstractFactory.getInstance(AfipBO.class);
		List<TipoComprobante> tiposComprobantes;
		try {
			tiposComprobantes = afipBO.obtenerTiposComprobantes();
			if (WUtils.isNotEmpty(tiposComprobantes)) {
				for (TipoComprobante tipoComprobante : tiposComprobantes) {
					getCmbTipoComp().addItem(
							new WOption((long) tipoComprobante.getId(),
									tipoComprobante.getDesc()));
				}
			}
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}

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

	private WTablePanel<Comprobante> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(ComprobanteConsultaModel.class, Boolean.FALSE);
			tablePanel.setBounds(10, 76, 978, 364);
		}
		return tablePanel;
	}

	private JButton getBtnCerrar() {
		if (btnCerrar == null) {
			btnCerrar = new JButton("Salir");
			btnCerrar.setFocusable(false);
			btnCerrar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCerrar.setIcon(new ImageIcon(ConsultaComprobanteIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(885, 451, 103, 30);
		}
		return btnCerrar;
	}

	public void search() {
		// Filtro
		long idTipoComprobante = ((WOption)getCmbTipoComp().getSelectedItem()).getValue();

		try {
			AfipBO afipBO = AbstractFactory.getInstance(AfipBO.class);
			List<Comprobante> comprobantes = afipBO
					.consultarComprobantes(idTipoComprobante);
			getTablePanel().addData(comprobantes);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	@Override
	protected JComponent getFocusComponent() {
		return null;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBounds(10, 11, 978, 54);
			panel.setLayout(null);
			panel.add(getLblTipoComp());
			panel.add(getCmbTipoComp());
		}
		return panel;
	}

	private JLabel getLblTipoComp() {
		if (lblTipoComp == null) {
			lblTipoComp = new JLabel("Tipo Comprobante:");
			lblTipoComp.setBounds(10, 11, 128, 25);
		}
		return lblTipoComp;
	}

	private JComboBox getCmbTipoComp() {
		if (cmbTipoComp == null) {
			cmbTipoComp = new JComboBox();
			cmbTipoComp.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						search();
					}
				}
			});
			cmbTipoComp.setBounds(148, 11, 182, 25);
		}
		return cmbTipoComp;
	}
}
