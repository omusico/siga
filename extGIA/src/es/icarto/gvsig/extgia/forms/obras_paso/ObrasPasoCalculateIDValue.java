package es.icarto.gvsig.extgia.forms.obras_paso;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.CalculateComponentValue;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class ObrasPasoCalculateIDValue extends CalculateComponentValue {

    public ObrasPasoCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String obraPasoID = String.valueOf(SqlUtils.
		getNextIdOfSequence("audasa_extgia.obras_paso_id_obra_paso_seq"));
	resultComponent.setText(obraPasoID);
	form.getFormController().setValue(resultComponentName, obraPasoID);

    }

}
