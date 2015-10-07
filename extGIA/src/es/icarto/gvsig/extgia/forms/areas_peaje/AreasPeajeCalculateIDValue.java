package es.icarto.gvsig.extgia.forms.areas_peaje;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.CalculateComponentValue;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class AreasPeajeCalculateIDValue extends CalculateComponentValue {

    public AreasPeajeCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String areaPeajeID = String.valueOf(SqlUtils.
		getNextIdOfSequence("audasa_extgia.areas_peaje_id_area_peaje_seq"));
	resultComponent.setText(areaPeajeID);
	form.getFormController().setValue(resultComponentName, areaPeajeID);

    }

}
