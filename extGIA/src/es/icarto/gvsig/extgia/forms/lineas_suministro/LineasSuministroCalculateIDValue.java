package es.icarto.gvsig.extgia.forms.lineas_suministro;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class LineasSuministroCalculateIDValue extends CalculateComponentValue {

    public LineasSuministroCalculateIDValue(AbstractForm form,
	    HashMap<String, JComponent> allFormWidgets,
	    String resultComponentName, String... operatorComponentsNames) {
	super(form, allFormWidgets, resultComponentName, operatorComponentsNames);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setValue(boolean validate) {
	String lineaSuministroID = String.valueOf(SqlUtils.
		getNextIdOfSequence("audasa_extgia.lineas_suministro_id_linea_suministro_seq"));
	resultComponent.setText(lineaSuministroID);
	form.getFormController().setValue(resultComponentName, lineaSuministroID);

    }

}
