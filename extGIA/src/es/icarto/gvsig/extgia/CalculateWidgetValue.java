package es.icarto.gvsig.extgia;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;
import es.icarto.gvsig.navtableforms.validation.ComponentValidator;

public class CalculateWidgetValue {

    private JTextField taludidWidget;
    private AbstractForm form;
    private HashMap<String, JComponent> baseWidgets;
    private BaseWidgetsListener handler;
    private ArrayList<ComponentValidator> baseValidators;

    /**
     * in the setListeners of the Form we must call the setListeners of this
     * class in the removeListeners of the Form we must call the remove
     * Listeners of this class The object must be initialized in setListeners.
     */
    // TODO: The ideal behavior should be create this object in
    // fillSpecificValues and have the listeners registered in the form, so we
    // don't have to make specific calls
    public CalculateWidgetValue(AbstractForm form, String taludidName,
	    String... baseNames) {
	this.form = form;

	setBaseWidgets(taludidName, baseNames);
	setBaseValidators(baseNames);

	this.handler = new BaseWidgetsListener();
    }

    private void setBaseWidgets(String taludidName, String[] baseNames) {
	HashMap<String, JComponent> allFormWidgets = form.getWidgetComponents();
	taludidWidget = (JTextField) allFormWidgets.get(taludidName);
	baseWidgets = new HashMap<String, JComponent>();
	for (String name : baseNames) {
	    baseWidgets.put(name, allFormWidgets.get(name));
	}
    }

    private void setBaseValidators(String[] baseNames) {
	baseValidators = new ArrayList<ComponentValidator>();
	for (String name : baseNames) {
	    ComponentValidator cv = form.getFormValidator()
		    .getComponentValidator(name);
	    if (cv != null) {
		baseValidators.add(cv);
	    }
	}
    }

    private boolean validate() {
	for (JComponent widget : baseWidgets.values()) {
	    if (widget instanceof JComboBox) {
		if (!(((JComboBox) widget).getSelectedItem() instanceof KeyValue)) {
		    return false;
		}
	    }
	}

	for (ComponentValidator v : baseValidators) {
	    if (!v.validate()) {
		return false;
	    }
	}
	return true;
    }

    void setListeners() {
	for (JComponent widget : baseWidgets.values()) {
	    if (widget instanceof JFormattedTextField) {
		((JFormattedTextField) widget).addKeyListener(handler);
	    } else if (widget instanceof JTextField) {
		((JTextField) widget).addKeyListener(handler);
	    } else if (widget instanceof JComboBox) {
		((JComboBox) widget).addActionListener(handler);
	    } else if (widget instanceof JCheckBox) {
		((JCheckBox) widget).addActionListener(handler);
	    } else if (widget instanceof JTextArea) {
		((JTextArea) widget).addKeyListener(handler);
	    }
	}
    }

    /**
     * (primera letra "Tipo de Talud")&-&("Número de Talud")&(Primera letra de
     * "Base decontratista") ; EJ: D-584N
     * 
     */
    void setValue() {

	// TODO: Aplicar el formato adecuado a los valores base

	JComboBox tipoTaludWidget = (JComboBox) baseWidgets
		.get(DBFieldNames.TIPO_TALUD);
	JTextField numeroTaludWidget = (JTextField) baseWidgets
		.get(DBFieldNames.NUMERO_TALUD);
	JComboBox baseContratistaWidget = (JComboBox) baseWidgets
		.get(DBFieldNames.BASE_CONTRATISTA);

	if (validate()) {
	    String taludID = ((KeyValue) tipoTaludWidget.getSelectedItem())
		    .getValue().substring(0, 1);
	    taludID += "-";
	    taludID += numeroTaludWidget.getText();
	    taludID += ((KeyValue) baseContratistaWidget.getSelectedItem())
		    .getValue().substring(0, 1);
	    taludidWidget.setText(taludID);
	    // TODO: Test if needed
	    form.getFormController().setValue(DBFieldNames.ID_TALUD, taludID);
	}
    }

    void removeListeners() {
	for (JComponent widget : baseWidgets.values()) {
	    if (widget instanceof JFormattedTextField) {
		((JFormattedTextField) widget).removeKeyListener(handler);
	    } else if (widget instanceof JTextField) {
		((JTextField) widget).removeKeyListener(handler);
	    } else if (widget instanceof JComboBox) {
		((JComboBox) widget).removeActionListener(handler);
	    } else if (widget instanceof JCheckBox) {
		((JCheckBox) widget).removeActionListener(handler);
	    } else if (widget instanceof JTextArea) {
		((JTextArea) widget).removeKeyListener(handler);
	    }
	}
    }

    public class BaseWidgetsListener implements KeyListener, ActionListener {

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	    if (!form.isFillingValues()) {
		setValue();
	    }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    if (!form.isFillingValues()) {
		setValue();
	    }
	}
    }
}
