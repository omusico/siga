package es.icarto.gvsig.extgia.forms;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.ANCHO;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.LONGITUD;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.MEDICION;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.UNIDAD;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.hardcode.gdbms.engine.values.Value;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public abstract class VegetationCalculateMedicion extends Calculation {

    UnidadListener unidadHandler;

    public VegetationCalculateMedicion(IValidatableForm form) {
	super(form);
	if (!form.isFillingValues()) {
	    unidadHandler = new UnidadListener();
	    ((JComboBox) form.getWidgets().get(UNIDAD))
		    .addActionListener(unidadHandler);
	}
    }

    public VegetationCalculateMedicion(IValidatableForm form, Value longitud) {
	super(form);
	if (!form.isFillingValues()) {
	    unidadHandler = new UnidadListener();
	    ((JComboBox) form.getWidgets().get(UNIDAD))
		    .addActionListener(unidadHandler);
	}
    }

    protected abstract String getIDField();

    protected abstract ForeignValue getMedicionForeignValue();

    @Override
    protected String resultName() {
	return MEDICION;
    }

    @Override
    protected String[] operandNames() {
	return new String[] { LONGITUD, ANCHO };
    }

    @Override
    protected String calculate() {
	BigDecimal value = new BigDecimal(1);

	value = operandValue(LONGITUD).multiply(value);
	value = operandValue(ANCHO).multiply(value);

	return formatter.format(value);
    }

    protected HashMap<String, String> getForeignKey() {
	String primaryKeyValue = ((JTextField) form.getWidgets().get(
		getIDField())).getText();
	HashMap<String, String> foreignKey = new HashMap<String, String>();
	foreignKey.put(getIDField(), primaryKeyValue);
	return foreignKey;
    }

    public class UnidadListener extends OperandComponentListener implements
	    ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    super.actionPerformed(e);
	    if (e.getSource() != null
		    && ((JComboBox) e.getSource()).getSelectedItem() != null) {
		if (((JComboBox) e.getSource()).getSelectedItem().toString()
			.equalsIgnoreCase("Herbicida")) {
		    updateMedicionLastJobValue();
		} else {
		    if (!((GIATrabajosSubForm) form).isEditing()) {
			updateMedicionValue();
		    }
		    updateMedicionLastJobValue();
		}
	    }
	}

	private void updateMedicionValue() {
	    ((JTextField) form.getWidgets().get(MEDICION))
		    .setText(getMedicionForeignValue().getValue());
	}

	private void updateMedicionLastJobValue() {
	    if (getMedicionForeignValue().getComponent().equalsIgnoreCase(
		    DBFieldNames.MEDICION_ULTIMO_TRABAJO)) {
		((JTextField) form.getWidgets().get(
			DBFieldNames.MEDICION_ULTIMO_TRABAJO))
			.setText(getMedicionForeignValue().getValue());
	    } else {
		((JTextField) form.getWidgets().get(
			DBFieldNames.MEDICION_ULTIMO_TRABAJO)).setText(null);
	    }
	}
    }

}
