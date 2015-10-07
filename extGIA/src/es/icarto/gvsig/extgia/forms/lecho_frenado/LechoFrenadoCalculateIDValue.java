package es.icarto.gvsig.extgia.forms.lecho_frenado;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.CalculateComponentValue;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class LechoFrenadoCalculateIDValue extends CalculateComponentValue {

    public LechoFrenadoCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String lechoFrenadoID = String.valueOf(SqlUtils.getNextIdOfSequence(
		"audasa_extgia.lecho_frenado_id_lecho_frenado_seq"));
	resultComponent.setText(lechoFrenadoID);
	form.getFormController().setValue(resultComponentName, lechoFrenadoID);

    }

}
