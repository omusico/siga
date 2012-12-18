package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class SenhalizacionVerticalCalculateIDValue extends CalculateComponentValue {

    public SenhalizacionVerticalCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String senhalVerticalID = String.valueOf(SqlUtils.getNextIdOfSequence("audasa_extgia.senhalizacion_vertical_id_senhal_vertical_seq"));
	resultComponent.setText(senhalVerticalID);
	form.getFormController().setValue(resultComponentName, senhalVerticalID);
    }

}
