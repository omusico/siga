package es.icarto.gvsig.extgia.forms.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public abstract class CalculateReconocimientoIndexValue {
    protected JTextField resultComponent;
    protected String resultComponentName;
    protected HashMap<String, JComponent> operatorComponents;
    protected OperatorComponentsListener handler;
    private final HashMap<String, JComponent> allFormWidgets;
    protected AbstractSubForm form;

    public CalculateReconocimientoIndexValue(AbstractSubForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	this.form = form;
	this.resultComponentName = resultComponentName;
	this.allFormWidgets = allFormWidgets;

	setComponents(resultComponentName, operatorComponentsNames);

	this.handler = new OperatorComponentsListener();
    }

    public abstract void setValue(boolean validate);

    private void setComponents(String resultComponentName,
	    String[] operatorComponentsNames) {
	resultComponent = (JTextField) allFormWidgets.get(resultComponentName);
	operatorComponents = new HashMap<String, JComponent>();
	for (String name : operatorComponentsNames) {
	    operatorComponents.put(name, allFormWidgets.get(name));
	}
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
	    setValue(form.isFillingValues());
	}

    }

}
