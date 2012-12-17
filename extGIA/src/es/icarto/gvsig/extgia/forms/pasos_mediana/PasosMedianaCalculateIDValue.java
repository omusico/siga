package es.icarto.gvsig.extgia.forms.pasos_mediana;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class PasosMedianaCalculateIDValue extends CalculateComponentValue {

    public PasosMedianaCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String tramoValue = "";

	JComboBox tramoWidget = (JComboBox) operatorComponents
		.get(DBFieldNames.TRAMO);
	if (tramoWidget.getItemCount()!=0) {
	    tramoValue = ((KeyValue) tramoWidget.
		    getSelectedItem()).getKey();
	}

	JTextField pkWidget = (JTextField) operatorComponents
		.get(DBFieldNames.PK);
	String pkValue = pkWidget.getText();
	String[] pkValues = pkValue.split("\\.");
	String pkValueFormated = "";
	if (pkValues.length>1) {
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

	if (pkWidget.getText().isEmpty()) {
	    validate = false;
	}

	String pasoMedianaID = "";
	if (validate) {

	    pasoMedianaID = tramoValue + pkValueFormated;
	}
	resultComponent.setText(pasoMedianaID);
	form.getFormController().setValue(resultComponentName, pasoMedianaID);
    }

}
