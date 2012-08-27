package es.icarto.gvsig.extgia;

import javax.swing.JComboBox;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class CalculateInclinacionMediaValue extends CalculateComponentValue {

    public CalculateInclinacionMediaValue(AbstractForm form,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, resultComponentName, operatorComponentsNames);
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
