package es.icarto.gvsig.extgia.forms.senhalizacion_variable;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class SenhalizacionVariableCalculateIDValue extends
CalculateComponentValue {

    public SenhalizacionVariableCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String senhalizacionVariableID = String.valueOf(SqlUtils.
		getNextIdOfSequence("audasa_extgia.senhalizacion_variable_id_senhal_variable_seq"));
	resultComponent.setText(senhalizacionVariableID);
	form.getFormController().setValue(resultComponentName, senhalizacionVariableID);

    }

}
