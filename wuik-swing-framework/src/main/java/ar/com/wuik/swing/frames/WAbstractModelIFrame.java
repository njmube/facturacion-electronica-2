package ar.com.wuik.swing.frames;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.text.JTextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.utils.WUtils;

/**
 * Clase base para el manejo JInternalFrames que requieren un Model.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public abstract class WAbstractModelIFrame extends WAbstractIFrame {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1434316636239962768L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WAbstractModelIFrame.class);

	/**
	 * Crea un WModel mediante los valores de los Componentes de la Ventana.
	 * 
	 * @return El WModel con los Valores.
	 * @see ar.com.wuik.swing.components.WModel
	 */
	public WModel populateModel() {
		WModel model = new WModel();
		Component[] components = getContentPane().getComponents();
		populateModelComponents(model, components);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Populating Model");
			LOGGER.debug("Model Data: " + model.toString());
		}
		return model;
	}

	private void populateModelComponents(WModel model, Component[] components) {
		if (WUtils.isNotEmpty(components)) {
			for (Component component : components) {

				if (component instanceof JComponent) {
					populateModelComponents(model,
							((JComponent) component).getComponents());
				}

				if (null != component.getName()) {
					Object value = null;
					if (component instanceof JTextComponent) {
						JTextComponent jC = (JTextComponent) component;
						value = jC.getText();
					} else if (component instanceof JToggleButton) {
						JToggleButton jC = (JToggleButton) component;
						value = jC.isSelected();
					} else if (component instanceof JComboBox) {
						JComboBox jC = (JComboBox) component;
						WOption comboItem = (WOption) jC.getSelectedItem();
						value = comboItem;
					} else if (component instanceof JCheckBox) {
						JCheckBox jC = (JCheckBox) component;
						value = jC.isSelected();
					} else if (component instanceof JRadioButton) {
						JRadioButton jC = (JRadioButton) component;
						value = jC.isSelected();
					} else if (component instanceof JSpinner) {
						JSpinner jC = (JSpinner) component;
						value = jC.getValue();
					} else if (component instanceof JSlider) {
						JSlider jC = (JSlider) component;
						value = jC.getValue();
					} else if (component instanceof JScrollPane) {
						JScrollPane jC = (JScrollPane) component;
						Component[] comps = jC.getComponents();
						if (WUtils.isNotEmpty(comps)) {
							for (Component subcomp : comps) {
								if (subcomp instanceof JViewport) {
									Component[] subsubcomps = ((JViewport) subcomp)
											.getComponents();
									for (Component subsubcomp : subsubcomps) {
										if (subsubcomp instanceof JTextArea) {
											JTextArea txa = (JTextArea) subsubcomp;
											value = txa.getText();
											break;
										} else if (subsubcomp instanceof JList) {
											JList list = (JList) subsubcomp;
											value = list.getSelectedValues();
											break;
										}
									}
									break;
								}
							}
						}
					}
					model.addValue(component.getName(), value);
				}
			}
		}
	}

	/**
	 * Popula los Componentes de la Ventana mediante el WModel pasado como
	 * argumento.
	 * 
	 * @param model
	 *            WModel - La instancia del Model a popular.
	 * @see ar.com.wuik.swing.components.WModel
	 */
	public void populateComponents(WModel model) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Populating Components");
			LOGGER.debug("Model Data: " + model.toString());
		}
		Component[] components = getContentPane().getComponents();
		populateComponentsModel(model, components);
	}

	/**
	 * //TODO: Describir el metodo populateModelComponents
	 * 
	 * @param model
	 */
	private void populateComponentsModel(WModel model, Component[] components) {
		if (WUtils.isNotEmpty(components)) {
			for (Component component : components) {

				if (component instanceof JComponent) {
					populateComponentsModel(model,
							((JComponent) component).getComponents());
				}

				if (null != component.getName()) {
					Object value = model.getValue(component.getName());
					if (component instanceof JTextComponent) {
						JTextComponent jC = (JTextComponent) component;
						if (null != value) {
							jC.setText(value.toString());
						}
					} else if (component instanceof JToggleButton) {
						JToggleButton jC = (JToggleButton) component;
						if (null != value) {
							jC.setSelected((Boolean) value);
						}
					} else if (component instanceof JComboBox) {
						JComboBox jC = (JComboBox) component;
						if (null != value) {
							if (value instanceof WOption) {
								jC.setSelectedItem(value);
							} else {
								jC.setSelectedItem(new WOption(Long
										.valueOf(value.toString())));
							}
						}
					} else if (component instanceof JCheckBox) {
						JCheckBox jC = (JCheckBox) component;
						if (null != value) {
							jC.setSelected((Boolean) value);
						}
					} else if (component instanceof JRadioButton) {
						JRadioButton jC = (JRadioButton) component;
						if (null != value) {
							jC.setSelected((Boolean) value);
						}
					} else if (component instanceof JSpinner) {
						JSpinner jC = (JSpinner) component;
						if (null != value) {
							jC.setValue((Integer) value);
						}
					} else if (component instanceof JSlider) {
						JSlider jC = (JSlider) component;
						if (null != value) {
							jC.setValue((Integer) value);
						}
					} else if (component instanceof JScrollPane) {
						JScrollPane jC = (JScrollPane) component;
						Component[] comps = jC.getComponents();
						if (WUtils.isNotEmpty(comps)) {
							for (Component subcomp : comps) {
								if (subcomp instanceof JViewport) {
									Component[] subsubcomps = ((JViewport) subcomp)
											.getComponents();
									for (Component subsubcomp : subsubcomps) {
										if (subsubcomp instanceof JTextArea) {
											JTextArea txa = (JTextArea) subsubcomp;
											if (null != value) {
												txa.setText(value.toString());
											}
											break;
										} else if (subsubcomp instanceof JList) {
											JList list = (JList) subsubcomp;
											if (null != value) {
												List<Long> items = (List<Long>) value;
												int size = list.getModel()
														.getSize();
												List<Integer> selectedIndexes = new ArrayList<Integer>();
												for (int i = 0; i < size; i++) {
													WOption item = (WOption) list
															.getModel()
															.getElementAt(i);
													if (items.contains(item
															.getValue())) {
														selectedIndexes.add(i);
													}
												}
												list.setSelectedIndices(WUtils
														.toPrimitive(selectedIndexes
																.toArray(new Integer[0])));
											}
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Valida la informacion del Model.
	 * 
	 * @param model
	 *            WModel - El Model a validar.
	 * @return Si el Model es valido o no.
	 */
	protected abstract boolean validateModel(WModel model);

	protected abstract JComponent getFocusComponent();

	/**
	 * @see ar.com.wuik.swing.frames.WAbstractIFrame#showFrame()
	 */
	@Override
	public void showFrame() {
		super.showFrame();
		JComponent c = getFocusComponent();
		if (null != c) {
			c.requestFocus();
		}
	}

}
