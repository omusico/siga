package es.icarto.gvsig.extgia.forms.transformadores;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class TransformadoresCalculateIDValue extends CalculateComponentValue {

    public TransformadoresCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String transformadorID = String.valueOf(SqlUtils.
		getNextIdOfSequence("audasa_extgia.transformadores_id_transformador_seq"));
	resultComponent.setText(transformadorID);
	form.getFormController().setValue(resultComponentName, transformadorID);

    }

}
