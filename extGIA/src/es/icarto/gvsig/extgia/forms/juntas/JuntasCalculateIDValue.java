package es.icarto.gvsig.extgia.forms.juntas;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class JuntasCalculateIDValue extends CalculateComponentValue {

    public JuntasCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String juntaID = String.valueOf(SqlUtils.getNextIdOfSequence("audasa_extgia.juntas_id_junta_seq"));
	resultComponent.setText(juntaID);
	form.getFormController().setValue(resultComponentName, juntaID);
    }

}
