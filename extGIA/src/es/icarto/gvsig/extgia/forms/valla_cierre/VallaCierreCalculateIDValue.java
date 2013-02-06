package es.icarto.gvsig.extgia.forms.valla_cierre;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class VallaCierreCalculateIDValue extends CalculateComponentValue {

    public VallaCierreCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String vallaCierreID = String.valueOf(SqlUtils.getNextIdOfSequence(
		"audasa_extgia.valla_cierre_id_valla_seq"));
	resultComponent.setText(vallaCierreID);
	form.getFormController().setValue(resultComponentName, vallaCierreID);

    }

}
