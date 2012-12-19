package es.icarto.gvsig.extgia.forms.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;
import es.icarto.gvsig.navtableforms.validation.ComponentValidator;

public abstract class CalculateComponentValue {

    protected JTextField resultComponent;
    protected String resultComponentName;
    protected HashMap<String, JComponent> operatorComponents;
    protected ArrayList<ComponentValidator> operatorValidators;
    protected OperatorComponentsListener handler;
    private final HashMap<String, JComponent> allFormWidgets;
    protected AbstractForm form;

    /**
     * in the setListeners of the Form we must call the setListeners of this
     * class in the removeListeners of the Form we must call the remove
     * Listeners of this class The object must be initialized in setListeners.
     */
    // TODO: The ideal behavior should be create this object in
    // fillSpecificValues and have the listeners registered in the form, so we
    // don't have to make specific calls
    public CalculateComponentValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	this.form = form;
	this.resultComponentName = resultComponentName;
	this.allFormWidgets = allFormWidgets;

	setComponents(resultComponentName, operatorComponentsNames);
	setOperatorValidators(operatorComponentsNames);

	this.handler = new OperatorComponentsListener();
    }

    private void setComponents(String resultComponentName,
	    String[] operatorComponentsNames) {
	// HashMap<String, JComponent> allFormWidgets =
	// form.getWidgetComponents();
	resultComponent = (JTextField) allFormWidgets.get(resultComponentName);
	operatorComponents = new HashMap<String, JComponent>();
	for (String name : operatorComponentsNames) {
	    operatorComponents.put(name, allFormWidgets.get(name));
	}
    }

    private void setOperatorValidators(String[] operatorComponentsNames) {
	operatorValidators = new ArrayList<ComponentValidator>();
	for (String name : operatorComponentsNames) {
	    ComponentValidator cv = form.getFormValidator()
		    .getComponentValidator(name);
	    if (cv != null) {
		operatorValidators.add(cv);
	    }
	}
    }

    private boolean validate() {
	for (JComponent widget : operatorComponents.values()) {
	    if (widget instanceof JComboBox) {
		if (!(((JComboBox) widget).getSelectedItem() instanceof KeyValue)) {
		    return false;
		}
	    }
	}

	for (ComponentValidator v : operatorValidators) {
	    if (!v.validate()) {
		return false;
	    }
	}
	return true;
    }

    public void setListeners() {
	for (JComponent widget : operatorComponents.values()) {
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

    public abstract void setValue(boolean validate);

    public void removeListeners() {
	for (JComponent widget : operatorComponents.values()) {
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

    public class OperatorComponentsListener implements KeyListener,
    ActionListener {

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	    delegate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    delegate();
	}

	private void delegate() {
	    if (!form.isFillingValues()) {
		boolean validate = validate();
		setValue(validate);
	    }
	}
    }

    protected String getPkFormatted(JTextField pkWidget) {
	NumberFormat format=DecimalFormat.getInstance();
	DecimalFormatSymbols symbols=((DecimalFormat) format).getDecimalFormatSymbols();
	char decimalSeparator=symbols.getDecimalSeparator();

	String pkValue = pkWidget.getText();
	String[] pkValues = null;
	if (String.valueOf(decimalSeparator).equals(".")) {
	    pkValues = pkValue.split("\\" + String.valueOf(decimalSeparator));
	}
	String pkValueFormated = "";

	if (pkValues != null && pkValues.length>1) {
	    if (pkValues[1].length() == 1) {
		pkValueFormated = String.format("%03d", Integer.valueOf(pkValues[0])) +
			pkValues[1] + "00";
	    }else if (pkValues[1].length() == 2) {
		pkValueFormated = String.format("%03d", Integer.valueOf(pkValues[0])) +
			pkValues[1] + "0";
	    }else {
		pkValueFormated = String.format("%03d", Integer.valueOf(pkValues[0])) +
			pkValues[1];
	    }
	}
	return pkValueFormated;
    }
}
