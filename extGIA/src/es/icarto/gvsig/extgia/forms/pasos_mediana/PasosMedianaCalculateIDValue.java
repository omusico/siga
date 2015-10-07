package es.icarto.gvsig.extgia.forms.pasos_mediana;

import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import es.icarto.gvsig.extgia.forms.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

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

	String pkValueFormatted = getPkFormatted(pkWidget);

	if (pkWidget.getText().isEmpty() || pkValueFormatted.isEmpty()) {
	    validate = false;
	}

	String pasoMedianaID = "";
	if (validate) {

	    pasoMedianaID = tramoValue + pkValueFormatted;
	}
	resultComponent.setText(pasoMedianaID);
	form.getFormController().setValue(resultComponentName, pasoMedianaID);
    }

}
