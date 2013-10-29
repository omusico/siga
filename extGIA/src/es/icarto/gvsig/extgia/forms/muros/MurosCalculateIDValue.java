package es.icarto.gvsig.extgia.forms.muros;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class MurosCalculateIDValue extends CalculateComponentValue {

    public MurosCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String murosID = String.valueOf(SqlUtils.getNextIdOfSequence(
		"audasa_extgia.muros_id_muro_seq"));
	resultComponent.setText(murosID);
	form.getFormController().setValue(resultComponentName, murosID);

    }

}
