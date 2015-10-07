package es.icarto.gvsig.extgia.forms.obras_desague;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.CalculateComponentValue;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class ObrasDesagueCalculateIDValue extends CalculateComponentValue {

    public ObrasDesagueCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String obraDesagueID = String.valueOf(SqlUtils.
		getNextIdOfSequence("audasa_extgia.obras_desague_id_obra_desague_seq"));
	resultComponent.setText(obraDesagueID);
	form.getFormController().setValue(resultComponentName, obraDesagueID);

    }

}
