package es.icarto.gvsig.extgia.forms.taludes;

import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class TaludesCalculateInclinacionMediaValue extends CalculateComponentValue {

    public TaludesCalculateInclinacionMediaValue(AbstractForm form,
	    Map<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName,
		operatorComponentsNames);
    }

    @Override
    public void setValue(boolean validate) {

	String value = "";
	if (validate) {
	    JComboBox sectorInclinacion = (JComboBox) operatorComponents
		    .get(DBFieldNames.SECTOR_INCLINACION);
	    String foo = ((KeyValue) sectorInclinacion.getSelectedItem())
		    .getValue();

	    if (foo.equals("A")) {
		value = "20/35";
	    } else if (foo.equals("B")) {
		value = "35/45";
	    } else if (foo.equals("C")) {
		value = "45/55";
	    }
	}
	resultComponent.setText(value);
	form.getFormController().setValue(resultComponentName, value);
    }

}
